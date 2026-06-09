package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.config.CacheNames;
import com.israel.studentmanagementsystem.dto.request.GradeRequest;
import com.israel.studentmanagementsystem.dto.response.EnrollmentResponse;
import com.israel.studentmanagementsystem.dto.response.GpaResponse;
import com.israel.studentmanagementsystem.entity.Course;
import com.israel.studentmanagementsystem.entity.Enrollment;
import com.israel.studentmanagementsystem.entity.StudentProfile;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.enums.EnrollmentStatus;
import com.israel.studentmanagementsystem.exception.ResourceNotFoundException;
import com.israel.studentmanagementsystem.exception.UnauthorizedException;
import com.israel.studentmanagementsystem.mapper.EnrollmentMapper;
import com.israel.studentmanagementsystem.repository.EnrollmentRepository;
import com.israel.studentmanagementsystem.repository.StudentProfileRepository;
import com.israel.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final GpaService gpaService;
    private final CacheManager cacheManager;
    private final EmailService emailService;

    @CacheEvict(value = CacheNames.STUDENT_GPA, key = "#email")
    @Transactional
    public EnrollmentResponse enroll(String email, Long courseId) {

        User user = loadUser(email);
        StudentProfile student = loadStudentProfile(user.getId());

        Course course = courseService.loadCourseWithTeacher(courseId);

        if (!course.getStatus().name().equals("ACTIVE")) {
            throw new IllegalStateException(
                    "Cannot enroll in an inactive course");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                student.getId(), courseId, EnrollmentStatus.ENROLLED)) {
            throw new IllegalStateException(
                    "You are already enrolled in this course");
        }

        if (course.isFull()) {
            throw new IllegalStateException(
                    "Course is full — no seats available");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);

        course.incrementEnrollment();

        emailService.sendEnrollmentEmail(user.getEmail(),user.getFirstName(), course.getTitle(), course.getCode(),course.getSemester());

        log.info("Student {} enrolled in course {}",
                user.getEmail(), course.getCode());

        return enrollmentMapper.toResponse(saved);
    }

    @CacheEvict(value = CacheNames.STUDENT_GPA, key = "#email")
    @Transactional
    public EnrollmentResponse drop(String email, Long enrollmentId) {

        User user = loadUser(email);
        StudentProfile student = loadStudentProfile(user.getId());

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment", enrollmentId));

        if (!enrollment.getStudent().getId().equals(student.getId())) {
            throw new UnauthorizedException(
                    "You can only drop your own enrollments");
        }

        if (!enrollment.getStatus().equals(EnrollmentStatus.ENROLLED)) {
            throw new IllegalStateException(
                    "You can only drop an active enrollment");
        }

        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollment.setDroppedAt(LocalDateTime.now());

        enrollment.getCourse().decrementEnrollment();

        log.info("Student {} dropped course {}",
                user.getEmail(), enrollment.getCourse().getCode());
        emailService.sendDropEmail(user.getEmail(),user.getFirstName(), enrollment.getCourse().getTitle(), enrollment.getCourse().getCode());

        return enrollmentMapper.toResponse(
                enrollmentRepository.save(enrollment));
    }


    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getMyEnrollments(String email) {

        User user = loadUser(email);
        StudentProfile student = loadStudentProfile(user.getId());

        return enrollmentRepository
                .findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ENROLLED)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getMyEnrollmentHistory(String email) {

        User user = loadUser(email);
        StudentProfile student = loadStudentProfile(user.getId());

        return enrollmentRepository
                .findAllByStudentId(student.getId())
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getCourseEnrollments(
            String email, Long courseId) {

        User user = loadUser(email);
        Course course = courseService.loadCourseWithTeacher(courseId);


        boolean isAdmin = user.getRole().name().equals("ROLE_ADMIN");
        boolean isOwner = course.getTeacher() != null &&
                course.getTeacher().getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException(
                    "You can only view enrollments for your own courses");
        }

        return enrollmentRepository
                .findByCourseIdAndStatus(courseId, EnrollmentStatus.ENROLLED)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Transactional
    public EnrollmentResponse gradeEnrollment(
            String email, GradeRequest request) {

        User user = loadUser(email);

        Enrollment enrollment = enrollmentRepository
                .findById(request.getEnrollmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enrollment", request.getEnrollmentId()));

        boolean isAdmin = user.getRole().name().equals("ROLE_ADMIN");
        boolean isOwner = enrollment.getCourse().getTeacher() != null &&
                enrollment.getCourse().getTeacher()
                        .getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException(
                    "You can only grade students in your own courses");
        }

        Double gradePoints =
                gpaService.toGradePoints(request.getFinalGrade());

        enrollment.setFinalGrade(request.getFinalGrade().toUpperCase());
        enrollment.setGradePoints(gradePoints);
        enrollment.setStatus(EnrollmentStatus.COMPLETED);

        Enrollment saved = enrollmentRepository.save(enrollment);

        StudentProfile student = enrollment.getStudent();
        Double newGpa = gpaService.calculateGpa(student.getId());
        student.setCurrentGpa(newGpa);
        studentProfileRepository.save(student);

        log.info("Student {} graded {} in course {} — GPA now {}",
                student.getUser().getEmail(),
                request.getFinalGrade(),
                enrollment.getCourse().getCode(),
                newGpa);
        String studentEmail = enrollment.getStudent().getUser().getEmail();
        cacheManager.getCache(CacheNames.STUDENT_GPA).evict(studentEmail);

        emailService.sendGradeEmail(    student.getUser().getEmail(),
                student.getUser().getFirstName(),
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getCode(),
                request.getFinalGrade(),
                gradePoints,
                newGpa);

        return enrollmentMapper.toResponse(saved);
    }

    @Cacheable(value = CacheNames.STUDENT_GPA, key = "#email")
    @Transactional(readOnly = true)
    public GpaResponse getMyGpa(String email) {

        User user = loadUser(email);
        StudentProfile student = loadStudentProfile(user.getId());

        Double gpa = gpaService.calculateGpa(student.getId());

        List<Enrollment> completed = enrollmentRepository
                .findCompletedWithGrades(student.getId());

        int totalCredits = completed.stream()
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();

        return new GpaResponse(
                student.getStudentNumber(),
                gpa,
                totalCredits,
                completed.size()
        );
    }

    private User loadUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private StudentProfile loadStudentProfile(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));
    }
}

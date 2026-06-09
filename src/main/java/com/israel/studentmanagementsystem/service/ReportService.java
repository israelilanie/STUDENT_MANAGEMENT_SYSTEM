package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.dto.response.*;
import com.israel.studentmanagementsystem.entity.Course;
import com.israel.studentmanagementsystem.entity.Enrollment;
import com.israel.studentmanagementsystem.entity.StudentProfile;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.enums.CourseStatus;
import com.israel.studentmanagementsystem.enums.EnrollmentStatus;
import com.israel.studentmanagementsystem.exception.ResourceNotFoundException;
import com.israel.studentmanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final GpaService gpaService;

    @Transactional(readOnly = true)
    public OverviewReportResponse getOverview() {

        long totalStudents = studentProfileRepository.count();
        long totalTeachers = teacherProfileRepository.count();
        long totalActiveCourses = courseRepository
                .countByStatus(CourseStatus.ACTIVE);
        long totalEnrollments = enrollmentRepository.count();
        long totalCompleted = enrollmentRepository
                .countByStatus(EnrollmentStatus.COMPLETED);
        long totalDropped = enrollmentRepository
                .countByStatus(EnrollmentStatus.DROPPED);

        // calculate system average GPA across all students
        List<StudentProfile> allStudents =
                studentProfileRepository.findAll();

        double averageGpa = allStudents.stream()
                .mapToDouble(StudentProfile::getCurrentGpa)
                .filter(gpa -> gpa > 0)
                .average()
                .orElse(0.0);

        // round to 2 decimal places
        averageGpa = Math.round(averageGpa * 100.0) / 100.0;

        return OverviewReportResponse.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalActiveCourses(totalActiveCourses)
                .totalEnrollments(totalEnrollments)
                .totalCompletedEnrollments(totalCompleted)
                .totalDroppedEnrollments(totalDropped)
                .averageGpa(averageGpa)
                .build();
    }

    // detailed report for a specific course
    @Transactional(readOnly = true)
    public CourseReportResponse getCourseReport(Long courseId) {

        Course course = courseRepository.findByIdWithTeacher(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", courseId));

        long totalEnrolled = enrollmentRepository
                .countByCourseIdAndStatus(courseId, EnrollmentStatus.ENROLLED);
        long totalCompleted = enrollmentRepository
                .countByCourseIdAndStatus(courseId, EnrollmentStatus.COMPLETED);
        long totalDropped = enrollmentRepository
                .countByCourseIdAndStatus(courseId, EnrollmentStatus.DROPPED);

        // get all completed enrollments with grades
        List<Enrollment> completed =
                enrollmentRepository.findCompletedByCourseId(courseId);

        // calculate average GPA for this course
        double averageGpa = completed.stream()
                .mapToDouble(Enrollment::getGradePoints)
                .average()
                .orElse(0.0);
        averageGpa = Math.round(averageGpa * 100.0) / 100.0;

        // pass rate — grades above D (gradePoints > 1.0)
        long passCount = completed.stream()
                .filter(e -> e.getGradePoints() != null
                        && e.getGradePoints() > 1.0)
                .count();

        double passRate = completed.isEmpty() ? 0.0 :
                Math.round((passCount * 100.0 / completed.size()) * 10.0) / 10.0;

        // highest and lowest grades
        String highestGrade = completed.stream()
                .filter(e -> e.getFinalGrade() != null)
                .max((a, b) -> Double.compare(
                        a.getGradePoints(), b.getGradePoints()))
                .map(Enrollment::getFinalGrade)
                .orElse("N/A");

        String lowestGrade = completed.stream()
                .filter(e -> e.getFinalGrade() != null)
                .min((a, b) -> Double.compare(
                        a.getGradePoints(), b.getGradePoints()))
                .map(Enrollment::getFinalGrade)
                .orElse("N/A");

        int availableSeats =
                course.getMaxCapacity() - course.getCurrentEnrollment();

        return CourseReportResponse.builder()
                .courseCode(course.getCode())
                .courseTitle(course.getTitle())
                .totalEnrolled(totalEnrolled)
                .totalCompleted(totalCompleted)
                .totalDropped(totalDropped)
                .availableSeats(availableSeats)
                .passRate(passRate)
                .averageGpa(averageGpa)
                .highestGrade(highestGrade)
                .lowestGrade(lowestGrade)
                .build();
    }

    // GPA distribution across all students
    @Transactional(readOnly = true)
    public GpaDistributionResponse getGpaDistribution() {

        List<StudentProfile> allStudents =
                studentProfileRepository.findAll();

        long excellent = allStudents.stream()
                .filter(s -> s.getCurrentGpa() >= 3.5)
                .count();

        long good = allStudents.stream()
                .filter(s -> s.getCurrentGpa() >= 3.0
                        && s.getCurrentGpa() < 3.5)
                .count();

        long average = allStudents.stream()
                .filter(s -> s.getCurrentGpa() >= 2.0
                        && s.getCurrentGpa() < 3.0)
                .count();

        long belowAverage = allStudents.stream()
                .filter(s -> s.getCurrentGpa() > 0
                        && s.getCurrentGpa() < 2.0)
                .count();

        long noGrades = allStudents.stream()
                .filter(s -> s.getCurrentGpa() == 0.0)
                .count();

        return GpaDistributionResponse.builder()
                .excellentCount(excellent)
                .goodCount(good)
                .averageCount(average)
                .belowAverageCount(belowAverage)
                .noGradesCount(noGrades)
                .totalStudents((long) allStudents.size())
                .build();
    }

    // full transcript for a student by their profile ID
    @Transactional(readOnly = true)
    public TranscriptResponse getTranscript(Long studentProfileId) {

        StudentProfile student = studentProfileRepository
                .findById(studentProfileId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "StudentProfile", studentProfileId));

        return buildTranscript(student);
    }

    // student gets their own transcript
    @Transactional(readOnly = true)
    public TranscriptResponse getMyTranscript(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        StudentProfile student = studentProfileRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));

        return buildTranscript(student);
    }

    // private — builds transcript from student profile
    private TranscriptResponse buildTranscript(StudentProfile student) {

        List<Enrollment> allEnrollments =
                enrollmentRepository.findAllByStudentId(student.getId());

        // build transcript entries
        List<TranscriptEntryResponse> entries = allEnrollments.stream()
                .map(e -> TranscriptEntryResponse.builder()
                        .courseCode(e.getCourse().getCode())
                        .courseTitle(e.getCourse().getTitle())
                        .credits(e.getCourse().getCredits())
                        .semester(e.getCourse().getSemester())
                        .status(e.getStatus())
                        .finalGrade(e.getFinalGrade())
                        .gradePoints(e.getGradePoints())
                        .enrolledAt(e.getEnrolledAt())
                        .droppedAt(e.getDroppedAt())
                        .build())
                .toList();

        long completed = allEnrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                .count();

        long dropped = allEnrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.DROPPED)
                .count();

        int totalCredits = allEnrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();

        String fullName = student.getUser().getFirstName()
                + " " + student.getUser().getLastName();

        return TranscriptResponse.builder()
                .studentNumber(student.getStudentNumber())
                .fullName(fullName)
                .email(student.getUser().getEmail())
                .cumulativeGpa(student.getCurrentGpa())
                .totalCredits(totalCredits)
                .totalCoursesCompleted((int) completed)
                .totalCoursesDropped((int) dropped)
                .entries(entries)
                .generatedAt(LocalDateTime.now())
                .build();
    }

}

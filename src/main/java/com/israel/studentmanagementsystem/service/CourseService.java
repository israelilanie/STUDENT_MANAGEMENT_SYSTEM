package com.israel.studentmanagementsystem.service;


import com.israel.studentmanagementsystem.dto.request.CreateCourseRequest;
import com.israel.studentmanagementsystem.dto.request.UpdateCourseRequest;
import com.israel.studentmanagementsystem.dto.response.CourseResponse;
import com.israel.studentmanagementsystem.entity.Course;
import com.israel.studentmanagementsystem.entity.TeacherProfile;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.enums.CourseStatus;
import com.israel.studentmanagementsystem.exception.ResourceNotFoundException;
import com.israel.studentmanagementsystem.exception.UnauthorizedException;
import com.israel.studentmanagementsystem.mapper.CourseMapper;
import com.israel.studentmanagementsystem.repository.CourseRepository;
import com.israel.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TeacherService teacherService;
    private final CourseMapper courseMapper;


    @Transactional(readOnly = true)
    public Page<CourseResponse> searchCourses(
            String search, Pageable pageable) {

        return courseRepository
                .searchCourses(search, CourseStatus.ACTIVE, pageable)
                .map(courseMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        return courseMapper.toResponse(loadCourseWithTeacher(id));
    }


    @Transactional
    public CourseResponse createCourse(
            CreateCourseRequest request, Long teacherId) {

        if (courseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException(
                    "Course code already exists: " + request.getCode());
        }

        TeacherProfile teacher =
                teacherService.getProfileEntityByUserId(teacherId);

        Course course = Course.builder()
                .code(request.getCode().toUpperCase())
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .maxCapacity(request.getMaxCapacity())
                .currentEnrollment(0)
                .status(CourseStatus.ACTIVE)
                .semester(request.getSemester())
                .teacher(teacher)
                .build();

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse createCourseAsTeacher(
            CreateCourseRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return createCourse(request, user.getId());
    }

    // admin or teacher updates a course
    @Transactional
    public CourseResponse updateCourse(
            Long courseId,
            UpdateCourseRequest request,
            String email) {

        Course course = loadCourseWithTeacher(courseId);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));


        boolean isAdmin = user.getRole().name().equals("ROLE_ADMIN");
        boolean isOwner = course.getTeacher() != null &&
                course.getTeacher().getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException(
                    "You can only update your own courses");
        }

        if (request.getTitle() != null)
            course.setTitle(request.getTitle());
        if (request.getDescription() != null)
            course.setDescription(request.getDescription());
        if (request.getCredits() != null)
            course.setCredits(request.getCredits());
        if (request.getMaxCapacity() != null)
            course.setMaxCapacity(request.getMaxCapacity());
        if (request.getSemester() != null)
            course.setSemester(request.getSemester());

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse assignTeacher(Long courseId, Long teacherUserId) {

        Course course = loadCourseWithTeacher(courseId);
        TeacherProfile teacher =
                teacherService.getProfileEntityByUserId(teacherUserId);

        course.setTeacher(teacher);
        return courseMapper.toResponse(courseRepository.save(course));
    }


    @Transactional
    public CourseResponse archiveCourse(Long courseId) {
        Course course = loadCourseWithTeacher(courseId);
        course.setStatus(CourseStatus.ARCHIVED);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getMyCourses(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        TeacherProfile teacher =
                teacherService.getProfileEntityByUserId(user.getId());

        return courseRepository
                .findByTeacherIdAndStatus(teacher.getId(), CourseStatus.ACTIVE)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public Course loadCourseWithTeacher(Long courseId) {
        return courseRepository.findByIdWithTeacher(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", courseId));
    }
}

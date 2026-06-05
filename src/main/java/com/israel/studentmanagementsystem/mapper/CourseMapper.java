package com.israel.studentmanagementsystem.mapper;

import com.israel.studentmanagementsystem.dto.response.CourseResponse;
import com.israel.studentmanagementsystem.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeacherProfileMapper.class})
public interface CourseMapper {

    @Mapping(target = "availableSeats", expression = "java(course.getMaxCapacity() - course.getCurrentEnrollment())")
    @Mapping(target = "full", expression = "java(course.isFull())")
    CourseResponse toResponse(Course course);
}
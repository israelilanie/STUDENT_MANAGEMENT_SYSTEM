package com.israel.studentmanagementsystem.mapper;

import com.israel.studentmanagementsystem.dto.response.EnrollmentResponse;
import com.israel.studentmanagementsystem.entity.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface EnrollmentMapper {
    EnrollmentResponse toResponse(Enrollment enrollment);
}

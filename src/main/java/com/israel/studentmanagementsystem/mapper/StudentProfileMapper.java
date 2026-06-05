package com.israel.studentmanagementsystem.mapper;

import com.israel.studentmanagementsystem.dto.response.StudentProfileResponse;
import com.israel.studentmanagementsystem.entity.StudentProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StudentProfileMapper {

    StudentProfileResponse toResponse(StudentProfile studentProfile);
}
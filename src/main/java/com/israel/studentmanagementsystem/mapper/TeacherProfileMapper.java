package com.israel.studentmanagementsystem.mapper;

import com.israel.studentmanagementsystem.dto.response.TeacherProfileResponse;
import com.israel.studentmanagementsystem.entity.TeacherProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TeacherProfileMapper {

    TeacherProfileResponse toResponse(TeacherProfile teacherProfile);
}
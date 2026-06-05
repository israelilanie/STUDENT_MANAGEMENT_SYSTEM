package com.israel.studentmanagementsystem.mapper;

import com.israel.studentmanagementsystem.dto.request.RegisterRequest;
import com.israel.studentmanagementsystem.dto.response.UserResponse;
import com.israel.studentmanagementsystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(RegisterRequest registerRequest);

    UserResponse toResponse(User user);

}

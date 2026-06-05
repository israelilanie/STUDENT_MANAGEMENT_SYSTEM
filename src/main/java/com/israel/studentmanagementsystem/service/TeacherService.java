package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.dto.request.CreateTeacherRequest;
import com.israel.studentmanagementsystem.dto.request.UpdateTeacherProfileRequest;
import com.israel.studentmanagementsystem.dto.response.TeacherProfileResponse;
import com.israel.studentmanagementsystem.entity.TeacherProfile;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.enums.Role;
import com.israel.studentmanagementsystem.enums.UserStatus;
import com.israel.studentmanagementsystem.exception.DuplicateEmailException;
import com.israel.studentmanagementsystem.exception.ResourceNotFoundException;
import com.israel.studentmanagementsystem.mapper.TeacherProfileMapper;
import com.israel.studentmanagementsystem.repository.TeacherProfileRepository;
import com.israel.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherProfileRepository teacherProfileRepository;
    private final UserRepository userRepository;
    private final TeacherProfileMapper teacherProfileMapper;
    private final EmployeeNumberGenerator employeeNumberGenerator;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public TeacherProfileResponse createTeacher(CreateTeacherRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.ROLE_TEACHER)
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        TeacherProfile profile = TeacherProfile.builder()
                .user(savedUser)
                .employeeNumber(employeeNumberGenerator.generate())
                .department(request.getDepartment())
                .title(request.getTitle())
                .specialization(request.getSpecialization())
                .officeHours(request.getOfficeHours())
                .build();

        TeacherProfile saved = teacherProfileRepository.save(profile);
        return teacherProfileMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TeacherProfileResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        TeacherProfile profile = teacherProfileRepository
                .findByUserIdWithUser(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher profile not found"));

        return teacherProfileMapper.toResponse(profile);
    }


    @Transactional
    public TeacherProfileResponse updateMyProfile(
            String email,
            UpdateTeacherProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        TeacherProfile profile = teacherProfileRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher profile not found"));

        if (request.getFirstName() != null)
            user.setFirstName(request.getFirstName());
        if (request.getLastName() != null)
            user.setLastName(request.getLastName());
        if (request.getDepartment() != null)
            profile.setDepartment(request.getDepartment());
        if (request.getTitle() != null)
            profile.setTitle(request.getTitle());
        if (request.getSpecialization() != null)
            profile.setSpecialization(request.getSpecialization());
        if (request.getOfficeHours() != null)
            profile.setOfficeHours(request.getOfficeHours());

        userRepository.save(user);
        TeacherProfile saved = teacherProfileRepository.save(profile);
        return teacherProfileMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public List<TeacherProfileResponse> getAllTeachers() {
        return teacherProfileRepository.findAll()
                .stream()
                .map(teacherProfileMapper::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public TeacherProfileResponse getTeacherById(Long id) {
        TeacherProfile profile = teacherProfileRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TeacherProfile", id));
        return teacherProfileMapper.toResponse(profile);
    }


    @Transactional(readOnly = true)
    public TeacherProfile getProfileEntityByUserId(Long userId) {
        return teacherProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher profile not found"));
    }
}

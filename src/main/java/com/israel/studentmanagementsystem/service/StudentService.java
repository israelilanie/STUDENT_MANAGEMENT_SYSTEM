package com.israel.studentmanagementsystem.service;


import com.israel.studentmanagementsystem.config.CacheNames;
import com.israel.studentmanagementsystem.dto.request.UpdateStudentProfileRequest;
import com.israel.studentmanagementsystem.dto.response.StudentProfileResponse;
import com.israel.studentmanagementsystem.entity.StudentProfile;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.enums.StudentStatus;
import com.israel.studentmanagementsystem.exception.ResourceNotFoundException;
import com.israel.studentmanagementsystem.mapper.StudentProfileMapper;
import com.israel.studentmanagementsystem.repository.StudentProfileRepository;
import com.israel.studentmanagementsystem.repository.UserRepository;
import com.israel.studentmanagementsystem.service.storage.S3StorageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;
    private final StudentProfileMapper studentProfileMapper;
    private final StudentNumberGenerator studentNumberGenerator;
    private final S3StorageService s3StorageService;

    @CacheEvict(value = CacheNames.STUDENT_PROFILE, key = "#email")
    @Transactional
    public StudentProfileResponse uploadAvatar(
            String email, MultipartFile file) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        StudentProfile profile = studentProfileRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));

        if (profile.getAvatarUrl() != null) {
            s3StorageService.deleteFile(profile.getAvatarUrl());
        }

        // upload new file to S3
        String avatarUrl = s3StorageService.uploadAvatar(file, profile.getId());

        // save URL in database
        profile.setAvatarUrl(avatarUrl);
        StudentProfile saved = studentProfileRepository.save(profile);

        return studentProfileMapper.toResponse(saved);
    }

    @Transactional
    public StudentProfile createProfile(User user) {

        StudentProfile profile = StudentProfile.builder()
                .user(user)
                .studentNumber(studentNumberGenerator.generate())
                .enrollmentDate(LocalDate.now())
                .currentGpa(0.0)
                .totalCredits(0)
                .status(StudentStatus.ACTIVE)
                .build();

        return studentProfileRepository.save(profile);
    }

    @Cacheable(value = CacheNames.STUDENT_PROFILE, key = "#email")
    @Transactional(readOnly = true)
    public StudentProfileResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        StudentProfile profile = studentProfileRepository
                .findByUserIdWithUser(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));

        return studentProfileMapper.toResponse(profile);
    }

    @CacheEvict(value = CacheNames.STUDENT_PROFILE, key = "#email")
    @Transactional
    public StudentProfileResponse updateMyProfile(
            String email,
            UpdateStudentProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        StudentProfile profile = studentProfileRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));

        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        userRepository.save(user);
        StudentProfile saved = studentProfileRepository.save(profile);
        return studentProfileMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public StudentProfileResponse getStudentById(Long studentProfileId) {

        StudentProfile profile = studentProfileRepository
                .findById(studentProfileId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("StudentProfile", studentProfileId));

        return studentProfileMapper.toResponse(profile);
    }


    @Transactional(readOnly = true)
    public List<StudentProfileResponse> getAllStudents() {
        return studentProfileRepository.findAll()
                .stream()
                .map(studentProfileMapper::toResponse)
                .toList();
    }
}

package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.repository.TeacherProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class EmployeeNumberGenerator {
    private final TeacherProfileRepository teacherProfileRepository;

    public String generate() {
        int year = Year.now().getValue();
        long count = teacherProfileRepository.count() + 1;
        return String.format("EMP-%d-%04d", year, count);
    }
}

package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class StudentNumberGenerator {
    private final StudentProfileRepository studentProfileRepository;

    public String generate() {
        int year = Year.now().getValue();
        long count = studentProfileRepository.count() + 1;
        return String.format("STU-%d-%04d", year, count);
    }
}

package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.entity.Enrollment;
import com.israel.studentmanagementsystem.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GpaService {
    private final EnrollmentRepository enrollmentRepository;

    private static final Map<String, Double> GRADE_POINTS = Map.of(
            "A+", 4.0,
            "A",  4.0,
            "A-", 3.7,
            "B+", 3.3,
            "B",  3.0,
            "B-", 2.7,
            "C+", 2.3,
            "C",  2.0,
            "D",  1.0,
            "F",  0.0
    );

    public Double toGradePoints(String letterGrade) {
        return GRADE_POINTS.getOrDefault(
                letterGrade.toUpperCase(), 0.0);
    }

    public Double calculateGpa(Long studentId) {

        List<Enrollment> completed =
                enrollmentRepository.findCompletedWithGrades(studentId);

        if (completed.isEmpty()) return 0.0;

        double totalWeightedPoints = 0.0;
        int totalCredits = 0;

        for (Enrollment e : completed) {
            int credits = e.getCourse().getCredits();
            double points = e.getGradePoints();
            totalWeightedPoints += points * credits;
            totalCredits += credits;
        }

        if (totalCredits == 0) return 0.0;

        // round to 2 decimal places
        double gpa = totalWeightedPoints / totalCredits;
        return Math.round(gpa * 100.0) / 100.0;
    }
}

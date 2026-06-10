package com.israel.studentmanagementsystem.entity;


import com.israel.studentmanagementsystem.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "student_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String studentNumber;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    @Column(nullable = false)
    private Double currentGpa;

    @Column(nullable = false)
    private Integer totalCredits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status;

    @Version
    private Integer version;

    @Column(length = 500)
    private String avatarUrl;
}






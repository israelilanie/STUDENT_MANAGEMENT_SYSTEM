package com.israel.studentmanagementsystem.entity;

import com.israel.studentmanagementsystem.enums.TeacherTitle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String employeeNumber;

    private String department;

    @Enumerated(EnumType.STRING)
    private TeacherTitle title;

    private String specialization;

    private String officeHours;

    @Version
    private Integer version;
}

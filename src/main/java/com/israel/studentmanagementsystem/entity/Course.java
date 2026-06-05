package com.israel.studentmanagementsystem.entity;

import com.israel.studentmanagementsystem.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(nullable = false)
    private Integer currentEnrollment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    private String semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private TeacherProfile teacher;

    @Version
    private Integer version;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentEnrollment == null) currentEnrollment = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public boolean isFull() {
        return currentEnrollment >= maxCapacity;
    }

    public void incrementEnrollment() {
        if (isFull()) throw new IllegalStateException(
                "Course is at full capacity");
        this.currentEnrollment++;
    }

    public void decrementEnrollment() {
        if (this.currentEnrollment > 0) this.currentEnrollment--;
    }
}

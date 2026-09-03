package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.entity.Course;
import com.israel.studentmanagementsystem.entity.Enrollment;
import com.israel.studentmanagementsystem.repository.EnrollmentRepository;
import com.israel.studentmanagementsystem.repository.StudentProfileRepository;
import com.israel.studentmanagementsystem.repository.TeacherProfileRepository;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoreServicesTest {

    @Test
    void generatorsUseCurrentYearAndNextRepositoryCount() {
        StudentProfileRepository students = mock(StudentProfileRepository.class);
        TeacherProfileRepository teachers = mock(TeacherProfileRepository.class);
        when(students.count()).thenReturn(41L);
        when(teachers.count()).thenReturn(7L);

        assertThat(new StudentNumberGenerator(students).generate())
                .isEqualTo("STU-" + Year.now().getValue() + "-0042");
        assertThat(new EmployeeNumberGenerator(teachers).generate())
                .isEqualTo("EMP-" + Year.now().getValue() + "-0008");
    }

    @Test
    void calculatesRoundedGpaAndHandlesNoCredits() {
        EnrollmentRepository repository = mock(EnrollmentRepository.class);
        GpaService service = new GpaService(repository);
        Course threeCredits = Course.builder().credits(3).build();
        Course oneCredit = Course.builder().credits(1).build();
        when(repository.findCompletedWithGrades(5L)).thenReturn(List.of(
                Enrollment.builder().course(threeCredits).gradePoints(3.7).build(),
                Enrollment.builder().course(oneCredit).gradePoints(4.0).build()));
        when(repository.findCompletedWithGrades(6L)).thenReturn(List.of(
                Enrollment.builder().course(Course.builder().credits(0).build()).gradePoints(4.0).build()));
        when(repository.findCompletedWithGrades(7L)).thenReturn(List.of());

        assertThat(service.calculateGpa(5L)).isEqualTo(3.78);
        assertThat(service.calculateGpa(6L)).isZero();
        assertThat(service.calculateGpa(7L)).isZero();
        assertThat(service.toGradePoints("a-")).isEqualTo(3.7);
        assertThat(service.toGradePoints("unknown")).isZero();
    }

    @Test
    void rateLimitServiceReusesBucketForSameKey() {
        RateLimitService service = new RateLimitService();
        Bucket bucket = service.resolveBucket("127.0.0.1");

        assertThat(service.resolveBucket("127.0.0.1")).isSameAs(bucket);
        assertThat(service.resolveBucket("127.0.0.2")).isNotSameAs(bucket);
    }
}

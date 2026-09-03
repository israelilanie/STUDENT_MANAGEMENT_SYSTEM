package com.israel.studentmanagementsystem.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class CourseTest {

    @Test
    void managesCapacityAndNeverDecrementsBelowZero() {
        Course course = Course.builder().maxCapacity(2).currentEnrollment(1).build();

        assertThat(course.isFull()).isFalse();
        course.incrementEnrollment();
        assertThat(course.isFull()).isTrue();
        assertThatIllegalStateException().isThrownBy(course::incrementEnrollment)
                .withMessage("Course is at full capacity");
        course.decrementEnrollment();
        course.decrementEnrollment();
        course.decrementEnrollment();
        assertThat(course.getCurrentEnrollment()).isZero();
    }
}

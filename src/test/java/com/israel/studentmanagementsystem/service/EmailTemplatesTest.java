package com.israel.studentmanagementsystem.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTemplatesTest {

    @Test
    void rendersAllTransactionalEmailsWithProvidedValues() {
        assertThat(EmailTemplates.welcomeEmail("Ada")).contains("Welcome, Ada!");
        assertThat(EmailTemplates.enrollmentEmail("Ada", "Algorithms", "CS101", null))
                .contains("Hi Ada,", "Algorithms", "CS101", "N/A");
        assertThat(EmailTemplates.gradeEmail("Ada", "Algorithms", "CS101", "A", 4.0, 3.75))
                .contains("A", "4.0 points", "3.75");
        assertThat(EmailTemplates.dropEmail("Ada", "Algorithms", "CS101"))
                .contains("Course Dropped", "Algorithms", "CS101");
    }
}

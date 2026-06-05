CREATE TABLE enrollments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             student_id BIGINT NOT NULL,
                             course_id BIGINT NOT NULL,
                             status VARCHAR(50) NOT NULL DEFAULT 'ENROLLED',
                             enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             dropped_at TIMESTAMP,
                             final_grade VARCHAR(5),
                             grade_points DOUBLE,
                             version INTEGER DEFAULT 0,
                             CONSTRAINT fk_enrollment_student
                                 FOREIGN KEY (student_id) REFERENCES student_profiles(id),
                             CONSTRAINT fk_enrollment_course
                                 FOREIGN KEY (course_id) REFERENCES courses(id),
                             CONSTRAINT uq_enrollment
                                 UNIQUE (student_id, course_id)
);
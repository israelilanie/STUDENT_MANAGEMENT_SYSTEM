CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         code VARCHAR(20) NOT NULL UNIQUE,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         credits INTEGER NOT NULL,
                         max_capacity INTEGER NOT NULL,
                         current_enrollment INTEGER NOT NULL DEFAULT 0,
                         status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                         semester VARCHAR(100),
                         teacher_id BIGINT,
                         version INTEGER DEFAULT 0,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_course_teacher
                             FOREIGN KEY (teacher_id) REFERENCES teacher_profiles(id)
);
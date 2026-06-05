CREATE TABLE student_profiles (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  user_id BIGINT NOT NULL UNIQUE,
                                  student_number VARCHAR(20) NOT NULL UNIQUE,
                                  date_of_birth DATE,
                                  enrollment_date DATE NOT NULL,
                                  current_gpa DOUBLE DEFAULT 0.0,
                                  total_credits INTEGER DEFAULT 0,
                                  status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                                  version INTEGER DEFAULT 0,
                                  CONSTRAINT fk_student_user
                                      FOREIGN KEY (user_id) REFERENCES users(id)
);
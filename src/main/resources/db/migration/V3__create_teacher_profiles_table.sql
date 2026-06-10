CREATE TABLE teacher_profiles (
                                  id BIGSERIAL PRIMARY KEY,
                                  user_id BIGINT NOT NULL UNIQUE,
                                  employee_number VARCHAR(20) NOT NULL UNIQUE,
                                  department VARCHAR(100),
                                  title VARCHAR(50),
                                  specialization VARCHAR(255),
                                  office_hours VARCHAR(255),
                                  version INTEGER DEFAULT 0,
                                  CONSTRAINT fk_teacher_user
                                      FOREIGN KEY (user_id) REFERENCES users(id)
);
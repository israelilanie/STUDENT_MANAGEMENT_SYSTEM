package com.israel.studentmanagementsystem.security;

import com.israel.studentmanagementsystem.dto.request.CreateCourseRequest;
import com.israel.studentmanagementsystem.dto.request.CreateTeacherRequest;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.enums.Role;
import com.israel.studentmanagementsystem.enums.TeacherTitle;
import com.israel.studentmanagementsystem.enums.UserStatus;
import com.israel.studentmanagementsystem.repository.CourseRepository;
import com.israel.studentmanagementsystem.repository.UserRepository;
import com.israel.studentmanagementsystem.service.CourseService;
import com.israel.studentmanagementsystem.service.StudentService;
import com.israel.studentmanagementsystem.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class AdminSeeder implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final StudentService studentService;

    @Override
    public void run(String... args) throws Exception {
        seedAdmin();
        seedTeacher();
        seedStudent();
        seedCourses();
    }
    private void seedCourses() {
        if (courseRepository.existsByCode("CS101")) {
            log.info("Courses already seeded — skipping");
            return;
        }

        User teacher = userRepository.findByEmail("teacher@sms.com")
                .orElse(null);

        if (teacher == null) return;

        String[][] courses = {
                {"CS101", "Introduction to Programming", "3", "30", "Fall 2026"},
                {"CS201", "Data Structures", "4", "25", "Fall 2026"},
                {"MATH101", "Calculus I", "4", "35", "Fall 2026"},
                {"ENG101", "Technical Writing", "2", "40", "Fall 2026"},
                {"CS301", "Database Systems", "3", "20", "Fall 2026"}
        };

        for (String[] c : courses) {
            CreateCourseRequest req = new CreateCourseRequest();
            req.setCode(c[0]);
            req.setTitle(c[1]);
            req.setCredits(Integer.parseInt(c[2]));
            req.setMaxCapacity(Integer.parseInt(c[3]));
            req.setSemester(c[4]);
            courseService.createCourse(req, teacher.getId());
        }

        log.info("5 courses seeded successfully");
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin@sms.com")) {
            log.info("Admin already exists — skipping");
            return;
        }

        User admin = User.builder()
                .email("admin@sms.com")
                .password(passwordEncoder.encode("admin123"))
                .firstName("System")
                .lastName("Admin")
                .role(Role.ROLE_ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(admin);
        log.info("Admin created — email: admin@sms.com password: admin123");
    }

    private void seedTeacher() {
        if (userRepository.existsByEmail("teacher@sms.com")) {
            log.info("Teacher already exists — skipping");
            return;
        }

        CreateTeacherRequest request = new CreateTeacherRequest();
        request.setEmail("teacher@sms.com");
        request.setPassword("teacher123");
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setDepartment("Computer Science");
        request.setTitle(TeacherTitle.PROFESSOR);
        request.setSpecialization("Software Engineering");
        request.setOfficeHours("Mon-Wed 2pm-4pm");

        teacherService.createTeacher(request);
        log.info("Teacher created — email: teacher@sms.com password: teacher123");
    }

    private void seedStudent() {
        if (userRepository.existsByEmail("student@sms.com")) {
            log.info("Student already exists — skipping");
            return;
        }

        User student = User.builder()
                .email("student@sms.com")
                .password(passwordEncoder.encode("student123"))
                .firstName("Jane")
                .lastName("Doe")
                .role(Role.ROLE_STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(student);
        studentService.createProfile(student);
        log.info("Student created — email: student@sms.com password: student123");
    }
}

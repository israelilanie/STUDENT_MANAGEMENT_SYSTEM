package com.israel.studentmanagementsystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    @Async
    public void sendWelcomeEmail(String toEmail, String firstName) {
        sendEmail(
                toEmail,
                "Welcome to Student Management System",
                EmailTemplates.welcomeEmail(firstName)
        );
    }

    @Async
    public void sendEnrollmentEmail(
            String toEmail,
            String firstName,
            String courseTitle,
            String courseCode,
            String semester) {
        sendEmail(
                toEmail,
                "Enrollment Confirmed — " + courseTitle,
                EmailTemplates.enrollmentEmail(
                        firstName, courseTitle, courseCode, semester)
        );
    }

    @Async
    public void sendGradeEmail(
            String toEmail,
            String firstName,
            String courseTitle,
            String courseCode,
            String letterGrade,
            Double gradePoints,
            Double newGpa) {
        sendEmail(
                toEmail,
                "Grade Posted — " + courseTitle,
                EmailTemplates.gradeEmail(
                        firstName, courseTitle, courseCode,
                        letterGrade, gradePoints, newGpa)
        );
    }

    @Async
    public void sendDropEmail(
            String toEmail,
            String firstName,
            String courseTitle,
            String courseCode) {
        sendEmail(
                toEmail,
                "Course Dropped — " + courseTitle,
                EmailTemplates.dropEmail(firstName, courseTitle, courseCode)
        );
    }

    private void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = html

            mailSender.send(message);
            log.info("Email sent to {} — subject: {}", to, subject);

        } catch (MessagingException e) {
            // never crash the main flow because email failed
            // log it and move on
            log.error("Failed to send email to {} — reason: {}",
                    to, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending email to {}: {}",
                    to, e.getMessage());
        }
    }
}

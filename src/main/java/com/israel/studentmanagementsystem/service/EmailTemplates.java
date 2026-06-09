package com.israel.studentmanagementsystem.service;

public final class EmailTemplates {

    private EmailTemplates() {}

    public static String welcomeEmail(String firstName) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background:#1F3864; padding:20px; text-align:center;">
                    <h1 style="color:white; margin:0;">Student Management System</h1>
                </div>
                <div style="padding:30px;">
                    <h2>Welcome, %s!</h2>
                    <p>Your student account has been created successfully.</p>
                    <p>You can now:</p>
                    <ul>
                        <li>Browse available courses</li>
                        <li>Enroll in courses</li>
                        <li>Track your grades and GPA</li>
                    </ul>
                    <p>Log in at any time to get started.</p>
                    <br>
                    <p style="color:#888; font-size:12px;">
                        This is an automated message. Please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(firstName);
    }

    public static String enrollmentEmail(
            String firstName,
            String courseTitle,
            String courseCode,
            String semester) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background:#1F3864; padding:20px; text-align:center;">
                    <h1 style="color:white; margin:0;">Enrollment Confirmation</h1>
                </div>
                <div style="padding:30px;">
                    <h2>Hi %s,</h2>
                    <p>You have successfully enrolled in the following course:</p>
                    <div style="background:#f5f5f5; padding:20px; border-radius:8px; margin:20px 0;">
                        <h3 style="margin:0 0 10px;">%s</h3>
                        <p style="margin:5px 0;"><strong>Code:</strong> %s</p>
                        <p style="margin:5px 0;"><strong>Semester:</strong> %s</p>
                    </div>
                    <p>Good luck with your studies!</p>
                    <br>
                    <p style="color:#888; font-size:12px;">
                        This is an automated message. Please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(firstName, courseTitle, courseCode,
                semester != null ? semester : "N/A");
    }

    public static String gradeEmail(
            String firstName,
            String courseTitle,
            String courseCode,
            String letterGrade,
            Double gradePoints,
            Double newGpa) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background:#1F3864; padding:20px; text-align:center;">
                    <h1 style="color:white; margin:0;">Grade Posted</h1>
                </div>
                <div style="padding:30px;">
                    <h2>Hi %s,</h2>
                    <p>A grade has been posted for the following course:</p>
                    <div style="background:#f5f5f5; padding:20px; border-radius:8px; margin:20px 0;">
                        <h3 style="margin:0 0 10px;">%s (%s)</h3>
                        <p style="margin:5px 0;">
                            <strong>Final Grade:</strong>
                            <span style="font-size:24px; color:#1F3864; font-weight:bold;">
                                %s
                            </span>
                            (%.1f points)
                        </p>
                        <p style="margin:5px 0;">
                            <strong>Updated GPA:</strong> %.2f
                        </p>
                    </div>
                    <p>Keep up the great work!</p>
                    <br>
                    <p style="color:#888; font-size:12px;">
                        This is an automated message. Please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(firstName, courseTitle, courseCode,
                letterGrade, gradePoints, newGpa);
    }

    public static String dropEmail(
            String firstName,
            String courseTitle,
            String courseCode) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background:#1F3864; padding:20px; text-align:center;">
                    <h1 style="color:white; margin:0;">Course Dropped</h1>
                </div>
                <div style="padding:30px;">
                    <h2>Hi %s,</h2>
                    <p>You have successfully dropped the following course:</p>
                    <div style="background:#f5f5f5; padding:20px; border-radius:8px; margin:20px 0;">
                        <h3 style="margin:0 0 10px;">%s</h3>
                        <p style="margin:5px 0;"><strong>Code:</strong> %s</p>
                    </div>
                    <p>If this was a mistake please contact your administrator.</p>
                    <br>
                    <p style="color:#888; font-size:12px;">
                        This is an automated message. Please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(firstName, courseTitle, courseCode);
    }
}
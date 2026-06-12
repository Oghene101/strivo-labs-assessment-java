package com.strivolabs.strivolabsassessmentjava.emails;

public final class EmailTemplate {

    private EmailTemplate() {
    }

    public static String getConfirmationEmail(String firstName, String confirmationLink) {
        return String.format(
                """
                            <html>
                                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                    <h2>Hello %s,</h2>
                                    <p>Thank you for registering with Strivo Labs! Please verify your email address to activate your account.</p>
                                    <div style="margin: 24px 0;">
                                        <a href="%s" style="background-color: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;">
                                            Confirm Email Address
                                        </a>
                                    </div>
                                    <hr style="border: none; border-top: 1px solid #eee; margin-top: 30px;" />
                                    <p style="font-size: 11px; color: #999;">This verification link will expire shortly.</p>
                                </body>
                            </html>
                        """,
                firstName, confirmationLink);
    }

    public static String getWelcomeEmail(String firstName) {
        return String.format(
                """
                        <html>
                            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                <h2>Welcome to Strivo Labs, %s!</h2>
                                <p>Your email address has been verified successfully.</p>
                                <p>Your account is now fully active. You can log into your app and begin to explore our services.</p>
                                <br/>
                                <p>Best regards,</p>
                                <p><strong>The Strivo Labs Team</strong></p>
                            </body>
                        </html>
                        """,
                firstName);
    }

    public static String getResetPasswordEmail(String firstName, String resetPasswordLink) {
        return String.format(
                """
                            <html>
                                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                    <h2>Hello %s,</h2>
                                    <p>We received a request to reset your Strivo Labs password. Click the button below to choose a new one.</p>
                                    <div style="margin: 24px 0;">
                                        <a href="%s" style="background-color: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;">
                                            Reset Password
                                        </a>
                                    </div>
                                    <p>If you didn't request a password reset, you can safely ignore this email — your password will remain unchanged.</p>
                                    <hr style="border: none; border-top: 1px solid #eee; margin-top: 30px;" />
                                    <p style="font-size: 11px; color: #999;">This link will expire shortly. Do not share it with anyone.</p>
                                </body>
                            </html>
                        """,
                firstName, resetPasswordLink);
    }
}

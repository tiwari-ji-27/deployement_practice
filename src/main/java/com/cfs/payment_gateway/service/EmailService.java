//package com.cfs.payment_gateway.service;
//
//import com.cfs.payment_gateway.entity.PaymentOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
///**
// * Service to handle sending transactional emails,
// * specifically for payment success confirmations/enrollment.
// */
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    /**
//     * Sends a simple payment confirmation/enrollment email.
//     *
//     * @param toEmail The recipient's email address.
//     * @param courseName The name of the course purchased (e.g., "ultimate placement way dsa course").
//     * @param order The PaymentOrder entity containing transaction details.
//     */
//    public void sendEmail(String toEmail, String courseName, PaymentOrder order) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//
//            message.setFrom("YOUR_SENDER_EMAIL@gmail.com");
//            message.setTo(toEmail);
//            // Changed subject to better reflect enrollment
//            message.setSubject("Enrollment Confirmation: STAR 3.0 BY TIWARI_JI");
//
//            // --- MODIFIED BODY START ---
//            // New personalized enrollment message
//            String body = String.format(
//                    "Dear Learner,\n\n" +
//                            "Congratulations! You have successfully enrolled in **STAR 3.0 BY TIWARI_JI** Ultimate Placement Way %s.\n\n" +
//                            "Your payment of ₹%.2f has been processed successfully.\n\n" +
//                            "Transaction Details:\n" +
//                            "Order ID: %s\n" +
//                            "Payment ID: %s\n" +
//                            "Status: %s\n\n" +
//                            "Get ready to code your way to success!\n\n" +
//                            "Thank you for choosing us!",
//                    // Parameters: Course Name (DSA), Amount, Order ID, Payment ID, Status
//                    courseName,
//                    order.getAmount(),
//                    order.getOrderId(),
//                    order.getPaymentId(),
//                    order.getStatus()
//            );
//            // --- MODIFIED BODY END ---
//
//            message.setText(body);
//
//            mailSender.send(message);
//            System.out.println("Enrollment email sent successfully to: " + toEmail);
//
//        } catch (Exception e) {
//            System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
//        }
//    }
//}
package com.cfs.payment_gateway.service;

import com.cfs.payment_gateway.entity.PaymentOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service to handle sending transactional emails,
 * specifically for payment success confirmations/enrollment.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a simple payment confirmation/enrollment email.
     *
     * @param toEmail The recipient's email address.
     * @param courseName The name of the course purchased (e.g., "DSA course").
     * @param order The PaymentOrder entity containing transaction details.
     */
    public void sendEmail(String toEmail, String courseName, PaymentOrder order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // Set the sender email (must match the username in application.properties)
            message.setFrom("YOUR_SENDER_EMAIL@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Enrollment Confirmation: STAR 3.0 BY TIWARI_JI");

            // Custom enrollment message body
            String body = String.format(
                    "Dear Learner,\n\n" +
                            "Congratulations! You have successfully enrolled in **STAR 3.0 BY TIWARI_JI** Ultimate Placement Way %s.\n\n" +
                            "Your payment of ₹%.2f has been processed successfully.\n\n" +
                            "Transaction Details:\n" +
                            "Order ID: %s\n" +
                            "Payment ID: %s\n" +
                            "Status: %s\n\n" +
                            "Get ready to code your way to success!\n\n" +
                            "Thank you for choosing us!",
                    // Parameters for the body text
                    courseName,
                    order.getAmount(),
                    order.getOrderId(),
                    order.getPaymentId(),
                    order.getStatus()
            );

            message.setText(body);

            mailSender.send(message);
            System.out.println("Enrollment email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
            // In a production application, you should log the full stack trace
        }
    }
}
//package com.cfs.payment_gateway.service;
//
//import com.cfs.payment_gateway.entity.PaymentOrder;
//import com.cfs.payment_gateway.repo.PaymentRepo;
//import com.razorpay.Order;
//// CRITICAL FIX: Missing imports for Razorpay classes
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
///**
// * The core business logic layer of the Payment Gateway.
// * Handles Razorpay order creation and status updates.
// * (Based on screenshots 2.58.58 PM.jpeg, 2.59.06 PM.jpeg, and 3.02.28 PM.jpeg)
// */
//@Service
//public class PaymentService {
//
//    // Keys fetched from application.properties
//    @Value("${razorpay.key_id}")
//    private String keyId;
//
//    @Value("${razorpay.key_secret}")
//    private String keySecret;
//
//    // Repository injected for database persistence
//    @Autowired
//    private PaymentRepo paymentRepo;
//    @Autowired
//    private EmailService emailService;
//    /**
//     * Creates a new order on the Razorpay platform and saves the initial record to the database.
//     * Maps to the /create-order API.
//     * @param orderDetails The entity containing order details (amount, receipt, etc.).
//     * @return The JSON string of the created Razorpay Order.
//     * @throws RazorpayException If the API call to Razorpay fails.
//     */
//    public String createOrder(PaymentOrder orderDetails) throws RazorpayException {
//        // Initialize Razorpay client using configured keys
//        RazorpayClient client = new RazorpayClient(keyId, keySecret);
//
//        // 1. Create the JSON request payload for Razorpay
//        JSONObject orderRequest = new JSONObject();
//
//        // Amount must be in the smallest currency unit (e.g., paise for INR)
//        // Note: The screenshot code uses a double cast, which might lose precision.
//        // We use Math.round for safer conversion here, but input should ideally be an integer type.
//        long amountInPaise = Math.round(orderDetails.getAmount() * 100);
//
//        orderRequest.put("amount", amountInPaise);
//        orderRequest.put("currency", "INR");
//
//        // Use a unique receipt ID
//        String receiptId = "txn_" + UUID.randomUUID();
//        orderRequest.put("receipt", receiptId);
//
//        // Log the JSON request for debugging
//        System.out.println("Razorpay Order Request: " + orderRequest.toString());
//
//        // 2. Call Razorpay API to create the order
//        Order razorpayOrder = client.orders.create(orderRequest);
//
//        // Log the response from Razorpay
//        System.out.println("Razorpay Order Response: " + razorpayOrder.toString());
//
//        // 3. Persist the order details locally
//        orderDetails.setOrderId(razorpayOrder.get("id")); // Set the ID returned by Razorpay
//        orderDetails.setReceipt(receiptId);
//        orderDetails.setStatus("CREATED");
//        orderDetails.setCreatedAt(LocalDateTime.now());
//
//        // Save the pending order in the local database
//        paymentRepo.save(orderDetails);
//
//        // Return the Razorpay order object (as a string) to the client
//        return razorpayOrder.toString();
//    }
//
//    /**
//     * Updates the payment status in the database based on a callback from the client/webhook.
//     * Maps to the /update-order API.
//     * @param paymentId The Razorpay Payment ID.
//     * @param orderId The Razorpay Order ID.
//     * @param status The final status ("SUCCESS" or "FAILED").
//     */
//    public void updateOrderStatus(String paymentId, String orderId, String status) {
//        // Find the existing order record by the Order ID
//        PaymentOrder order = paymentRepo.findByOrderId(orderId);
//
//        if (order != null) {
//            // Update the fields
//            order.setPaymentId(paymentId);
//            order.setStatus(status);
//
//            // Save the updated status to the database
//            paymentRepo.save(order);
//
//            // Check for success status (case-insensitive)
//            if ("SUCCESS".equalsIgnoreCase(order.getStatus())) {
//                // Send email service call (matching the screenshot logic)
//                emailService.sendEmail(
//                        order.getEmail(),        // Assuming PaymentOrder has getEmail()
//                        order.getCourseName(),    // Assuming PaymentOrder has getCourseName()
//                        order
//                );
//                System.out.println("SUCCESSFUL PAYMENT DETECTED. Enrollment email triggered.");
//            }
//        } else {
//            System.err.println("Error: Order not found for Order ID: " + orderId);
//        }
//    }
//}

package com.cfs.payment_gateway.service;

import com.cfs.payment_gateway.entity.PaymentOrder;
import com.cfs.payment_gateway.repo.PaymentRepo;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * The core business logic layer of the Payment Gateway.
 * Handles Razorpay order creation and status updates.
 */
@Service
public class PaymentService {

    // Keys fetched from application.properties
    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    // Repository injected for database persistence
    @Autowired
    private PaymentRepo paymentRepo;

    // EmailService injected for sending notifications
    @Autowired
    private EmailService emailService;

    /**
     * Creates a new order on the Razorpay platform and saves the initial record to the database.
     * Maps to the /create-order API.
     * @param orderDetails The entity containing order details (amount, receipt, email, courseName, etc.).
     * @return The JSON string of the created Razorpay Order.
     * @throws RazorpayException If the API call to Razorpay fails.
     */
    public String createOrder(PaymentOrder orderDetails) throws RazorpayException {
        // Initialize Razorpay client using configured keys
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        // 1. Create the JSON request payload for Razorpay
        JSONObject orderRequest = new JSONObject();

        // Amount must be in the smallest currency unit (e.g., paise for INR)
        long amountInPaise = Math.round(orderDetails.getAmount() * 100);

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");

        // Use a unique receipt ID
        String receiptId = "txn_" + UUID.randomUUID();
        orderRequest.put("receipt", receiptId);

        System.out.println("Razorpay Order Request: " + orderRequest.toString());

        // 2. Call Razorpay API to create the order
        Order razorpayOrder = client.orders.create(orderRequest);

        System.out.println("Razorpay Order Response: " + razorpayOrder.toString());

        // 3. Persist the order details locally
        orderDetails.setOrderId(razorpayOrder.get("id")); // Set the ID returned by Razorpay
        orderDetails.setReceipt(receiptId);
        orderDetails.setStatus("CREATED");
        orderDetails.setCreatedAt(LocalDateTime.now());

        // Save the pending order (including email and courseName) in the local database
        paymentRepo.save(orderDetails);

        // Return the Razorpay order object (as a string) to the client
        return razorpayOrder.toString();
    }

    /**
     * Updates the payment status in the database based on a callback from the client/webhook.
     * Maps to the /update-order API.
     * @param paymentId The Razorpay Payment ID.
     * @param orderId The Razorpay Order ID.
     * @param status The final status ("SUCCESS" or "FAILED").
     */
    public void updateOrderStatus(String paymentId, String orderId, String status) {
        // Find the existing order record by the Order ID
        PaymentOrder order = paymentRepo.findByOrderId(orderId);

        if (order != null) {
            // Update the fields
            order.setPaymentId(paymentId);
            order.setStatus(status);

            // Save the updated status to the database
            paymentRepo.save(order);

            // Check for success status (case-insensitive)
            if ("SUCCESS".equalsIgnoreCase(order.getStatus())) {
                // Trigger email service call using saved data (as per your screenshot)
                emailService.sendEmail(
                        order.getEmail(),
                        order.getCourseName(),
                        order
                );
                System.out.println("SUCCESSFUL PAYMENT DETECTED. Enrollment email triggered.");
            }
        } else {
            System.err.println("Error: Order not found for Order ID: " + orderId);
        }
    }
}

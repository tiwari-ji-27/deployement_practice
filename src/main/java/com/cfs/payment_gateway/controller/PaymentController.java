package com.cfs.payment_gateway.controller;

import com.cfs.payment_gateway.entity.PaymentOrder;
import com.cfs.payment_gateway.service.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for handling incoming payment API requests.
 * Exposes the two required endpoints: /create-order and /update-order.
 * (Based on screenshots 2.59.50 PM.jpeg and 3.15.44 PM.jpeg)
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * API to create a payment order in the gateway and with Razorpay.
     * Endpoint: POST http://localhost:8080/api/payment/create-order
     * @param order The PaymentOrder entity containing the initial details (e.g., amount).
     * @return The JSON response from Razorpay.
     */
    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody PaymentOrder order) {
        try {
            // Delegate the order creation to the service layer
            String response = paymentService.createOrder(order);
            // Return the Razorpay JSON response directly
            return ResponseEntity.ok(response);

        } catch (RazorpayException e) {
            // Handle Razorpay specific errors
            System.err.println("Razorpay Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating order with payment processor: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating order.");
        }
    }

    /**
     * API to update the status of a payment order after successful payment
     * (usually called by the client application post-payment, or a webhook).
     * Endpoint: POST http://localhost:8080/api/payment/update-order
     * @param paymentId The unique ID of the payment transaction.
     * @param orderId The unique ID of the order.
     * @param status The final transaction status.
     * @return A confirmation message.
     */
    @PostMapping("/update-order")
    public ResponseEntity<String> updateOrderStatus(
            @RequestParam String paymentId,
            @RequestParam String orderId,
            @RequestParam String status) {
        try {
            // Delegate status update to the service layer
            paymentService.updateOrderStatus(paymentId, orderId, status);

            System.out.println("Order place successfully....");
            return ResponseEntity.ok("Order updated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating order status: " + e.getMessage());
        }
    }
}
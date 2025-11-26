package com.cfs.payment_gateway.repo;


import com.cfs.payment_gateway.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing PaymentOrder entities.
 * (Based on screenshot 2.57.45 PM.jpeg)
 */
@Repository
public interface PaymentRepo extends JpaRepository<PaymentOrder, Long> {

    /**
     * Custom method to find a PaymentOrder by its Razorpay Order ID.
     * This is used during the /update-order callback.
     * @param orderId The Razorpay Order ID.
     * @return The found PaymentOrder entity.
     */
    PaymentOrder findByOrderId(String orderId);
}

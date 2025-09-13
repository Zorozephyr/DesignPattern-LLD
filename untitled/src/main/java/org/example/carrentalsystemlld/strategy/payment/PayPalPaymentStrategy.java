package org.example.carrentalsystemlld.strategy.payment;

import org.example.carrentalsystemlld.model.Payment;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Strategy for processing PayPal payments.
 * Simulates PayPal payment processing with email validation.
 */
public class PayPalPaymentStrategy implements PaymentStrategy {
    
    private static final Logger logger = Logger.getLogger(PayPalPaymentStrategy.class.getName());
    
    /**
     * Inner class to represent PayPal payment details.
     */
    public static class PayPalDetails {
        private final String email;
        private final String password;
        
        public PayPalDetails(String email, String password) {
            this.email = email;
            this.password = password;
        }
        
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }
    
    @Override
    public boolean processPayment(Payment payment, Object paymentDetails) {
        if (!validatePaymentDetails(paymentDetails)) {
            payment.processPaymentFailure("Invalid PayPal credentials");
            logger.warning("PayPal payment failed: Invalid credentials for payment " + payment.getPaymentId());
            return false;
        }
        
        PayPalDetails paypalDetails = (PayPalDetails) paymentDetails;
        
        try {
            // Simulate payment processing
            logger.info("Processing PayPal payment for " + payment.getPaymentId());
            
            // Simulate PayPal API call
            boolean paymentSuccessful = simulatePayPalAPI(payment.getAmount(), paypalDetails);
            
            if (paymentSuccessful) {
                String transactionId = generateTransactionId();
                payment.processPaymentSuccess(transactionId);
                logger.info("PayPal payment successful: " + transactionId);
                return true;
            } else {
                payment.processPaymentFailure("PayPal payment failed - insufficient funds or account issue");
                logger.warning("PayPal payment failed for payment " + payment.getPaymentId());
                return false;
            }
            
        } catch (Exception e) {
            payment.processPaymentFailure("PayPal processing error: " + e.getMessage());
            logger.severe("PayPal payment error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean validatePaymentDetails(Object paymentDetails) {
        if (!(paymentDetails instanceof PayPalDetails)) {
            return false;
        }
        
        PayPalDetails paypalDetails = (PayPalDetails) paymentDetails;
        
        // Validate email format
        if (!isValidEmail(paypalDetails.getEmail())) {
            return false;
        }
        
        // Validate password (basic check)
        if (paypalDetails.getPassword() == null || paypalDetails.getPassword().length() < 6) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getPaymentMethodName() {
        return "PayPal";
    }
    
    @Override
    public double getProcessingFee(double amount) {
        return amount * 0.029 + 0.30; // PayPal's standard fee: 2.9% + $0.30
    }
    
    private boolean simulatePayPalAPI(double amount, PayPalDetails paypalDetails) {
        // Simulate 97% success rate (PayPal is generally more reliable)
        return Math.random() > 0.03;
    }
    
    private String generateTransactionId() {
        return "PP_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}

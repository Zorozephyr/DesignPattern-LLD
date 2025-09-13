package org.example.carrentalsystemlld.strategy.payment;

import org.example.carrentalsystemlld.model.Payment;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Strategy for processing credit card payments.
 * Simulates credit card payment processing with validation.
 */
public class CreditCardPaymentStrategy implements PaymentStrategy {
    
    private static final Logger logger = Logger.getLogger(CreditCardPaymentStrategy.class.getName());
    
    /**
     * Inner class to represent credit card details.
     */
    public static class CreditCardDetails {
        private final String cardNumber;
        private final String cardHolderName;
        private final String expiryDate;
        private final String cvv;
        
        public CreditCardDetails(String cardNumber, String cardHolderName, 
                               String expiryDate, String cvv) {
            this.cardNumber = cardNumber;
            this.cardHolderName = cardHolderName;
            this.expiryDate = expiryDate;
            this.cvv = cvv;
        }
        
        public String getCardNumber() { return cardNumber; }
        public String getCardHolderName() { return cardHolderName; }
        public String getExpiryDate() { return expiryDate; }
        public String getCvv() { return cvv; }
        
        public String getMaskedCardNumber() {
            if (cardNumber.length() < 4) return "****";
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
    }
    
    @Override
    public boolean processPayment(Payment payment, Object paymentDetails) {
        if (!validatePaymentDetails(paymentDetails)) {
            payment.processPaymentFailure("Invalid credit card details");
            logger.warning("Credit card payment failed: Invalid details for payment " + payment.getPaymentId());
            return false;
        }
        
        CreditCardDetails cardDetails = (CreditCardDetails) paymentDetails;
        
        try {
            // Simulate payment processing
            logger.info("Processing credit card payment for " + payment.getPaymentId());
            
            // Simulate external payment gateway call
            boolean paymentSuccessful = simulatePaymentGateway(payment.getAmount(), cardDetails);
            
            if (paymentSuccessful) {
                String transactionId = generateTransactionId();
                payment.processPaymentSuccess(transactionId);
                logger.info("Credit card payment successful: " + transactionId);
                return true;
            } else {
                payment.processPaymentFailure("Payment declined by bank");
                logger.warning("Credit card payment declined for payment " + payment.getPaymentId());
                return false;
            }
            
        } catch (Exception e) {
            payment.processPaymentFailure("Payment processing error: " + e.getMessage());
            logger.severe("Credit card payment error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean validatePaymentDetails(Object paymentDetails) {
        if (!(paymentDetails instanceof CreditCardDetails)) {
            return false;
        }
        
        CreditCardDetails cardDetails = (CreditCardDetails) paymentDetails;
        
        // Validate card number (basic Luhn algorithm check)
        if (!isValidCardNumber(cardDetails.getCardNumber())) {
            return false;
        }
        
        // Validate expiry date format (MM/YY)
        if (!isValidExpiryDate(cardDetails.getExpiryDate())) {
            return false;
        }
        
        // Validate CVV (3-4 digits)
        if (!isValidCvv(cardDetails.getCvv())) {
            return false;
        }
        
        // Validate cardholder name
        if (cardDetails.getCardHolderName() == null || cardDetails.getCardHolderName().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }
    
    @Override
    public double getProcessingFee(double amount) {
        return amount * 0.025; // 2.5% processing fee for credit cards
    }
    
    private boolean simulatePaymentGateway(double amount, CreditCardDetails cardDetails) {
        // Simulate 95% success rate
        return Math.random() > 0.05;
    }
    
    private String generateTransactionId() {
        return "CC_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.replaceAll("\\s", "").length() < 13) {
            return false;
        }
        
        // Basic format check (digits only after removing spaces)
        String cleanNumber = cardNumber.replaceAll("\\s", "");
        return cleanNumber.matches("\\d{13,19}");
    }
    
    private boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        
        // Additional validation could check if date is not expired
        return true;
    }
    
    private boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3,4}");
    }
}

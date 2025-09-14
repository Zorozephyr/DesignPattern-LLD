package org.example.lowleveldesignexamples.carrentalsystemlld.strategy.payment;

import org.example.lowleveldesignexamples.carrentalsystemlld.model.Payment;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Strategy for processing cash payments.
 * Simulates in-person cash payment processing.
 */
public class CashPaymentStrategy implements PaymentStrategy {
    
    private static final Logger logger = Logger.getLogger(CashPaymentStrategy.class.getName());
    
    /**
     * Inner class to represent cash payment details.
     */
    public static class CashDetails {
        private final double amountReceived;
        private final String employeeId;
        private final String locationId;
        
        public CashDetails(double amountReceived, String employeeId, String locationId) {
            this.amountReceived = amountReceived;
            this.employeeId = employeeId;
            this.locationId = locationId;
        }
        
        public double getAmountReceived() { return amountReceived; }
        public String getEmployeeId() { return employeeId; }
        public String getLocationId() { return locationId; }
        
        public double getChange(double billAmount) {
            return Math.max(0, amountReceived - billAmount);
        }
    }
    
    @Override
    public boolean processPayment(Payment payment, Object paymentDetails) {
        if (!validatePaymentDetails(paymentDetails)) {
            payment.processPaymentFailure("Invalid cash payment details");
            logger.warning("Cash payment failed: Invalid details for payment " + payment.getPaymentId());
            return false;
        }
        
        CashDetails cashDetails = (CashDetails) paymentDetails;
        
        try {
            // Check if received amount is sufficient
            if (cashDetails.getAmountReceived() < payment.getAmount()) {
                payment.processPaymentFailure("Insufficient cash received. Required: $" + 
                                            String.format("%.2f", payment.getAmount()) + 
                                            ", Received: $" + String.format("%.2f", cashDetails.getAmountReceived()));
                logger.warning("Cash payment failed: Insufficient amount for payment " + payment.getPaymentId());
                return false;
            }
            
            // Process cash payment
            logger.info("Processing cash payment for " + payment.getPaymentId());
            
            String transactionId = generateTransactionId(cashDetails);
            payment.processPaymentSuccess(transactionId);
            
            double change = cashDetails.getChange(payment.getAmount());
            if (change > 0) {
                logger.info("Change to return: $" + String.format("%.2f", change));
            }
            
            logger.info("Cash payment successful: " + transactionId);
            return true;
            
        } catch (Exception e) {
            payment.processPaymentFailure("Cash payment processing error: " + e.getMessage());
            logger.severe("Cash payment error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean validatePaymentDetails(Object paymentDetails) {
        if (!(paymentDetails instanceof CashDetails)) {
            return false;
        }
        
        CashDetails cashDetails = (CashDetails) paymentDetails;
        
        // Validate amount received
        if (cashDetails.getAmountReceived() <= 0) {
            return false;
        }
        
        // Validate employee ID
        if (cashDetails.getEmployeeId() == null || cashDetails.getEmployeeId().trim().isEmpty()) {
            return false;
        }
        
        // Validate location ID
        if (cashDetails.getLocationId() == null || cashDetails.getLocationId().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getPaymentMethodName() {
        return "Cash";
    }
    
    @Override
    public double getProcessingFee(double amount) {
        return 0.0; // No processing fee for cash payments
    }
    
    private String generateTransactionId(CashDetails cashDetails) {
        return "CASH_" + cashDetails.getLocationId() + "_" + 
               UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}

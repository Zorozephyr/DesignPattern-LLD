package org.example.lowleveldesignexamples.carrentalsystemlld.strategy.payment;

import org.example.lowleveldesignexamples.carrentalsystemlld.model.Payment;

import java.util.logging.Logger;

/**
 * Context class for payment processing strategies.
 * Provides a unified interface to execute different payment strategies.
 * 
 * Design Pattern: Strategy Pattern
 * Purpose: Allows runtime selection of payment methods and decouples
 * payment processing logic from the client code.
 */
public class PaymentContext {
    
    private static final Logger logger = Logger.getLogger(PaymentContext.class.getName());
    
    private PaymentStrategy paymentStrategy;
    
    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    /**
     * Sets a new payment strategy.
     * 
     * @param paymentStrategy The new payment strategy to use
     */
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    /**
     * Processes a payment using the current payment strategy.
     * 
     * @param payment The payment to process
     * @param paymentDetails Payment details (strategy-specific)
     * @return true if payment was successful, false otherwise
     */
    public boolean processPayment(Payment payment, Object paymentDetails) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        
        logger.info("Processing payment " + payment.getPaymentId() + 
                   " using " + paymentStrategy.getPaymentMethodName());
        
        // Add processing fee to the payment amount if applicable
        double processingFee = paymentStrategy.getProcessingFee(payment.getAmount());
        if (processingFee > 0) {
            logger.info("Processing fee applied: $" + String.format("%.2f", processingFee));
            // Note: In a real system, you might want to add this to the bill
        }
        
        return paymentStrategy.processPayment(payment, paymentDetails);
    }
    
    /**
     * Validates payment details using the current payment strategy.
     * 
     * @param paymentDetails Payment details to validate
     * @return true if details are valid, false otherwise
     */
    public boolean validatePaymentDetails(Object paymentDetails) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        
        return paymentStrategy.validatePaymentDetails(paymentDetails);
    }
    
    /**
     * Gets the name of the current payment strategy.
     * 
     * @return Strategy name
     */
    public String getCurrentPaymentMethod() {
        return paymentStrategy != null ? paymentStrategy.getPaymentMethodName() : "No payment method set";
    }
    
    /**
     * Gets the processing fee for the current payment method.
     * 
     * @param amount Payment amount
     * @return Processing fee
     */
    public double getProcessingFee(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        
        return paymentStrategy.getProcessingFee(amount);
    }
}

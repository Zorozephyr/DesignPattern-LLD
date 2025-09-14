package org.example.lowleveldesignexamples.carrentalsystemlld.strategy.payment;

import org.example.lowleveldesignexamples.carrentalsystemlld.model.Payment;

/**
 * Strategy interface for payment processing algorithms.
 * 
 * Design Pattern: Strategy Pattern
 * Purpose: Allows different payment methods to be used interchangeably
 * and enables easy addition of new payment processors without modifying existing code.
 */
public interface PaymentStrategy {
    
    /**
     * Processes the payment using the specific payment method.
     * 
     * @param payment The payment to process
     * @param paymentDetails Additional payment details (method-specific)
     * @return true if payment was successful, false otherwise
     */
    boolean processPayment(Payment payment, Object paymentDetails);
    
    /**
     * Validates the payment details before processing.
     * 
     * @param paymentDetails Payment details to validate
     * @return true if details are valid, false otherwise
     */
    boolean validatePaymentDetails(Object paymentDetails);
    
    /**
     * Gets the name of this payment strategy.
     * 
     * @return Strategy name
     */
    String getPaymentMethodName();
    
    /**
     * Gets the processing fee for this payment method.
     * 
     * @param amount Payment amount
     * @return Processing fee
     */
    double getProcessingFee(double amount);
}

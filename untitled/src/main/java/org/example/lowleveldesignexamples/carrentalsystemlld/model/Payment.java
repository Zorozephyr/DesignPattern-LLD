package org.example.lowleveldesignexamples.carrentalsystemlld.model;

import org.example.lowleveldesignexamples.carrentalsystemlld.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a payment transaction in the car rental system.
 * Tracks payment details and status.
 */
public class Payment {
    private String paymentId;
    private Bill bill;
    private double amount;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private LocalDateTime processedDate;
    private String transactionId;
    private String paymentDetails;
    private String failureReason;

    public Payment(String paymentId, Bill bill, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.bill = bill;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
        this.paymentDate = LocalDateTime.now();
    }

    /**
     * Processes the payment successfully.
     */
    public void processPaymentSuccess(String transactionId) {
        this.status = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;
        this.processedDate = LocalDateTime.now();
        this.bill.markAsPaid();
        generatePaymentDetails();
    }

    /**
     * Marks the payment as failed.
     */
    public void processPaymentFailure(String failureReason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = failureReason;
        this.processedDate = LocalDateTime.now();
        generatePaymentDetails();
    }

    /**
     * Processes a refund for this payment.
     */
    public void processRefund(String refundTransactionId) {
        if (status == PaymentStatus.COMPLETED) {
            this.status = PaymentStatus.REFUNDED;
            this.transactionId = refundTransactionId;
            this.processedDate = LocalDateTime.now();
            this.bill.setPaid(false);
            generatePaymentDetails();
        }
    }

    /**
     * Generates detailed payment information.
     */
    private void generatePaymentDetails() {
        StringBuilder details = new StringBuilder();
        details.append("PAYMENT DETAILS\n");
        details.append("===============\n");
        details.append("Payment ID: ").append(paymentId).append("\n");
        details.append("Bill ID: ").append(bill.getBillId()).append("\n");
        details.append("Amount: $").append(String.format("%.2f", amount)).append("\n");
        details.append("Payment Method: ").append(paymentMethod).append("\n");
        details.append("Status: ").append(status.getDisplayName()).append("\n");
        details.append("Payment Date: ").append(paymentDate).append("\n");
        
        if (processedDate != null) {
            details.append("Processed Date: ").append(processedDate).append("\n");
        }
        
        if (transactionId != null) {
            details.append("Transaction ID: ").append(transactionId).append("\n");
        }
        
        if (failureReason != null) {
            details.append("Failure Reason: ").append(failureReason).append("\n");
        }
        
        this.paymentDetails = details.toString();
    }

    /**
     * Gets payment summary for display.
     */
    public String getPaymentSummary() {
        return String.format("Payment %s - $%.2f via %s - %s", 
                           paymentId, amount, paymentMethod, status.getDisplayName());
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status=" + status +
                '}';
    }
}

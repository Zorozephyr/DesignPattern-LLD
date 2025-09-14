package org.example.lowleveldesignexamples.carrentalsystemlld.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a bill for a vehicle reservation.
 * Contains detailed cost breakdown and billing information.
 */
public class Bill {
    private String billId;
    private Reservation reservation;
    private LocalDateTime billDate;
    private double baseCost;
    private double taxAmount;
    private double additionalFees;
    private double discountAmount;
    private double totalAmount;
    private boolean isPaid;
    private String billDetails;

    public Bill(String billId, Reservation reservation) {
        this.billId = billId;
        this.reservation = reservation;
        this.billDate = LocalDateTime.now();
        this.baseCost = reservation.getTotalCost();
        this.isPaid = false;
        calculateBillAmounts();
        generateBillDetails();
    }

    /**
     * Calculates tax, fees, and total amount for the bill.
     */
    private void calculateBillAmounts() {
        // Calculate tax (10% of base cost)
        this.taxAmount = baseCost * 0.10;
        
        // Additional fees (processing fee, insurance, etc.)
        this.additionalFees = 50.0; // Fixed processing fee
        
        // Add insurance fee based on vehicle type
        switch (reservation.getVehicle().getVehicleType()) {
            case CAR:
                additionalFees += 20.0;
                break;
            case MOTORCYCLE:
                additionalFees += 15.0;
                break;
            case TRUCK:
                additionalFees += 40.0;
                break;
            case VAN:
                additionalFees += 30.0;
                break;
        }
        
        // Calculate total
        this.totalAmount = baseCost + taxAmount + additionalFees - discountAmount;
    }

    /**
     * Applies a discount to the bill.
     */
    public void applyDiscount(double discountAmount) {
        this.discountAmount = Math.max(0, discountAmount);
        this.totalAmount = baseCost + taxAmount + additionalFees - this.discountAmount;
        generateBillDetails();
    }

    /**
     * Generates detailed bill description.
     */
    private void generateBillDetails() {
        StringBuilder details = new StringBuilder();
        details.append("BILL DETAILS\n");
        details.append("============\n");
        details.append("Reservation ID: ").append(reservation.getReservationId()).append("\n");
        details.append("Vehicle: ").append(reservation.getVehicle().getBrand())
               .append(" ").append(reservation.getVehicle().getModel()).append("\n");
        details.append("Rental Period: ").append(reservation.getStartDate())
               .append(" to ").append(reservation.getEndDate()).append("\n");
        details.append("Rental Days: ").append(reservation.getRentalDays()).append("\n");
        details.append("\nCOST BREAKDOWN\n");
        details.append("--------------\n");
        details.append(String.format("Base Cost: $%.2f\n", baseCost));
        details.append(String.format("Tax (10%%): $%.2f\n", taxAmount));
        details.append(String.format("Additional Fees: $%.2f\n", additionalFees));
        if (discountAmount > 0) {
            details.append(String.format("Discount: -$%.2f\n", discountAmount));
        }
        details.append("================\n");
        details.append(String.format("TOTAL AMOUNT: $%.2f\n", totalAmount));
        
        this.billDetails = details.toString();
    }

    /**
     * Marks the bill as paid.
     */
    public void markAsPaid() {
        this.isPaid = true;
    }

    /**
     * Gets the bill summary for display.
     */
    public String getBillSummary() {
        return String.format("Bill %s - Total: $%.2f - Status: %s", 
                           billId, totalAmount, isPaid ? "PAID" : "UNPAID");
    }

    // Getters and Setters
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        this.baseCost = reservation.getTotalCost();
        calculateBillAmounts();
        generateBillDetails();
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getAdditionalFees() {
        return additionalFees;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getBillDetails() {
        return billDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(billId, bill.billId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId='" + billId + '\'' +
                ", totalAmount=" + totalAmount +
                ", isPaid=" + isPaid +
                '}';
    }
}

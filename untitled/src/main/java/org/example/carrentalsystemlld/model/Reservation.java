package org.example.carrentalsystemlld.model;

import org.example.carrentalsystemlld.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a vehicle reservation in the car rental system.
 * Contains booking details and manages reservation lifecycle.
 */
public class Reservation {
    private String reservationId;
    private User user;
    private Vehicle vehicle;
    private LocalDateTime bookingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Location pickupLocation;
    private Location dropLocation;
    private ReservationStatus status;
    private double totalCost;
    private String notes;

    public Reservation(String reservationId, User user, Vehicle vehicle,
                      LocalDate startDate, LocalDate endDate,
                      Location pickupLocation, Location dropLocation) {
        this.reservationId = reservationId;
        this.user = user;
        this.vehicle = vehicle;
        this.bookingDate = LocalDateTime.now();
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = ReservationStatus.SCHEDULED;
        this.totalCost = calculateTotalCost();
    }

    /**
     * Calculates the total cost for this reservation based on vehicle type and rental days.
     */
    private double calculateTotalCost() {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            days = 1; // Minimum 1 day rental
        }
        return vehicle.calculateRentalCost((int) days);
    }

    /**
     * Gets the number of rental days for this reservation.
     */
    public long getRentalDays() {
        return Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
    }

    /**
     * Activates the reservation (vehicle picked up).
     */
    public void activateReservation() {
        if (status == ReservationStatus.SCHEDULED) {
            this.status = ReservationStatus.ACTIVE;
        }
    }

    /**
     * Completes the reservation (vehicle returned).
     */
    public void completeReservation() {
        if (status == ReservationStatus.ACTIVE) {
            this.status = ReservationStatus.COMPLETED;
        }
    }

    /**
     * Cancels the reservation.
     */
    public void cancelReservation() {
        if (status == ReservationStatus.SCHEDULED) {
            this.status = ReservationStatus.CANCELLED;
        }
    }

    /**
     * Checks if pickup and drop locations are the same.
     */
    public boolean isSameLocationRental() {
        return pickupLocation.equals(dropLocation);
    }

    // Getters and Setters
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.totalCost = calculateTotalCost(); // Recalculate when vehicle changes
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        this.totalCost = calculateTotalCost(); // Recalculate when dates change
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.totalCost = calculateTotalCost(); // Recalculate when dates change
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Location getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(Location dropLocation) {
        this.dropLocation = dropLocation;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", user=" + user.getName() +
                ", vehicle=" + vehicle.getModel() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", totalCost=" + totalCost +
                '}';
    }
}

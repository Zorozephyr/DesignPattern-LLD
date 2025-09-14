package org.example.lowleveldesignexamples.carrentalsystemlld.model;

import java.util.Objects;

import org.example.lowleveldesignexamples.carrentalsystemlld.enums.VehicleStatus;
import org.example.lowleveldesignexamples.carrentalsystemlld.enums.VehicleType;

/**
 * Abstract base class for all vehicles in the rental system.
 * Implements common vehicle properties and behaviors.
 */
public abstract class Vehicle {
    protected String vehicleId;
    protected String vehicleNumber;
    protected VehicleType vehicleType;
    protected VehicleStatus status;
    protected double dailyRentalCost;
    protected String model;
    protected String brand;
    protected int manufacturingYear;
    protected long kmDriven;
    protected int seatingCapacity;
    protected String fuelType;

    public Vehicle(String vehicleId, String vehicleNumber, VehicleType vehicleType,
                   double dailyRentalCost, String model, String brand, 
                   int manufacturingYear, int seatingCapacity, String fuelType) {
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.status = VehicleStatus.AVAILABLE;
        this.dailyRentalCost = dailyRentalCost;
        this.model = model;
        this.brand = brand;
        this.manufacturingYear = manufacturingYear;
        this.seatingCapacity = seatingCapacity;
        this.fuelType = fuelType;
        this.kmDriven = 0;
    }

    /**
     * Abstract method to get vehicle-specific features.
     * Each vehicle type implements its own features.
     */
    public abstract String getVehicleFeatures();

    /**
     * Abstract method to calculate rental cost based on days and vehicle-specific factors.
     */
    public abstract double calculateRentalCost(int days);

    // Getters and Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public double getDailyRentalCost() {
        return dailyRentalCost;
    }

    public void setDailyRentalCost(double dailyRentalCost) {
        this.dailyRentalCost = dailyRentalCost;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(int manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    public long getKmDriven() {
        return kmDriven;
    }

    public void setKmDriven(long kmDriven) {
        this.kmDriven = kmDriven;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Checks if vehicle is available for rental.
     */
    public boolean isAvailableForRental() {
        return status.isAvailableForRental();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vehicleId, vehicle.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId);
    }

    @Override
    public String toString() {
        return String.format("%s{vehicleId='%s', vehicleNumber='%s', model='%s', brand='%s', status=%s}", 
                           getClass().getSimpleName(), vehicleId, vehicleNumber, model, brand, status);
    }
}

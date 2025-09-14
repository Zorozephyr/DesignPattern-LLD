package org.example.lowleveldesignexamples.carrentalsystemlld.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user in the car rental system.
 * Contains personal information and rental history.
 */
public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String drivingLicense;
    private String address;
    private List<Reservation> rentalHistory;

    public User(String userId, String name, String email, String phone, 
                String drivingLicense, String address) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.drivingLicense = drivingLicense;
        this.address = address;
        this.rentalHistory = new ArrayList<>();
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Reservation> getRentalHistory() {
        return new ArrayList<>(rentalHistory);
    }

    public void addReservation(Reservation reservation) {
        this.rentalHistory.add(reservation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

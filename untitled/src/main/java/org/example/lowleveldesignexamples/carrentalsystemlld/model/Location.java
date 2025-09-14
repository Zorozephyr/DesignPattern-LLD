package org.example.lowleveldesignexamples.carrentalsystemlld.model;

import java.util.Objects;

/**
 * Represents a geographic location in the car rental system.
 * Contains address information and coordinates.
 */
public class Location {
    private String locationId;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private double latitude;
    private double longitude;

    public Location(String locationId, String address, String city, 
                   String state, String zipCode, String country) {
        this.locationId = locationId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Location(String locationId, String address, String city, 
                   String state, String zipCode, String country, 
                   double latitude, double longitude) {
        this(locationId, address, city, state, zipCode, country);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the full formatted address.
     */
    public String getFullAddress() {
        return String.format("%s, %s, %s %s, %s", 
                            address, city, state, zipCode, country);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(locationId, location.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId);
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId='" + locationId + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

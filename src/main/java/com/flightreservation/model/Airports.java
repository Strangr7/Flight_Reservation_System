package com.flightreservation.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "airports")
public class Airports implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    @Column(name = "airport_id")
    private int airportId;

    @NotNull(message = "Airport code cannot be empty!!")
    @Column(name = "airport_code", unique = true, length = 3)
    private String airportCode;

    @NotNull(message = "Airport name cannot be empty!!")
    @Column(name = "airport_name", length = 100)
    private String airportName;

    @NotNull(message = "City cannot be empty!!")
    @Column(name = "city", length = 100)
    private String city;

    @NotNull(message = "Country cannot be empty!!")
    @Column(name = "country", length = 100)
    private String country;

    // Default constructor
    public Airports() {
    }

    // Parameterized constructor
    public Airports(int airportId, String airportCode, String airportName, String city, String country) {
        this.airportId = airportId;
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
        this.country = country;
    }

    // Getters and Setters
    public int getAirportId() {
        return airportId;
    }

    public void setAirportId(int airportId) {
        this.airportId = airportId;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Airports{" +
                "airportId=" + airportId +
                ", airportCode='" + airportCode + '\'' +
                ", airportName='" + airportName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
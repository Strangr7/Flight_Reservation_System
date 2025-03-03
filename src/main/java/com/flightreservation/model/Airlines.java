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
@Table(name = "airlines")
public class Airlines implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    @Column(name = "airline_id")
    private int airlineId;

    @NotNull(message = "Airline name cannot be empty!!")
    @Column(name = "airline_name", length = 100)
    private String airlineName;

    @NotNull(message = "Airline code cannot be empty!!")
    @Column(name = "airline_code", length = 2)
    private String airlineCode;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    // Default constructor
    public Airlines() {
    }

    // Parameterized constructor
    public Airlines(int airlineId, String airlineName, String airlineCode, String logoUrl) {
        this.airlineId = airlineId;
        this.airlineName = airlineName;
        this.airlineCode = airlineCode;
        this.logoUrl = logoUrl;
    }

    // Getters and Setters
    public int getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return "Airlines{" +
                "airlineId=" + airlineId +
                ", airlineName='" + airlineName + '\'' +
                ", airlineCode='" + airlineCode + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}
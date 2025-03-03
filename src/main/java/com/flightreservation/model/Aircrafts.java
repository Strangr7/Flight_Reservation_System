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
@Table(name = "aircraft")
public class Aircrafts implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    @Column(name = "aircraft_id")
    private int aircraftId;

    @NotNull(message = "Aircraft model cannot be empty!!")
    @Column(name = "aircraft_model", length = 100)
    private String aircraftModel;

    @NotNull(message = "Manufacturer cannot be empty!!")
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @NotNull(message = "Seating capacity cannot be empty!!")
    @Column(name = "seating_capacity")
    private int seatingCapacity;

    // Default constructor
    public Aircrafts() {
    }

    // Parameterized constructor
    public Aircrafts(int aircraftId, String aircraftModel, String manufacturer, int seatingCapacity) {
        this.aircraftId = aircraftId;
        this.aircraftModel = aircraftModel;
        this.manufacturer = manufacturer;
        this.seatingCapacity = seatingCapacity;
    }

    // Getters and Setters
    public int getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getAircraftModel() {
        return aircraftModel;
    }

    public void setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "aircraftId=" + aircraftId +
                ", aircraftModel='" + aircraftModel + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", seatingCapacity=" + seatingCapacity +
                '}';
    }
}
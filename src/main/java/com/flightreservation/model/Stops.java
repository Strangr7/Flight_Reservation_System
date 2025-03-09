package com.flightreservation.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "stops")
public class Stops  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    @Column(name = "stop_id")
    private int stopId;

    @NotNull(message = "Flight cannot be empty!!")
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flights flight;

    @NotNull(message = "Stop number cannot be empty!!")
    @Column(name = "stop_number")
    private int stopNumber;

    @NotNull(message = "Stop airport cannot be empty!!")
    @ManyToOne
    @JoinColumn(name = "stop_airport_id", nullable = false)
    private Airports stopAirport;

    @NotNull(message = "Stop duration cannot be empty!!")
    @Column(name = "stop_duration")
    private int stopDuration;

    // Default constructor
    public Stops() {
    }

    // Parameterized constructor
    public Stops(int stopId, Flights flight, int stopNumber, Airports stopAirport, int stopDuration) {
        this.stopId = stopId;
        this.flight = flight;
        this.stopNumber = stopNumber;
        this.stopAirport = stopAirport;
        this.stopDuration = stopDuration;
    }

    // Getters and Setters
    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public Flights getFlight() {
        return flight;
    }

    public void setFlight(Flights flight) {
        this.flight = flight;
    }

    public int getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }

    public Airports getStopAirport() {
        return stopAirport;
    }

    public void setStopAirport(Airports stopAirport) {
        this.stopAirport = stopAirport;
    }

    public int getStopDuration() {
        return stopDuration;
    }

    public void setStopDuration(int stopDuration) {
        this.stopDuration = stopDuration;
    }

    @Override
    public String toString() {
        return "Stops{" +
                "stopId=" + stopId +
                ", flight=" + flight.getFlightId() +
                ", stopNumber=" + stopNumber +
                ", stopAirport=" + stopAirport.getAirportId() +
                ", stopDuration=" + stopDuration +
                '}';
    }
}
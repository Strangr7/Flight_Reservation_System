package com.flightreservation.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "flights")
public class Flights implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private int flightId;

    @NotNull(message = "Airline cannot be empty!!")
    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    private Airlines airline;

    @NotNull(message = "Flight number cannot be empty!!")
    @Column(name = "flight_number", length = 10)
    private String flightNumber;

    @NotNull(message = "Departure airport cannot be empty!!")
    @ManyToOne
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airports departureAirport;

    @NotNull(message = "Destination airport cannot be empty!!")
    @ManyToOne
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airports destinationAirport;

    @NotNull(message = "Departure time cannot be empty!!")
    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time cannot be empty!!")
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Aircraft cannot be empty!!")
    @ManyToOne
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircrafts aircraft;

    @NotNull(message = "Seats available cannot be empty!!")
    @Column(name = "seats_available")
    private int seatsAvailable;

    @NotNull(message = "Status cannot be empty!!")
    @Column(name = "status")
    private String status;

    @Column(name = "delay_minutes")
    private Integer delayMinutes;

    @NotNull(message = "Trip duration cannot be empty!!")
    @Column(name = "trip_duration")
    private int tripDuration;

    @Transient // This field is not stored in the database
    private int stopCount;
    
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Stops> stops = new ArrayList<>();

    // Default constructor
    public Flights() {
    }

    // Parameterized constructor
    public Flights(int flightId, Airlines airline, String flightNumber, Airports departureAirport,
                   Airports destinationAirport, LocalDateTime departureTime, LocalDateTime arrivalTime,
                   Aircrafts aircraft, int seatsAvailable, String status, Integer delayMinutes,
                   int tripDuration, int stopCount, List<Stops>stops) {
        this.flightId = flightId;
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraft = aircraft;
        this.seatsAvailable = seatsAvailable;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.tripDuration = tripDuration;
        this.stopCount = stopCount;
        this.stops = stops != null ? stops : new ArrayList<>();
    }

    // Getters and Setters
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public Airlines getAirline() {
        return airline;
    }

    public void setAirline(Airlines airline) {
        this.airline = airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airports getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airports departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airports getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airports destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Aircrafts getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircrafts aircraft) {
        this.aircraft = aircraft;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(Integer delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getStopCount() {
        return stopCount;
    }

    // Add this setter for stopCount
    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }
    public List<Stops> getStops() { return stops; }
    public void setStops(List<Stops> stops) { this.stops = stops; }

    @Override
    public String toString() {
        return "Flights{" +
                "flightId=" + flightId +
                ", airline=" + airline.getAirlineId() +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureAirport=" + departureAirport.getAirportId() +
                ", destinationAirport=" + destinationAirport.getAirportId() +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", aircraft=" + aircraft.getAircraftId() +
                ", seatsAvailable=" + seatsAvailable +
                ", status='" + status + '\'' +
                ", delayMinutes=" + delayMinutes +
                ", tripDuration=" + tripDuration +
                ", stopCount=" + stopCount +
                ", stops="+
                '}';
    }
}
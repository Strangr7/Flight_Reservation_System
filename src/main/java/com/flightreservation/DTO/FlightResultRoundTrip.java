package com.flightreservation.DTO;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a round-trip flight search result.
 * Combines lists of outbound and return flights, each as FlightResultOneWay objects.
 * Used to pass round-trip data between layers (e.g., DAO to controller to UI).
 */
public class FlightResultRoundTrip {
    // List of one-way flight results for the outbound leg (e.g., departure to destination)
    private List<FlightResultOneWay> outboundFlights;

    // List of one-way flight results for the return leg (e.g., destination to departure)
    private List<FlightResultOneWay> returnFlights;

    /**
     * Constructor to initialize a round-trip flight result.
     * Takes lists of outbound and return flights directly from the caller.
     * @param outboundFlights List of FlightResultOneWay objects for the outbound leg
     * @param returnFlights List of FlightResultOneWay objects for the return leg
     */
    public FlightResultRoundTrip(List<FlightResultOneWay> outboundFlights, List<FlightResultOneWay> returnFlights) {
        this.outboundFlights = outboundFlights;  // Set outbound flights directly
        this.returnFlights = returnFlights;      // Set return flights directly
    }

    /**
     * @return The list of outbound flight results
     */
    public List<FlightResultOneWay> getOutboundFlights() { 
        return outboundFlights; 
    }

    /**
     * Sets the list of outbound flights.
     * @param outboundFlights The list of FlightResultOneWay objects to set
     */
    public void setOutboundFlights(List<FlightResultOneWay> outboundFlights) { 
        this.outboundFlights = outboundFlights; 
    }

    /**
     * @return The list of return flight results
     */
    public List<FlightResultOneWay> getReturnFlights() { 
        return returnFlights; 
    }

    /**
     * Sets the list of return flights.
     * @param returnFlights The list of FlightResultOneWay objects to set
     */
    public void setReturnFlights(List<FlightResultOneWay> returnFlights) { 
        this.returnFlights = returnFlights; 
    }
}
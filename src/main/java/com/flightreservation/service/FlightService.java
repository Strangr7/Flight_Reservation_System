package com.flightreservation.service;

import com.flightreservation.DTO.FlightResultOneWay;
import com.flightreservation.DTO.FlightResultRoundTrip;
import com.flightreservation.dao.FlightDAO;

import com.flightreservation.model.Flights;

import java.time.LocalDate;

import java.util.List;

/**
 * Service layer class for managing flight-related operations.
 * Acts as an intermediary between the application's business logic and the data access layer (FlightDAO).
 * Provides methods to fetch flights, search one-way flights, and search round-trip flights.
 */
public class FlightService {
	private final FlightDAO flightDAO = new FlightDAO();

	/**
     * Retrieves a flight by its unique ID.
     * Delegates the operation to FlightDAO.
     * @param flightID The unique identifier of the flight to fetch.
     * @return The Flights object if found, or null if not found or an error occurs in DAO.
     */
    public Flights fetchFlightById(int flightID) {
        // Simply pass the request to the DAO layer and return the result
        return flightDAO.fetchFlightById(flightID);
    }

    /**
     * Fetches all flights available in the system.
     * Delegates the operation to FlightDAO.
     * @return A list of all Flights objects, or null if an error occurs in DAO.
     */
    public List<Flights> fetchAllFlights() {
        // Delegate to DAO to retrieve all flights and return the result directly
        return flightDAO.fetchAllFlights();
    }

    /**
     * Searches for one-way flights based on departure and destination airports and a departure date.
     * Delegates the search operation to FlightDAO.
     * @param departureAirportCode The code of the departure airport (e.g., "JFK").
     * @param destinationAirportCode The code of the destination airport (e.g., "LAX").
     * @param departureDate The date of departure for the flight search.
     * @return A list of FlightResultOneWay objects, empty if no flights are found.
     */
    public List<FlightResultOneWay> searchFlights(String departureAirportCode, String destinationAirportCode,
            LocalDate departureDate) {
        // Pass the parameters to the DAO's search method and return the results
        return flightDAO.searchFlight(departureAirportCode, destinationAirportCode, departureDate);
    }

    /**
     * Searches for round-trip flights based on departure and destination airports,
     * a departure date, and a return date.
     * Delegates the round-trip search to FlightDAO.
     * @param departureAirportCode The code of the departure airport (e.g., "JFK").
     * @param destinationAirportCode The code of the destination airport (e.g., "LAX").
     * @param departureDate The date of the outbound flight.
     * @param returnDate The date of the return flight.
     * @return A FlightResultRoundTrip object containing outbound and return flight results.
     */
    public FlightResultRoundTrip searchRoundTripFlights(String departureAirportCode, String destinationAirportCode,
            LocalDate departureDate, LocalDate returnDate) {
        // Delegate to DAO's round-trip search method and return the combined result
        return flightDAO.searchRoundTripFlights(departureAirportCode, destinationAirportCode, departureDate,
                returnDate);
    }

}
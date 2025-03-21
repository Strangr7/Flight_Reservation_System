package com.flightreservation.service;

import com.flightreservation.DTO.FlightResultOneWay;
import com.flightreservation.DTO.FlightResultRoundTrip;
import com.flightreservation.DTO.SuggetedDestination;
import com.flightreservation.dao.FlightDAO;

import com.flightreservation.model.Flights;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Service layer class for managing flight-related operations. Acts as an
 * intermediary between the application's business logic and the data access
 * layer (FlightDAO). Provides methods to fetch flights, search one-way flights,
 * and search round-trip flights.
 */
public class FlightService {
	private final FlightDAO flightDAO = new FlightDAO();

	/**
	 * Retrieves a flight by its unique ID. Delegates the operation to FlightDAO.
	 * 
	 * @param flightID The unique identifier of the flight to fetch.
	 * @return The Flights object if found, or null if not found or an error occurs
	 *         in DAO.
	 */
	public Flights fetchFlightById(int flightID) {
		// Simply pass the request to the DAO layer and return the result
		return flightDAO.fetchFlightById(flightID);
	}

	/**
	 * Fetches all flights available in the system. Delegates the operation to
	 * FlightDAO.
	 * 
	 * @return A list of all Flights objects, or null if an error occurs in DAO.
	 */
	public List<Flights> fetchAllFlights() {
		// Delegate to DAO to retrieve all flights and return the result directly
		return flightDAO.fetchAllFlights();
	}

	/**
	 * Searches for one-way flights based on departure and destination airports,
	 * a departure date, and an optional sorting preference. Delegates the search
	 * operation to FlightDAO and applies sorting based on the specified criteria.
	 * 
	 * @param departureAirportCode   The code of the departure airport (e.g., "JFK").
	 * @param destinationAirportCode The code of the destination airport (e.g., "LAX").
	 * @param departureDate          The date of departure for the flight search.
	 * @param sortBy                 The sorting criteria ("fastest", "cheapest", or "best");
	 *                               defaults to "best" if null.
	 * @return A list of FlightResultOneWay objects sorted according to the sortBy parameter,
	 *         empty if no flights are found.
	 */
	public List<FlightResultOneWay> searchFlights(String departureAirportCode, String destinationAirportCode,
			LocalDate departureDate, String sortBy) {
		List<FlightResultOneWay> flights = flightDAO.searchFlight(departureAirportCode, destinationAirportCode,
				departureDate);
		if (flights != null && !flights.isEmpty()) {
			switch (sortBy == null ? "best" : sortBy) {
			case "fastest":
				flights.sort(Comparator.comparing(flight -> {
					Duration duration = Duration.between(flight.getFlight().getDepartureTime(),
							flight.getFlight().getArrivalTime());
					return duration.toMinutes();
				}));
				break;
			case "cheapest":
				flights.sort(Comparator.comparing(flight ->
					flight.getPricesAndClasses().isEmpty() ?
					Double.MAX_VALUE :
					flight.getPricesAndClasses().get(0).getDynamicPrice()));
				break;
			case "best":
				// Custom "best" logic (e.g., balance of price and duration)
				flights.sort(Comparator.comparing(flight -> {
					Duration duration = Duration.between(
						flight.getFlight().getDepartureTime(),
						flight.getFlight().getArrivalTime());
					double price = flight.getPricesAndClasses().isEmpty() ?
						Double.MAX_VALUE :
						flight.getPricesAndClasses().get(0).getDynamicPrice();
					return (duration.toMinutes() * 0.5) + (price * 0.5); // Example weighted score
				}));
				break;
			}
		}
		return flights;
	}

	/**
	 * Searches for one-way flights based on departure and destination airports
	 * and a departure date, with default sorting by "best". Delegates to the
	 * overloaded searchFlights method with sortBy set to "best".
	 * 
	 * @param departureAirportCode   The code of the departure airport (e.g., "JFK").
	 * @param destinationAirportCode The code of the destination airport (e.g., "LAX").
	 * @param departureDate          The date of departure for the flight search.
	 * @return A list of FlightResultOneWay objects sorted by "best" criteria,
	 *         empty if no flights are found.
	 */
	public List<FlightResultOneWay> searchFlights(String departureAirportCode, String destinationAirportCode,
			LocalDate departureDate) {
		// Default to "best" sorting if no sortBy specified
		return searchFlights(departureAirportCode, destinationAirportCode, departureDate, "best");
	}

	/**
	 * Searches for round-trip flights based on departure and destination airports,
	 * a departure date, and a return date. Delegates the round-trip search to
	 * FlightDAO.
	 * 
	 * @param departureAirportCode   The code of the departure airport (e.g., "JFK").
	 * @param destinationAirportCode The code of the destination airport (e.g., "LAX").
	 * @param departureDate          The date of the outbound flight.
	 * @param returnDate             The date of the return flight.
	 * @return A FlightResultRoundTrip object containing outbound and return flight
	 *         results.
	 */
	public FlightResultRoundTrip searchRoundTripFlights(String departureAirportCode, String destinationAirportCode,
			LocalDate departureDate, LocalDate returnDate) {
		// Delegate to DAO's round-trip search method and return the combined result
		return flightDAO.searchRoundTripFlights(departureAirportCode, destinationAirportCode, departureDate,
				returnDate);
	}

	/**
	 * Fetches up to 6 random suggested destinations from a given departure airport
	 * code.
	 *
	 * @param departureAirportCode The departure airport code (e.g., "YYZ").
	 * @return A list of SuggestedDestination objects.
	 */
	public List<SuggetedDestination> fetchSuggestedDestinations(String departureAirportCode) {
		return flightDAO.getSuggestedDestinationsByDeparture(departureAirportCode);
	}
}
package com.flightreservation.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.DTO.FlightResultOneWay;
import com.flightreservation.DTO.FlightResultRoundTrip;
import com.flightreservation.DTO.SuggetedDestination;
import com.flightreservation.model.BaggageRules;
import com.flightreservation.model.Classes;
import com.flightreservation.model.Flights;
import com.flightreservation.model.Stops;
import com.flightreservation.model.enums.FlightClasses;

import com.flightreservation.util.HibernateUtil;

public class FlightDAO {

	private static final Logger logger = LoggerFactory.getLogger(FlightDAO.class);

	/**
	 * Retrieves a flight from the database by its unique flight ID. Uses
	 * Hibernate's `get` method for a direct lookup.
	 * 
	 * @param flightId The unique identifier of the flight to fetch.
	 * @return The Flights object if found, or null if an error occurs or flight
	 *         doesn't exist.
	 */
	public Flights fetchFlightById(int flightId) {
		// Try-with-resources ensures the Hibernate session is closed automatically
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

			String hql = "SELECT br,s From Baggagerules JOIN br.flight f JOIN Seats s  ON s.flight = f WHERE f.flightId = :flightId";
			Query<Flights> query = session.createQuery(hql, Flights.class);
			query.setParameter("flightId", flightId);
			return query.uniqueResult(); // Fetch the flight directly using its ID; maps to the Flights table

		} catch (Exception e) {
			// Log the error with details for debugging; {} is a placeholder for the message
			logger.error("Error fetching flight by ID: {}", e.getMessage(), e);
			// Return null to indicate failure (could throw an exception instead in some
			// designs)
			return null;
		}
	}

	/**
	 * Fetches all flights stored in the database. Uses a simple HQL query to
	 * retrieve all rows from the Flights table.
	 * 
	 * @return A list of all Flights objects, or null if an error occurs.
	 */
	public List<Flights> fetchAllFlights() {
		// Open a Hibernate session to interact with the database
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// HQL (Hibernate Query Language) query: "FROM Flights" selects all flights
			String hql = "FROM Flights";
			// Create a query object, specifying the return type as Flights.class
			Query<Flights> query = session.createQuery(hql, Flights.class);
			// Execute the query and return the list of flights
			return query.list();
		} catch (Exception e) {
			// Log any errors (e.g., database connection failure) with stack trace
			logger.error("Error fetching all flights: {}", e.getMessage(), e);
			// Return null to signal an issue (alternative: empty list or custom exception)
			return null;
		}
	}

	/**
	 * Searches for one-way flights based on departure and destination airport codes
	 * and a specific date. Returns a list of FlightResultOneWay objects, including
	 * flight details, stops, and pricing.
	 * 
	 * @param departureAirportCode   The code of the departure airport (e.g.,
	 *                               "JFK").
	 * @param destinationAirportCode The code of the destination airport (e.g.,
	 *                               "LAX").
	 * @param departureDate          The date of departure to filter flights.
	 * @return A list of FlightResultOneWay objects, or an empty list if no flights
	 *         are found or an error occurs.
	 */
	public List<FlightResultOneWay> searchFlight(String departureAirportCode, String destinationAirportCode,
			LocalDate departureDate) {
		List<FlightResultOneWay> results = new ArrayList<>();
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// Step 1: Fetch flights
			String flightHql = "SELECT f FROM Flights f " + "LEFT JOIN f.departureAirport dep "
					+ "LEFT JOIN f.destinationAirport dest "
					+ "WHERE (dep.airportCode = :departureAirportCode OR dep.airportCode IS NULL) "
					+ "AND (dest.airportCode = :destinationAirportCode OR dest.airportCode IS NULL) "
					+ "AND CAST(f.departureTime AS date) = :departureDate";
			Query<Flights> flightQuery = session.createQuery(flightHql, Flights.class);
			flightQuery.setParameter("departureAirportCode", departureAirportCode);
			flightQuery.setParameter("destinationAirportCode", destinationAirportCode);
			flightQuery.setParameter("departureDate", departureDate);
			List<Flights> flights = flightQuery.getResultList();
			logger.info("Found {} flights for ATL->JFK on 2023-10-15", flights.size());

			if (flights.isEmpty()) {
				return Collections.emptyList();
			}

			// Step 2: Fetch stops
			List<Integer> flightIds = flights.stream().map(Flights::getFlightId).collect(Collectors.toList());
			String stopsHql = "FROM Stops s WHERE s.flight.flightId IN (:flightIds)";
			Query<Stops> stopsQuery = session.createQuery(stopsHql, Stops.class);
			stopsQuery.setParameter("flightIds", flightIds);
			List<Stops> stopsList = stopsQuery.getResultList();
			Map<Integer, List<Stops>> stopsByFlight = stopsList.stream()
					.collect(Collectors.groupingBy(s -> s.getFlight().getFlightId()));

			// Step 3: Fetch pricing and class information
			String priceHql = "SELECT fp.flight.flightId, fp.flightClass, fp.basePrice, fp.dynamicPrice "
					+ "FROM FlightPrices fp WHERE fp.flight.flightId IN (:flightIds)";
			Query<Object[]> priceQuery = session.createQuery(priceHql, Object[].class);
			priceQuery.setParameter("flightIds", flightIds);
			List<Object[]> priceResults = priceQuery.getResultList();

			// Step 4: Fetch BaggageRules
			String baggageHql = "FROM com.flightreservation.model.BaggageRules br " + "JOIN FETCH br.classes c "
					+ "WHERE br.flight.flightId IN (:flightIds)";
			Query<BaggageRules> baggageQuery = session.createQuery(baggageHql, BaggageRules.class);
			baggageQuery.setParameter("flightIds", flightIds);
			List<BaggageRules> baggageRulesList = baggageQuery.getResultList();
			logger.info("Found {} baggage rules", baggageRulesList.size());

			// Step 5: Fetch available Seats
			String seatsHql = "SELECT s.flight.flightId, s.seatClass.classId, COUNT(s) " + "FROM Seats s "
					+ "WHERE s.flight.flightId IN (:flightIds) " + "AND s.isAvailable = true "
					+ "GROUP BY s.flight.flightId, s.seatClass.classId";
			Query<Object[]> seatsQuery = session.createQuery(seatsHql, Object[].class);
			seatsQuery.setParameter("flightIds", flightIds);
			List<Object[]> seatsResults = seatsQuery.getResultList();

			Map<String, Integer> availableSeatsByFlightAndClass = seatsResults.stream()
					.collect(Collectors.toMap(row -> row[0] + "-" + row[1], row -> ((Long) row[2]).intValue(),
							(existing, replacement) -> existing)); // Handle duplicates

			Map<String, BaggageRules> baggageRulesByFlightAndClass = baggageRulesList.stream()
					.collect(Collectors.toMap(br -> br.getFlight().getFlightId() + "-" + br.getClasses().getClassId(),
							br -> br, (existing, replacement) -> existing));

			// Step 6: Organize prices, baggage rules, and seats
			Map<Integer, List<FlightResultOneWay.PriceClass>> priceByFlight = new HashMap<>();
			for (Object[] row : priceResults) {
				int flightId = (int) row[0];
				Classes flightClass = (Classes) row[1]; // Correct cast to Classes entity
				Double basePrice = (Double) row[2];
				Double dynamicPrice = (Double) row[3];

				// Convert Classes entity to FlightClasses enum
				FlightClasses className = flightClass.getClassName(); // This is correct
				String key = flightId + "-" + flightClass.getClassId();
				BaggageRules baggageRules = baggageRulesByFlightAndClass.get(key);
				int availableSeats = availableSeatsByFlightAndClass.getOrDefault(key, 0);

				priceByFlight.computeIfAbsent(flightId, k -> new ArrayList<>()).add(new FlightResultOneWay.PriceClass(
						className, basePrice, dynamicPrice, baggageRules, availableSeats));
			}

			// Step 7: Build results
			for (Flights flight : flights) {
				List<Stops> flightStops = stopsByFlight.getOrDefault(flight.getFlightId(), new ArrayList<>());
				List<FlightResultOneWay.PriceClass> flightPrices = priceByFlight.getOrDefault(flight.getFlightId(),
						new ArrayList<>());

				FlightResultOneWay flightResult = new FlightResultOneWay(flight, (long) flightStops.size(),
						flightStops);
				flightResult.getPricesAndClasses().addAll(flightPrices);
				results.add(flightResult);
			}

			return results;
		} catch (Exception e) {
			logger.error("Error searching for flights: {}", e.getMessage(), e);
			return results; // Return partial results
		}
	}

	/**
	 * Searches for round-trip flights: outbound flights from departure to
	 * destination, and return flights from destination back to departure, filtered
	 * by airline consistency.
	 * 
	 * @param departureAirportCode   The starting airport code (e.g., "JFK").
	 * @param destinationAirportCode The destination airport code (e.g., "LAX").
	 * @param departureDate          The outbound flight date.
	 * @param returnDate             The return flight date.
	 * @return A FlightResultRoundTrip object with outbound and return flights, or
	 *         empty lists if no matches.
	 */
	public FlightResultRoundTrip searchRoundTripFlights(String departureAirportCode, String destinationAirportCode,
			LocalDate departureDate, LocalDate returnDate) {
		// Search outbound flights (departure -> destination)
		List<FlightResultOneWay> outboundFlights = searchFlight(departureAirportCode, destinationAirportCode,
				departureDate);
		// Search return flights (destination -> departure)
		List<FlightResultOneWay> returnFlights = searchFlight(destinationAirportCode, departureAirportCode, returnDate);

		// Group flights by airline ID for filtering
		Map<Integer, List<FlightResultOneWay>> outboundByAirline = new HashMap<>();
		Map<Integer, List<FlightResultOneWay>> returnByAirline = new HashMap<>();

		// Populate outbound flights by airline
		for (FlightResultOneWay outbound : outboundFlights) {
			int airlineId = outbound.getFlight().getAirline().getAirlineId();
			outboundByAirline.computeIfAbsent(airlineId, k -> new ArrayList<>()).add(outbound);
		}
		// Populate return flights by airline
		for (FlightResultOneWay returnFlight : returnFlights) {
			int airlineId = returnFlight.getFlight().getAirline().getAirlineId();
			returnByAirline.computeIfAbsent(airlineId, k -> new ArrayList<>()).add(returnFlight);
		}

		// Filter to include only airlines offering both outbound and return flights
		List<FlightResultOneWay> filteredOutbound = new ArrayList<>();
		List<FlightResultOneWay> filteredReturn = new ArrayList<>();
		for (Integer airlineId : outboundByAirline.keySet()) {
			if (returnByAirline.containsKey(airlineId)) {
				filteredOutbound.addAll(outboundByAirline.get(airlineId));
				filteredReturn.addAll(returnByAirline.get(airlineId));
			}
		}

		// Return the round-trip result
		return new FlightResultRoundTrip(filteredOutbound, filteredReturn);
	}

	// CRUD for the flights

	// sorting fot flights

	/**
	 * Fetches 5 random flights with different destinations from a given departure
	 * airport.
	 * 
	 * @param departureAirportCode The code of the departure airport (e.g., "YYZ").
	 * @return
	 * @return A list of FlightResultOneWay objects with unique destinations.
	 */

	public List<SuggetedDestination> getSuggestedDestination(String departureAirportCode) {
		List<SuggetedDestination> suggestions = new ArrayList<>();
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

			// Step 1: Fetch all flights from the departure airport

			String hql = "SELECT f FROM Flights f" + "LEFT JOIN f.departureAirport dep"
					+ "WHERE dep.airportCode = :departureAirportCode";

			Query<Flights> flights = session.createQuery(hql, Flights.class);
			flights.setParameter("departureAirportCode", departureAirportCode);
			List<Flights> flightsList = flights.getResultList();

			if (flightsList.isEmpty()) {
				return Collections.emptyList();
			}
			
			// Step 2: Randomly select 6 flights with unique destinations
			

			return suggestions;

		} catch (Exception e) {
			logger.error("Error fetching random suggested destinations: {}", e.getMessage(), e);
			return suggestions;
		}
	}

}

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
import com.flightreservation.model.Airports;
import com.flightreservation.model.BaggageRules;
import com.flightreservation.model.Classes;
import com.flightreservation.model.Flights;
import com.flightreservation.model.Stops;
import com.flightreservation.model.enums.FlightClasses;

import com.flightreservation.util.HibernateUtil;

import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;

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
			System.out.println("----------------------From dao: " + query.list());
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
	 * Fetches up to 6 random suggested destinations from a given departure airport
	 * code. Ensures destinations are unique by grouping on destination airport ID.
	 *
	 * @param departureAirportCode The departure airport code (e.g., "YYZ").
	 * @return A list of SuggestedDestination objects.
	 */

	public List<SuggetedDestination> getSuggestedDestinationsByDeparture(String departureAirportCode) {
		List<SuggetedDestination> suggestions = new ArrayList<>();
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

			// Step 1: Fetch up to 6 random flights with unique destinations
			String sql = """
					    SELECT f.*
					    FROM Flights f
					    JOIN Airports dep ON f.departure_airport_id = dep.airport_id
					    WHERE dep.airport_code = :departureAirportCode
					    AND f.flight_id = (
					        SELECT f_inner.flight_id
					        FROM Flights f_inner
					        WHERE f_inner.destination_airport_id = f.destination_airport_id
					        ORDER BY RAND()
					        LIMIT 1
					    )
					    LIMIT 6
					""";

			logger.debug("Executing native SQL query: {}", sql);
			Query<Flights> flightQuery = session.createNativeQuery(sql, Flights.class);
			flightQuery.setParameter("departureAirportCode", departureAirportCode);

			List<Flights> selectedFlights = flightQuery.getResultList();
			if (selectedFlights.isEmpty()) {
				logger.warn("No flights found for departure airport code: {}", departureAirportCode);
				return Collections.emptyList();
			}

			// Step 2: Fetch cheapest prices for the selected flights
			List<Integer> flightIds = selectedFlights.stream().map(Flights::getFlightId).collect(Collectors.toList());

			String priceHql = """
					    SELECT fp.flight.flightId, MIN(fp.basePrice + fp.dynamicPrice)
					    FROM FlightPrices fp
					    WHERE fp.flight.flightId IN :flightIds
					    GROUP BY fp.flight.flightId
					""";

			Query<Object[]> priceQuery = session.createQuery(priceHql, Object[].class);
			priceQuery.setParameter("flightIds", flightIds);
			List<Object[]> priceResults = priceQuery.getResultList();
			Map<Integer, Double> priceMap = priceResults.stream()
					.collect(Collectors.toMap(row -> (Integer) row[0], row -> (Double) row[1]));

			// Step 3: Build SuggestedDestination objects
			for (Flights flight : selectedFlights) {
				Airports destination = flight.getDestinationAirport();
				if (destination != null) {
					String destName = destination.getCity();
					String destCode = destination.getAirportCode();
					String depDate = flight.getDepartureTime() != null
							? flight.getDepartureTime().toLocalDate().toString()
							: LocalDate.now().plusDays(1).toString();
					double flightPrice = priceMap.getOrDefault(flight.getFlightId(), 0.0);
					String imageUrl = destination.getAirportImage() != null ? destination.getAirportImage()
							: "/images/destinations/" + destination.getCity().toLowerCase().replace(" ", "-") + ".jpg";

					SuggetedDestination suggestion = new SuggetedDestination(departureAirportCode, destName, destCode,
							depDate, flightPrice, imageUrl);
					suggestions.add(suggestion);
				}
			}
			logger.info("Fetched {} suggested destinations for departure={}", suggestions.size(), departureAirportCode);
			return suggestions;

		} catch (Exception e) {
			logger.error("Error fetching suggested destinations: {}", e.getMessage(), e);
			return suggestions; // Return partial results if any
		}
	}

	/**
	 * Fetches the flight status based on flight number and departure date.
	 * 
	 * @param flightNumber  The flight number (e.g., "AA123").
	 * @param departureDate The date of departure to filter the flight.
	 * @return A String representing the flight status, or null if the flight is not
	 *         found or an error occurs.
	 */

	public String fetchFlightStatus(String flightNumber, LocalDate departureDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Flights f WHERE f.flightNumber = :flightNumber "
					+ "AND CAST(f.departureTime AS date) = :departureDate";

			Query<Flights> query = session.createQuery(hql, Flights.class);
			query.setParameter("flightNumber", flightNumber);
			query.setParameter("departureDate", departureDate);

			Flights flight = query.uniqueResult();
			if (flight == null) {
				logger.warn("No flight found for flightNumber={} on departureDate={}", flightNumber, departureDate);
				return null;
			}
			return flight.getStatus();

		} catch (Exception e) {
			logger.error("Error fetching flight status for flightNumber={} on departureDate={}: {}", flightNumber,
					departureDate, e.getMessage(), e);
			return null;

		}
	}
	
	public long countActiveFlights() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate today = LocalDate.now();
            Query<Long> query = session.createQuery(
                "SELECT COUNT(f) FROM Flights f WHERE DATE(f.departureTime) = :today", 
                Long.class
            );
            query.setParameter("today", today);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting active flights", e);
            return 0;
        }
    }
	
	public double getOnTimePercentage() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate today = LocalDate.now();
            
            // Count total flights today
            Query<Long> totalQuery = session.createQuery(
                "SELECT COUNT(f) FROM Flights f WHERE DATE(f.departureTime) = :today", 
                Long.class
            );
            totalQuery.setParameter("today", today);
            long total = totalQuery.uniqueResult();
            
            if (total == 0) return 100.0; // Avoid division by zero
            
            // Count on-time flights
            Query<Long> onTimeQuery = session.createQuery(
                "SELECT COUNT(f) FROM Flights f WHERE DATE(f.departureTime) = :today AND f.status = 'ON_TIME'", 
                Long.class
            );
            onTimeQuery.setParameter("today", today);
            long onTime = onTimeQuery.uniqueResult();
            
            return (onTime * 100.0) / total;
        } catch (Exception e) {
            logger.error("Error calculating on-time percentage", e);
            return 0.0;
        }
    }
	
	public double getOccupancyRate() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	// Get total available seats for today's flights
            String seatsHql = "SELECT SUM(a.seatingCapacity) " +
                            "FROM Flights f " +
                            "JOIN f.aircraft a " +
                            "WHERE DATE(f.departureTime) = CURRENT_DATE";
            Query<Long> seatsQuery = session.createQuery(seatsHql, Long.class);
            Long totalSeats = seatsQuery.uniqueResult();
            
            if (totalSeats == null || totalSeats == 0) {
                return 0.0; // Prevent division by zero
            }
            
            // Get number of bookings for today's flights
            String bookingsHql = "SELECT COUNT(b) " +
                               "FROM Bookings b " +
                               "WHERE DATE(b.flights.departureTime) = CURRENT_DATE";
            Query<Long> bookingsQuery = session.createQuery(bookingsHql, Long.class);
            Long bookedSeats = bookingsQuery.uniqueResult();
            
            return (bookedSeats * 100.0) / totalSeats;
        } catch (Exception e) {
            logger.error("Error calculating occupancy rate", e);
            return 0.0;
        }
    }

	public boolean createFlight(Flights flight) {
		
		org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	
            transaction = session.beginTransaction();
            session.save(flight);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
	}
	

    public List<Flights> getPaginatedFlights(int offset, int limit, String searchQuery, String statusFilter, String dateFilter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	StringBuilder hql = new StringBuilder("SELECT DISTINCT f FROM Flights f " +
                    "LEFT JOIN FETCH f.airline " +
                    "LEFT JOIN FETCH f.departureAirport " +
                    "LEFT JOIN FETCH f.destinationAirport " +
                    "LEFT JOIN FETCH f.aircraft WHERE 1=1");
            
            Map<String, Object> parameters = new HashMap<>();
            
            if (searchQuery != null && !searchQuery.isEmpty()) {
                hql.append(" AND f.flightNumber LIKE :searchQuery");
                parameters.put("searchQuery", "%" + searchQuery + "%");
            }
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                hql.append(" AND f.status = :statusFilter");
                parameters.put("statusFilter", statusFilter);
            }
            if (dateFilter != null && !dateFilter.isEmpty()) {
                hql.append(" AND DATE(f.departureTime) = :dateFilter");
                parameters.put("dateFilter", LocalDate.parse(dateFilter));
            }
            
            Query<Flights> query = session.createQuery(hql.toString(), Flights.class);
            
            // Set all parameters
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            
            return query.list();
        }
    }
    
    public int getFilteredFlightCount(String searchQuery, String statusFilter, String dateFilter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT f) FROM Flights f WHERE 1=1");
            
            Map<String, Object> parameters = new HashMap<>();
            
            if (searchQuery != null && !searchQuery.isEmpty()) {
                hql.append(" AND f.flightNumber LIKE :searchQuery");
                parameters.put("searchQuery", "%" + searchQuery + "%");
            }
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                hql.append(" AND f.status = :statusFilter");
                parameters.put("statusFilter", statusFilter);
            }
            
            if (dateFilter != null && !dateFilter.isEmpty()) {
                hql.append(" AND DATE(f.departureTime) = :dateFilter");
                parameters.put("dateFilter", LocalDate.parse(dateFilter));
            }
            
            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            
            // Set all parameters
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            
            return query.uniqueResult().intValue();
        }
    }

    public int getTotalFlightCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Flights", Long.class);
            return query.uniqueResult().intValue();
        }
    }
    
    public Flights getFlightById(int flightId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            String hql = "SELECT DISTINCT f FROM Flights f "
                       + "LEFT JOIN FETCH f.airline "
                       + "LEFT JOIN FETCH f.departureAirport "
                       + "LEFT JOIN FETCH f.destinationAirport "
                       + "LEFT JOIN FETCH f.aircraft "
                       + "WHERE f.flightId = :flightId";
            
            return session.createQuery(hql, Flights.class)
                        .setParameter("flightId", flightId)
                        .uniqueResult();
        } catch (Exception e) {
            logger.error("Error fetching flight by ID {}: {}", flightId, e.getMessage(), e);
            return null;
        }
    }
    
 // Update
    public boolean updateFlight(Flights flight) {
    	
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(flight);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
    
 // Delete
    public boolean deleteFlight(int flightId) {
    	System.out.println("0000000000000000 to delete the data");
    	org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Flights flight = session.get(Flights.class, flightId);
            if (flight != null) {
                session.delete(flight);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                try {
					transaction.rollback();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
            e.printStackTrace();
            return false;
        }
    }

}

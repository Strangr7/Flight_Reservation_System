package com.flightreservation.DTO;

import com.flightreservation.model.BaggageRules;
import com.flightreservation.model.Flights;

import com.flightreservation.model.Stops;
import com.flightreservation.model.enums.FlightClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a one-way flight search result.
 * Encapsulates flight details, stop information, and pricing/class options.
 * Used to transfer data between layers (e.g., DAO to controller to UI).
 */
public class FlightResultOneWay {
	// The flight entity containing core flight details (e.g., departure time,
	// airline)
	private Flights flight;

	// Number of stops in the flight (e.g., 0 for direct, 1 for one stop)
	private int stopCount;

	// List of pricing options, each tied to a flight class (e.g., economy price,
	// business price)
	private List<PriceClass> pricesAndClasses;

	// List of detailed stop information (e.g., airport codes, layover times)
	private List<Stops> stops; // New field for detailed stop information

	/**
	 * Constructor to initialize a one-way flight result. Handles null values
	 * gracefully by providing defaults.
	 * 
	 * @param flight    The Flights entity with core flight data
	 * @param stopCount The number of stops (passed as Long to match DB types,
	 *                  converted to int)
	 * @param stops     List of Stops objects with detailed stop info
	 */
	public FlightResultOneWay(Flights flight, Long stopCount, List<Stops> stops) {
		this.flight = flight; // Set the flight object directly
		// Convert Long to int, default to 0 if null (avoids NullPointerException)
		this.stopCount = stopCount != null ? stopCount.intValue() : 0;
		// Use provided stops list, or an empty list if null
		this.stops = stops != null ? stops : new ArrayList<>();
		// Initialize pricesAndClasses as an empty list (populated later)
		this.pricesAndClasses = new ArrayList<>();

	}

	// Getters
	/**
	 * @return The Flights object containing flight details
	 */
	public Flights getFlight() {
		return flight;	
	}

	/**
	 * @return The number of stops in the flight
	 */
	public int getStopCount() {
		return stopCount;
	}

	/**
	 * @return The list of pricing options for different flight classes
	 */
	public List<PriceClass> getPricesAndClasses() {
		return pricesAndClasses;
	}

	/**
	 * @return The list of detailed stop information
	 */
	public List<Stops> getStops() {
		return stops;
	}

	// Setters
	/**
	 * Sets the flight entity.
	 * 
	 * @param flight The Flights object to set
	 */
	public void setFlight(Flights flight) {
		this.flight = flight;
	}

	/**
	 * Sets the number of stops.
	 * 
	 * @param stopCount The number of stops to set
	 */
	public void setStopCount(int stopCount) {
		this.stopCount = stopCount;
	}

	/**
	 * Sets the list of pricing options.
	 * 
	 * @param pricesAndClasses The list of PriceClass objects to set
	 */
	public void setPricesAndClasses(List<PriceClass> pricesAndClasses) {
		this.pricesAndClasses = pricesAndClasses;
	}

	/**
	 * Sets the list of detailed stop information.
	 * 
	 * @param stops The list of Stops objects to set
	 */
	public void setStops(List<Stops> stops) {
		this.stops = stops;
	}

	/**
	 * Inner class representing a pricing option tied to a specific flight class.
	 * Holds base and dynamic prices for flexibility (e.g., seasonal pricing).
	 */
	public static class PriceClass {
		// The flight class (e.g., ECONOMY, BUSINESS) from the enum
		private FlightClasses className;

		// Base price for this class (standard rate)
		private double basePrice;

		// Dynamic price for this class (e.g., adjusted for demand or time)
		private double dynamicPrice;

		private BaggageRules baggageRules;

		private int availableSeats;

		/**
		 * Constructor for a pricing option. Handles null values by defaulting to 0.0.
		 * 
		 * @param className    The flight class (e.g., ECONOMY)
		 * @param basePrice    The base price (can be null from DB)
		 * @param dynamicPrice The dynamic price (can be null from DB)
		 */
		public PriceClass(FlightClasses className, Double basePrice, Double dynamicPrice, BaggageRules baggageRules,
				int availableSeats) {
			this.className = className; // Set the class directly
			// Default to 0.0 if basePrice is null
			this.basePrice = basePrice != null ? basePrice : 0.0;
			// Default to 0.0 if dynamicPrice is null
			this.dynamicPrice = dynamicPrice != null ? dynamicPrice : 0.0;

			this.baggageRules = baggageRules;
			this.availableSeats = availableSeats;
		}

		// Getters
		/**
		 * @return The flight class (e.g., ECONOMY, BUSINESS)
		 */
		public FlightClasses getClassName() {
			return className;
		}

		/**
		 * @return The base price for this class
		 */
		public double getBasePrice() {
			return basePrice;
		}

		/**
		 * @return The dynamic price for this class
		 */
		public double getDynamicPrice() {
			return dynamicPrice;
		}

		public BaggageRules getBaggageRules() {
			return baggageRules;
		}

		public int getAvailableSeats() {
			return availableSeats;
		}

		// Setters
		/**
		 * Sets the flight class.
		 * 
		 * @param className The flight class to set
		 */
		public void setClassName(FlightClasses className) {
			this.className = className;
		}

		/**
		 * Sets the base price.
		 * 
		 * @param basePrice The base price to set
		 */
		public void setBasePrice(double basePrice) {
			this.basePrice = basePrice;
		}

		/**
		 * Sets the dynamic price.
		 * 
		 * @param dynamicPrice The dynamic price to set
		 */
		public void setDynamicPrice(double dynamicPrice) {
			this.dynamicPrice = dynamicPrice;
		}

		public void setBaggageRules(BaggageRules baggageRules) {
			this.baggageRules = baggageRules;
		}
		
		public void setAvailableSeats(int availableSeats) {
			this.availableSeats = availableSeats;
		}

	}
}
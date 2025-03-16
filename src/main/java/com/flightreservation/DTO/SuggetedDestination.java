package com.flightreservation.DTO;

public class SuggetedDestination {
	private String departureCode;
	private String destinationName;
	private String destinationCode;
	private String departureDate;
	private double price;
	private String airportImage;


	public SuggetedDestination(String departureCode, String destinationName, String destinationCode,
			String departureDate, double price, String airportImage) {
		this.departureCode = departureCode;
		this.destinationName = destinationName;
		this.destinationCode = destinationCode;
		this.departureDate = departureDate;
		this.price = price;
		this.airportImage = airportImage;
	}

	// Getters
	public String getDepartureCode() {
		return departureCode;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public double getPrice() {
		return price;
	}

	public String getAirportImage() {
		return airportImage;
	}


	public void setDepartureCode(String departureCode) {
		this.departureCode = departureCode;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setAirportImage(String airportImage) {
		this.airportImage = airportImage;
	}
}
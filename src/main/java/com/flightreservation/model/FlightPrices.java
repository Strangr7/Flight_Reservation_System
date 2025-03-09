package com.flightreservation.model;

import com.flightreservation.model.embedded.FlightPriceId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "FlightPrices")
public class FlightPrices {

	@EmbeddedId
	private FlightPriceId id;

	@ManyToOne
	@MapsId("flightId")
	@JoinColumn(name = "flight_id", nullable = false)
	private Flights flight;

	@ManyToOne
	@MapsId("ClassId")
	@JoinColumn(name = "class_id", nullable = false)
	private Classes flightClass;

	@Column(name = "base_price", nullable = false)
	private double basePrice;

	@Column(name = "dynamic_price", nullable = false)
	private double dynamicPrice;

	public FlightPriceId getId() {
		return id;
	}

	public void setId(FlightPriceId id) {
		this.id = id;
	}

	public Flights getFlight() {
		return flight;
	}

	public void setFlight(Flights flight) {
		this.flight = flight;
	}

	public Classes getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(Classes flightClass) {
		this.flightClass = flightClass;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getDynamicPrice() {
		return dynamicPrice;
	}

	public void setDynamicPrice(double dynamicPrice) {
		this.dynamicPrice = dynamicPrice;
	}

	@Override
	public String toString() {
		return "FlightPrices{" + "id=" + id + ", flight=" + flight.getFlightId() + ", flightClass="
				+ flightClass.getClassId() + ", basePrice=" + basePrice + ", dynamicPrice=" + dynamicPrice + '}';
	}

}

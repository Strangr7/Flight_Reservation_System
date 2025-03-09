package com.flightreservation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "passengers")
public class Passengers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "passenger_id")
	private int passengerId;

	@ManyToOne
	@JoinColumn(name = "booking_id")
	private Bookings bookings;

	@Column(name = "passenger_name", nullable = false, length = 255)
	private String passengerName;

	@Column(name = "passenger_age", nullable = false)
	private int passangerAge;

	@Column(name = "passenger_passport", nullable = false, length = 255)
	private String passangerPassport;

	public Passengers() {
		
	}

	public Passengers(int passengerId, Bookings bookings, String passengerName, int passengerAge,
			String passengerPassport) {
		this.passengerId = passengerId;
		this.bookings = bookings;
		this.passengerName = passengerName;
		this.passangerAge = passengerAge;
		this.passangerPassport = passengerPassport;
	}

	public int getPassengerId() {
		return passengerId;
	}

	public Bookings getBookings() {
		return bookings;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public int getPassangerAge() {
		return passangerAge;
	}

	public String getPassangerPassport() {
		return passangerPassport;
	}

	public void setPassengerId(int passengerId) {
		this.passengerId = passengerId;
	}

	public void setBookings(Bookings bookings) {
		this.bookings = bookings;
	}

	public void setPassangerAge(int passangerAge) {
		this.passangerAge = passangerAge;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public void setPassangerPassport(String passangerPassport) {
		this.passangerPassport = passangerPassport;
	}

	@Override
	public String toString() {
		return "Passengers{" + "" + "passengerId=" + passengerId + ", bookingId=" + bookings.getBookingId()
				+ ", passengerName" + passengerName + ", passengerAge" + passangerAge + ", passsengerPassport"
				+ passangerPassport + "}";
	}
}

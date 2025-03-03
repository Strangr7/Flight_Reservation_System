package com.flightreservation.model;

import java.io.Serializable;

public class Passengers implements Serializable {

	private static final long serialVersionUID = 1L;

	private int passengerId;
	private Bookings bookings;
	private String passengerName;
	private int passangerAge;
	private String passangerPassport;

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

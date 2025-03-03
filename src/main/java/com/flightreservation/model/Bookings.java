package com.flightreservation.model;

import java.io.Serializable;

import com.flightreservation.model.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Bookings implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private int bookingId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Users users;

	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flights flights;

	@ManyToOne
	@JoinColumn(name = "return_flight_id", nullable = true)
	private Flights returnFlights;

	@ManyToOne
	@JoinColumn(name = "seat_id", nullable = false)
	private Seats seats;

	@ManyToOne
	@JoinColumn(name = "meal_id", nullable = true)
	private Meals meals;

	@Column(name = "pnr", unique = true, nullable = false)
	private String PNR;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus;

	public Bookings(int bookingId, Users users, Flights flights, Flights returnFlights, Seats seats, Meals meals,
			String PNR, BookingStatus bookingStatus) {
		// TODO Auto-generated constructor stub
	}

	public int getBookingId() {
		return bookingId;
	}

	public Users getUsers() {
		return users;
	}

	public Flights getFlights() {
		return flights;
	}

	public Flights getReurnFlights() {
		return returnFlights;
	}

	public Seats getSeats() {
		return seats;
	}

	public Meals getMeals() {
		return meals;
	}

	public String getPNR() {
		return PNR;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public void setFlights(Flights flights) {
		this.flights = flights;
	}

	public void setReturnFlights(Flights returnFlights) {
		this.returnFlights = returnFlights;
	}

	public void setSeats(Seats seats) {
		this.seats = seats;
	}

	public void setMeals(Meals meals) {
		this.meals = meals;
	}

	public void setPNR(String PNR) {
		this.PNR = PNR;
	}

	public void setStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

}

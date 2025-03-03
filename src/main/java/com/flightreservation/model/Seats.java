package com.flightreservation.model;

import java.io.Serializable;
import jakarta.persistence.*;
import com.flightreservation.util.annotation.NotNull;

@Entity
@Table(name = "seats")
public class Seats implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id")
	private int seatId;

	@NotNull(message = "Flight cannot be empty!!")
	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flights flight; // Changed from 'flights' to 'flight' for clarity

	@NotNull(message = "Seat Number cannot be empty")
	@Column(name = "seat_number", nullable = false, length = 10)
	private String seatNumber;

	@NotNull(message = "Class cannot be empty")
	@ManyToOne
	@JoinColumn(name = "class_id", nullable = false) // Fixed incorrect annotation
	private Classes seatClass; // Changed name from 'classes' to 'seatClass'

	@NotNull
	@Column(name = "is_available", nullable = false)
	private boolean isAvailable;

	// Default Constructor
	public Seats() {
	}

	// Parameterized Constructor
	public Seats(int seatId, Flights flight, String seatNumber, Classes seatClass, boolean isAvailable) {
		this.seatId = seatId;
		this.flight = flight;
		this.seatNumber = seatNumber;
		this.seatClass = seatClass;
		this.isAvailable = isAvailable;
	}

	// Getters and Setters
	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public Flights getFlight() {
		return flight;
	}

	public void setFlight(Flights flight) {
		this.flight = flight;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public Classes getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(Classes seatClass) {
		this.seatClass = seatClass;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;

	}

	@Override
	public String toString() {
		return "Seats{" + "seatId=" + seatId + ", flightId=" + (flight != null ? flight.getFlightId() : "null")
				+ ", seatNumber='" + seatNumber + '\'' + ", classId="
				+ (seatClass != null ? seatClass.getClassId() : "null") + ", isAvailable=" + isAvailable + '}';
	}

}

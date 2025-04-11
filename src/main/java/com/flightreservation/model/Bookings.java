package com.flightreservation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Bookings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private int bookingId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
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
	
	@Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // When the booking was made

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate; // Departure date of the booked flight

	@Column(name = "pnr", unique = true, nullable = false)
	private String PNR;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus;
	
	@OneToMany(mappedBy = "bookings")
	private List<Passengers> passengers;
	
	public Bookings() {
		// TODO Auto-generated constructor stub
	}

	public Bookings(int bookingId, Users users, Flights flights, Flights returnFlights, Seats seats, Meals meals, String PNR, BookingStatus bookingStatus, LocalDate bookingDate, Passengers passengers) {
	    this.bookingId = bookingId;
	    this.users = users;
	    this.flights = flights;
	    this.returnFlights = returnFlights;
	    this.seats = seats;
	    this.meals = meals;
	    this.PNR = PNR;
	    this.bookingStatus = bookingStatus;
	    this.bookingDate = bookingDate;
	    this.passengers = new ArrayList<>();
	}
	
	@PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // Auto-set on creation
        // If bookingDate isn't set, derive it from the flight
        if (this.bookingDate == null && this.flights != null) {
            this.bookingDate = this.flights.getDepartureTime().toLocalDate();
        }
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

	public Flights getReturnFlights() {
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
	
	public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

    public List<Passengers> getPassengers() {
        return passengers;
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
	
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setPassengers(List<Passengers> passengers) {
        this.passengers = passengers;
    }
}

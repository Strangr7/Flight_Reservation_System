package com.flightreservation.model;

import java.time.LocalDateTime;

import com.flightreservation.model.enums.PaymentStatus;

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
@Table(name = "payments")
public class Payments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private int paymentId;

	@ManyToOne
	@JoinColumn(name = "booking_id", nullable = false)
	private Bookings bookings;

	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "payment_status")
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	@Column(name = "payment_date")
	private LocalDateTime paymentDateTime;

	public Payments() {
	}

	public Payments(int paymentId, Bookings bookings, Double amount, PaymentStatus paymentStatus,
			LocalDateTime paymentDateTime) {
		this.paymentId = paymentId;
		this.bookings = bookings;
		this.amount = amount;
		this.paymentStatus = paymentStatus;
		this.paymentDateTime = paymentDateTime;

	}

	public int getPaymentId() {
		return paymentId;
	}

	public Bookings getBookings() {
		return bookings;
	}

	public Double getAmount() {
		return amount;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public LocalDateTime getPaymentDateTime() {
		return paymentDateTime;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public void setBookings(Bookings bookings) {
		this.bookings = bookings;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public void setPaymentDateTime(LocalDateTime paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}
}

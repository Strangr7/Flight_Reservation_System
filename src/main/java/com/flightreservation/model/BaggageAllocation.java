package com.flightreservation.model;



public class BaggageAllocation  {



	private int baggageAllocationId;
	private Bookings bookings;
	private int checkedbags;
	private int carryOns;
	private int extraBaggageFee;

	public BaggageAllocation(int baggageAllocationid, Bookings bookings, int checkedbags, int carryOns,
			int extraBaggagefee) {
		this.baggageAllocationId = baggageAllocationid;
		this.bookings = bookings;
		this.checkedbags = checkedbags;
		this.carryOns = carryOns;
		this.extraBaggageFee = extraBaggagefee;
	}

	public int getBaggageAllocationId() {
		return baggageAllocationId;
	}

	public Bookings getBookings() {
		return bookings;
	}

	public int getCheckedbags() {
		return checkedbags;
	}

	public int getCarryOns() {
		return carryOns;
	}

	public int getExtraBaggageFee() {
		return extraBaggageFee;
	}

	public void setBaggageAllocationId(int baggageAllocationId) {
		this.baggageAllocationId = baggageAllocationId;
	}

	public void setBookings(Bookings bookings) {
		this.bookings = bookings;
	}

	public void setCarryOns(int carryOns) {
		this.carryOns = carryOns;
	}

	public void setCheckedbags(int checkedbags) {
		this.checkedbags = checkedbags;
	}

	public void setExtraBaggageFee(int extraBaggageFee) {
		this.extraBaggageFee = extraBaggageFee;
	}

}

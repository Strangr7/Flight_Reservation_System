package com.flightreservation.model.enums;

public enum BookingStatus {
	CONFIRMED("Confirmed"), CANCELLED("Cancelled"), CHECKEDIN("Checked-in");

	private final String displayName;

	BookingStatus(String displayName) {
		this.displayName = displayName;
		// TODO Auto-generated constructor stub
	}

	public String displayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}

}

package com.flightreservation.model.enums;

public enum FlightClasses {
	ECONOMY("Economy"), BUSINESS("Business"), FIRST_CLASS("First Class");

	private final String displayName;

	FlightClasses(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}

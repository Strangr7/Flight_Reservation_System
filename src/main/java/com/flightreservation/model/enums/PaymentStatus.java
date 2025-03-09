package com.flightreservation.model.enums;

public enum PaymentStatus {
	PENDING("Pending"), COMPLETED("Completed"), FAILED("Failed");

	private final String displayName;

	PaymentStatus(String displayName) {
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

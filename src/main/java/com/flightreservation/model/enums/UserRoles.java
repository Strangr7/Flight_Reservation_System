package com.flightreservation.model.enums;

public enum UserRoles {
	TRAVELER("Traveler"), ADMIN("Admin");

	private final String displayName;

	public String getDisplayName() {
		return displayName;
	}

	UserRoles(String displayName) {
		this.displayName = displayName;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return displayName;
	}

}

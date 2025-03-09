package com.flightreservation.model.enums;

public enum UserRoles {
	TRAVELER("Traveler"), ADMIN("Admin");

	private final String displayName;
	
	UserRoles(String displayName) {
		this.displayName = displayName;
		// TODO Auto-generated constructor stub
	}

	public String getDisplayName() {
		return displayName;
	}

	
	
	@Override
	public String toString() {
		return displayName;
	}

}

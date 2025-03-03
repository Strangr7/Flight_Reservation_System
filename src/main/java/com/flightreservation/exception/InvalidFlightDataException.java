package com.flightreservation.exception;

public class InvalidFlightDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidFlightDataException(String message) {
		super(message);
	}

}

package com.flightreservation.service;

import java.util.List;

import com.flightreservation.dao.AirportDao;
import com.flightreservation.model.Airports;

public class AirportService {

	private final AirportDao airportDao = new AirportDao();

	// fetch all flight

	public List<Airports> fetchAllAirport() {
		return airportDao.fetchAllAirports();
	}

	// Search airports based on a term (for autocomplete)

	public List<Airports> searchAirports(String term) {
		return airportDao.searchAirports(term);
	}

}

package com.flightreservation.service;

import java.util.List;

import com.flightreservation.dao.AirlineDAO;
import com.flightreservation.model.Airlines;

public class AirlineService {

	private final AirlineDAO airlineDAO = new AirlineDAO();
	// fetch all airlines

	public List<Airlines> fetchAllAirlines() {
		return airlineDAO.fetchAllAirlines();
	}

}

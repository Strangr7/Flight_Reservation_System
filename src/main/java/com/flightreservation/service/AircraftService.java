package com.flightreservation.service;

import java.util.List;

import com.flightreservation.dao.AircraftDAO;
import com.flightreservation.model.Aircrafts;

public class AircraftService {

	public AircraftService() {
		// TODO Auto-generated constructor stub
	}
	private final AircraftDAO aircraftDAO = new AircraftDAO();
	// fetch all airlines

	public List<Aircrafts> fetchAllAircrafts() {
		System.out.println("*********************" + aircraftDAO.fetchAllAircrafts());
		return aircraftDAO.fetchAllAircrafts();
	}

}

package com.flightreservation.service;

import java.util.List;

import com.flightreservation.dao.MealDAO;
import com.flightreservation.dao.SeatDAO;
import com.flightreservation.model.Meals;
import com.flightreservation.model.Seats;

public class SeatService {

	private final SeatDAO seatDAO = new SeatDAO();
	
	//fetch al;l available seats
	
	public List<Seats> fetchallAvailableSeats(){
		return seatDAO.getAvailableSeats(0, null);
	}

}

package com.flightreservation.service;

import java.util.List;

import com.flightreservation.dao.MealDAO;
import com.flightreservation.model.Meals;

public class MealService {

	private final MealDAO mealDAO = new MealDAO();

	// fetch all meals

	public List<Meals> fetchMeals() {
		return mealDAO.getAllMeals();
	}

}

package com.flightreservation.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.model.Meals;
import com.flightreservation.util.HibernateUtil;

public class MealDAO {

	private static final Logger logger = LoggerFactory.getLogger(MealDAO.class);

	// Fetch all meals options

	public List<Meals> getAllMeals() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Meals";
			Query<Meals> query = session.createQuery(hql, Meals.class);
			List<Meals> meals = query.list();
			logger.info("Found {} meal options", meals.size());
			return meals;
		}catch (Exception e) {
            logger.error("Error fetching meals: {}", e.getMessage(), e);
            return List.of();
        }

	}

}

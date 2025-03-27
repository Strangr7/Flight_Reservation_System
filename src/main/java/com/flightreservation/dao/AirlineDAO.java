package com.flightreservation.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.model.Airlines;
import com.flightreservation.util.HibernateUtil;

public class AirlineDAO {

	private static final Logger Logger = LoggerFactory.getLogger(AirlineDAO.class);

	// fetch all airlines
	public List<Airlines> fetchAllAirlines() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Airlines";
			Query<Airlines> query = session.createQuery(hql, Airlines.class);
			return query.list();

		} catch (Exception e) {
			Logger.error("Error fetching all airlines: {}", e.getMessage(), e);
			return null;
		}
	}
}

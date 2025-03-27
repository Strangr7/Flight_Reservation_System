package com.flightreservation.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.model.Airports;
import com.flightreservation.util.HibernateUtil;

public class AirportDao {

	private static final Logger Logger = LoggerFactory.getLogger(AirportDao.class);

	// Fetch all airports
	public List<Airports> fetchAllAirports() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Airports";
			Query<Airports> query = session.createQuery(hql, Airports.class);
			return query.list();
		} catch (Exception e) {
			Logger.error("Error fetching all flights: {}", e.getMessage(), e);
			return null;
		}
	}

	// Search airports
	public List<Airports> searchAirports(String term) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Airports WHERE LOWER(airportCode) LIKE :term OR LOWER(city) LIKE :term OR LOWER(country) LIKE :term";
			Query<Airports> query = session.createQuery(hql, Airports.class);
			query.setParameter("term", "%" + term.toLowerCase() + "%");
			List<Airports> results = query.list();
			Logger.debug("HQL Query: {}, Search term: {}, Results: {}", hql, term, results.size());
			return results;
		} catch (Exception e) {
			Logger.error("Error searching airports: {}", e.getMessage(), e);
			return null;
		}
	}

	// add airport

}
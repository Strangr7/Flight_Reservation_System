package com.flightreservation.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.model.Aircrafts;
import com.flightreservation.util.HibernateUtil;

public class AircraftDAO {
	private static final Logger Logger = LoggerFactory.getLogger(AircraftDAO.class);

	// fetch all aircrafts
		public List<Aircrafts> fetchAllAircrafts() {
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				String hql = "FROM Aircrafts";
				Query<Aircrafts> query = session.createQuery(hql, Aircrafts.class);
				return query.list();

			} catch (Exception e) {
				Logger.error("Error fetching all aircrafts: {}", e.getMessage(), e);
				return null;
			}
		}

}

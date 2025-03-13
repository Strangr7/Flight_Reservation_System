package com.flightreservation.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightreservation.model.Airports;
import com.flightreservation.util.HibernateUtil;

public class AirportDao {

	private static final Logger Logger = LoggerFactory.getLogger(AirportDao.class);
	private static final String API_KEY = "64886dd092dda154e9b82de5fc7225a1"; 
	private static final String API_URL = "http://api.aviationstack.com/v1/airports?access_key=" + API_KEY;

	// Fetch all airports (unchanged)
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

	// Search airports (unchanged)
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

	// Import airports from API with pagination (for one-time use)
	public void importAirportsFromAPI() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();

			int offset = 0;
			int limit = 100;
			boolean hasMore = true;
			int totalImported = 0;

			while (hasMore) {
				String urlString = API_URL + "&offset=" + offset;
				String jsonResponse = fetchAirportData(urlString);
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readTree(jsonResponse);
				JsonNode airportsArray = rootNode.path("data");

				for (JsonNode airportNode : airportsArray) {
					String iataCode = airportNode.path("iata_code").asText();
					String name = airportNode.path("airport_name").asText();
					String city = airportNode.path("city").asText();
					String country = airportNode.path("country_name").asText();

					if (iataCode == null || iataCode.isEmpty() || "null".equals(iataCode)) {
						continue;
					}

					if (name.length() > 100)
						name = name.substring(0, 100);
					if (city.length() > 100)
						city = city.substring(0, 100);
					if (country.length() > 100)
						country = country.substring(0, 100);

					Airports airport = new Airports();
					airport.setAirportCode(iataCode);
					airport.setAirportName(name);
					airport.setCity(city);
					airport.setCountry(country);

					session.merge(airport);
					totalImported++;
				}

				offset += limit;
				hasMore = airportsArray.size() == limit;
				Logger.info("Imported batch at offset {}, total so far: {}", offset, totalImported);
			}

			session.getTransaction().commit();
			Logger.info("Finished importing {} airports from Aviationstack API", totalImported);
		} catch (Exception e) {
			Logger.error("Error importing airports from API: {}", e.getMessage(), e);
		}
	}

	// Fetch data from API
	private String fetchAirportData(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			throw new RuntimeException("Failed to fetch data: HTTP " + responseCode);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder response = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		conn.disconnect();
		return response.toString();
	}

	// Main method for one-time import
	public static void main(String[] args) {
		AirportDao dao = new AirportDao();
		dao.importAirportsFromAPI();
	}
}
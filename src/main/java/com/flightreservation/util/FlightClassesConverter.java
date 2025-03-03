package com.flightreservation.util;

import com.flightreservation.model.enums.FlightClasses;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FlightClassesConverter implements AttributeConverter<FlightClasses, String> {
	@Override
	public String convertToDatabaseColumn(FlightClasses attribute) {
		if (attribute == null)
			return null;
		return attribute.getDisplayName(); // Uses "Economy", "Business", "First Class" for the database
	}

	@Override
	public FlightClasses convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;
		switch (dbData.trim().toLowerCase()) {
		case "economy":
			return FlightClasses.ECONOMY;
		case "business":
			return FlightClasses.BUSINESS;
		case "first class":
			return FlightClasses.FIRST_CLASS;
		default:
			throw new IllegalArgumentException("Unknown class name: " + dbData);
		}
	}
}
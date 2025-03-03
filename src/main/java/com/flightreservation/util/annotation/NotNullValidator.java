package com.flightreservation.util.annotation;

import jakarta.validation.ValidationException;
import java.lang.reflect.Field;

public class NotNullValidator {
	public static void NotNullValidate(Object obj) throws ValidationException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(NotNull.class)) {
				field.setAccessible(true); // Allow access to private fields
				Object value = field.get(obj);

				// Check if the field is null or empty
				if (value == null || (value instanceof String && ((String) value).isEmpty())) {
					NotNull annotation = field.getAnnotation(NotNull.class);
					throw new ValidationException(annotation.message());
				}
			}
		}
	}
}

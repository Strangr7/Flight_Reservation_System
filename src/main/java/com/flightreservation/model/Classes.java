package com.flightreservation.model;



import com.flightreservation.model.enums.FlightClasses;
import com.flightreservation.util.FlightClassesConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "classes")
public class Classes {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "class_id")
	private int classId;

	@Convert(converter = FlightClassesConverter.class)
	@Column(name = "class_name", nullable = false, unique = true)
	private FlightClasses className;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public FlightClasses getClassName() {
		return className;
	}

	public void setClassName(FlightClasses className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "Classes{" + "classId=" + classId + ", className=" + className + +'\'' + '}';
	}

}

package com.flightreservation.model.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class FlightPriceId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int flightId;
	private int classId;
	
	public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightPriceId that = (FlightPriceId) o;
        return flightId == that.flightId && classId == that.classId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, classId);
    }

}

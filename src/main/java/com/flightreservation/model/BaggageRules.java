package com.flightreservation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "baggagerules")
public class BaggageRules implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "baggage_rule_id")
	private int baggageRuleId;

	@NotNull(message = "Flight cannot be empty!!")
	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flights flight;

	@NotNull(message = "Class cannot be empty!!")
	@ManyToOne
	@JoinColumn(name = "class_id", nullable = false)
	private Classes classes;

	@NotNull(message = "Allowed checked bags cannot be empty")
	@Column(name = "allowed_checked_bags", nullable = false)
	private int allowedCheckedBags;

	@NotNull(message = "Allowed carry-ons cannot be empty")
	@Column(name = "allowed_carry_ons", nullable = false)
	private int allowedCarryOns;

	@NotNull(message = "Checked bag weight limit cannot be empty")
	@Column(name = "checked_bag_weight_limit", nullable = false, precision = 5, scale = 2)
	private BigDecimal checkedBagWeightLimit;

	@NotNull(message = "Carry-on weight limit cannot be empty")
	@Column(name = "carry_on_weight_limit", nullable = false, precision = 5, scale = 2)
	private BigDecimal carryOnWeightLimit;

	// Getters and Setters
	public int getBaggageRuleId() {
		return baggageRuleId;
	}

	public void setBaggageRuleId(int baggageRuleId) {
		this.baggageRuleId = baggageRuleId;
	}

	public Flights getFlight() {
		return flight;
	}

	public void setFlight(Flights flight) {
		this.flight = flight;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public int getAllowedCheckedBags() {
		return allowedCheckedBags;
	}

	public void setAllowedCheckedBags(int allowedCheckedBags) {
		this.allowedCheckedBags = allowedCheckedBags;
	}

	public int getAllowedCarryOns() {
		return allowedCarryOns;
	}

	public void setAllowedCarryOns(int allowedCarryOns) {
		this.allowedCarryOns = allowedCarryOns;
	}

	public BigDecimal getCheckedBagWeightLimit() {
		return checkedBagWeightLimit;
	}

	public void setCheckedBagWeightLimit(BigDecimal checkedBagWeightLimit) {
		this.checkedBagWeightLimit = checkedBagWeightLimit;
	}

	public BigDecimal getCarryOnWeightLimit() {
		return carryOnWeightLimit;
	}

	public void setCarryOnWeightLimit(BigDecimal carryOnWeightLimit) {
		this.carryOnWeightLimit = carryOnWeightLimit;
	}

	// toString method
	@Override
	public String toString() {
		return "BaggageRules{" + "baggageRuleId=" + baggageRuleId + ", flight="
				+ (flight != null ? flight.getFlightId() : "null") + ", classes="
				+ (classes != null ? classes.getClassId() : "null") + ", allowedCheckedBags=" + allowedCheckedBags
				+ ", allowedCarryOns=" + allowedCarryOns + ", checkedBagWeightLimit=" + checkedBagWeightLimit
				+ ", carryOnWeightLimit=" + carryOnWeightLimit + '}';
	}
}

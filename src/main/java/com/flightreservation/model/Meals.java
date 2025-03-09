package com.flightreservation.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "meals")
public class Meals  {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meal_id")
	private int mealsId;

	@Column(name = "meal_name")
	private String mealName;

	@Column(name = "description")
	private String description;
	
	public Meals() {}

	public Meals(int mealsId, String mealName, String decription) {
		this.mealsId = mealsId;
		this.mealName = mealName;
		this.description = decription;

	}

	public int getMealsId() {
		return mealsId;

	}

	public String getMealName() {
		return mealName;
	}

	public String getDescription() {
		return description;
	}

	public void setMealId(int mealsId) {
		this.mealsId = mealsId;
	}

	public void setMealName(String meanlName) {
		this.mealName = meanlName;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Meals{" +"mealsID= "+mealsId+", mealName=" + mealName+", description=" +description+"}";
	}

}

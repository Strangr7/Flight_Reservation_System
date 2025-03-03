package com.flightreservation.model;

import com.flightreservation.model.enums.UserRoles;


public class Users {
	private int userId;
	private String name;
	private String phone;
	private String password;
	private UserRoles userRoles;

	public Users(int userId, String name, String phone, String password, UserRoles userRoles) {
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.userRoles = userRoles;
	}

	// toString method

	// Getters and Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRoles getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(UserRoles userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public String toString() {
		return "Users{" + "userId=" + userId + ", name='" + name + '\'' + ", phone='" + phone + '\'' + ", password='"
				+ password + '\'' + ", userRoles=" + userRoles + '}';
	}
}
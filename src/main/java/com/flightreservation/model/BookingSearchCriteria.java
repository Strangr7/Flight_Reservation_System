package com.flightreservation.model;

import java.time.LocalDate;

public class BookingSearchCriteria {
    private String searchQuery;
    private String statusFilter;
    private String dateFilter;
    private String flightNumber; 

    // Default constructor
    public BookingSearchCriteria() {
    }

    // Constructor with parameters
    public BookingSearchCriteria(String searchQuery, String statusFilter, String dateFilter, String flightNumber) {
        this.searchQuery = searchQuery;
        this.statusFilter = statusFilter;
        this.dateFilter = dateFilter;
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    // Getters and Setters
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(String statusFilter) {
        this.statusFilter = statusFilter;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    // Helper method to check if search is active
    public boolean hasFilters() {
        return (searchQuery != null && !searchQuery.isEmpty()) ||
               (statusFilter != null && !statusFilter.isEmpty()) ||
               (dateFilter != null && !dateFilter.isEmpty()) || 
               (flightNumber != null && !flightNumber.isEmpty());
    }
}
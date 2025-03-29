<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- Detect current page --%>
<c:set var="currentPage" value="${fn:substringAfter(pageContext.request.requestURI, '/')}" />
<div class="sidenav d-flex flex-column flex-shrink-0 p-3 text-white" style="width: 280px; height: 100vh;">

    <!-- Main Menu -->
    <ul class="nav nav-pills flex-column mb-auto">
        <!-- Dashboard -->
        <li class="nav-item ${fn:contains(currentPage, 'adminPanel') ? 'active' : ''}">
            <a href="admin?page=adminPanel" class="nav-link ${param.page == 'adminPanel' ? 'active' : ''}">
                <i class="bi bi-speedometer2 me-2"></i>
                Dashboard
            </a>
        </li>

        <!-- Flight Management -->
        <li class="nav-item ${fn:contains(currentPage, 'flights') ? 'active' : ''}">
            <a href="admin?page=flights" class="nav-link ${param.page == 'flights' ? 'active' : ''}">
                <i class="bi bi-airplane me-2"></i>
                Flights
            </a>
        </li>

        <!-- Booking Management -->
        <li>
            <a href="admin?page=bookings" class="nav-link ${param.page == 'bookings' ? 'active' : ''}">
                <i class="bi bi-ticket-perforated me-2"></i>
                Bookings
            </a>
        </li>

        <!-- Airport Management -->
        <li>
            <a href="admin?page=airports" class="nav-link ${param.page == 'airports' ? 'active' : ''}">
                <i class="bi bi-geo-alt me-2"></i>
                Airports
            </a>
        </li>

        <!-- User Management -->
        <li>
            <a href="admin?page=users" class="nav-link ${param.page == 'users' ? 'active' : ''}">
                <i class="bi bi-people me-2"></i>
                Users
            </a>
        </li>

        <!-- Aircraft Management -->
        <li>
            <a href="admin?page=aircrafts" class="nav-link ${param.page == 'aircrafts' ? 'active' : ''}">
                <i class="bi bi-jet me-2"></i>
                Aircrafts
            </a>
        </li>
    </ul>
    <hr>

    <!-- Secondary Menu -->
    <ul class="nav nav-pills flex-column">
        <!-- Reports -->
        <li>
            <a href="admin?page=reports" class="nav-link ${param.page == 'reports' ? 'active' : ''}">
                <i class="bi bi-graph-up me-2"></i>
                Reports
            </a>
        </li>

        <!-- Settings -->
        <li>
            <a href="admin?page=settings" class="nav-link ${param.page == 'settings' ? 'active' : ''}">
                <i class="bi bi-gear me-2"></i>
                Settings
            </a>
        </li>

        <!-- Support -->
        <li>
            <a href="admin?page=support" class="nav-link ${param.page == 'support' ? 'active' : ''}">
                <i class="bi bi-question-circle me-2"></i>
                Support
            </a>
        </li>
    </ul>

</div>

<style>
	.sidenav {
		background-color: #f1f1f1!important;
	}
    /* Active item styling */
    .sidenav .nav-item.active {
        background-color: #e3e3e3!important;
        border-left: 3px solid #0d6efd;
        font-weight: 700;
    }
    .sidenav .nav-link {
    	color: var(--bs-dark);
    	
    }
    
    .sidenav .nav-link.active {
        font-weight: 900;
    }
    
    /* Hover effects */
    .sidenav .nav-link:hover {
        background-color: rgba(255, 255, 255, 0.05);
    }
</style>
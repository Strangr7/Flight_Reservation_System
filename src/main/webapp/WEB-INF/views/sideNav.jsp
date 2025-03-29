<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- Detect current page --%>

<c:set var="currentPath" value="${fn:replace(pageContext.request.requestURI, pageContext.request.contextPath, '')}" />
<c:set var="basePath" value="${fn:split(currentPath, '/')[2]}" />
<c:set var="withoutExtension" value="${fn:split(basePath, '.')[0]}" />



<!-- DEBUG: 
  RequestURI = ${pageContext.request.requestURI}
  ContextPath = ${pageContext.request.contextPath}
  CurrentPath = ${currentPath}
  withoutExtension = ${withoutExtension}
-->
<div class="sidenav d-flex flex-column flex-shrink-0 p-3 text-white" style="width: 280px; height: 100vh;">

    <!-- Main Menu -->
    <ul class="nav nav-pills flex-column mb-auto">
        <!-- Dashboard -->
        <li class="nav-item  ${withoutExtension eq 'adminPanel' ? 'active' : ''}">
            <a href="<c:url value='/adminPanel'/>" class="nav-link">
                <i class="bi bi-speedometer2 me-2"></i>
                Dashboard
            </a>
        </li>

        <!-- Flight Management -->        
        <li class="nav-item ${withoutExtension eq 'flights' ? 'active' : ''}">
            <a href="<c:url value='/flights'/>" class="nav-link ">
                <i class="bi bi-airplane me-2"></i>
                Flights
            </a>
        </li>

        <!-- Booking Management -->
        <li>
            <a href="admin?page=bookings" class="nav-link ">
                <i class="bi bi-ticket-perforated me-2"></i>
                Bookings
            </a>
        </li>

        <!-- Airport Management -->
        <li>
            <a href="admin?page=airports" class="nav-link ">
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
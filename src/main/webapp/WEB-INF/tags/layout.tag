<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="styles" fragment="true" required="false" %>
<%@ attribute name="scripts" fragment="true" required="false" %>
<%@ attribute name="showSideNav" type="java.lang.Boolean" required="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>${title} | SkyReserve</title>
    <!-- Default CSS that all pages need -->
    <link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
	
	
	<!-- Page-specific CSS -->
    <jsp:invoke fragment="styles"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF//views/navbar.jsp"/>
    
    <div class="d-flex flex-grow-1">
        <c:if test="${showSideNav}">
            <jsp:include page="/WEB-INF//views/sideNav.jsp"/>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
		   <div class="alert alert-danger">${errorMessage}</div>
		</c:if>
        <main class="main-content container-fluid flex-grow-1 p-4" style="width: calc(100% - 280px);">
            <jsp:doBody/>
        </main>
    </div>

    <jsp:invoke fragment="scripts"/>
</body>
</html>
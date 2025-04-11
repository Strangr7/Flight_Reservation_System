<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Admin Panel</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
	<link href="<%=request.getContextPath()%>/style/base.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/form.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/buttons.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="navbar.jsp" />
	<div class = "container mt-4">
	<h1>Admin Panel</h1>
	<p>Welcome, Admin! Manage flights here.</p>
	
	</div>
	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Confirmation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h2 class="header-title mt-4">Booking Confirmed!</h2>
        <div class="alert alert-success">
            Your booking has been successfully confirmed. Your PNR is: <strong><%=request.getAttribute("pnr")%></strong>.
        </div>
        <p>Please save this PNR for future reference.</p>
        <a href="<%=request.getContextPath()%>/searchFlights" class="btn btn-primary">Book Another Flight</a>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
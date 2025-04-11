<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.ZoneId" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Flight Overview</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/base.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/navbar.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/footer.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/utilities.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/responsive.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #1661af;
            --secondary-color: #333;
            --danger-color: #d71921;
            --neutral-gray: #6c757d;
            --border-radius: 5px;
            --transition-ease: 0.25s ease-in-out;
            --spacing-unit: 1rem;
            --white: #fff;
            --light-blue: #f0f8ff;
            --highlight-blue: #4a90e2;
            --overlay-dark: rgba(0, 0, 0, 0.3);
            --shadow-color: rgba(0, 0, 0, 0.15);
            --glow-color: rgba(22, 97, 175, 0.3);
            --gradient-border: linear-gradient(45deg, var(--primary-color), var(--highlight-blue));
            --button-glow: rgba(74, 144, 226, 0.5);
        }

        body {
            font-family: 'Poppins', sans-serif;
            background: var(--white);
            color: var(--secondary-color);
            padding: var(--spacing-unit) 0;
            margin: 0;
            box-sizing: border-box;
            line-height: 1.6;
        }

        *, *:before, *:after {
            box-sizing: inherit;
        }

        html, body {
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }

        /* Flight Overview Container */
        .flight-overview {
            padding: calc(var(--spacing-unit) * 2) 0;
            max-width: 900px;
            margin: 0 auto;
        }

        .header-title {
            font-size: 2rem;
            font-weight: 600;
            color: var(--primary-color);
            margin-bottom: calc(var(--spacing-unit) * 1.5);
            text-align: center;
        }

        /* Progress Bar */
        .progress-container {
            position: relative;
            margin-bottom: calc(var(--spacing-unit) * 2);
        }

        .progress-steps {
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: relative;
            z-index: 1;
        }

        .progress-step {
            width: 32px;
            height: 32px;
            background-color: var(--neutral-gray);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            color: var(--white);
            transition: background-color var(--transition-ease), transform var(--transition-ease);
            position: relative;
        }

        .progress-step.active {
            background-color: var(--primary-color);
            box-shadow: 0 0 8px var(--glow-color);
            transform: scale(1.05);
        }

        .progress-step.completed {
            background-color: var(--highlight-blue);
        }

        .progress-bar-bg {
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 4px;
            background-color: var(--light-blue);
            z-index: 0;
            transform: translateY(-50%);
        }

        .progress-bar-fill {
            position: absolute;
            top: 50%;
            left: 0;
            height: 4px;
            background: var(--gradient-border);
            z-index: 0;
            transform: translateY(-50%);
            transition: width var(--transition-ease);
        }

        .progress-label {
            font-size: 0.9rem;
            color: var(--neutral-gray);
            text-align: center;
            margin-top: calc(var(--spacing-unit) / 2);
        }

        /* Card Styling */
        .card {
            border: none;
            box-shadow: 0 4px 12px var(--shadow-color);
            border-radius: var(--border-radius);
            background: var(--white);
            overflow: hidden;
        }

        .card-body {
            padding: calc(var(--spacing-unit) * 2);
        }

        .card-body p {
            margin: calc(var(--spacing-unit) * 0.75) 0;
            font-size: 1.1rem;
            color: var(--secondary-color);
        }

        .card-body strong {
            color: var(--primary-color);
            font-weight: 600;
        }

        /* Button Styles */
        .btn-primary {
            background: var(--primary-color);
            color: var(--white);
            font-size: 0.95rem;
            padding: 0.625rem 1.25rem;
            border: none;
            border-radius: var(--border-radius);
            font-weight: 600;
            cursor: pointer;
            transition: background var(--transition-ease), transform var(--transition-ease), box-shadow var(--transition-ease);
            text-transform: uppercase;
            letter-spacing: 0.7px;
            height: 40px;
        }

        .btn-primary:hover {
            background: #004073;
            transform: translateY(-3px);
            box-shadow: 0 4px 10px var(--button-glow);
        }

        .btn-primary:active {
            background: #dd9d3b;
            transform: translateY(0);
            box-shadow: none;
        }

        .btn-primary:focus {
            outline: 2px solid var(--primary-color);
            outline-offset: 2px;
        }

        .btn-secondary {
            background-color: var(--white);
            color: var(--neutral-gray);
            border: 2px solid var(--neutral-gray);
            padding: 0.4375rem 1rem;
            border-radius: var(--border-radius);
            font-weight: 600;
            transition: all var(--transition-ease);
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-size: 0.85rem;
        }

        .btn-secondary:hover {
            background-color: var(--neutral-gray);
            color: var(--white);
            transform: translateY(-2px);
        }

        .btn-secondary:active {
            transform: translateY(0);
        }

        .btn-secondary:focus {
            outline: 2px solid var(--neutral-gray);
            outline-offset: 2px;
        }

        /* Alert */
        .alert-danger {
            border-radius: var темеvar(--border-radius);
            padding: var(--spacing-unit);
            font-size: 1rem;
            background-color: var(--danger-color);
            color: var(--white);
            text-align: center;
        }
    </style>
</head>
<body>
    <jsp:include page="navbar.jsp" />
    <div class="container flight-overview">
        <!-- Enhanced Progress Bar -->
        <div class="progress-container">
            <div class="progress-bar-bg"></div>
            <div class="progress-bar-fill" style="width: 33%;"></div>
            <div class="progress-steps">
                <div class="progress-step active">1<div class="progress-label">Flight Details</div></div>
                <div class="progress-step">2<div class="progress-label">Passenger Details</div></div>
                <div class="progress-step">3<div class="progress-label">Confirmation</div></div>
            </div>
        </div>

        <h2 class="header-title">Flight Overview</h2>
        <%
            // Reuse SimpleDateFormat instance for performance
            SimpleDateFormat timeFormat24 = (SimpleDateFormat) application.getAttribute("timeFormat24");
            if (timeFormat24 == null) {
                timeFormat24 = new SimpleDateFormat("HH:mm");
                application.setAttribute("timeFormat24", timeFormat24); // Store in application scope
            }

            FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
            String className = (String) session.getAttribute("selectedClass");

            if (flight == null || className == null) {
        %>
            <div class="alert alert-danger">No flight selected. Please try again.</div>
            <a href="<%=request.getContextPath()%>/SearchFlights" class="btn btn-secondary">Back to Search</a>
        <%
            } else {
                FlightResultOneWay.PriceClass selectedClass = flight.getPricesAndClasses().stream()
                        .filter(pc -> pc.getClassName().toString().equals(className))
                        .findFirst().orElse(null);
                
                // Pre-compute formatted times once
                String departureTime = timeFormat24.format(
                    java.util.Date.from(flight.getFlight().getDepartureTime().atZone(ZoneId.systemDefault()).toInstant())
                );
                String arrivalTime = timeFormat24.format(
                    java.util.Date.from(flight.getFlight().getArrivalTime().atZone(ZoneId.systemDefault()).toInstant())
                );
        %>
            <div class="card">
                <div class="card-body">
                    <h5>Flight ID: <%=flight.getFlight().getFlightId()%></h5>
                    <p><strong>Airline:</strong> <%=flight.getFlight().getAirline().getAirlineName()%></p>
                    <p><strong>Flight Number:</strong> <%=flight.getFlight().getFlightNumber()%></p>
                    <p><strong>Route:</strong> 
                        <%=flight.getFlight().getDepartureAirport().getAirportCode()%> ➝ 
                        <%=flight.getFlight().getDestinationAirport().getAirportCode()%>
                    </p>
                    <p><strong>Departure:</strong> <%=departureTime%></p>
                    <p><strong>Arrival:</strong> <%=arrivalTime%></p>
                    <p><strong>Trip Duration:</strong> <%=flight.getFlight().getTripDuration()%> minutes</p>
                    <p><strong>Stops:</strong> 
                        <%=flight.getStopCount() == 0 ? "Non-stop" : flight.getStopCount() + " stop(s)"%>
                    </p>
                    <p><strong>Class:</strong> <%=className%></p>
                    <p><strong>Price:</strong> CAD 
                        <%=selectedClass != null ? String.format("%.2f", selectedClass.getDynamicPrice()) : "N/A"%>
                    </p>
                    <%
                        if (selectedClass != null && selectedClass.getBaggageRules() != null) {
                    %>
                        <p><strong>Baggage:</strong> 
                            <%=selectedClass.getBaggageRules().getAllowedCheckedBags()%> checked bags, 
                            <%=selectedClass.getBaggageRules().getCarryOnWeightLimit()%> kg carry-on
                        </p>
                    <%
                        }
                    %>
                    <form action="<%=request.getContextPath()%>/PassengerDetails" method="post">
                        <input type="hidden" name="token" value="<%=session.getAttribute("bookingToken")%>">
                        <button type="submit" class="btn btn-primary mt-3">Continue to Passenger Details</button>
                    </form>
                </div>
            </div>
            <a href="<%=request.getContextPath()%>/view?page=searchFlights" class="btn btn-secondary mt-3">Back to Search</a>
        <%
            }
        %>
    </div>
    <jsp:include page="footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
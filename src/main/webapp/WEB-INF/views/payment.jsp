<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay" %>
<%@ page import="com.flightreservation.model.Seats" %>
<%@ page import="com.flightreservation.model.Meals" %>
<%@ page import="com.flightreservation.dao.SeatDAO" %>
<%@ page import="com.flightreservation.dao.MealDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment</title>
   <link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/base.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/form.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/buttons.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/navbar.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/components/footer.css"
	rel="stylesheet">

<link href="<%=request.getContextPath()%>/style/utilities.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/style/responsive.css"
	rel="stylesheet">
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

        .container {
            padding: calc(var(--spacing-unit) * 3) 0;
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

        /* Passenger Form */
        .passenger-form {
            border: 1px solid var(--light-blue);
            padding: calc(var(--spacing-unit) * 1.5);
            border-radius: var(--border-radius);
            background: var(--white);
            box-shadow: 0 2px 8px var(--shadow-color);
            margin-bottom: calc(var(--spacing-unit) * 1.5);
        }

        /* Payment Form */
        .payment-form {
            background: var(--white);
            padding: calc(var(--spacing-unit) * 2);
            border-radius: var(--border-radius);
            box-shadow: 0 4px 12px var(--shadow-color);
        }

        .form-label {
            color: var(--primary-color);
            font-weight: 500;
            margin-bottom: calc(var(--spacing-unit) * 0.5);
        }

        .form-control {
            border-radius: var(--border-radius);
            border: 1px solid var(--neutral-gray);
            padding: calc(var(--spacing-unit) * 0.75);
            font-size: 1rem;
            transition: border-color var(--transition-ease), box-shadow var(--transition-ease);
        }

        .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 5px var(--glow-color);
            outline: none;
        }

        .form-control.is-invalid {
            border-color: var(--danger-color);
        }

        .invalid-feedback {
            color: var(--danger-color);
            font-size: 0.85rem;
            margin-top: 0.25rem;
        }

        /* Buttons */
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
            width: 100%;
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

        .btn-primary:disabled {
            opacity: 0.65;
            cursor: not-allowed;
            background: #b0b0b0;
            transform: none;
            box-shadow: none;
        }

        /* Alert */
        .alert-danger, .alert-warning {
            border-radius: var(--border-radius);
            padding: var(--spacing-unit);
            font-size: 1rem;
            text-align: center;
            margin-bottom: calc(var(--spacing-unit) * 1.5);
        }

        .alert-danger {
            background-color: var(--danger-color);
            color: var(--white);
        }

        .alert-warning {
            background-color: #ffc107;
            color: var(--secondary-color);
        }
    </style>
</head>
<body>
    <jsp:include page="navbar.jsp" />
    <div class="container">
        <!-- Enhanced Progress Bar -->
        <div class="progress-container">
            <div class="progress-bar-bg"></div>
            <div class="progress-bar-fill" style="width: 100%;"></div>
            <div class="progress-steps">
                <div class="progress-step completed">1<div class="progress-label">Flight Details</div></div>
                <div class="progress-step completed">2<div class="progress-label">Passenger Details</div></div>
                <div class="progress-step active">3<div class="progress-label">Payment</div></div>
            </div>
        </div>

        <h2 class="header-title">Payment Details</h2>
        <% 
            FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
            String className = (String) session.getAttribute("selectedClass");
            if (flight == null || className == null) {
        %>
            <div class="alert alert-danger">No flight selected. Please start over.</div>
            <a href="<%=request.getContextPath()%>/SearchFlights" class="btn btn-primary">Back to Search</a>
        <% 
            } else {
                String[] passengerNames = (String[]) session.getAttribute("passengerNames");
                int[] passengerAges = (int[]) session.getAttribute("passengerAges");
                String[] passengerPassports = (String[]) session.getAttribute("passengerPassports");
                int[] seatIds = (int[]) session.getAttribute("seatIds");
                Integer[] mealIds = (Integer[]) session.getAttribute("mealIds");
                String paymentAmount = (String) session.getAttribute("paymentAmount");

                SeatDAO seatDAO = new SeatDAO();
                MealDAO mealDAO = new MealDAO();

                if (request.getParameter("error") != null) { 
        %>
                    <div class="alert alert-danger"><%=request.getParameter("error")%></div>
        <% } %>
                <p><strong>Flight:</strong> <%=flight.getFlight().getFlightNumber()%></p>
                <p><strong>Class:</strong> <%=className%></p>

                <% 
                    if (passengerNames != null && passengerNames.length > 0) {
                        for (int i = 0; i < passengerNames.length; i++) {
                            Seats seat = seatDAO.getSeatById(seatIds[i]);
                            String seatNumber = seat != null ? seat.getSeatNumber() : "Unknown";
                            String mealName = "None";
                            if (mealIds[i] != null) {
                                Meals meal = mealDAO.getMealById(mealIds[i]);
                                mealName = meal != null ? meal.getMealName() : "Unknown";
                            }
                %>
                            <div class="passenger-form">
                                <h5>Passenger <%= (i + 1) %></h5>
                                <p><strong>Name:</strong> <%=passengerNames[i]%></p>
                                <p><strong>Age:</strong> <%=passengerAges[i]%></p>
                                <p><strong>Passport:</strong> <%=passengerPassports[i]%></p>
                                <p><strong>Seat:</strong> <%=seatNumber%></p>
                                <p><strong>Meal:</strong> <%=mealName%></p>
                            </div>
                <% 
                        }
                    } else { 
                %>
                    <div class="alert alert-warning">No passenger details available.</div>
                <% } %>

                <p><strong>Amount to Pay:</strong> $<%=paymentAmount != null ? paymentAmount : "0.00"%> (for <%=passengerNames != null ? passengerNames.length : 0%> passengers)</p>

                <div class="payment-form">
                    <form action="<%=request.getContextPath()%>/processPayment" method="post" id="paymentForm">
                        <input type="hidden" name="token" value="<%=request.getParameter("token")%>">
                        <div class="mb-3">
                            <label for="cardNumber" class="form-label">Card Number</label>
                            <input type="text" class="form-control" id="cardNumber" name="cardNumber" required>
                            <div class="invalid-feedback">Card number must be 16 digits.</div>
                        </div>
                        <div class="mb-3">
                            <label for="expiryDate" class="form-label">Expiry Date (MM/YY)</label>
                            <input type="text" class="form-control" id="expiryDate" name="expiryDate" required>
                            <div class="invalid-feedback">Expiry date must be in MM/YY format and not expired.</div>
                        </div>
                        <div class="mb-3">
                            <label for="cvv" class="form-label">CVV</label>
                            <input type="text" class="form-control" id="cvv" name="cvv" required>
                            <div class="invalid-feedback">CVV must be 3-4 digits.</div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="submitBtn">Pay Now</button>
                    </form>
                </div>
        <% } %>
    </div>
    <jsp:include page="footer.jsp" />
    <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validation functions
        function validateCardNumber(cardNumber) {
            return /^\d{16}$/.test(cardNumber);
        }

        function validateExpiryDate(expiryDate) {
            if (!/^\d{2}\/\d{2}$/.test(expiryDate)) return false;
            const [month, year] = expiryDate.split('/').map(Number);
            if (month < 1 || month > 12) return false;
            const currentDate = new Date();
            const currentYear = currentDate.getFullYear() % 100; // Last 2 digits
            const currentMonth = currentDate.getMonth() + 1; // 1-12
            const expiryYear = parseInt(year, 10);
            return expiryYear > currentYear || (expiryYear === currentYear && month >= currentMonth);
        }

        function validateCVV(cvv) {
            return /^\d{3,4}$/.test(cvv);
        }

        function validateForm() {
            const cardNumberInput = document.getElementById('cardNumber');
            const expiryDateInput = document.getElementById('expiryDate');
            const cvvInput = document.getElementById('cvv');
            const submitBtn = document.getElementById('submitBtn');
            let isValid = true;

            if (!validateCardNumber(cardNumberInput.value)) {
                cardNumberInput.classList.add('is-invalid');
                isValid = false;
            } else {
                cardNumberInput.classList.remove('is-invalid');
            }

            if (!validateExpiryDate(expiryDateInput.value)) {
                expiryDateInput.classList.add('is-invalid');
                isValid = false;
            } else {
                expiryDateInput.classList.remove('is-invalid');
            }

            if (!validateCVV(cvvInput.value)) {
                cvvInput.classList.add('is-invalid');
                isValid = false;
            } else {
                cvvInput.classList.remove('is-invalid');
            }

            submitBtn.disabled = !isValid;
            return isValid;
        }

        // Event listeners
        document.querySelectorAll('input').forEach(input => {
            input.addEventListener('input', validateForm);
        });

        document.getElementById('paymentForm').addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                alert('Please correct the errors in the form before submitting.');
            }
        });

        // Initial validation
        validateForm();
    </script>
</body>
</html>
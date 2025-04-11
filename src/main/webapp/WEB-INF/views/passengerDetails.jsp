<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.flightreservation.DTO.FlightResultOneWay" %>
<%@ page import="com.flightreservation.model.Seats" %>
<%@ page import="com.flightreservation.model.Meals" %>
<%@ page import="java.util.List" %>
<%@ page import="com.flightreservation.dao.SeatDAO" %>
<%@ page import="com.flightreservation.dao.MealDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Passenger Details</title>
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
            margin-bottom: calc(var(--spacing-unit) * 1.5);
        }

        .card-body {
            padding: calc(var(--spacing-unit) * 1.5);
        }

        .card-body p {
            margin: calc(var(--spacing-unit) * 0.5) 0;
            font-size: 1rem;
            color: var(--secondary-color);
        }

        .card-body strong {
            color: var(--primary-color);
            font-weight: 600;
        }

        /* Passenger Form */
        .passenger-form {
            border: 1px solid var(--light-blue);
            padding: calc(var(--spacing-unit) * 1.5);
            border-radius: var(--border-radius);
            background: var(--white);
            box-shadow: 0 2px 8px var(--shadow-color);
            margin-bottom: calc(var(--spacing-unit) * 1.5);
            position: relative;
        }

        .form-label {
            color: var(--primary-color);
            font-weight: 500;
        }

        .form-control, .form-select {
            border-radius: var(--border-radius);
            border: 1px solid var(--neutral-gray);
            padding: calc(var(--spacing-unit) * 0.75);
            font-size: 1rem;
            transition: border-color var(--transition-ease);
        }

        .form-control:focus, .form-select:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 5px var(--glow-color);
            outline: none;
        }

        .is-invalid {
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

        .btn-secondary:disabled {
            opacity: 0.65;
            cursor: not-allowed;
            border-color: #b0b0b0;
            color: #666;
            transform: none;
        }

        .btn-danger {
            background-color: var(--danger-color);
            border-color: var(--danger-color);
            color: var(--white);
            padding: 0.5rem 1.125rem;
            border-radius: var(--border-radius);
            font-weight: 600;
            transition: all var(--transition-ease);
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-size: 0.85rem;
        }

        .btn-danger:hover {
            filter: brightness(90%);
            transform: translateY(-2px);
        }

        .btn-danger:active {
            transform: translateY(0);
        }

        /* Alert */
        .alert-danger {
            border-radius: var(--border-radius);
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
    <div class="container">
        <!-- Enhanced Progress Bar -->
        <div class="progress-container">
            <div class="progress-bar-bg"></div>
            <div class="progress-bar-fill" style="width: 66%;"></div>
            <div class="progress-steps">
                <div class="progress-step completed">1<div class="progress-label">Flight Details</div></div>
                <div class="progress-step active">2<div class="progress-label">Passenger Details</div></div>
                <div class="progress-step">3<div class="progress-label">Payment</div></div>
            </div>
        </div>

        <h2 class="header-title">Passenger Details</h2>
        <%
            FlightResultOneWay flight = (FlightResultOneWay) session.getAttribute("selectedFlight");
            String className = (String) session.getAttribute("selectedClass");
            if (flight == null || className == null) {
        %>
            <div class="alert alert-danger">No flight selected. Please try again.</div>
            <a href="<%=request.getContextPath()%>/SearchFlights" class="btn btn-secondary">Back to Search</a>
        <%
            } else {
                SeatDAO seatDAO = new SeatDAO();
                MealDAO mealDAO = new MealDAO();
                List<Seats> availableSeats = seatDAO.getAvailableSeats(flight.getFlight().getFlightId(), className);
                List<Meals> mealOptions = mealDAO.getAllMeals();

                // Business Logic: Check if seats are available
                if (availableSeats == null || availableSeats.isEmpty()) {
        %>
                    <div class="alert alert-danger">No seats available for this flight and class. Please try another flight.</div>
                    <a href="<%=request.getContextPath()%>/SearchFlights" class="btn btn-secondary">Back to Search</a>
        <%
                } else {
                    // Store available seats count for client-side limit
                    int maxPassengers = availableSeats.size();
                    session.setAttribute("maxPassengers", maxPassengers);
        %>
                    <div class="card">
                        <div class="card-body">
                            <h5>Flight Summary</h5>
                            <p><strong>Flight ID:</strong> <%=flight.getFlight().getFlightId()%></p>
                            <p><strong>Route:</strong> 
                                <%=flight.getFlight().getDepartureAirport().getAirportCode()%> ➝ 
                                <%=flight.getFlight().getDestinationAirport().getAirportCode()%>
                            </p>
                            <p><strong>Class:</strong> <%=className%></p>
                            <p><strong>Available Seats:</strong> <%=maxPassengers%></p>
                        </div>
                    </div>

                    <form action="<%=request.getContextPath()%>/savePassengerDetails" method="post" id="passengerForm">
                        <input type="hidden" name="token" value="<%=session.getAttribute("bookingToken")%>">
                        <div id="passengerContainer">
                            <div class="passenger-form" id="passenger1">
                                <h5>Passenger 1</h5>
                                <div class="mb-3">
                                    <label for="passengerName1" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="passengerName1" name="passengerName[]" required>
                                    <div class="invalid-feedback">Name must be 2+ characters, letters and spaces only.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="passengerAge1" class="form-label">Age</label>
                                    <input type="number" class="form-control" id="passengerAge1" name="passengerAge[]" required>
                                    <div class="invalid-feedback">Age must be between 1 and 120.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="passengerPassport1" class="form-label">Passport Number</label>
                                    <input type="text" class="form-control" id="passengerPassport1" name="passengerPassport[]" required>
                                    <div class="invalid-feedback">Passport must be 6-12 alphanumeric characters.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="seatId1" class="form-label">Select Seat</label>
                                    <select class="form-select" id="seatId1" name="seatId[]" required>
                                        <option value="">Choose a seat</option>
                                        <% for (Seats seat : availableSeats) { %>
                                            <option value="<%=seat.getSeatId()%>">
                                                <%=seat.getSeatNumber()%> (Class: <%=seat.getSeatClass().getClassName()%>)
                                            </option>
                                        <% } %>
                                    </select>
                                    <div class="invalid-feedback">Please select a seat.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="mealId1" class="form-label">Select Meal (Optional)</label>
                                    <select class="form-select" id="mealId1" name="mealId[]">
                                        <option value="">No meal</option>
                                        <% for (Meals meal : mealOptions) { %>
                                            <option value="<%=meal.getMealsId()%>">
                                                <%=meal.getMealName()%> - <%=meal.getDescription()%>
                                            </option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <button type="button" class="btn btn-secondary mb-3" id="addPassenger">Add Another Passenger</button>
                        <button type="submit" class="btn btn-primary" id="submitBtn">Next: Payment</button>
                    </form>
                    <a href="<%=request.getContextPath()%>/flightDetails?token=<%=session.getAttribute("bookingToken")%>" class="btn btn-secondary mt-3">Back to Flight Details</a>
        <%
                }
            }
        %>
    </div>
    <jsp:include page="footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const maxPassengers = <%=session.getAttribute("maxPassengers") != null ? session.getAttribute("maxPassengers") : 0%>;
        let passengerCount = 1;

        // Validation functions
        function validateName(name) {
            return /^[A-Za-z\s]{2,}$/.test(name);
        }

        function validateAge(age) {
            const num = parseInt(age, 10);
            return !isNaN(num) && num >= 1 && num <= 120;
        }

        function validatePassport(passport) {
            return /^[A-Za-z0-9]{6,12}$/.test(passport);
        }

        function validateForm() {
            let isValid = true;
            document.querySelectorAll('.passenger-form').forEach(form => {
                const nameInput = form.querySelector('input[name="passengerName[]"]');
                const ageInput = form.querySelector('input[name="passengerAge[]"]');
                const passportInput = form.querySelector('input[name="passengerPassport[]"]');
                const seatSelect = form.querySelector('select[name="seatId[]"]');

                if (!validateName(nameInput.value)) {
                    nameInput.classList.add('is-invalid');
                    isValid = false;
                } else {
                    nameInput.classList.remove('is-invalid');
                }

                if (!validateAge(ageInput.value)) {
                    ageInput.classList.add('is-invalid');
                    isValid = false;
                } else {
                    ageInput.classList.remove('is-invalid');
                }

                if (!validatePassport(passportInput.value)) {
                    passportInput.classList.add('is-invalid');
                    isValid = false;
                } else {
                    passportInput.classList.remove('is-invalid');
                }

                if (!seatSelect.value) {
                    seatSelect.classList.add('is-invalid');
                    isValid = false;
                } else {
                    seatSelect.classList.remove('is-invalid');
                }
            });
            document.getElementById('submitBtn').disabled = !isValid;
            return isValid;
        }

        // Update seat availability
        function updateSeatOptions() {
            const selectedSeats = Array.from(document.querySelectorAll('select[name="seatId[]"]'))
                .map(select => select.value)
                .filter(value => value !== "");
            
            document.querySelectorAll('select[name="seatId[]"]').forEach(select => {
                Array.from(select.options).forEach(option => {
                    if (option.value && selectedSeats.includes(option.value) && option.value !== select.value) {
                        option.disabled = true;
                    } else {
                        option.disabled = false;
                    }
                });
            });
        }

        // Add passenger
        document.getElementById('addPassenger').addEventListener('click', function() {
            if (passengerCount >= maxPassengers) {
                alert('Maximum passenger limit reached (' + maxPassengers + ').');
                this.disabled = true;
                return;
            }

            passengerCount++;
            const container = document.getElementById('passengerContainer');
            const newForm = document.createElement('div');
            newForm.className = 'passenger-form';
            newForm.id = 'passenger' + passengerCount;

            newForm.innerHTML = `
                <h5>Passenger ${passengerCount}</h5>
                <div class="mb-3">
                    <label for="passengerName${passengerCount}" class="form-label">Full Name</label>
                    <input type="text" class="form-control" id="passengerName${passengerCount}" name="passengerName[]" required>
                    <div class="invalid-feedback">Name must be 2+ characters, letters and spaces only.</div>
                </div>
                <div class="mb-3">
                    <label for="passengerAge${passengerCount}" class="form-label">Age</label>
                    <input type="number" class="form-control" id="passengerAge${passengerCount}" name="passengerAge[]" required>
                    <div class="invalid-feedback">Age must be between 1 and 120.</div>
                </div>
                <div class="mb-3">
                    <label for="passengerPassport${passengerCount}" class="form-label">Passport Number</label>
                    <input type="text" class="form-control" id="passengerPassport${passengerCount}" name="passengerPassport[]" required>
                    <div class="invalid-feedback">Passport must be 6-12 alphanumeric characters.</div>
                </div>
                <div class="mb-3">
                    <label for="seatId${passengerCount}" class="form-label">Select Seat</label>
                    <select class="form-select" id="seatId${passengerCount}" name="seatId[]" required>
                        <option value="">Choose a seat</option>
                    </select>
                    <div class="invalid-feedback">Please select a seat.</div>
                </div>
                <div class="mb-3">
                    <label for="mealId${passengerCount}" class="form-label">Select Meal (Optional)</label>
                    <select class="form-select" id="mealId${passengerCount}" name="mealId[]">
                        <option value="">No meal</option>
                    </select>
                </div>
                <button type="button" class="btn btn-danger removePassenger">Remove</button>
            `;

            container.appendChild(newForm);

            // Clone seat options
            const originalSeatSelect = document.getElementById('seatId1');
            const newSeatSelect = newForm.querySelector(`#seatId${passengerCount}`);
            Array.from(originalSeatSelect.options).forEach(option => {
                newSeatSelect.add(new Option(option.text, option.value));
            });

            // Clone meal options
            const originalMealSelect = document.getElementById('mealId1');
            const newMealSelect = newForm.querySelector(`#mealId${passengerCount}`);
            Array.from(originalMealSelect.options).forEach(option => {
                newMealSelect.add(new Option(option.text, option.value));
            });

            // Add event listeners for validation and seat updates
            newForm.querySelectorAll('input, select').forEach(input => {
                input.addEventListener('input', () => {
                    validateForm();
                    if (input.name === 'seatId[]') updateSeatOptions();
                });
            });

            // Remove functionality
            newForm.querySelector('.removePassenger').addEventListener('click', function() {
                container.removeChild(newForm);
                passengerCount--;
                document.getElementById('addPassenger').disabled = passengerCount >= maxPassengers;
                validateForm();
                updateSeatOptions();
            });

            updateSeatOptions();
            validateForm();
        });

        // Form submission
        document.getElementById('passengerForm').addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                alert('Please correct the errors in the form before submitting.');
            }
        });

        // Initial validation and seat update
        document.querySelectorAll('input, select').forEach(input => {
            input.addEventListener('input', () => {
                validateForm();
                if (input.name === 'seatId[]') updateSeatOptions();
            });
        });
        validateForm();
        updateSeatOptions();
    </script>
</body>
</html>
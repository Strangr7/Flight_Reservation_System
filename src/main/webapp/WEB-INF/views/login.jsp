<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/base.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/navbar.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/footer.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/utilities.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/responsive.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/form.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/components/buttons.css" rel="stylesheet">
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
            max-width: 500px;
            margin: 0 auto;
        }

        .header-title {
            font-size: 2rem;
            font-weight: 600;
            color: var(--primary-color);
            margin-bottom: calc(var(--spacing-unit) * 1.5);
            text-align: center;
        }

        /* Form Styling */
        .login-form {
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
        .alert-danger {
            border-radius: var(--border-radius);
            padding: var(--spacing-unit);
            font-size: 1rem;
            background-color: var(--danger-color);
            color: var(--white);
            text-align: center;
            margin-bottom: calc(var(--spacing-unit) * 1.5);
        }

        /* Register Link */
        .register-link {
            text-align: center;
            margin-top: calc(var(--spacing-unit) * 1.5);
            font-size: 0.95rem;
        }

        .register-link a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 600;
            transition: color var(--transition-ease);
        }

        .register-link a:hover {
            color: var(--highlight-blue);
        }
    </style>
</head>
<body>
    
    <div class="container">
        <h2 class="header-title">Login</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%=request.getAttribute("error")%></div>
        <% } %>
        <div class="login-form">
            <form action="<%=request.getContextPath()%>/login" method="post" id="loginForm">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                    <div class="invalid-feedback">Please enter a valid email address.</div>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                    <div class="invalid-feedback">Password must be at least 6 characters.</div>
                </div>
                <button type="submit" class="btn btn-primary" id="submitBtn">Login</button>
            </form>
            <p class="register-link">
                Don’t have an account? <a href="<%=request.getContextPath()%>/view?page=register">Register</a>
            </p>
        </div>
    </div>
    <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validation functions
        function validateEmail(email) {
            return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
        }

        function validatePassword(password) {
            return password.length >= 6;
        }

        function validateForm() {
            const emailInput = document.getElementById('email');
            const passwordInput = document.getElementById('password');
            const submitBtn = document.getElementById('submitBtn');
            let isValid = true;

            if (!validateEmail(emailInput.value)) {
                emailInput.classList.add('is-invalid');
                isValid = false;
            } else {
                emailInput.classList.remove('is-invalid');
            }

            if (!validatePassword(passwordInput.value)) {
                passwordInput.classList.add('is-invalid');
                isValid = false;
            } else {
                passwordInput.classList.remove('is-invalid');
            }

            submitBtn.disabled = !isValid;
            return isValid;
        }

        // Event listeners
        document.querySelectorAll('input').forEach(input => {
            input.addEventListener('input', validateForm);
        });

        document.getElementById('loginForm').addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                alert('Please correct the errors in the form before submitting.');
            }
        });

        // Initial validation
        validateForm();
    </script>
    <jsp:include page="footer.jsp" />
</body>
</html>
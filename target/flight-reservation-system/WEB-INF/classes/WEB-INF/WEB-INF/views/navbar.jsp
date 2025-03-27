<nav class="navbar navbar-expand-lg bg-white shadow-sm py-2 sticky-top">
    <div class="container-fluid px-4">
        <!-- Brand Logo -->
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/view?page=searchFlights">
            <i class="bi bi-airplane-engines-fill text-primary fs-3 me-2"></i>
            <span class="fw-bold fs-4 text-dark">SkyReserve</span>
        </a>

        <!-- Mobile Toggle Button -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <i class="bi bi-list fs-2"></i>
        </button>

        <!-- Navigation Content -->
        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                <!-- Help Link -->
                <li class="nav-item mx-2">
                    <a href="${pageContext.request.contextPath}/view?page=help" class="nav-link d-flex align-items-center">
                        <i class="bi bi-question-circle-fill text-muted me-1"></i>
                        <span class="d-none d-lg-inline">Help</span>
                    </a>
                </li>

                <!-- Language Selector -->
                <li class="nav-item mx-2 dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="bi bi-globe text-muted me-1"></i>
                        <span class="d-none d-lg-inline">EN</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="#">English</a></li>
                        <li><a class="dropdown-item" href="#">Español</a></li>
                        <li><a class="dropdown-item" href="#">Français</a></li>
                    </ul>
                </li>

                <% 
                String user = (String) session.getAttribute("user");
                String role = (String) session.getAttribute("role");
                
                if (user == null) { 
                %>
                <!-- Authentication Buttons -->
                <li class="nav-item ms-2">
                    <div class="d-flex">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary me-2">
                            <i class="bi bi-box-arrow-in-right me-1"></i>
                            <span class="d-none d-lg-inline">Log In</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">
                            <i class="bi bi-person-plus me-1"></i>
                            <span class="d-none d-lg-inline">Sign Up</span>
                        </a>
                    </div>
                </li>
                <% 
                } else { 
                    if ("ADMIN".equals(role)) { 
                %>
                <!-- Admin Panel Link -->
                <li class="nav-item mx-2">
                    <a href="${pageContext.request.contextPath}/adminPanel" class="nav-link d-flex align-items-center">
                        <i class="bi bi-shield-check text-danger me-1"></i>
                        <span class="d-none d-lg-inline">Admin</span>
                    </a>
                </li>
                <% 
                    } 
                %>
                <!-- User Dropdown -->
                <li class="nav-item mx-2 dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="bi bi-person-circle text-primary me-1"></i>
                        <span class="d-none d-lg-inline"><%= user %></span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="#"><i class="bi bi-person me-2"></i>Profile</a></li>
                        <li><a class="dropdown-item" href="#"><i class="bi bi-ticket-perforated me-2"></i>My Bookings</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                <i class="bi bi-box-arrow-right me-2"></i>Sign Out
                            </a>
                        </li>
                    </ul>
                </li>
                <% 
                } 
                %>
            </ul>
        </div>
    </div>
</nav>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
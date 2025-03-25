
document.addEventListener('DOMContentLoaded', () => {
    const navItems = document.querySelectorAll('.search-nav-item');
    const tabContents = document.querySelectorAll('.tab-content');

    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();

            const tabId = item.getAttribute('data-tab');

            // Redirect to servlet for Manage Bookings
            if (tabId === 'manage-bookings') {
                window.location.href = `${window.contextPath}/manageBooking`;
                return; // Exit the function to prevent further processing
            }

            // Remove active class from all nav items
            navItems.forEach(nav => nav.classList.remove('active'));
            // Add active class to clicked item
            item.classList.add('active');

            // Hide all tab contents
            tabContents.forEach(content => content.classList.remove('active'));
            // Show the selected tab content
            document.getElementById(tabId).classList.add('active');
        });
    });
});
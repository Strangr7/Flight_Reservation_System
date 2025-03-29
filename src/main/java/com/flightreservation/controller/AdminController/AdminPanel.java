package com.flightreservation.controller.AdminController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import com.flightreservation.service.DashboardService;

/**
 * Servlet implementation class AdminPanel
 */
@WebServlet("/adminPanel")
public class AdminPanel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final DashboardService dashboardService = new DashboardService();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminPanel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String role = (String) request.getSession().getAttribute("role");
		if (!"ADMIN".equals(role)) {
			response.sendRedirect(request.getContextPath() + "/view?page=searchFlights");
			return;

		}
//		Get dashboard metrics
        Map<String, Object> metrics = dashboardService.getDashboardMetrics();
        request.setAttribute("metrics", metrics);

		request.getRequestDispatcher("/WEB-INF/views/adminPanel.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

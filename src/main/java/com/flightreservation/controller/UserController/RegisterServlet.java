package com.flightreservation.controller.UserController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flightreservation.model.Users;
import com.flightreservation.model.enums.UserRoles;
import com.flightreservation.util.HibernateUtil;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class); // Fixed Logger to logger

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// Check if user already exists
			Users existingUser = session.createQuery("FROM Users WHERE email = :email", Users.class)
					.setParameter("email", email).uniqueResult();

			if (existingUser != null) {
				logger.warn("Registration failed: Email {} already exists", email);
				request.setAttribute("error", "Email already registered."); // Fixed to lowercase "error"
				request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response); // Updated to
																										// register.jsp
				return;
			}

			// Password hashing
			String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

			// Insert user via native SQL
			String sql = "INSERT INTO users (name, email, phone, password_hash, role) " // Fixed passwordHash to
																						// password_hash
					+ "VALUES (:name, :email, :phone, :passwordHash, :role)";

			session.beginTransaction();
			session.createNativeQuery(sql, Void.class).setParameter("name", name).setParameter("email", email)
					.setParameter("phone", phone).setParameter("passwordHash", passwordHash)
					.setParameter("role", UserRoles.TRAVELER.name()).executeUpdate();
			session.getTransaction().commit();

			logger.info("User registered successfully: {}", email);
			response.sendRedirect(request.getContextPath() + "/view?page=login"); // Redirect to login page
		} catch (Exception e) {
			logger.error("Error registering the user", e);
			request.setAttribute("error", "Registration failed. Please try again.");
			request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/view?page=register");
	}
}
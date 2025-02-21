package com.flightreservation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")  // This will intercept all requests
public class ErrorHandling implements Filter {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ErrorHandling.class);

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson for JSON serialization

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // Proceed with the request
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Log the error
            logger.error("Unhandled error occurred", e);

            // Set HTTP response code
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            ApiError apiError = new ApiError(500, "Internal Server Error", e.getMessage());

            // Set HTTP status code
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.setContentType("application/json");

            // Convert ApiError object to JSON using Jackson
            String jsonResponse = objectMapper.writeValueAsString(apiError);

            // Send the error response
            response.getWriter().write(jsonResponse);
        }
    }

    @Override
    public void destroy() {
    }
}

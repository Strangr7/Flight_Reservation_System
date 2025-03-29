package com.flightreservation.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*") // Applies to ALL requests (adjust URL pattern if needed)
public class MessageCleanupFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code (if needed)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false); // Get session (if exists)

        if (session != null) {
            // Check if there's a pending success message
            if (session.getAttribute("successMessage") != null) {
                boolean messageShown = session.getAttribute("messageShown") != null 
                    && (boolean) session.getAttribute("messageShown");

                if (messageShown) {
                    // Message was already displayed → REMOVE IT
                    session.removeAttribute("successMessage");
                    session.removeAttribute("messageShown");
                } else {
                    // First time showing → MARK AS DISPLAYED
                    session.setAttribute("messageShown", true);
                }
            }
        }

        // Continue the request chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code (if needed)
    }
}

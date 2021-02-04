package in.co.raystech.maven.project4.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.raystech.maven.project4.bean.RoleBean;
import in.co.raystech.maven.project4.bean.UserBean;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * front Controller performs session checking and logging operations before
 * calling any application controller. It prevents any user to access
 * application without login.
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebFilter(filterName = "FrontController", urlPatterns = { "/OnePartner/ctl/*" })
public class FrontController implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(true);

		if (session.getAttribute("user") == null) {
			request.setAttribute("message", "Your session has been expired. Please re-Login");
			ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
		} else {
			chain.doFilter(req, resp);
		}
	}

	public void init(FilterConfig conf) throws ServletException {
	}
}

package in.co.raystech.maven.project4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.raystech.maven.project4.bean.BaseBean;
import in.co.raystech.maven.project4.bean.RoleBean;
import in.co.raystech.maven.project4.bean.UserBean;
import in.co.raystech.maven.project4.exception.ApplicationException;
import in.co.raystech.maven.project4.model.RoleModel;
import in.co.raystech.maven.project4.model.UserModel;
import in.co.raystech.maven.project4.util.DataUtility;
import in.co.raystech.maven.project4.util.DataValidator;
import in.co.raystech.maven.project4.util.PropertyReader;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * Login functionality Controller. Performs operation for Login
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "OnePartner/LoginCtl" })
public class LoginCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	public static final String OP_REGISTER = "Register";
	public static final String OP_SIGN_IN = "SignIn";
	public static final String OP_SIGN_UP = "SignUp";
	public static final String OP_LOG_OUT = "logout";
	private static Logger log = Logger.getLogger(LoginCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("LoginCtl Method validate Started");
		boolean pass = true;
		String op = request.getParameter("operation");
		if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
			return pass;
		}
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		} else if (!DataValidator.isEmail(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login Id"));
			pass = false;
		}
		if (DataValidator.isNull(password)) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}
		log.debug("LoginCtl Method validate Ended");
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("LoginCtl Method populatebean Started");
		UserBean bean = new UserBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		log.debug("LoginCtl Method populatebean Ended");
		return bean;
	}

	/**
	 * Display Login form
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("LoginCtl Method doGet Started");
		System.out.println("in LoginCtl do get method");
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		// get model
		UserModel model = new UserModel();
		RoleModel role = new RoleModel();
		if (OP_LOG_OUT.equals(op)) {
			HttpSession session = request.getSession(true);
			session = request.getSession();
			session.invalidate();
			ServletUtility.redirect(ORSView.LOGIN_CTL, request, response);
			return;
		} else if (id > 0) {
			UserBean userbean;
			try {
				userbean = model.findByPK(id);
				ServletUtility.setBean(userbean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("UserCtl Method doGet Ended");
	}

	/**
	 * Submitting or login action performing
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("LoginCtl Method doPost Started");
		HttpSession session = request.getSession(true);
		System.out.println("in LoginCtl do post method");
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out.println(op);
		// get model
		UserModel model = new UserModel();
		RoleModel role = new RoleModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SIGN_IN.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			try {
				bean = model.authenticate(bean.getLogin(), bean.getPassword());
				if (bean != null) {
					session.setAttribute("user", bean);
					long rollId = bean.getRoleId();
					RoleBean rolebean = role.findByPK(rollId);
					if (rolebean != null) {
						session.setAttribute("role", rolebean);
					}
					if (bean.getLogin().equals("admin@onepartner.in")) {
						ServletUtility.redirect(ORSView.MANAGE_PARTNERS_CTL, request, response);
					} else {
						System.out.println("'''''''''''''''");
						ServletUtility.redirect(ORSView.MARKET_PLACE_CTL, request, response);
					}
					return;
				} else {
					bean = (UserBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Invalid LoginId / Password", request);
				}
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_SIGN_UP.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("LoginCtl Method doPost Ended");

	}

	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}
}
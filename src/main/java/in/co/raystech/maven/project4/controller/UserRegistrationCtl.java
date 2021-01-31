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
import in.co.raystech.maven.project4.exception.DuplicateRecordException;
import in.co.raystech.maven.project4.model.UserModel;
import in.co.raystech.maven.project4.util.DataUtility;
import in.co.raystech.maven.project4.util.DataValidator;
import in.co.raystech.maven.project4.util.PropertyReader;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * User registration functionality Controller. Performs operation for User
 * Registration
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebServlet(name = "UserRegistrationCtl", urlPatterns = { "/OnePartner/UserRegistrationCtl" })
public class UserRegistrationCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(UserRegistrationCtl.class);
	public static final String OP_SIGN_UP = "SignUp";
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.info("UserRegistrationCtl Method validat Started");
		boolean pass = true;
		String login = request.getParameter("login");
		String dob = request.getParameter("dob");
		String password = request.getParameter("password");

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		} else if (!DataValidator.isAlpha(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.number", "Name "));
			pass = false;
		}

		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		} else if (!DataValidator.isEmail(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
			pass = false;
		}
		if (DataValidator.isNull(password)) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}

		if (DataValidator.isNotNull(password)) {
			if (!DataValidator.checkPasswordLength(password)) {
				request.setAttribute("password", PropertyReader.getValue("error.checkpassword", "Password"));
				pass = false;
			}
		}

		if (DataValidator.isNotNull(password) && DataValidator.checkPasswordLength(password)) {
			if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
				request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
				pass = false;
			}
		}

		if (DataValidator.isNotNull(password) && DataValidator.isNotNull(request.getParameter("confirmPassword"))) {
			if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))) {
				request.setAttribute("confirmPassword", PropertyReader.getValue("error.passwordnotmatch", "Password"));
				pass = false;
			}
		}

		if (DataValidator.isNull(request.getParameter("contactNumber")))

		{
			request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "Contact No"));
			pass = false;
		}
		if (DataValidator.isNotNull(request.getParameter("contactNumber")))

		{
			if (!DataValidator.isCorrectPhoneNumber(request.getParameter("contactNumber"))) {
				request.setAttribute("contactNumber", PropertyReader.getValue("error.phone", "Contact No"));
				pass = false;
			}
		}
		log.info("UserRegistrationCtl Method validat ended");
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("UserRegistrationCtl Method populateBean Started");
		UserBean bean = new UserBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setRoleId(RoleBean.STUDENT);
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("contactNumber")));
		populateDTO(bean, request);
		log.info("UserRegistrationCtl Method populateBean Ended");
		return bean;
	}

	/**
	 * Display concept of user registration
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtility.forward(getView(), request, response);

	}

	/**
	 * Submit concept of user registration
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("UserRegistrationCtl Method doPpost Started");
		UserBean bean = (UserBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SIGN_UP.equalsIgnoreCase(op)) {
			try {
				long pk = model.registerUser(bean);
				bean.setId(pk);
				request.getSession().setAttribute("UserBean", bean);
				HttpSession session = request.getSession(true);
				session.setAttribute("rgstrtnsucs", "You have successfully registered, you may proceed to Login");
				ServletUtility.redirect(ORSView.LOGIN_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				HttpSession session = request.getSession(true);
				session.setAttribute("chngpwd", e.getMessage());
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				log.error(e);
				HttpSession session = request.getSession(true);
				session.setAttribute("duplicateRecords", e.getMessage());
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.forward(getView(), request, response);
			}

		}

		else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
			return;
		}
		log.info("UserRegistrationCtl Method doPost Ended");

	}

	@Override
	protected String getView() {
		return ORSView.USER_REGISTRATION_VIEW;
	}

}
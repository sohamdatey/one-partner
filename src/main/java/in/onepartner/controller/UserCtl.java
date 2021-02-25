package in.onepartner.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.onepartner.bean.BaseBean;
import in.onepartner.bean.RoleBean;
import in.onepartner.bean.UserBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.exception.DuplicateRecordException;
import in.onepartner.model.RoleModel;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.DataValidator;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.ServletUtility;

/**
 * * User functionality Controller. Performs operation for add, update and get
 * User
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebServlet(name = "UserCtl", urlPatterns = { "/ctl/UserCtl" })
public class UserCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(UserCtl.class);
	
	@Override
	protected void preload(HttpServletRequest request) {
		RoleModel model = new RoleModel();
		try {
			List l = model.list();
			request.setAttribute("roleList", l);
		} catch (ApplicationException e) {
			log.error(e);
		}

	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		log.info("UserCtl Method validate Started");

		boolean pass = true;

		String login = request.getParameter("login");
		String dob = request.getParameter("dob");
		String password = request.getParameter("password");

		if (DataValidator.isNull(DataUtility.getString(request.getParameter("firstName")))) {
			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass = false;
		} else if (!DataValidator.isAlpha(DataUtility.getString(request.getParameter("firstName")))) {
			request.setAttribute("firstName", PropertyReader.getValue("error.number", "First Name "));
			pass = false;
		}

		if (DataValidator.isNull(DataUtility.getString(request.getParameter("lastName")))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;
		} else if (!DataValidator.isAlpha(request.getParameter("lastName"))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.number", "Last Name "));
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

		if (DataValidator.isNotNull(password) && DataValidator.isNotNull(request.getParameter("confirmPassword"))
				&& DataValidator.checkPasswordLength(password)) {
			if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))) {
				request.setAttribute("confirmPassword", PropertyReader.getValue("error.passwordnotmatch", "Password"));
				pass = false;
			}
		}

		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		}

		if (DataValidator.isNotNull(login)) {
			if (!DataValidator.isEmail(login)) {
				request.setAttribute("login", PropertyReader.getValue("error.email", "Is"));
				pass = false;
			}
		}

		if (DataValidator.isNull(request.getParameter("roleId"))) {
			request.setAttribute("roleId", PropertyReader.getValue("error.require", "Role "));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
		}

		if (DataValidator.isNull(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
			pass = false;

		} else if (DataValidator.isNotNull(dob)) {

			if (!DataValidator.isDate(dob)) {
				request.setAttribute("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
				pass = false;
			}
		}

		if (!DataValidator.checkAge(DataUtility.getSQLDate(DataUtility.getDate(request.getParameter("dob")))))

		{
			request.setAttribute("dob", PropertyReader.getValue("error.agerestriction", "You"));
			pass = false;
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

		log.info("UserCtl Method validate Ended");

		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("UserCtl Method populatebean Started");

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));

		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setLogin(DataUtility.getString(request.getParameter("login")));

		HttpSession session = request.getSession(true);

		UserBean ubean = (UserBean) session.getAttribute("user");

		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

		bean.setMobileNo(DataUtility.getString(request.getParameter("contactNumber")));

		populateDTO(bean, request);

		log.info("UserCtl Method populatebean Ended");

		return bean;
	}

	/**
	 * Contains DIsplay logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		UserBean ubean = (UserBean) session.getAttribute("user");

		if (ubean.getRoleId() != RoleBean.ADMIN) {
			ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
			return;
		}

		log.info("UserCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 || op != null) {
			UserBean bean;
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.info("UserCtl Method doGet Ended");
	}

	/**
	 * Contains Submit logics
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("UserCtl Method doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);

			try {
				if (id > 0) {

					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data is successfully Updated", request);

				}

				else {

					long pk = 0;
					pk = model.add(bean);
					bean.setId(pk);
					ServletUtility.setSuccessMessage("Data is successfully saved", request);

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage(e.getMessage(), request);
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			UserBean bean = (UserBean) populateBean(request);
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);

		log.info("UserCtl Method doPostEnded");

	}

	@Override
	protected String getView() {
		return ORSView.USER_VIEW;
	}

}

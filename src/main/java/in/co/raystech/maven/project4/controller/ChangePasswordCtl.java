package in.co.raystech.maven.project4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.raystech.maven.project4.bean.BaseBean;
import in.co.raystech.maven.project4.bean.UserBean;
import in.co.raystech.maven.project4.exception.ApplicationException;
import in.co.raystech.maven.project4.exception.RecordNotFoundException;
import in.co.raystech.maven.project4.model.UserModel;
import in.co.raystech.maven.project4.util.DataUtility;
import in.co.raystech.maven.project4.util.DataValidator;
import in.co.raystech.maven.project4.util.PropertyReader;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * Change Password functionality Controller. Performs operation for Change
 * Password
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "ChangePasswordCtl", urlPatterns = { "/OnePartner/ctl/ChangePasswordCtl" })
public class ChangePasswordCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";
	private static Logger log = Logger.getLogger(ChangePasswordCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		log.info("ChangePasswordCtl Method validate Started");
		boolean pass = true;
		String op = request.getParameter("operation");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
			return pass;
		}
		if (DataValidator.isNull(oldPassword)) {
			request.setAttribute("oldPassword", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}
		if (DataValidator.isNotNull(oldPassword)) {
			if (!DataValidator.checkPasswordLength(oldPassword)) {
				request.setAttribute("oldPassword", PropertyReader.getValue("error.checkpassword", "Password"));
				pass = false;
			}
		}
		if (DataValidator.isNotNull(oldPassword) && DataValidator.checkPasswordLength(oldPassword)) {
			if (DataValidator.isNull(newPassword)) {
				request.setAttribute("newPassword", PropertyReader.getValue("error.require", "New Password"));
				pass = false;
			}
		}
		if (DataValidator.isNotNull(newPassword)) {
			if (!DataValidator.checkPasswordLength(newPassword)) {
				request.setAttribute("newPassword", PropertyReader.getValue("error.checkpassword", "Password"));
				pass = false;
			}
		}
		if (DataValidator.isNotNull(newPassword) && DataValidator.checkPasswordLength(newPassword)) {
			if (DataValidator.isNull(confirmPassword)) {
				request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
				pass = false;
			}
		}
		if (DataValidator.isNotNull(newPassword) && DataValidator.isNotNull(confirmPassword)) {
			if (!newPassword.equals(confirmPassword)) {
				request.setAttribute("confirmPassword",
						PropertyReader.getValue("error.passwordnotmatch", "New Password"));
				pass = false;
			}
		}
		log.info("ChangePasswordCtl Method validate Ended");
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("ChangePasswordCtl Method populatebean Started");
		UserBean bean = new UserBean();
		bean.setPassword(DataUtility.getString(request.getParameter("oldPassword")));
		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
		bean.setNewPassword(request.getParameter("newPassword"));
		populateDTO(bean, request);
		log.info("ChangePasswordCtl Method populatebean Ended");
		return bean;
	}

	/**
	 * Display Logics inside this method
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Submit logic inside it
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		log.info("ChangePasswordCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserModel model = new UserModel();
		UserBean bean = (UserBean) populateBean(request);
		UserBean UserBean = (UserBean) session.getAttribute("user");
		long id = UserBean.getId();
		if (OP_SAVE.equalsIgnoreCase(op)) {
			try {
				boolean flag = model.changePassword(id, bean.getPassword(), bean.getNewPassword());
				if (flag == true) {
					bean = model.findByLogin(UserBean.getLogin());
					session.setAttribute("user", bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Your Password has been changed Successfully", request);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				session.setAttribute("chngpwd", e.getMessage());
				ServletUtility.handleException(e, request, response);
				return;
			} catch (RecordNotFoundException e) {
				log.error(e);
				ServletUtility.setErrorMessage("Old Password is Invalid", request);
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request, response);
			return;
		}
		ServletUtility.forward(ORSView.CHANGE_PASSWORD_VIEW, request, response);
		log.info("ChangePasswordCtl Method doGet Ended");
	}

	@Override
	protected String getView() {
		return ORSView.CHANGE_PASSWORD_VIEW;
	}

}

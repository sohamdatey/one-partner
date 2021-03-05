package in.onepartner.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;

import in.onepartner.bean.BaseBean;
import in.onepartner.bean.UserBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.exception.RecordNotFoundException;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.DataValidator;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.ServletUtility;

/**
 * Forget Password functionality Controller. Performs operation for Forget
 * Password
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/OnePartner/ForgetPasswordCtl" })
public class ForgetPasswordCtl extends BaseCtl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	org.slf4j.Logger log = LoggerFactory.getLogger(ForgetPasswordCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.info("ForgetPasswordCtl Method validate Started");
		boolean pass = true;
		String login = request.getParameter("login");
		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login-Id"));
			pass = false;
		} else if (!DataValidator.isEmail(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login-Id"));
			pass = false;
		}
		log.info("ForgetPasswordCtl Method validate Ended");
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("ForgetPasswordCtl Method populateBean Started");
		UserBean bean = new UserBean();
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		log.info("ForgetPasswordCtl Method validate Ended");
		return bean;
	}

	/**
	 * DIsplay Concept are there
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ForgetPasswordCtl Method doGet Started");
		ServletUtility.forward(getView(), request, response);
		log.info("ForgetPasswordCtl Method doGet Ended");
	}

	/**
	 * Submit Concepts
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ForgetPasswordCtl Method doPost Started this time we have slf4j :D");
		HttpSession session = request.getSession(true);
		String op = DataUtility.getString(request.getParameter("operation"));
		UserBean bean = (UserBean) populateBean(request);
		// get model
		UserModel model = new UserModel();
		if (OP_GO.equalsIgnoreCase(op)) {
			try {
				model.forgetPassword(bean.getLogin());
				ServletUtility.setSuccessMessage("Password has been sent successfully to your Email-Id", request);
			} catch (RecordNotFoundException e) {
				log.error("error", e);
				ServletUtility.setErrorMessage(e.getMessage(), request);
			} catch (ApplicationException e) {
				log.error("error", e);
				session.setAttribute("chngpwd", e.getMessage());
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (Exception e) {
				log.error("error", e);
				request.setAttribute(BaseCtl.MSG_ERROR, "leo aagya, p2 bualo ab..");
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.handleException(e, request, response);
				return;
			}
			ServletUtility.forward(getView(), request, response);
		}
		log.info("ForgetPasswordCtl Method validate Started");
	}

	@Override
	protected String getView() {
		return ORSView.FORGET_PASSWORD_VIEW;
	}

}
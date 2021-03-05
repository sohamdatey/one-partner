package in.onepartner.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.onepartner.bean.BaseBean;
import in.onepartner.bean.UserBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.exception.DuplicateRecordException;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.DataValidator;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.ServletUtility;

/**
 * My Profile functionality Controller. Performs operation for update your
 * Profile
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebServlet(name = "AdminProfileCtl", urlPatterns = { "/OnePartner/ctl/AdminProfileCtl" })
public class AdminProfileCtl extends BaseCtl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String OP_CHANGE_MY_PASSWORD = "ChangePassword";

	private static Logger log = Logger.getLogger(AdminProfileCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("MyProfileCtl Method populatebean Started");

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setLogin(DataUtility.getString(request.getParameter("login")));

		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));

		populateDTO(bean, request);

		return bean;
	}

	/**
	 * Display Concept for viewing profile page view
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		log.info("MyprofileCtl Method doGet Started");
		UserBean UserBean = (UserBean) session.getAttribute("user");
		long id = UserBean.getId();
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserModel model = new UserModel();
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
		}
		ServletUtility.forward(getView(), request, response);

		log.info("MyProfileCtl Method doGet Ended");
	}

	/**
	 * Submit Concept
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		log.info("MyprofileCtl Method doPost Started");

		UserBean sessionUserBean = (UserBean) session.getAttribute("user");
		long id = sessionUserBean.getId();
		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		UserModel model = new UserModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			try {
				if (id > 0) {
					UserBean bean3 = new UserBean();
					bean3 = model.findByLogin(bean.getLogin());
				
					sessionUserBean.setName(bean.getName());
					sessionUserBean.setMobileNo(bean.getMobileNo());
					sessionUserBean.setLogin(bean.getLogin());
				
					sessionUserBean.setId(bean3.getId());
					sessionUserBean.setPassword(bean3.getPassword());
					sessionUserBean.setRoleId(bean3.getRoleId());
					sessionUserBean.setDescription(bean3.getDescription());
					model.updatePartner(sessionUserBean);
				}
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Profile has been updated Successfully. ", request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request, response);
			return;

		}

		ServletUtility.forward(getView(), request, response);

		log.info("MyProfileCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.ADMIN_PROFILE_VIEW ;
	}

}
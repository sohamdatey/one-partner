package in.co.raystech.maven.project4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.raystech.maven.project4.bean.BaseBean;
import in.co.raystech.maven.project4.bean.UserBean;
import in.co.raystech.maven.project4.exception.ApplicationException;
import in.co.raystech.maven.project4.exception.DuplicateRecordException;
import in.co.raystech.maven.project4.model.UserModel;
import in.co.raystech.maven.project4.util.DataUtility;
import in.co.raystech.maven.project4.util.DataValidator;
import in.co.raystech.maven.project4.util.PropertyReader;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * Faculty functionality Controller. Performs operation for add, update, delete
 * and get Faculty
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "ChangeSenderEmailUserCtl", urlPatterns = { "/ctl/ChangeSenderEmailUserCtl" })
public class ChangeSenderEmailUserCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(ChangeSenderEmailUserCtl.class);

	/**
	 * rebuild
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("ManageMarketPartnersCtl Method populateBean Started");
		UserBean bean = new UserBean();
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		populateDTO(bean, request);
		log.info("ManageMarketPartnersCtl Method PopulateBean Ended");
		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("ManageMarketPartnersCtl doGet Started");
		UserBean bean = (UserBean) populateBean(request);

		try {
			bean = UserModel.getEmailSenderUser();
			bean.setLogin(bean.getLogin());
			bean.setPassword(bean.getPassword());
		} catch (ApplicationException e) {
			log.error(e);
		}
		ServletUtility.setBean(bean, request);
		ServletUtility.forward(getView(), request, response);
		log.info("ManageMarketPartnersCtl doGet ended");

		return;

	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ManageMarketPartnersCtl Method doPost Started");
		UserBean bean = (UserBean) populateBean(request);
		UserModel model = new UserModel();
		try {
			model.updateEmailSenderUser(bean);
		} catch (ApplicationException e) {
			log.error(e);
		} catch (DuplicateRecordException e) {
			log.error(e);
		}
		ServletUtility.setBean(bean, request);
		ServletUtility.forward(getView(), request, response);
		return;
	}

	@Override
	protected String getView() {
		return ORSView.CHANGE_EMAIL_SENDERUSER_VIEW;

	}

}

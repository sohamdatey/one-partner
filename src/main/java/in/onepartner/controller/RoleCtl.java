package in.onepartner.controller;

import java.io.IOException;

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
import in.onepartner.util.DataUtility;
import in.onepartner.util.DataValidator;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.ServletUtility;

/**
 * Role functionality Controller. Performs operation for add, update and get
 * Role
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS

 */
@WebServlet(name = "RoleCtl", urlPatterns = { "/OnePartner/ctl/RoleCtl" })
public class RoleCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(RoleCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		log.info("RoleCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}

		else if (!DataValidator.isAlpha(DataUtility.getString(request.getParameter("name")))) {
			request.setAttribute("name", PropertyReader.getValue("error.number", "First Name "));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}

		log.info("RoleCtl Method validate Ended");

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("RoleCtl Method populatebean Started");

		RoleBean bean = new RoleBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);

		log.info("RoleCtl Method populatebean Ended");

		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		UserBean ubean = (UserBean) session.getAttribute("user");

		if (ubean.getRoleId() != RoleBean.ADMIN) {
			ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
			return;
		}

		log.info("RoleCtl Method doGet Started");


		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		RoleModel model = new RoleModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			RoleBean bean;
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
		log.info("RoleCtl Method doGetEnded");
	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("RoleCtl Method doGet Started");


		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		RoleModel model = new RoleModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {

			RoleBean bean = (RoleBean) populateBean(request);

			try {
				if (id > 0) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data is successfully Updated", request);

				} else {
					long pk = model.add(bean);
					bean.setId(pk);
				}

				ServletUtility.setSuccessMessage("Data is successfully saved", request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Role already exists", request);
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			RoleBean bean = (RoleBean) populateBean(request);
			try {
				model.delete(bean);
				ServletUtility.setSuccessMessage("Role has been deleted succcessfully", request);
				ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
			return;

		}

		else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
			return;

		}

		
		ServletUtility.forward(getView(), request, response);

		log.info("RoleCtl Method doPOst Ended");
	}

	@Override
	protected String getView() {
		return ORSView.ROLE_VIEW;
	}

}
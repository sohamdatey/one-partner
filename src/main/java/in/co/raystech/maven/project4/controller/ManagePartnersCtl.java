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
@WebServlet(name = "ManagePartnersCtl", urlPatterns = { "/ctl/ManagePartnersCtl" })
public class ManagePartnersCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(ManagePartnersCtl.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("ManageMarketPartnersCtl Method validate Started");
		boolean pass = true;
		String op = DataUtility.getString(request.getParameter("operation"));
		if (DataValidator.isNull(request.getParameter("collegeId"))) {
			request.setAttribute("collegeId", PropertyReader.getValue("error.require", "College Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("userId"))) {
			request.setAttribute("userId", PropertyReader.getValue("error.require", "Student Name"));
			pass = false;
		}
		log.debug("ManageMarketPartnersCtl Method validate Ended");
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("ManageMarketPartnersCtl Method populateBean Started");
		UserBean bean = new UserBean();
		bean.setName(DataUtility.getString(request.getParameter("name")));
		populateDTO(bean, request);
		log.debug("ManageMarketPartnersCtl Method PopulateBean Ended");
		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ManageMarketPartnersCtl Method doGet Started");
		List list = null;
		UserModel model = new UserModel();
		UserBean bean = (UserBean) populateBean(request);
		long id = DataUtility.getLong(request.getParameter("id"));
		String description = DataUtility.getString(request.getParameter("description"));
		if (id > 0) {
			try {
				bean = model.findByPK(id);
				bean.setDescription(description);
				model.updatePartner(bean);
				ServletUtility.setBean(bean, request);
			} catch (Exception e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
			ServletUtility.redirect(ORSView.MANAGE_PARTNERS_CTL, request, response);
			return;
		} else {
			try {
				list = model.search(bean, 0, 0);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);

			ServletUtility.forward(getView(), request, response);
		}
		log.debug("ManageMarketPartnersCtl Method doGet Ended");

	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ManageMarketPartnersCtl Method doPost Started");
		UserBean bean = (UserBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));
		String[] ids = request.getParameterValues("ids");
		UserModel model = new UserModel();
		if (OP_DELETE.equalsIgnoreCase(op)) {
			if (ids != null && ids.length > 0) {
				UserBean deletebean = new UserBean();
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					try {
						model.delete(deletebean);
					} catch (ApplicationException e) {
						log.error(e);
						e.printStackTrace();
					}
					ServletUtility.setSuccessMessage("Record deleted successfully", request);
				}
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
			ServletUtility.redirect(ORSView.MANAGE_PARTNERS_CTL, request, response);
			return;
		}
		if (search != null) {
			try {
				System.out.println("pro....." + search);
				List list = null;
				bean.setName(search);
				list = model.searchSpecific(bean, 0, 0);
				ServletUtility.setList(list, request);
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
				ServletUtility.setBeanP(bean, request);
				ServletUtility.setList(list, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
			ServletUtility.forward(getView(), request, response);
			return;
		}
		List list = null;
		try {
			list = model.search(bean, 0, 0);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		if (list == null || list.size() == 0) {
			ServletUtility.setErrorMessage("No record found ", request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("ManageMarketPartnersCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		System.out.println("view");
		return ORSView.MANAGE_PARTNER_LIST_VIEW;

	}

}

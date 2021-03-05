package in.onepartner.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.onepartner.bean.BaseBean;
import in.onepartner.bean.UserBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.DataValidator;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.ServletUtility;

/**
 * Faculty functionality Controller. Performs operation for add, update, delete
 * and get Faculty
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "ManagePartnersCtl", urlPatterns = { "/OnePartner/ctl/ManagePartnersCtl" })
public class ManagePartnersCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(ManagePartnersCtl.class);

	/**
	 * rebuild
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("ManageMarketPartnersCtl Method populateBean Started");
		UserBean bean = new UserBean();
		bean.setName(DataUtility.getString(request.getParameter("search")));
		populateDTO(bean, request);
		log.info("ManageMarketPartnersCtl Method PopulateBean Ended");
		return bean;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ManageMarketPartnersCtl Method doGet Started");
		List<UserBean> list = null;
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
				try {
					list = model.searchInGetMethod(0, 0);
				} catch (ApplicationException e) {
					log.error(e);
					ServletUtility.handleException(e, request, response);
					return;
				}
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
				ServletUtility.setList(list, request);

			} catch (Exception e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
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
		}
		ServletUtility.forward(getView(), request, response);
		log.info("ManageMarketPartnersCtl Method doGet Ended");

	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ManageMarketPartnersCtl Method doPost Started");
		UserBean bean = (UserBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
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
			ServletUtility.redirect(getView(), request, response);
			return;
		}
		try {
			List<UserBean> list = null;
			list = model.searchSpecificUser(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setBean(bean, request);
			ServletUtility.setList(list, request);
			ServletUtility.forward(getView(), request, response);
			return;

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_PARTNER_LIST_VIEW;

	}

}

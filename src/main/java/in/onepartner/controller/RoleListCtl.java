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
import in.onepartner.model.RoleModel;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.DataValidator;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.ServletUtility;

/**
 * Role List functionality Controller. Performs operation for list, search
 * operations of Role
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebServlet(name = "RoleListCtl", urlPatterns = { "/OnePartner/ctl/RoleListCtl" })
public class RoleListCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(RoleListCtl.class);
	
	
	
	@Override
	protected void preload(HttpServletRequest request) {
		RoleModel roleModel = new RoleModel();

		try {
			int listSize = roleModel.list().size();
			int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
			int buttonNumber = listSize / pageSize;
			if (listSize % pageSize != 0) {
				buttonNumber++;
			}
			request.setAttribute("buttonNumber", buttonNumber);

			List roleList = roleModel.list();
			request.setAttribute("roleList", roleList);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		RoleBean bean = new RoleBean();
		bean.setName(DataUtility.getString(request.getParameter("name")));

		return bean;
	}

	/**
	 * Contains Display logics
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		UserBean ubean = (UserBean) session.getAttribute("user");

		if (ubean.getRoleId() != RoleBean.ADMIN) {
			ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
			return;
		}

		log.info("RoleListCtl doGet Start");
		List list = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size")) + 1;
		RoleBean bean = (RoleBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		RoleModel model = new RoleModel();
		try {
			list = model.search(bean, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.info("RoleListCtl doGet End");
	}

	/**
	 * Contains Submit logics
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("RoleListCtl doPost Start");

		List list = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize")) + 1;

		pageNo = (pageNo == 0) ? 1 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		RoleBean bean = (RoleBean) populateBean(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");

		RoleModel model = new RoleModel();

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					RoleBean deletebean = new RoleBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);
						ServletUtility.setSuccessMessage("Record deleted successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Please select at least one record", request);
				}
			} else if (DataValidator.isInteger(op)) {
				pageNo = DataUtility.getInt(op);
			}

			int listSize = model.search(bean).size();
			int buttonNumber = listSize / pageSize;
			if (listSize % pageSize != 0) {
				buttonNumber++;
			}
			request.setAttribute("buttonNumber", buttonNumber);

			list = model.search(bean, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.info("MarksheetListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.ROLE_LIST_VIEW;
	}

}

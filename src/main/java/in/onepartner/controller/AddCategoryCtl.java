package in.onepartner.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.onepartner.bean.BaseBean;
import in.onepartner.bean.CategoryBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.exception.DuplicateRecordException;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.ServletUtility;

/**
 * Servlet implementation class ManageCategoryCtl
 */

@WebServlet(name = "AddCategoryCtl", urlPatterns = { "/OnePartner/ctl/AddCategoryCtl" })
public class AddCategoryCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(AddCategoryCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("AddCategoryCtl Method populateBean Started");

		CategoryBean bean = new CategoryBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setCategory(DataUtility.getString(request.getParameter("category")));

		bean.setMarketPlaceId(DataUtility.getInt(request.getParameter("marketPlaceId")));

		populateDTO(bean, request);

		log.debug("AddCategoryCtl Method populateBean Ended");

		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("AddCategoryCtl Method doGet Started");

		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		String op = DataUtility.getString(request.getParameter("operation"));

		try {
			List list = null;
			CategoryBean bean = new CategoryBean();
			list = model.searchCategory(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
		} catch (ApplicationException e1) {
			log.error(e1);
			e1.printStackTrace();
		}

		if (id > 0) {

			CategoryBean catBean;
			try {
				catBean = model.findByPKCategory(id);
				request.setAttribute("catBean", catBean);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

			ServletUtility.forward(getView(), request, response);
		}
		log.debug("AddCategoryCtl Method doGet ended");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("AddCategoryCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));
		String highlightchk = DataUtility.getString(request.getParameter("highlightchk"));
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		CategoryBean bean = (CategoryBean) populateBean(request);
		if (OP_ADD.equalsIgnoreCase(op) || OP_EDIT.equalsIgnoreCase(op)) {

			if (id > 0) {
				if (OP_EDIT.equalsIgnoreCase(op)) {

					model = new UserModel();
					try {
						model.updateCategory(bean);
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DuplicateRecordException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Category is successfully Updated", request);
				}
			}

			else if (OP_ADD.equalsIgnoreCase(op)) {
				long pk = 0;
				try {
					pk = model.addCategory(bean);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DuplicateRecordException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage(e.getMessage(), request);
					e.printStackTrace();
				}
				bean.setId(pk);
				ServletUtility.setSuccessMessage("Category added successfully", request);
				ServletUtility.forward(ORSView.MANAGE_CATEGORY_CTL, request, response);
				return;

			}

			if (OP_DELETE.equalsIgnoreCase(op)) {
				CategoryBean deletebean = new CategoryBean();
				deletebean.setId(id);
				try {
					model.deleteCategory(deletebean);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			List list = null;
			if (bean.getCategory() != null) {

				try {
					list = model.searchCategory(bean, 0, 0);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServletUtility.setList(list, request);
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}

			} else {
				try {
					list = model.categoryList();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServletUtility.setList(list, request);
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}
			ServletUtility.setBean(bean, request);
			ServletUtility.setList(list, request);
			ServletUtility.forward(getView(), request, response);
		}

	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_CATEGORY_VIEW;
	}

}

package in.co.raystech.maven.project4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.raystech.maven.project4.bean.BaseBean;
import in.co.raystech.maven.project4.bean.CategoryBean;
import in.co.raystech.maven.project4.bean.UserBean;
import in.co.raystech.maven.project4.exception.ApplicationException;
import in.co.raystech.maven.project4.exception.DuplicateRecordException;
import in.co.raystech.maven.project4.model.UserModel;
import in.co.raystech.maven.project4.util.DataUtility;
import in.co.raystech.maven.project4.util.PropertyReader;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * Servlet implementation class ManageCategoryCtl
 */

@WebServlet(name = "ManageCategoryCtl", urlPatterns = { "/OnePartner/ctl/ManageCategoryCtl" })
public class ManageCategoryCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ManageCategoryCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("ManageCategoryCtl Method populateBean Started");

		CategoryBean bean = new CategoryBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setCategory(DataUtility.getString(request.getParameter("category")));

		bean.setMarketPlaceId(DataUtility.getInt(request.getParameter("marketPlaceId")));

		populateDTO(bean, request);

		log.info("ManageCategoryCtl Method populateBean Ended");

		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("ManageCategoryCtl Method doGet Started");

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

		}

		ServletUtility.forward(getView(), request, response);
		log.info("ManageCategoryCtl Method doGet ended");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("ManageCategoryCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		String category= DataUtility.getString(request.getParameter("category"));
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
					ServletUtility.setErrorMessage(e.getMessage(), request);
					e.printStackTrace();
				}
				bean.setId(pk);
				ServletUtility.setSuccessMessage("Category added successfully", request);
			}

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
			ServletUtility.setSuccessMessage("Category deleted successfully", request);
		}

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			log.info("-----------------------------------------");
			log.info("-----------------------------------------");
			log.info(category);
			log.info(op);
			log.info(category);
			log.info("-----------------------------------------");
			log.info("-----------------------------------------");
			List<CategoryBean> list = null;
			try {
				bean.setCategory(category);
				list = model.searchSpecificCategory(bean, 0, 0);
			} catch (ApplicationException e) {
				log.info(e);
			}
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
			return;

		}
		List list = null;
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
		ServletUtility.setBean(bean, request);
		ServletUtility.forward(getView(), request, response);
		return;
	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_CATEGORY_VIEW;
	}

}

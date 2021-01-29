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

@WebServlet(name = "AddCategoryCtl", urlPatterns = { "/ctl/AddCategoryCtl" })
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
			System.out.println("in do post Add category/////+ id........" + id);

			try {
				if (id > 0) {
					if (OP_EDIT.equalsIgnoreCase(op)) {
						try {

							System.out.println("thiss isss market place id ----" + bean.getMarketPlaceId());
							model = new UserModel();
							model.updateCategory(bean);
							ServletUtility.setBean(bean, request);
							ServletUtility.setSuccessMessage("Category is successfully Updated", request);
						} catch (ApplicationException e) {
							log.error(e);
							e.printStackTrace();
						} catch (DuplicateRecordException e) {
							log.error(e);
							e.printStackTrace();
						}
					}
				}

				else if (OP_ADD.equalsIgnoreCase(op)) {
					System.out.println("ADDD ho rha........." + bean.getCategory());
					long pk = 0;
					pk = model.addCategory(bean);
					bean.setId(pk);
					ServletUtility.setSuccessMessage("Category added successfully", request);
				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage(e.getMessage(), request);
			}

		}

		if (OP_DELETE.equalsIgnoreCase(op)) {
			CategoryBean deletebean = new CategoryBean();
			deletebean.setId(id);
			try {
				model.deleteCategory(deletebean);
			} catch (ApplicationException e) {
				log.error(e);
				e.printStackTrace();
			}
			ServletUtility.redirect(ORSView.MANAGE_CATEGORY_CTL, request, response);
			return;

		}

		try {
			List list = null;
			list = model.searchCategory(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setBean(bean, request);
			ServletUtility.setList(list, request);
			ServletUtility.redirect(ORSView.MANAGE_CATEGORY_CTL, request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("AddCategoryCtl Method doPost Ended");

	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_CATEGORY_VIEW;
	}

}

package in.co.raystech.maven.project4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

@WebServlet(name = "ManageCategoryListCtl", urlPatterns = { "/OnePartner/ctl/ManageCategoryListCtl" })
public class ManageCategoryListCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list = null;
		CategoryBean bean = (CategoryBean) populateBean(request);
		UserModel model = new UserModel();

		try {
			list = null;
			list = model.searchCategory(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage(e.getMessage(), request);
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("post post post post=======================================");
		CategoryBean bean = new CategoryBean();
		try {
			List list = null;
			UserModel model = new UserModel();
			list = model.searchCategory(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setBean(bean, request);
			ServletUtility.setList(list, request);
			ServletUtility.redirect(ORSView.MANAGE_CATEGORY_CTL, request, response);

		} catch (ApplicationException e) {
			ServletUtility.handleException(e, request, response);
			return;
		}

	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_CATEGORY_VIEW;
	}

}

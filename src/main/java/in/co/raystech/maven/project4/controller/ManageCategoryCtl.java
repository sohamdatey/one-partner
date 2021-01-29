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

@WebServlet(name = "ManageCategoryCtl", urlPatterns = { "/ctl/ManageCategoryCtl" })
public class ManageCategoryCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ManageCategoryCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("ManageCategoryCtl Method populateBean Started");
		CategoryBean bean = new CategoryBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setCategory(DataUtility.getString(request.getParameter("search")));
		bean.setMarketPlaceId(DataUtility.getInt(request.getParameter("marketPlaceId")));
		populateDTO(bean, request);
		log.debug("ManageCategoryCtl Method populateBean Ended");
		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ManageCategoryCtl Method doGet Started");
		System.out.println("manageCategoryCtl do get..........");
		List list = null;
		CategoryBean bean = (CategoryBean) populateBean(request);
		UserModel model = new UserModel();
		try {
			list = model.searchCategory(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("ManageCategoryCtl Method doGet Ended");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ManageCategoryCtl Method doPost Started");
		CategoryBean bean = (CategoryBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));
		System.out.println(search + " ..search ");
		System.out.println(op + ".. this is operation");
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("manageCategoryCtl do post..");
		if (OP_ADD.equalsIgnoreCase(op)) {
			try {
				if (id > 0) {
					if (OP_EDIT.equalsIgnoreCase(op)) {
						System.out.println(id + " ------------------------Edit-----------------");
						try {
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
					} else {
						System.out.println("ADDD ho rha........." + bean.getCategory());
						long pk = 0;
						pk = model.addCategory(bean);
						bean.setId(pk);
						ServletUtility.setSuccessMessage("Category added successfully", request);
					}
				}
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setErrorMessage(e.getMessage(), request);

			}
			ServletUtility.forward(ORSView.MANAGE_CATEGORY_LIST_CTL, request, response);
			return;
		}

		else if (OP_SEARCH.equalsIgnoreCase(op)) {
			try {
				System.out.println("operation....." + op);
				System.out.println("cat....." + bean.getCategory());
				List list = null;
				list = model.searchCategory(bean, 0, 0);
				ServletUtility.setList(list, request);
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
				
				ServletUtility.setBean(bean, request);
				ServletUtility.setList(list, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
			ServletUtility.forward(getView(), request, response);
		}
		log.debug("ManageCategoryCtl Method doPost Started");
	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_CATEGORY_VIEW;
	}

}

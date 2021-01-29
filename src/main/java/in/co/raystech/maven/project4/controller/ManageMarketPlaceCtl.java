package in.co.raystech.maven.project4.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sun.org.apache.xpath.internal.operations.Mod;

import in.co.raystech.maven.project4.bean.BaseBean;
import in.co.raystech.maven.project4.bean.CategoryBean;
import in.co.raystech.maven.project4.bean.ProductsBean;
import in.co.raystech.maven.project4.exception.ApplicationException;
import in.co.raystech.maven.project4.exception.DuplicateRecordException;
import in.co.raystech.maven.project4.model.UserModel;
import in.co.raystech.maven.project4.util.DataUtility;
import in.co.raystech.maven.project4.util.PropertyReader;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * Servlet implementation class ManageCategoryCtl
 */

@WebServlet(name = "ManageMarketPlaceCtl", urlPatterns = { "/ctl/ManageMarketPlaceCtl" })
public class ManageMarketPlaceCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ManageMarketPlaceCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("ManageMarketPlaceCtl Method populateBean Started");
		ProductsBean bean = new ProductsBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setProductName(DataUtility.getString(request.getParameter("productName")));
		populateDTO(bean, request);
		log.debug("ManageMarketPlaceCtl Method populateBean ended");
		return bean;
	}

	@Override
	protected void preload(HttpServletRequest request) {
		UserModel model = new UserModel();
		log.debug("ManageMarketPlaceCtl Method preload Started");
		try {
			int listSize = model.list().size();
			int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
			int buttonNumber = listSize / pageSize;
			if (listSize % pageSize != 0) {
				buttonNumber++;
			}
			request.setAttribute("buttonNumber", buttonNumber);
			List categoryList = model.categoryList(0, 0);
			List highlitedCategories = model.categoryListHighlightOMarketPlace(0, 0);
			request.setAttribute("highlitedCategories", highlitedCategories);
			request.setAttribute("categoryList", categoryList);
		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
		}
		log.debug("ManageMarketPlaceCtl Method preload Ended");

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ManageMarketPlaceCtl Method doGet Started");
		System.out.println("manageCategoryCtl do get........++++..");
		List list = null;
		ProductsBean bean = (ProductsBean) populateBean(request);
		UserModel model = new UserModel();
		try {
			list = model.searchProducts(bean, 0, 0);
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
		log.debug("ManageMarketPlaceCtl Method doGet Ended");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ManageMarketPlaceCtl Method doPost Started");
		System.out.println("in post..");
		long catID = DataUtility.getLong(request.getParameter("categoryId"));
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));
		ProductsBean bean = (ProductsBean) populateBean(request);

		/*
		 * searching by category selection
		 */

		UserModel model = new UserModel();
		if (catID != 0) {
			try {
				List list = null;
				list = model.findProductsByCategoryFK(catID);
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
		}

		/*
		 * searching by input
		 */
		if (bean.getProductName() != null && catID == 0) {
			try {
				List list = null;
				bean.setProductName(bean.getProductName());
				list = model.searchSpecificProducts(bean, 0, 0);
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

		}

		if (op == null && search == null && bean.getProductName() == null && catID == 0) {

			System.out.println("manage partners searcmmmmmmmh....." + search);
			List list = null;
			try {
				list = model.searchProducts(bean, 0, 0);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setBeanP(bean, request);
			ServletUtility.setList(list, request);
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("ManageMarketPlaceCtl Method doPost ended");
		return;
	}

	@Override
	protected String getView() {
		return ORSView.MARKET_PLACE_VIEW;
	}

}

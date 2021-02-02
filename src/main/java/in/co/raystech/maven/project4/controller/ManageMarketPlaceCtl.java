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

@WebServlet(name = "ManageMarketPlaceCtl", urlPatterns = { "/OnePartner/ctl/ManageMarketPlaceCtl" })
public class ManageMarketPlaceCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ManageMarketPlaceCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("ManageMarketPlaceCtl Method populateBean Started");
		ProductsBean bean = new ProductsBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setProductName(DataUtility.getString(request.getParameter("productName")));
		populateDTO(bean, request);
		log.info("ManageMarketPlaceCtl Method populateBean ended");
		return bean;
	}

	@Override
	protected void preload(HttpServletRequest request) {
		UserModel model = new UserModel();
		log.info("ManageMarketPlaceCtl Method preload Started");
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
		log.info("ManageMarketPlaceCtl Method preload Ended");

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ManageMarketPlaceCtl Method doGet Started");
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
		log.info("ManageMarketPlaceCtl Method doGet Ended");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ManageMarketPlaceCtl Method doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));
		ProductsBean bean = (ProductsBean) populateBean(request);
		String[] ids = request.getParameterValues("ids");

		UserModel model = new UserModel();

		if (ids != null && ids.length > 0) {
			try {
				List<ProductsBean> list = null;
				list = UserModel.findProductsByCategoryFK(ids);
				ServletUtility.setList(list, request);
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
				ServletUtility.setBeanP(bean, request);
				ServletUtility.setList(list, request);

				System.out.println("prodnameprodnameprodnameprodnameprodname");
				Iterator<ProductsBean> it = list.iterator();
				while (it.hasNext()) {
					bean = it.next();
					System.out.println(bean.getProductName());
				}
				System.out.println("prodnameprodnameprodnameprodnameprodname");
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

			ServletUtility.forward(getView(), request, response);
			return;

		}

		/*
		 * searching by input
		 */
		if (bean.getProductName() != null && ids == null) {
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
			ServletUtility.forward(getView(), request, response);
			return;
		}

		if (op == null && search == null && bean.getProductName() == null) {

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
		log.info("ManageMarketPlaceCtl Method doPost ended");
		return;
	}

	@Override
	protected String getView() {
		return ORSView.MARKET_PLACE_VIEW;
	}

}

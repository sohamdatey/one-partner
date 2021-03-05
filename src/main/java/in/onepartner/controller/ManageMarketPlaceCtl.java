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
import in.onepartner.bean.ProductsBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.DataValidator;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.ServletUtility;

/**
 * Servlet implementation class ManageCategoryCtl
 */

@WebServlet(name = "ManageMarketPlaceCtl", urlPatterns = { "/OnePartner/ctl/ManageMarketPlaceCtl" })
public class ManageMarketPlaceCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ManageMarketPlaceCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.info("ManageMarketPlaceCtl Method validat Started");
		boolean pass = true;

		if (DataValidator.isNull(request.getParameterValues("ids"))) {
			request.setAttribute("ids", PropertyReader.getValue("error.require", "Category"));
			List<ProductsBean> list = null;
			ProductsBean bean = (ProductsBean) populateBean(request);
			UserModel model = new UserModel();
			try {
				list = model.searchProducts(bean, 0, 0);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
			ServletUtility.setList(list, request);
			pass = false;
		}
		log.info("ManageMarketPlaceCtl Method validat ended");
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("ManageMarketPlaceCtl Method populateBean Started");
		ProductsBean bean = new ProductsBean();
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
			List<CategoryBean> categoryList = model.categoryList(0, 0);
			List<CategoryBean> highlitedCategories = model.categoryListHighlightOMarketPlace(0, 0);
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
		List<ProductsBean> list = null;
		ProductsBean bean = (ProductsBean) populateBean(request);
		UserModel model = new UserModel();
		try {
			list = model.searchProducts(bean, 0, 0);
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
		ProductsBean bean = (ProductsBean) populateBean(request);
		String[] ids = request.getParameterValues("ids");
		UserModel model = new UserModel();
		if (ids.length >= 0 && op.equalsIgnoreCase(BaseCtl.OP_APPLY) && bean.getProductName() == null) {
			try {
				List<ProductsBean> list = null;
				list = UserModel.findProductsByCategoryFK(ids);
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

		/*
		 * searching by input
		 */
		if (bean.getProductName() != null) {

			try {
				List<ProductsBean> list = null;
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
			ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
		List<ProductsBean> list = null;
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
		ServletUtility.forward(getView(), request, response);
		log.info("ManageMarketPlaceCtl Method doPost ended");
		return;
	}

	@Override
	protected String getView() {
		return ORSView.MARKET_PLACE_VIEW;
	}

}

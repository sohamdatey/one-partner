package in.co.raystech.maven.project4.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Servlet implementation class ManageProductCtl
 */

@WebServlet(name = "ManageProductCtl", urlPatterns = { "/ctl/ManageProductCtl" })
public class ManageProductCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		ProductsBean bean = new ProductsBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setProductName(DataUtility.getString(request.getParameter("productName")));

		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		bean.setPartnershipOffer(DataUtility.getString(request.getParameter("partnershipOffer")));

		bean.setFormLink(DataUtility.getString(request.getParameter("formLink")));

		populateDTO(bean, request);

		return bean;
	}

	@Override
	protected void preload(HttpServletRequest request) {
		UserModel model = new UserModel();

		System.out.println("pre loadddddddddddddddddddddddddddddddddddddddddddddddddddddd manage Products");

		try {
			int listSize = model.list().size();
			int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
			int buttonNumber = listSize / pageSize;
			if (listSize % pageSize != 0) {
				buttonNumber++;
			}
			request.setAttribute("buttonNumber", buttonNumber);

			List categoryList = model.categoryList(0, 0);
			System.out.println("in pre load..................");
			CategoryBean bean = new CategoryBean();
			Iterator<CategoryBean> it = categoryList.iterator();
			while (it.hasNext()) {
				bean = it.next();
				System.out.println(bean.getId());

			}
			System.out.println("in pre load..................");

			request.setAttribute("categoryList", categoryList);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("manageProductCtl do get..........");

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
			ServletUtility.handleException(e, request, response);
			return;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ProductsBean bean = (ProductsBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));

		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_ADD.equalsIgnoreCase(op)) {
			try {
				if (id > 0) {
					if (OP_EDIT.equalsIgnoreCase(op)) {
						System.out.println(id + " ------------------------Edit-----------------");
						try {
							model = new UserModel();
							model.updateProducts(bean);
							ServletUtility.setBean(bean, request);
							ServletUtility.setSuccessMessage("product is  successfully Updated", request);
						} catch (ApplicationException e) {
							e.printStackTrace();
						} catch (DuplicateRecordException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("ADDD ho rha........." + bean.getProductName());

						long pk = 0;
						pk = model.addProducts(bean);
						bean.setId(pk);
						ServletUtility.setSuccessMessage("Category added successfully", request);
					}
				}

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage(e.getMessage(), request);

			}
			ServletUtility.forward(ORSView.MANAGE_PRODUCT_CTL, request, response);
			return;
		}

		else if (search != null) {
			System.out.println("opsearchhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhiiiiiiingggggggg");
			try {
				System.out.println("pro....." + search);
				List list = null;
				bean.setProductName(search);
				list = model.searchSpecificProducts(bean, 0, 0);
				ServletUtility.setList(list, request);
				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
				ServletUtility.setBeanP(bean, request);
				ServletUtility.setList(list, request);

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
			ServletUtility.forward(getView(), request, response);
		}

	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_PRODUCTS_VIEW;
	}

}

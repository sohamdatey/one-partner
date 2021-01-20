package in.co.raystech.maven.project4.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import in.co.raystech.maven.project4.bean.BaseBean;
import in.co.raystech.maven.project4.bean.CategoryBean;
import in.co.raystech.maven.project4.bean.ProductsBean;
import in.co.raystech.maven.project4.bean.UserBean;
import in.co.raystech.maven.project4.exception.ApplicationException;
import in.co.raystech.maven.project4.exception.DatabaseException;
import in.co.raystech.maven.project4.exception.DuplicateRecordException;
import in.co.raystech.maven.project4.exception.ImageSaveException;
import in.co.raystech.maven.project4.model.UserModel;
import in.co.raystech.maven.project4.util.DataUtility;
import in.co.raystech.maven.project4.util.PropertyReader;
import in.co.raystech.maven.project4.util.ServletUtility;

/**
 * Servlet implementation class ManageProductsCtl
 */

@WebServlet(name = "ManageProductsCtl", urlPatterns = { "/ctl/ManageProductsCtl" })
@MultipartConfig
public class ManageProductsCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		ProductsBean bean = new ProductsBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setProductName(DataUtility.getString(request.getParameter("productName")));

		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		bean.setCatId(DataUtility.getLong(request.getParameter("categoryId")));

		bean.setPartnershipOffer(DataUtility.getString(request.getParameter("partnershipOffer")));

		bean.setFormLink(DataUtility.getString(request.getParameter("formLink")));

		populateDTO(bean, request);

		return bean;
	}

	@Override
	protected void preload(HttpServletRequest request) {
		UserModel model = new UserModel();

		try {
			int listSize = model.list().size();
			int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
			int buttonNumber = listSize / pageSize;
			if (listSize % pageSize != 0) {
				buttonNumber++;
			}
			request.setAttribute("buttonNumber", buttonNumber);

			List categoryList = model.categoryList(0, 0);
			CategoryBean categoryBean = new CategoryBean();
			request.setAttribute("categoryList", categoryList);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		String op = DataUtility.getString(request.getParameter("operation"));

		try {
			List list = null;
			ProductsBean bean = new ProductsBean();
			list = model.searchProducts(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
		} catch (ApplicationException e1) {
			e1.printStackTrace();
		}

		if (id > 0) {

			ProductsBean productsBean;
			try {
				productsBean = model.findByPKProducts(id);
				System.out.println(productsBean.getId());
				System.out.println(productsBean.getProductName());
				List list = model.createCategoryBeans(productsBean.getId());

				request.setAttribute("listOfCats", list);
				ServletUtility.setBean(productsBean, request);
				request.setAttribute("productBeanAttr", productsBean);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}

		}

		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ProductsBean bean = (ProductsBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));
		Part filePart = null;
		filePart = request.getPart("file");
		UserModel model = new UserModel();

		if (search == null) {
			long id = DataUtility.getLong(request.getParameter("id"));
			System.out.println("manageProductsCtl do post..idddddddddddd   " + id);

			if (OP_DELETE.equalsIgnoreCase(op)) {
				try {
					bean.setId(id);
					model.deleteProduct(bean);

				} catch (ApplicationException e) {
					e.printStackTrace();
				}
				ServletUtility.redirect(ORSView.MANAGE_PRODUCTS_CTL, request, response);
				return;

			}
			if (OP_ADD.equalsIgnoreCase(op) || OP_EDIT.equalsIgnoreCase(op)) {
				try {
					long pk = 0;

					if (filePart != null && OP_ADD.equalsIgnoreCase(op)) {

						String[] ids = request.getParameterValues("categoryId");

						// saveimagehere
						bean.setImage("..\\productImages\\" + String.valueOf(UserModel.nextProductPK()) + ".jpg");
						pk = model.addProducts(bean);

						for (String idd : ids) {
							System.out.println(idd + "ididididid");
							model.addProductCategory(idd, pk);

						}
						try {

							DataUtility.saveImage(BaseCtl.FILE_LOCATION, filePart, String.valueOf(pk) + ".jpg");
						} catch (ImageSaveException e) {
							ServletUtility.handleException(e, request, response);
						}
						bean.setId(pk);
						ServletUtility.setSuccessMessage("Category added successfully", request);
						ServletUtility.redirect(ORSView.MANAGE_PRODUCTS_CTL, request, response);
						return;

					}
					if (id > 0) {
						if (OP_EDIT.equalsIgnoreCase(op)) {
							try {
								pk = UserModel.nextProductPK();
								model = new UserModel();
								bean.setImage("..\\productImages\\" + String.valueOf(bean.getId()) + ".jpg");
								model.updateProducts(bean);
								try {

									DataUtility.saveImage(BaseCtl.FILE_LOCATION, filePart,
											String.valueOf(bean.getId()) + ".jpg");
								} catch (ImageSaveException e) {
									ServletUtility.handleException(e, request, response);
								}
								ServletUtility.setBean(bean, request);
								ServletUtility.setSuccessMessage("product is  successfully Updated", request);
							} catch (ApplicationException e) {
								e.printStackTrace();
							} catch (DuplicateRecordException e) {
								e.printStackTrace();
							}
						}
						ServletUtility.redirect(ORSView.MANAGE_PRODUCTS_CTL, request, response);
						return;
					}

				} catch (ApplicationException e) {
					ServletUtility.handleException(e, request, response);
					return;
				} catch (DuplicateRecordException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage(e.getMessage(), request);

				} catch (DatabaseException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage(e.getMessage(), request);
				}
				ServletUtility.forward(ORSView.MARKET_PLACE_CTL, request, response);
				return;
			}
		}

		if (bean.getProductName() != null) {
			try {
				System.out.println("search op....." + search);
				System.out.println("name....." + bean.getProductName() + "  ............");

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
				ServletUtility.handleException(e, request, response);
				return;
			}

		}
		List list = null;
		try {
			list = model.searchProducts(bean, 0, 0);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		ServletUtility.setList(list, request);
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_PRODUCTS_VIEW;
	}

}

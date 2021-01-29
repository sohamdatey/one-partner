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

import org.apache.log4j.Logger;

import in.co.raystech.maven.project4.bean.BaseBean;
import in.co.raystech.maven.project4.bean.CategoryBean;
import in.co.raystech.maven.project4.bean.ProductsBean;
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
	private static Logger log = Logger.getLogger(ManageProductsCtl.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("ManageProductsCtl Method populateBean Started");
		ProductsBean bean = new ProductsBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setProductName(DataUtility.getString(request.getParameter("productName")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setCatId(DataUtility.getLong(request.getParameter("categoryId")));
		bean.setPartnershipOffer(DataUtility.getString(request.getParameter("partnershipOffer")));
		bean.setFormLink(DataUtility.getString(request.getParameter("formLink")));
		populateDTO(bean, request);
		log.debug("ManageProductsCtl Method populateBean ended");
		return bean;
	}

	@Override
	protected void preload(HttpServletRequest request) {

		log.debug("ManageProductsCtl Method preload Started");
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
			log.error(e);
			e.printStackTrace();
		}
		log.debug("ManageProductsCtl Method populateBean ended");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ManageProductsCtl Method doget Started");
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
		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
		}
		System.out.println("into the get id  list ------" + id);

		if (id > 0) {
			ProductsBean productsBean;
			try {
				productsBean = model.findByPKProducts(id);
				System.out.println(productsBean.getId());
				System.out.println(productsBean.getProductName());
				List<CategoryBean> list = model.createCategoryBeans(productsBean.getId());
				List<CategoryBean> list2 = model.categoryList();
				request.setAttribute("listOfCats", list);
				request.setAttribute("allCategoriesList", list2);
				CategoryBean catBean = new CategoryBean();
				Iterator<CategoryBean> it = list.iterator();
				int size = list.size();
				while (it.hasNext()) {
					catBean = it.next();
					System.out.println(catBean.getCategory());
				}
				System.out.println("-------------------------------");
				Iterator<CategoryBean> it2 = list2.iterator();
				int size2 = list2.size();
				while (it.hasNext()) {
					catBean = it.next();
					System.out.println(catBean.getCategory());

				}

				ServletUtility.setBean(productsBean, request);
				request.setAttribute("productBeanAttr", productsBean);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		}

		ServletUtility.forward(getView(), request, response);

		log.debug("ManageProductsCtl Method doGet Ended");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("ManageProductsCtl Method dopost Started");
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
				bean.setId(id);
				try {
					model.deleteProduct(bean);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServletUtility.redirect(ORSView.MANAGE_PRODUCTS_CTL, request, response);
				return;
			}
			if (OP_ADD.equalsIgnoreCase(op) || OP_EDIT.equalsIgnoreCase(op)) {
				long pk = 0;
				if (filePart != null && OP_ADD.equalsIgnoreCase(op)) {
					String[] ids = request.getParameterValues("categoryId");
					// saveimagehere
					try {
						bean.setImage("..\\productImages\\" + String.valueOf(UserModel.nextProductPK()) + ".jpg");
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						pk = model.addProducts(bean);
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DuplicateRecordException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (String idd : ids) {
						System.out.println(idd + "ididididid");
						try {
							model.addProductCategory(idd, pk);
						} catch (ApplicationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (DuplicateRecordException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						DataUtility.saveImage(BaseCtl.FILE_LOCATION, filePart, String.valueOf(pk) + ".jpg");
					} catch (ImageSaveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
						} catch (DatabaseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						model = new UserModel();
						bean.setImage("..\\productImages\\" + String.valueOf(bean.getId()) + ".jpg");
						try {
							model.updateProducts(bean);
						} catch (ApplicationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (DuplicateRecordException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							DataUtility.saveImage(BaseCtl.FILE_LOCATION, filePart,
									String.valueOf(bean.getId()) + ".jpg");
						} catch (ImageSaveException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						ServletUtility.setBean(bean, request);
						ServletUtility.setSuccessMessage("product is  successfully Updated", request);

					}
					ServletUtility.redirect(ORSView.MANAGE_PRODUCTS_CTL, request, response);
					return;
				}

			}
		}

		List list = null;
		try {
			list = model.searchSpecificProducts(bean, 0, 0);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServletUtility.setList(list, request);
		if (list == null || list.size() == 0) {
			ServletUtility.setErrorMessage("No record found ", request);
		}
		ServletUtility.setBeanP(bean, request);
		ServletUtility.setList(list, request);
		ServletUtility.setList(list, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("ManageProductsCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_PRODUCTS_VIEW;
	}

}

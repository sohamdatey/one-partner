package in.onepartner.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import in.onepartner.bean.BaseBean;
import in.onepartner.bean.CategoryBean;
import in.onepartner.bean.ProductsBean;
import in.onepartner.exception.ApplicationException;
import in.onepartner.exception.DatabaseException;
import in.onepartner.exception.DuplicateRecordException;
import in.onepartner.exception.ImageSaveException;
import in.onepartner.model.UserModel;
import in.onepartner.util.DataUtility;
import in.onepartner.util.PropertyReader;
import in.onepartner.util.S3Handler;
import in.onepartner.util.ServletUtility;

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
		log.info("ManageProductsCtl Method populateBean Started");
		ProductsBean bean = new ProductsBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setProductName(DataUtility.getString(request.getParameter("productName")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setCatId(DataUtility.getLong(request.getParameter("categoryId")));
		bean.setPartnershipOffer(DataUtility.getString(request.getParameter("partnershipOffer")));
		bean.setFormLink(DataUtility.getString(request.getParameter("formLink")));
		populateDTO(bean, request);
		log.info("ManageProductsCtl Method populateBean ended");
		return bean;
	}

	@Override
	protected void preload(HttpServletRequest request) {

		log.info("ManageProductsCtl Method preload Started");
		UserModel model = new UserModel();

		try {
			int listSize = model.list().size();
			int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
			int buttonNumber = listSize / pageSize;
			if (listSize % pageSize != 0) {
				buttonNumber++;
			}
			request.setAttribute("buttonNumber", buttonNumber);
			List<CategoryBean> categoryList = model.categoryList(0, 0);
			request.setAttribute("categoryList", categoryList);
		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
		}
		log.info("ManageProductsCtl Method populateBean ended");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("ManageProductsCtl Method doget Started");
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		String op = DataUtility.getString(request.getParameter("operation"));
		try {
			List<ProductsBean> list = null;
			ProductsBean bean = new ProductsBean();
			list = model.searchProducts(bean, 0, 0);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
		} catch (ApplicationException e) {
			log.error(e);
		}

		if (id > 0) {
			ProductsBean productsBean;
			try {
				productsBean = model.findByPKProducts(id);
				List<CategoryBean> list = UserModel.createCategoryBeans(productsBean.getId());
				List<CategoryBean> list2 = model.categoryList();

				request.setAttribute("listOfCats", list);
				request.setAttribute("allCategoriesList", list2);
				ServletUtility.setBean(productsBean, request);
				request.setAttribute("productBeanAttr", productsBean);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		}

		ServletUtility.forward(getView(), request, response);

		log.info("ManageProductsCtl Method doGet Ended");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("ManageProductsCtl Method dopost Started");
		ProductsBean bean = (ProductsBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("productName"));
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_DELETE.equalsIgnoreCase(op)) {
			bean.setId(id);
			try {
				bean = model.findByPKProducts(bean.getId());
				S3Handler.deleteImage(bean.getImageId());
				model.deleteProduct(bean);
				UserModel.deleteAllCategoriesFromProduct_CaterogyTable(bean.getId());
				ServletUtility.setSuccessMessage("product deleted successfully", request);
			} catch (ApplicationException e) {
				log.error(e);
			}
		}
		if (OP_ADD.equalsIgnoreCase(op)) {

			long pk = 0;
			Part filePart = null;
			filePart = request.getPart("fileAdd");
			String extension = FilenameUtils.getExtension(ManageProductsCtl.getFileName(filePart));
			System.out.println("File Extension " + extension);

			if (filePart != null && OP_ADD.equalsIgnoreCase(op)) {

				try {
					String[] ids = request.getParameterValues("categoryId");
					bean.setImageId(String.valueOf(UserModel.nextProductPK()) + "." + extension);
					bean.setImageURL(S3Handler.getUrl(bean.getImageId()));
					pk = addProducts(request, response, bean, model, pk);
					addProductCategories(model, pk, ids);
					saveImage(bean, filePart);
					updateProduct(bean, model);
					bean.setId(pk);
					ServletUtility.setSuccessMessage("Product added successfully", request);
				} catch (ImageSaveException e) {
					log.error(e);
				} catch (DatabaseException e) {
					log.error(e);
				}
			}
		}

		if (id > 0) {
			if (OP_EDIT.equalsIgnoreCase(op)) {
				try {
					Part filePartEdit = null;
					filePartEdit = request.getPart("fileEdit");
					String extensionEdit = FilenameUtils.getExtension(ManageProductsCtl.getFileName(filePartEdit));
					System.out.println("File Extension " + extensionEdit);
					ProductsBean prodBean = (ProductsBean) populateBean(request);
					model = new UserModel();
					prodBean = model.findByPKProducts(bean.getId());
					S3Handler.deleteImage(prodBean.getImageId());
					String[] ids = request.getParameterValues("categoryId");
					log.info("lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
					log.info(extensionEdit);
					log.info(extensionEdit);
					log.info(extensionEdit);
					log.info(extensionEdit);
					if (extensionEdit.length() > 2) {
						bean.setImageId(String.valueOf(bean.getId() + "." + extensionEdit));
						bean.setImageURL(S3Handler.getUrl(bean.getImageId()));
					} else if (extensionEdit.length() < 2) {
						bean.setImageId(prodBean.getImageId());
						bean.setImageURL(prodBean.getImageURL());
					}
					UserModel.deleteAllCategoriesFromProduct_CaterogyTable(bean.getId());
					addProductCategories(model, bean.getId(), ids);
					saveImage(bean, filePartEdit);
					updateProduct(bean, model);
					ServletUtility.setSuccessMessage("product is successfully Updated", request);
				} catch (ImageSaveException e) {
					log.error(e);
					e.printStackTrace();
				} catch (ApplicationException e) {
					log.error(e);
					e.printStackTrace();
				}
			}
		}

		if (OP_SEARCH.equalsIgnoreCase(op))

		{

			List<ProductsBean> list = null;
			try {
				list = model.searchSpecificProducts(bean, 0, 0);
			} catch (ApplicationException e) {
				log.error(e);
			}
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
			return;
		}

		List<ProductsBean> list = null;
		try {
			list = model.searchAllProducts(bean, 0, 0);
		} catch (ApplicationException e) {
			log.error(e);
		}
		if (list == null || list.size() == 0) {
			ServletUtility.setErrorMessage("No record found ", request);
		}

		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setBeanP(bean, request);
		ServletUtility.forward(getView(), request, response);
		return;

	}

	private long addProducts(HttpServletRequest request, HttpServletResponse response, ProductsBean bean,
			UserModel model, long pk) throws IOException, ServletException {
		try {
			pk = model.addProducts(bean);
		} catch (ApplicationException e) {
			log.error(e);
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage(e.getMessage(), request);
			ServletUtility.forward(ORSView.MANAGE_PRODUCTS_CTL, request, response);
			e.printStackTrace();
		}
		return pk;
	}

	private void addProductCategories(UserModel model, long pk, String[] ids) {
		for (String idd : ids) {
			try {
				model.addProductCategory(idd, pk);
			} catch (ApplicationException e) {
				log.error(e);
			} catch (DuplicateRecordException e) {
				log.error(e);

			}
		}
	}

	private void saveImage(ProductsBean bean, Part filePart) throws IOException, ImageSaveException {
		S3Handler.uploadProductImage(filePart.getInputStream(), bean.getImageId());
	}

	private void updateProduct(ProductsBean bean, UserModel model) {
		try {
			model.updateProducts(bean);
		} catch (ApplicationException e) {
			log.error(e);
		} catch (DuplicateRecordException e) {
			log.error(e);
		}
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// Create path components to save the file
		final String path = request.getParameter("destination");
		final Part filePart = request.getPart("file");
		final String fileName = getFileName(filePart);

		OutputStream out = null;
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();

		try {
			out = new FileOutputStream(new File(path + File.separator + fileName));
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			writer.println("New file " + fileName + " created at " + path);

		} catch (FileNotFoundException fne) {
			writer.println("You either did not specify a file to upload or are "
					+ "trying to upload a file to a protected or nonexistent " + "location.");
			writer.println("<br/> ERROR: " + fne.getMessage());

		} finally {
			if (out != null) {
				out.close();
			}
			if (filecontent != null) {
				filecontent.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	private static String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : part.getHeader("content-disposition").split(";")) {

			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");

			}
		}
		return partHeader;

	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_PRODUCTS_VIEW;
	}

}

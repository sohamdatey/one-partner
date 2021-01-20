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

@WebServlet(name = "AddCategoryCtl", urlPatterns = { "/ctl/AddCategoryCtl" })
public class AddCategoryCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		CategoryBean bean = new CategoryBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setCategory(DataUtility.getString(request.getParameter("category")));

		populateDTO(bean, request);

		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
			e1.printStackTrace();
		}

		if (id > 0) {

			CategoryBean catBean;
			try {
				catBean = model.findByPKCategory(id);
				System.out.println(catBean.getId());
				System.out.println(catBean.getCategory());
				request.setAttribute("catBean", catBean);

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}

			ServletUtility.forward(getView(), request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CategoryBean bean = (CategoryBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String search = DataUtility.getString(request.getParameter("search"));
		UserModel model = new UserModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println(id + "do pppppppppppppppppppppppppppppppppppppppppplllll" + op);

		if (OP_ADD.equalsIgnoreCase(op) || OP_EDIT.equalsIgnoreCase(op)) {
			System.out.println("in do post Add category/////+ id........" + id);

			try {
				if (id > 0) {
					if (OP_EDIT.equalsIgnoreCase(op)) {
						System.out.println("------------------------Edit-----------------");
						try {
							model = new UserModel();
							model.updateCategory(bean);
							ServletUtility.setBean(bean, request);
							ServletUtility.setSuccessMessage("Category is successfully Updated", request);
						} catch (ApplicationException e) {
							e.printStackTrace();
						} catch (DuplicateRecordException e) {
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
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage(e.getMessage(), request);

			}

		}

		if (OP_DELETE.equalsIgnoreCase(op)) {

			System.out.println(op + "op del////////////");
			CategoryBean deletebean = new CategoryBean();
			deletebean.setId(id);
			try {
				model.deleteCategory(deletebean);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ServletUtility.forward(ORSView.MANAGE_CATEGORY_LISTT_CTL, request, response);
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
			ServletUtility.handleException(e, request, response);
			return;
		}

	}

	@Override
	protected String getView() {
		return ORSView.MANAGE_CATEGORY_VIEW;
	}

}

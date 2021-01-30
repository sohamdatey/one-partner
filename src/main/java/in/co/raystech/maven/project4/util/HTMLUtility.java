package in.co.raystech.maven.project4.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.ListUI;

import in.co.raystech.maven.project4.bean.CategoryBean;
import in.co.raystech.maven.project4.bean.DropdownListBean;
import in.co.raystech.maven.project4.bean.ProductsBean;
import in.co.raystech.maven.project4.model.BaseModel;

/**
 * HTML Utility class to produce HTML contents like Dropdown List.
 * 
 * @author Front Controller
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class HTMLUtility {

	/**
	 * Create HTML SELECT list from MAP parameters values
	 * 
	 * @param name
	 * @param selectedVal
	 * @param map
	 * @return
	 */

	public static String getList(String name, String selectedVal, HashMap<String, String> map) {

		StringBuffer sb = new StringBuffer("<select class='form-control' name='" + name + "'>");

		sb.append("<option disabled selected value=''>------select------</option>");

		Set<String> keys = map.keySet();
		String val = null;

		for (String key : keys) {
			val = map.get(key);

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key + "'>" + val + "</option>");
			} else {
				sb.append("<option value='" + key + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * Create HTML SELECT List from List parameter
	 * 
	 * @param name
	 * @param selectedVal
	 * @param list
	 * @return
	 */
	public static String getList(String name, String selectedVal, List list) {

		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		StringBuffer sb = new StringBuffer(
				"<select class='form-control'  onchange=\"if (this.value=='1'){this.form['admin'].style.visibility='visible'}else {this.form['admin'].style.visibility='hidden'};\"    name='"
						+ name + "'>");

		sb.append("<option selected value=''>------select------</option>");
		String key = null;
		String val = null;

		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key + "'>" + val + "</option>");
			} else {
				sb.append("<option value='" + key + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	public static String getCategoryList(String name, String selectedVal, List list) {
		System.out.println("categoryList.................");

		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		StringBuffer sb = new StringBuffer("");

//		sb.append("<a class='hilit'><label><input type='checkbox' oncheck='this.form.submit()' name='" + name
//				+ "'><span> " + name + " </span></label></a>");
		String key = null;
		String val = null;

		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();
			System.out.println("key-------------------++" + key);
			System.out.println("selected value---------------------" + selectedVal);

			if (key.trim().equals(selectedVal)) {

				sb.append("<a class='hilit'><label><input type='checkbox' checked id='check" + val + "' value='"
						+ key.trim() + "' onclick='this.form.submit()' name='" + name + "'><span> " + val
						+ " </span></label></a>");
			} else {
				sb.append("<a class='hilit'><label><input type='checkbox'   value='" + key.trim()
						+ "' onclick='this.form.submit()' name='" + name + "'><span> " + val + " </span></label></a>");
			}
		}
		return sb.toString();
	}

	public static String abhikimethod(String name, String selectedVal, List list) {
		System.out.println("categoryList.................");

		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		StringBuffer sb = new StringBuffer("");

//		sb.append("<a class='hilit'><label><input type='checkbox' oncheck='this.form.submit()' name='" + name
//				+ "'><span> " + name + " </span></label></a>");
		String key = null;
		String val = null;

		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();
			System.out.println("key-------------------++" + key);
			System.out.println("selected value---------------------" + selectedVal);

			if (key.trim().equals(selectedVal)) {

				sb.append("<a class='hilit'><label><input type='checkbox' checked id='check" + val + "' value='"
						+ key.trim() + "' name='" + name + "'><span> " + val + " </span></label></a>");
			} else {
				sb.append("<a class='hilit'><label><input type='checkbox'   value='" + key.trim() + "' name='" + name
						+ "'><span> " + val + " </span></label></a>");
			}
		}
		return sb.toString();
	}

	public static String getCategoryListForProducts(String name, String selectedVal, List list) {

		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		StringBuffer sb = new StringBuffer(
				"<select id='select1' class='form-control' multiple onchange=\"if (this.value=='1'){this.form['admin'].style.visibility='visible'}else {this.form['admin'].style.visibility='hidden'};\"    name='"
						+ name + "'>");

		String key = null;
		String val = null;

		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key.trim() + "'>" + val + "</option>");
			} else {
				sb.append("<option  value='" + key.trim() + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		sb.append("     ");
		return sb.toString();
	}

	/*
	 * this method is created for selecting all the added data previously as well as
	 * listing all the data
	 * 
	 */
	public static String methodForcheckUncheckAtMarketPlace(String name, String selectedVal, List<CategoryBean> list,
			List<CategoryBean> list2) {

		list2.removeAll(list);
		System.out.println(list2);
		System.out.println(list);

		StringBuffer sb = new StringBuffer(
				"<select id='select1' class='form-control' multiple onchange=\"if (this.value=='1'){this.form['admin'].style.visibility='visible'}else {this.form['admin'].style.visibility='hidden'};\"    name='"
						+ name + "'>");

		String key = null;
		String val = null;
		String key2 = null;
		String val2 = null;

		List newList = new ArrayList();
		newList.addAll(list);

		for (Object object : list2) {
			if (newList.contains(object)) {
			} else {
				newList.add(object);
			}
		}
		List<DropdownListBean> dd3 = (List<DropdownListBean>) newList;

		for (DropdownListBean obj : dd3) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {

				sb.append("<a class='hilit'><label><input type='checkbox' checked id='check" + val + "' value='"
						+ key.trim() + "' name='" + name + "'><span> " + val + " </span></label></a>");
			} else {
				sb.append("<a class='hilit'><label><input type='checkbox'   value='" + key.trim() + "' name='" + name
						+ "'><span> " + val + " </span></label></a>");
			}
		}
		return sb.toString();
	}

	public static String getCategoryListForProductsSelected(String name, String selectedVal, List<CategoryBean> list,
			List<CategoryBean> list2) {
		list2.removeAll(list);
		StringBuffer sb = new StringBuffer(
				"<select id='select1' class='form-control' multiple onchange=\"if (this.value=='1'){this.form['admin'].style.visibility='visible'}else {this.form['admin'].style.visibility='hidden'};\"    name='"
						+ name + "'>");

		String key = null;
		String val = null;
		String key2 = null;
		String val2 = null;

		List newList = new ArrayList();
		newList.addAll(list);

		for (Object object : list2) {
			if (newList.contains(object)) {
			} else {
				newList.add(object);
			}
		}
		List<DropdownListBean> dd3 = (List<DropdownListBean>) newList;

		for (DropdownListBean obj : list) {
			key = obj.getKey();
			val = obj.getValue();

			sb.append("<option selected value='" + key.trim() + "'>" + val + "</option>");
		}
		for (DropdownListBean obj2 : dd3) {
			key2 = obj2.getKey();
			val2 = obj2.getValue();
			sb.append("<option value='" + key2.trim() + "'>" + val2 + "</option>");
		}
		sb.append("</select>");
		sb.append("     ");
		return sb.toString();
	}

	public static String hilighterMethod(String name, String selectedVal, List<CategoryBean> list,
			List<CategoryBean> list2) {

		list2.removeAll(list);

		StringBuffer sb = new StringBuffer();

		String key = null;
		String val = null;
		String key2 = null;
		String val2 = null;

		List newList = new ArrayList();
		newList.addAll(list);

		for (Object object : list2) {
			if (newList.contains(object)) {
			} else {
				newList.add(object);
			}
		}
		List<DropdownListBean> dd3 = (List<DropdownListBean>) newList;

		for (DropdownListBean obj : list) {
			key = obj.getKey();
			val = obj.getValue();

			sb.append("<a class='hilit'><label><input type='checkbox' value='" + DataUtility.getStringData(key.trim())
					+ "'><span> " + DataUtility.getStringData(val) + " </span></label></a>");
		}
		for (DropdownListBean obj2 : dd3) {
			key2 = obj2.getKey();
			val2 = obj2.getValue();
			sb.append(
					"<a class='default'><label><input type='checkbox' value='" + DataUtility.getStringData(key2.trim())
							+ "'><span> " + DataUtility.getStringData(val2) + " </span></label></a>");

		}
		return sb.toString();
	}

	public static String getCategoryListOfCats(String name, String selectedVal, List list) {

		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		StringBuffer sb = new StringBuffer(
				"<select id='select1' class='form-control' multiple onchange=\"if (this.value=='1'){this.form['admin'].style.visibility='visible'}else {this.form['admin'].style.visibility='hidden'};\"    name='"
						+ name + "'>");

		String key = null;
		String val = null;

		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key.trim() + "'>" + val + "</option>");
			} else {
				sb.append("<option selected value='" + key.trim() + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		sb.append("     ");
		return sb.toString();
	}

	public static String getSelectedValue(String name, String selectedVal, List list) {

		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		String key = null;
		String val = null;
		String value = null;
		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				value = val;
			}
		}
		return value;
	}

	public static String getListFromAnotherTable(String name, String selectedVal, List list) {

		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		StringBuffer sb = new StringBuffer(
				"<select onchange='this.form.submit()' class='form-control' name='" + name + "'>");

		sb.append("<option selected value=''>------select------</option>");
		String key = null;
		String val = null;

		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key.trim() + "'>" + val + "</option>");
			} else {
				sb.append("<option value='" + key.trim() + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * Create HTML SELECT List from List parameter
	 * 
	 * @param name
	 * @param selectedVal
	 * @param list
	 * @return
	 */

	public static String getList(String name, String selectedVal, HashMap<String, String> map, boolean select) {

		StringBuffer sb = new StringBuffer("<select class='form-control' name='" + name + "'>");

		Set<String> keys = map.keySet();
		String val = null;

		if (select) {

			sb.append("<option selected value=''> ------Select------ </option>");
		}

		for (String key : keys) {
			val = map.get(key);
			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key + "'>" + val + "</option>");
			} else {
				sb.append("<option value='" + key + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	public static String getInputErrorMessages(HttpServletRequest request) {

		Enumeration<String> e = request.getAttributeNames();

		StringBuffer sb = new StringBuffer("<UL>");
		String name = null;

		while (e.hasMoreElements()) {
			name = e.nextElement();
			if (name.startsWith("error.")) {
				sb.append("<LI class='error'>" + request.getAttribute(name) + "</LI>");
			}
		}
		sb.append("</UL>");
		return sb.toString();
	}

	/**
	 * Returns Error Message with HTML tag and CSS
	 * 
	 * @param request
	 * @return
	 */
	public static String getErrorMessage(HttpServletRequest request) {
		String msg = ServletUtility.getErrorMessage(request);
		if (!DataValidator.isNull(msg)) {
			msg = "<p class='st-error-header'>" + msg + "</p>";
		}
		return msg;
	}

	/**
	 * Returns Success Message with HTML tag and CSS
	 * 
	 * @param request
	 * @return
	 */

	public static String getSuccessMessage(HttpServletRequest request) {
		String msg = ServletUtility.getSuccessMessage(request);
		if (!DataValidator.isNull(msg)) {
			msg = "<p class='st-success-header'>" + msg + "</p>";
		}
		return msg;
	}

	/**
	 * Creates submit button if user has access permission.
	 * 
	 * @param label
	 * @param access
	 * @param request
	 * @return
	 */
	public static String getSubmitButton(String label, boolean access, HttpServletRequest request) {

		String button = "";

		if (access) {
			button = "<input type='submit' name='operation'    value='" + label + "' >";
		}
		return button;
	}

	public static String getCommonFields(HttpServletRequest request) {

		BaseModel model = ServletUtility.getModel(request);

		StringBuffer sb = new StringBuffer();

		sb.append("<input type='hidden' name='id' value=" + model.getId() + ">");
		/*
		 * sb.append("<input type='hidden' name='createdBy' value=" +
		 * DataUtility.getString(model.getCreatedBy()) + ">"); sb.append(
		 * "<input type='hidden' name='modifiedBy' value=" +
		 * DataUtility.getString(model.getModifiedBy()) + ">"); sb.append(
		 * "<input type='hidden' name='createdDatetime' value=" +
		 * DataUtility.getTimestamp(model.getCreatedDatetime()) + ">");
		 * sb.append("<input type='hidden' name='modifiedDatetime' value=" +
		 * DataUtility.getTimestamp(model.getModifiedDatetime()) + ">");
		 */
		return sb.toString();
	}
}

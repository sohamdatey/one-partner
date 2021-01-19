package in.co.raystech.maven.project4.bean;

/**
 * DropdownListBean interface is implemented by Beans those are used to create
 * drop down list on HTML pages
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
public interface DropdownListBean {

	/**
	 * Returns key of list element
	 * 
	 * @return key
	 */
	public String getKey();

	/**
	 * Returns display text of list element
	 * 
	 * @return value
	 */
	public String getValue();

}
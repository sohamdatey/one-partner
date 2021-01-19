
package in.co.raystech.maven.project4.bean;

/**
 * Course JavaBean encapsulates Course attributes
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class CategoryBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String category;
	private int categoryId;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String getKey() {
		return id + " ";
	}

	@Override
	public String getValue() {

		return category + " ";

	}

}

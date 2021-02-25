
package in.onepartner.bean;

/**
 * Course JavaBean encapsulates Course attributes
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class CategoryBean extends BaseBean {

	public CategoryBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String category;
	private int categoryId;
	private int marketPlaceId;

	public int getMarketPlaceId() {
		return marketPlaceId;
	}

	public void setMarketPlaceId(int marketPlaceId) {
		this.marketPlaceId = marketPlaceId;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + categoryId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryBean other = (CategoryBean) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (categoryId != other.categoryId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CategoryBean [category=" + category + ", categoryId=" + categoryId + "]";
	}

}

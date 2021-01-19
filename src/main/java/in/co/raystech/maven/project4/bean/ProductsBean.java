
package in.co.raystech.maven.project4.bean;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.Part;

/**
 * Course JavaBean encapsulates Course attributes
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class ProductsBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String description;
	private String partnershipOffer;
	private String formLink;
	private List<CategoryBean> categories;
	private String productName;
	private String image;

	private long catId;

	public long getCatId() {
		return catId;
	}

	public void setCatId(long catId) {
		this.catId = catId;
	}

	public List<CategoryBean> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryBean> categories) {
		this.categories = categories;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPartnershipOffer() {
		return partnershipOffer;
	}

	public void setPartnershipOffer(String partnershipOffer) {
		this.partnershipOffer = partnershipOffer;
	}

	public String getFormLink() {
		return formLink;
	}

	public void setFormLink(String formLink) {
		this.formLink = formLink;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id + "";
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return "";
	}

}

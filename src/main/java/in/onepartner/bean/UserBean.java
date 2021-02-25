package in.onepartner.bean;

import java.sql.Timestamp;
import java.util.Date;

/**
 * User JavaBean encapsulates User attributes
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class UserBean extends BaseBean {

	public UserBean() {
	}

	/**
	 * Lock Active constant for User
	 */
	public static String ACTIVE = "Active";
	/**
	 * Lock Inactive constant for User
	 */
	public static String INACTIVE = "Inactive";

	/**
	 * user id
	 */
	private String uId;

	private String img;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * First Name of User
	 */
	private String name;

	private String login;
	/**
	 * Password of User
	 */
	private String password;
	/**
	 * Confirm Password of User
	 */
	private String confirmPassword;

	/**
	 * New Password of User
	 */
	private String newPassword;

	private String mobileNo;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Role of User
	 */
	private long roleId;

	public static String getActive() {
		return ACTIVE;
	}

	public static void setActive(String active) {
		ACTIVE = active;
	}

	public static String getInactive() {
		return INACTIVE;
	}

	public static void setInactive(String inactive) {
		INACTIVE = inactive;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	@Override
	public String getKey() {
		return id + "";
	}

	@Override
	public String getValue() {
		return name + " " + " (" + login + ")";
	}

}

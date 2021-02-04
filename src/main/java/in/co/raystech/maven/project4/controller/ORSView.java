package in.co.raystech.maven.project4.controller;

/**
 * Contains ORS View and Controller URI
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface ORSView {

	public String LAYOUT_VIEW = "/BaseLayout.jsp";
	public String PAGE_FOLDER = "/jsp";
	public String APPLICATION_CONTEXT = "/OnePartner";
	public String JAVA_DOC_VIEW = "/doc/index.html";
	public String ERROR_VIEW = PAGE_FOLDER + "/ErrorView.jsp";
	public String USER_VIEW = PAGE_FOLDER + "/UserView.jsp";
	public String USER_LIST_VIEW = PAGE_FOLDER + "/UserListView.jsp";
	public String ROLE_VIEW = PAGE_FOLDER + "/RoleView.jsp";
	public String ROLE_LIST_VIEW = PAGE_FOLDER + "/RoleListView.jsp";
	public String USER_REGISTRATION_VIEW = PAGE_FOLDER + "/UserRegistrationView.jsp";
	public String LOGIN_VIEW = PAGE_FOLDER + "/LoginView.jsp";
	public String WELCOME_VIEW = PAGE_FOLDER + "/Welcome.jsp";
	public String CHANGE_PASSWORD_VIEW = PAGE_FOLDER + "/ChangePasswordView.jsp";
	public String MY_PROFILE_VIEW = PAGE_FOLDER + "/MyProfileView.jsp";
	public String FORGET_PASSWORD_VIEW = PAGE_FOLDER + "/ForgetPasswordView.jsp";
	public String MARKET_PLACE_VIEW = PAGE_FOLDER + "/MarketPlace.jsp";
	public String MANAGE_PARTNER_LIST_VIEW = PAGE_FOLDER + "/ManagePartnersList.jsp";
	public String MANAGE_PARTNER_VIEW = PAGE_FOLDER + "/ManagePartners.jsp";
	public String MANAGE_PRODUCTS_VIEW = PAGE_FOLDER + "/ManageProducts.jsp";
	public String MANAGE_CATEGORY_VIEW = PAGE_FOLDER + "/ManageCategory.jsp";
	public String CATEGORYPOPUP_VIEW = PAGE_FOLDER + "/CategoryPopup.jsp";
	public String PRODUCTSPOPUP_VIEW = PAGE_FOLDER + "/ProductsPopup.jsp";
	public String CHANGE_EMAIL_SENDERUSER_VIEW = PAGE_FOLDER + "/ChangeEmailSenderUser.jsp";
			
	
	public String ERROR_CTL = "/ErrorCtl";
	
	
	public String CHANGESENDER_EMAIL_USER_CTL= APPLICATION_CONTEXT + "/ctl/ChangeSenderEmailUserCtl";
	public String MARKET_PLACE_CTL = APPLICATION_CONTEXT + "/ctl/ManageMarketPlaceCtl";
	public String MARKET_PLACESS_CTL = APPLICATION_CONTEXT + "/ctl/ManageMarketPlaceCtl";
	public String MANAGE_PARTNERS_CTL = APPLICATION_CONTEXT + "/ctl/ManagePartnersCtl";
	public String MANAGE_PRODUCTS_CTL = APPLICATION_CONTEXT + "/ctl/ManageProductsCtl";
	public String MANAGE_CATEGORY_CTL = APPLICATION_CONTEXT + "/ctl/ManageCategoryCtl";
	public String MANAGE_CATEGORY_LIST_CTL = APPLICATION_CONTEXT + "/ctl/ManageCategoryListCtl";
	public String MANAGE_PRODUCTS_LIST_CTL = APPLICATION_CONTEXT + "/ctl/ManageProductsListCtl";
	public String MANAGE_PRODUCTS_LISTTT_CTL = APPLICATION_CONTEXT + "/ctl/ManageProductsListCtl";
	public String MANAGE_CATEGORY_LISTT_CTL = APPLICATION_CONTEXT + "/ctl/ManageCategoryListCtl";
	public String ADD_CATEGORY_CTL = APPLICATION_CONTEXT + "/ctl/AddCategoryCtl";
	public String USER_CTL = APPLICATION_CONTEXT + "/ctl/UserCtl";
	public String USER_LIST_CTL = APPLICATION_CONTEXT + "/ctl/UserListCtl";
	public String ROLE_CTL = APPLICATION_CONTEXT + "/ctl/RoleCtl";
	public String ROLE_LIST_CTL = APPLICATION_CONTEXT + "/ctl/RoleListCtl";
	public String USER_REGISTRATION_CTL = APPLICATION_CONTEXT + "/UserRegistrationCtl";
	public String LOGIN_CTL = APPLICATION_CONTEXT + "/LoginCtl";
	public String WELCOME_CTL = APPLICATION_CONTEXT + "/WelcomeCtl";
	public String LOGOUT_CTL = APPLICATION_CONTEXT + "/LoginCtl";
	public String CHANGE_PASSWORD_CTL = APPLICATION_CONTEXT + "/ctl/ChangePasswordCtl";
	public String MY_PROFILE_CTL = APPLICATION_CONTEXT + "/ctl/MyProfileCtl";
	public String FORGET_PASSWORD_CTL = "/ForgetPasswordCtl";

}

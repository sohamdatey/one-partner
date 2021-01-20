package in.co.raystech.maven.project4.controller;

/**
 * Contains ORS View and Controller URI
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public interface ORSView {

	public String APP_CONTEXT = "/OnePartner";
	public String LAYOUT_VIEW = "/BaseLayout.jsp";
	public String PAGE_FOLDER = "/jsp";
	public String JAVA_DOC_VIEW = APP_CONTEXT + "/doc/index.html";
	public String ERROR_VIEW = PAGE_FOLDER + "/ErrorView.jsp";
	public String MARKSHEET_VIEW = PAGE_FOLDER + "/MarksheetView.jsp";
	public String MARKSHEET_LIST_VIEW = PAGE_FOLDER + "/MarksheetListView.jsp";
	public String GET_MARKSHEET_VIEW = PAGE_FOLDER + "/GetMarksheetView.jsp";
	public String USER_VIEW = PAGE_FOLDER + "/UserView.jsp";
	public String USER_LIST_VIEW = PAGE_FOLDER + "/UserListView.jsp";
	public String COLLEGE_VIEW = PAGE_FOLDER + "/CollegeView.jsp";
	public String COLLEGE_LIST_VIEW = PAGE_FOLDER + "/CollegeListView.jsp";
	public String STUDENT_VIEW = PAGE_FOLDER + "/StudentView.jsp";

	public String ROLE_VIEW = PAGE_FOLDER + "/RoleView.jsp";
	public String ROLE_LIST_VIEW = PAGE_FOLDER + "/RoleListView.jsp";
	public String USER_REGISTRATION_VIEW = PAGE_FOLDER + "/UserRegistrationView.jsp";
	public String LOGIN_VIEW = PAGE_FOLDER + "/LoginView.jsp";
	public String WELCOME_VIEW = PAGE_FOLDER + "/Welcome.jsp";
	public String CHANGE_PASSWORD_VIEW = PAGE_FOLDER + "/ChangePasswordView.jsp";
	public String MY_PROFILE_VIEW = PAGE_FOLDER + "/MyProfileView.jsp";
	public String FORGET_PASSWORD_VIEW = PAGE_FOLDER + "/ForgetPasswordView.jsp";
	public String MARKSHEET_MERIT_LIST_VIEW = PAGE_FOLDER + "/MarksheetMeritListView.jsp";
	public String COURSE_VIEW = PAGE_FOLDER + "/CourseView.jsp";
	public String COURSE_LIST_VIEW = PAGE_FOLDER + "/CourseListView.jsp";
	public String FACULTY_VIEW = PAGE_FOLDER + "/FacultyView.jsp";
	public String SUBJECT_VIEW = PAGE_FOLDER + "/SubjectView.jsp";
	public String FACULTY_LIST_VIEW = PAGE_FOLDER + "/FacultyListView.jsp";
	public String TIMETABLE_VIEW = PAGE_FOLDER + "/TimeTableView.jsp";

	public String MARKET_PLACE_VIEW = PAGE_FOLDER + "/MarketPlace.jsp";
	public String MANAGE_PARTNER_LIST_VIEW = PAGE_FOLDER + "/ManagePartnersList.jsp";
	public String MANAGE_PARTNER_VIEW = PAGE_FOLDER + "/ManagePartners.jsp";

	public String MANAGE_PRODUCTS_VIEW = PAGE_FOLDER + "/ManageProducts.jsp";
	public String MANAGE_CATEGORY_VIEW = PAGE_FOLDER + "/ManageCategory.jsp";
	public String CATEGORYPOPUP_VIEW = PAGE_FOLDER + "/CategoryPopup.jsp";
	public String PRODUCTSPOPUP_VIEW = PAGE_FOLDER + "/ProductsPopup.jsp";

	public String ERROR_CTL = APP_CONTEXT + "/ErrorCtl";

	public String MARKET_PLACE_CTL = APP_CONTEXT + "/ctl/ManageMarketPlaceCtl";

	public String MARKET_PLACEeess_CTL = "/ctl/ManageMarketPlacessCtl";
	public String MARKET_PLACESS_CTL = "/ctl/ManageMarketPlaceCtl";

	public String MANAGE_PARTNERS_CTL = APP_CONTEXT + "/ctl/ManagePartnersCtl";
	public String MANAGE_PRODUCTS_CTL = APP_CONTEXT + "/ctl/ManageProductsCtl";
	public String MANAGE_PRODUCT_CTL = "/ctl/ManageProductCtl";
	public String MANAGE_CATEGORY_CTL = APP_CONTEXT + "/ctl/ManageCategoryCtl";
	public String MANAGE_CATEGORY_LIST_CTL = APP_CONTEXT + "/ctl/ManageCategoryListCtl";
	public String MANAGE_PRODUCTS_LIST_CTL = APP_CONTEXT + "/ctl/ManageProductsListCtl";

	public String MANAGE_PRODUCTS_LISTTT_CTL = "/ctl/ManageProductsListCtl";

	public String MANAGE_CATEGORY_LISTT_CTL = "/ctl/ManageCategoryListCtl";
	public String ADD_CATEGORY_CTL = APP_CONTEXT + "/ctl/AddCategoryCtl";

	public String USER_CTL = APP_CONTEXT + "/ctl/UserCtl";
	public String USER_LIST_CTL = APP_CONTEXT + "/ctl/UserListCtl";
	public String ROLE_CTL = APP_CONTEXT + "/ctl/RoleCtl";
	public String ROLE_LIST_CTL = APP_CONTEXT + "/ctl/RoleListCtl";
	public String USER_REGISTRATION_CTL = APP_CONTEXT + "/UserRegistrationCtl";
	public String LOGIN_CTL = APP_CONTEXT + "/LoginCtl";
	public String WELCOME_CTL = APP_CONTEXT + "/WelcomeCtl";
	public String LOGOUT_CTL = APP_CONTEXT + "/LoginCtl";
	public String CHANGE_PASSWORD_CTL = APP_CONTEXT + "/ctl/ChangePasswordCtl";
	public String MY_PROFILE_CTL = APP_CONTEXT + "/ctl/MyProfileCtl";
	public String FORGET_PASSWORD_CTL = APP_CONTEXT + "/ForgetPasswordCtl";

}

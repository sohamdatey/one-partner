<%@page import="in.co.raystech.maven.project4.bean.UserBean"%>
<%@page import="in.co.raystech.maven.project4.controller.ORSView"%>
<%@page import="in.co.raystech.maven.project4.util.DataValidator"%>
<%@page import="in.co.raystech.maven.project4.util.DataUtility"%>
<%@page import="in.co.raystech.maven.project4.controller.LoginCtl"%>
<%@page import="javax.mail.Header"%>
<%@page import="in.co.raystech.maven.project4.controller.UserListCtl"%>
<%@page import="in.co.raystech.maven.project4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.raystech.maven.project4.util.HTMLUtility"%>
<nav class="navbar navbar-blue navbar-fixed-top">

	<jsp:useBean id="userBean"
		class="in.co.raystech.maven.project4.bean.UserBean" scope="session"></jsp:useBean>
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle collapsed"
			data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
			aria-expanded="false">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<!-- <a class="navbar-brand" href="#">Brand</a> -->
		<a href="#" class="navbar-brand">
			<h3>
				<span>ONE</span>Partner
			</h3>
		</a>
		<%
			if (DataValidator.isNotNull(DataUtility.getString(ServletUtility.getSuccessMessage(request)))) {
		%>
		<font color="#369a47"><%=ServletUtility.getSuccessMessage(request)%></font>
		<%
			} else if (DataUtility.getString(ServletUtility.getErrorMessage(request)) != null) {
		%>
		<font color="#ff3030"><%=ServletUtility.getErrorMessage(request)%></font>
		<%
			}
		%>
	</div>


	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

		<ul class="nav navbar-nav navbar-right">

			<li><a href="<%=ORSView.MANAGE_PARTNERS_CTL%>"><span>Partners</span></a></li>
			<li><a href="<%=ORSView.MANAGE_CATEGORY_CTL%>"><span>Categories</span></a></li>
			<li><a href="<%=ORSView.MANAGE_PRODUCTS_CTL%>"><span>Products</span></a></li>

			<li class="visible-xs" style="clear: both"><a href="#"
				class="profDrop"><img class="mrR1" src="../images/profile.png" />
					ADMIN</a></li>
			<li class="visible-xs"><a href="<%=ORSView.MARKET_PLACE_CTL%>" target="_blank"><i
					class="fa fa-shopping-basket mrR"></i> View Marketplace</a></li>
			<li class="visible-xs"><button
					class="btn btn-sm btn-inverse mrB mrL1 mrT1">LOGOUT</button></li>

			<li class="dropdown hidden-xs"><a href="#"
				class="profDrop dropdown-toggle" data-toggle="dropdown"><img
					src="../images/profile.png" /> ADMIN <small class="caret"
					style="margin: -2px 0 0 9px"></small></a>
				<ul class="dropdown-menu">

					<li><a href="<%=ORSView.MARKET_PLACE_CTL%>" target="_blank"><i
					class="fa fa-shopping-basket mrR"></i> View Marketplace</a></li>


					<%
						userBean = (UserBean) session.getAttribute("user");

						boolean userLoggedIn = userBean != null;

						String welcomeMsg = "Hi, ";

						if (userLoggedIn) {

							welcomeMsg += userBean.getName();
						} else {
							welcomeMsg += "Guest";
						}
					%>

					<li role="separator" class="divider"></li>

					<li>
						<%
							if (userLoggedIn) {
						%> <div><a
						href="<%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>" class="btn btn-sm btn-inverse mrB mrL1"><i class="fa fa-power-off mrR"></i> LOGOUT</a></div>
						<%
							} else {
						%> <a class="btn btn-info" href="<%=ORSView.LOGIN_CTL%>">Login</a>
						<%
							}
						%>
					</li>
				</ul></li>
		</ul>
	</div>

</nav>
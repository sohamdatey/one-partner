<%@page import="in.co.raystech.maven.project4.controller.ChangeSenderEmailUserCtl"%>
<%@page import="in.co.raystech.maven.project4.controller.MyProfileCtl"%>
<%@page import="in.co.raystech.maven.project4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.raystech.maven.project4.util.DataUtility"%>
<%@page import="in.co.raystech.maven.project4.util.ServletUtility"%>
<html>
<head>

<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link rel="stylesheet" href="../css/bootstrap.min.css" />
<link rel="stylesheet" href="../css/select2.min.css" />
<link rel="stylesheet" href="../css/font-awesome.min.css" />
<link rel="stylesheet" href="../css/onepartner.css" />
<script src="../js/jquery-2.2.4.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/bootstrap-select.min.js"></script>
<script src="../js/select2.min.js"></script>


</head>
<body>
	<%@ include file="Header.jsp"%>
	<form action="<%=ORSView.CHANGESENDER_EMAIL_USER_CTL%>" method="post">


		<jsp:useBean id="bean"
			class="in.co.raystech.maven.project4.bean.UserBean" scope="request"></jsp:useBean>


		<nav class="navbar navbar-blue navbar-white navbar-fixed-top">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<!-- <a class="navbar-brand" href="#">Brand</a> -->
				<a href="#" class="navbar-brand">
					<h3>
						<span>ONE</span>Partner
					</h3>
				</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">

				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#"
						class="profDrop dropdown-toggle" data-toggle="dropdown"><%=welcomeMsg%>
							<img src="../images/profile.png" /> <small class="caret"
							style="margin: -2px 0 0 9px"></small> </a>
						<ul class="dropdown-menu">
							<li>
								<%
									if (userLoggedIn) {
								%>
							
							<li><a href="<%=ORSView.MY_PROFILE_CTL%>"><i
									class="fa fa-user-circle mrR"></i> View Profile</a></li>
							<li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>"><i
									class="fa fa-key mrR"></i> Change Password</a></li>
							<li role="separator" class="divider"></li>

							<a class="btn btn-inverse mrL1 mrB"
								href="<%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>"><i
								class="fa fa-power-off mrR"></i> Logout</b></a>

							<%
								} else {
							%>
							<a class="btn btn-inverse mrL1 mrB" href="<%=ORSView.LOGIN_CTL%>">Login</b></a>
							<%
								}
							%>
						</li>
				</ul>
				</li>
				</ul>


				<a class="pull-right hidden-xs" href="<%=ORSView.MARKET_PLACE_CTL%>"
					style="padding: 18px"> <i class="fa fa-arrow-left mrR"></i>
					Back to marketplace
				</a>

			</div>
		</nav>
		<!-- / .navbar-->


		<div class="container-fluid">
			<div class="prdBox mrC">

				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font> <font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
				<h3 class="mrT mrB3">
					<i class="fa fa-user mrR"></i> Partner Profile
				</h3>

				<div class="form-group">
					<label>Login Id*</label> <input type="text" 
						name="login" size="30" class="form-control"
						value="<%=DataUtility.getStringData(bean.getLogin())%>"> <font
						color="red"><%=ServletUtility.getErrorMessage("login", request)%></font>
				</div>

				<div class="form-group">
					<label>Password*</label> <input type="text" name="password"
						size="30" class="form-control"
						value="<%=DataUtility.getStringData(bean.getPassword())%>"> <font
						color="red"><%=ServletUtility.getErrorMessage("password", request)%></font>
				</div>

				<input type="submit" name="operation"
					class="btn btn-primary btn-lg mrT1"
					value="<%=ChangeSenderEmailUserCtl.OP_SAVE%>">
			</div>
		</div>
	</form>
</body>
</html>

<%@page import="in.co.raystech.maven.project4.util.DataUtility"%>
<%@page import="in.co.raystech.maven.project4.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="in.co.raystech.maven.project4.controller.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Login View</title>
<!-- Mobile Specific Metas -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<!-- Font-->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- Main Style Css -->
<link rel="stylesheet" href="../css/onepartner.css" />
<link rel="stylesheet" href="css/onepartner.css" />
</head>
<body class="form-v8">

	<form action="<%=ORSView.LOGIN_CTL%>" method="post">
		<jsp:useBean id="bean"
			class="in.co.raystech.maven.project4.bean.UserBean" scope="request"></jsp:useBean>

		<div class="login-wrap">
			<div class="login-html">
				<H4>
					<font color="#ff3030"> <%=ServletUtility.getErrorMessage(request)%>
						<%=ServletUtility.getErrorMessage("message", request)%>
					</font> <font color="#3bbd45"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
					<%
						if (session.getAttribute("rgstrtnsucs") != null) {
					%>
					<font color="#3bbd45"> <%=session.getAttribute("rgstrtnsucs")%></font>
					<%
						}
					%>
				</H4>

				<h3>
					<span>ONE</span>Partner
				</h3>
				<label class="tab">Sign In to your account</label>
				<div class="login-form">
					<div class="sign-in-htm">
						<div class="group">
							<label for="user" class="label">Email id / mobile number</label>
							<input type="text" required
								value="<%=DataUtility.getStringData(bean.getLogin())%>"
								name="login" class="input"> <font color="#ff3030"><small><%=ServletUtility.getErrorMessage("login", request)%></font>
						</div>
						<div class="group">
							<label for="pass" class="label">Password</label> <input
								type="password" required
								value="<%=DataUtility.getStringData(bean.getPassword())%>"
								name="password" class="input" data-type="password"> <font
								color="#ff3030"><%=ServletUtility.getErrorMessage("password", request)%></font>

						</div>
						<div class="group">
							<input id="check" type="checkbox" class="check"> <label
								for="check"><span class="icon"></span> Keep me Signed in</label>
						</div>
						<div class="group">
							<input type="submit" name="operation" class="button"
								value="<%=LoginCtl.OP_SIGN_IN%>">
						</div>
						<div class="hr"></div>
						<div class="foot-lnk">
							<a href="<%=ORSView.FORGET_PASSWORD_CTL%>">Forgot Password?</a>
						</div>
						<div class="foot-lnk mrT2">
							<a href="<%=ORSView.USER_REGISTRATION_CTL%>">Don't have an account? Register now. </a>
						</div>
					</div>
				</div>
			</div>
		</div>


		<script src="../js/jquery-2.2.4.min.js"></script>

	</form>

</body>
</html>
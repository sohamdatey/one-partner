<%@page import="in.co.raystech.maven.project4.controller.*"%>
<%@page import="in.co.raystech.maven.project4.bean.*"%>
<%@page import="in.co.raystech.maven.project4.exception.*"%>
<%@page import="in.co.raystech.maven.project4.model.*"%>
<%@page import="in.co.raystech.maven.project4.util.*"%>
<%@page import="java.util.HashMap"%>
<html>
<head>
<meta charset="utf-8">
<title>User Registration</title>
<!-- Mobile Specific Metas -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<!-- Font-->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- Main Style Css -->
<link rel="stylesheet" href="css/onepartner.css" />
<link rel="stylesheet" href="../css/onepartner.css" />
</head>
<body class="form-v8">

	<jsp:useBean id="bean"
		class="in.co.raystech.maven.project4.bean.UserBean" scope="request"></jsp:useBean>

	<form action="<%=ORSView.USER_REGISTRATION_CTL%>" method="post">


		<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
			type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
		<input type="hidden" name="modifiedBy"
			value="<%=bean.getModifiedBy()%>"> <input type="hidden"
			name="createdDatetime"
			value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
		<input type="hidden" name="modifiedDatetime"
			value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

		<div class="login-wrap">
			<div class="login-html">
				<h3>
					<span>ONE</span>Partner
				</h3>

				<label class="tab">Register for free</label>
				<div class="login-form">
					<div class="sign-up-htm">
						<H4>
							<font color="#ff3030"> <%=ServletUtility.getErrorMessage(request)%>
							</font> <font color="#3bbd45"> <%=ServletUtility.getSuccessMessage(request)%>
							</font>

						</H4>

						<div class="group">
							<label for="user" class="label">Name</label> <input name="name"
								value="<%=DataUtility.getStringData(bean.getName())%>"
								type="text" required class="input"> <font color="red">
								<%=ServletUtility.getErrorMessage("name", request)%></font>

						</div>
						<div class="group">
							<label for="mobile" class="label">Mobile Number</label> <input
								value="<%=DataUtility.getStringData(bean.getMobileNo())%>"
								name="contactNumber" type="number" required class="input">
							<font color="red"> <%=ServletUtility.getErrorMessage("contactNumber", request)%></font>

						</div>
						<div class="group">
							<label for="mail" class="label">Email Address</label> <input
								name="login"
								value="<%=DataUtility.getStringData(bean.getLogin())%>"
								type="email" required class="input"> <font color="red">
								<%=ServletUtility.getErrorMessage("login", request)%></font>

						</div>
						<div class="group">
							<label for="pass" class="label">Password</label> <input
								name="password"
								value="<%=DataUtility.getStringData(bean.getPassword())%>"
								type="password" class="input" data-type="password"> <font
								color="red"> <%=ServletUtility.getErrorMessage("password", request)%></font>
						</div>

						<div class="group">
							<label for="pass" class="label">Confirm Password</label> <input
								name="confirmPassword"
								value="<%=DataUtility.getStringData(bean.getConfirmPassword())%>"
								type="password" class="input" data-type="password"> <font
								color="red"> <%=ServletUtility.getErrorMessage("confirmPassword", request)%></font>
						</div>
						<div class="group">
							<input id="check" type="checkbox" class="check" checked>
							<label for="check"><span class="icon"></span> I Agree to
								the Terms & Privacy</label>
						</div>
						<div class="group">
							<input type="submit" name="operation"
								value="<%=UserRegistrationCtl.OP_SIGN_UP%>" class="button"
								value="Sign Up">
						</div>
						<br>
						<div class="foot-lnk"><small>
							<a href="<%=ORSView.LOGIN_CTL%>">Already have an account? Login here</a></small>
						</div>
					</div>
				</div>
			</div>
		</div>

		<script src="js/jquery-2.2.4.min.js"></script>

	</form>
</body>
</html>
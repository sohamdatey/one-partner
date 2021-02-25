<%@page import="in.onepartner.controller.*"%>
<%@page import="in.onepartner.util.*"%>
<%@page import="in.onepartner.bean.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>One Partner</title>
<!-- Mobile Specific Metas -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<!-- Font-->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- Main Style Css -->
<link rel="stylesheet" href="css/onepartner.css" />
</script>
</head>

<body class="form-v8">

	<form action="<%=ORSView.FORGET_PASSWORD_CTL%>" method="post">

		<div class="login-wrap">
			<div class="login-html">
				<H2>
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</H2>
				<H2>
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</H2>

				<h3>
					<span>ONE</span>Partner
				</h3>
				<label class="tab">Forgot Password</label>
				<div class="login-form">
					<div class="sign-in-htm">
						<div class="group">
							<label for="user" class="label">Enter your Email ID</label> <input
								type="email" name="login" size="30" class="input"
								value="<%=ServletUtility.getParameter("login", request)%>">
							<font color="red"><%=ServletUtility.getErrorMessage("login", request)%></font>
						</div>
						<div class="group">
							<input type="submit" name="operation" class="button"
								value="<%=ForgetPasswordCtl.OP_GO%>">

						</div>
						<div class="hr"></div>
						<div class="foot-lnk">
							<a href="<%=ORSView.LOGIN_CTL%>">Back to Login </a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>

</html>
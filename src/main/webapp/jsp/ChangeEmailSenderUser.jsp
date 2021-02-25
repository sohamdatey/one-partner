<%@page import="in.onepartner.controller.ChangeSenderEmailUserCtl"%>
<%@page import="in.onepartner.controller.MyProfileCtl"%>
<%@page import="in.onepartner.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.onepartner.util.DataUtility"%>
<%@page import="in.onepartner.util.ServletUtility"%>
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
			class="in.onepartner.bean.UserBean" scope="request"></jsp:useBean>


		<div class="container-fluid">
			<div class="prdBox mrC">

				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font> <font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
				<h3 class="mrT mrB3">
					<i class="fa fa-envelope mrR"></i> Change Admin Email
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

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
	<form action="<%=ORSView.MY_PROFILE_CTL%>" method="post">


		<jsp:useBean id="bean"
			class="in.co.raystech.maven.project4.bean.UserBean" scope="request"></jsp:useBean>



		<div class="container-fluid">
			<div class="prdBox mrC">

				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font> <font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font> <input type="hidden" name="id" value="<%=bean.getId()%>"> <input
					type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
				<input type="hidden" name="modifiedBy"
					value="<%=bean.getModifiedBy()%>"> <input type="hidden"
					name="createdDatetime"
					value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
				<input type="hidden" name="modifiedDatetime"
					value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">
				<h3 class="mrT mrB3">
					<i class="fa fa-user-secret mrR"></i> Admin Profile
				</h3>

				<div class="form-group">
					<label>Login Id*</label> <input type="text" readonly="readonly"
						name="login" size="30" class="form-control"
						value="<%=DataUtility.getStringData(bean.getLogin())%>"> <font
						color="red"><%=ServletUtility.getErrorMessage("login", request)%></font>
				</div>

				<div class="form-group">
					<label>Name*</label> <input type="text" name="name" size="30"
						class="form-control"
						value="<%=DataUtility.getStringData(bean.getName())%>"> <font
						color="red"><%=ServletUtility.getErrorMessage("name", request)%></font>
				</div>

				<div class="form-group">
					<label>Mobile No*</label> <input type="text" name="mobileNo"
						size="30" class="form-control"
						value="<%=DataUtility.getStringData(bean.getMobileNo())%>">
					<font color="red"><%=ServletUtility.getErrorMessage("mobileNo", request)%></font>
				</div>

				
				<a class="btn btn-inverse btn-lg mrT1 mrR1" href="<%=ORSView.CHANGESENDER_EMAIL_USER_CTL%>">Change
						Email</a> 
						
					<input type="submit" name="operation"
					class="btn btn-primary btn-lg mrT1"
					value="<%=MyProfileCtl.OP_SAVE%>">
			</div>
		</div>
	</form>
</body>
</html>

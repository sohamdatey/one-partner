<%@page import="in.onepartner.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="in.onepartner.controller.ORSView"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error page</title>
</head>
<%@page isErrorPage="true"%>

<body>
	this is Error page

	<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>

</body>
</html>
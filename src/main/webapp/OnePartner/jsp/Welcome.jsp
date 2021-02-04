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
	<H1>niceh image hai</H1>
	<form method="POST" action="<%=ORSView.WELCOME_CTL%>"
		enctype="multipart/form-data">
		File: <input type="file" name="file" id="file" /> <br />
		Destination: <input type="text" value="/tmp" name="destination" /> </br> <input
			type="submit" value="Upload" name="upload" id="upload" />
	</form>

	<img
		src="https://one-partner-product-images.s3.amazonaws.com/harvey+mc+with+args+in+code" />
</body>
</html>
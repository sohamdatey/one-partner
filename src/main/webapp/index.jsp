<%@page import="in.onepartner.bean.CategoryBean"%>
<%@page import="in.onepartner.bean.UserBean"%>
<%@page import="in.onepartner.controller.ORSView"%>
<%@page import="in.onepartner.util.DataValidator"%>
<%@page import="in.onepartner.util.DataUtility"%>
<%@page import="javax.mail.Header"%>
<%@page import="in.onepartner.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.onepartner.util.HTMLUtility"%><%@page
	import="in.onepartner.controller.*"%>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<html>

<body>

	<H1>
		<a href="jsp/LoginView.jsp">Welcome</a>
	</H1>   


	<form method="POST" action="<%=ORSView.MANAGE_PRODUCTS_CTL%>"
		enctype="multipart/form-data">
		File: <input type="file" name="file" id="file" /> <br />
		Destination: <input type="text" value="/tmp" name="destination" /> </br> <input
			type="submit" value="Upload" name="upload" id="upload" />
	</form>

</body>
</html>

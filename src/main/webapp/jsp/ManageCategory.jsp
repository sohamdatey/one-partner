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
	<%@include file="Header.jsp"%>
	<%@include file="CategoryPopup.jsp"%>
	<div class="pd2 hdpad">
		<div class="clearfix tabTop">
			<div class="row">
				<div class="col-sm-6">
					<h4>Manage Categories</h4>
				</div>
				<div class="col-sm-6">
					<div class="clearfix">
						<button type="button" onclick="$('#addCategory').modal('show');"
							class="btn btn-primary pull-right">
							<i class="fa fa-plus mrR"> </i> Add Category
						</button>
						<%
							if (bean2 != null) {
						%>
						<script>
							$('#editCategory').modal('show')
						</script>
						<%
							}
						%>

						<form action="<%=ORSView.MANAGE_CATEGORY_CTL%>" method="post"
							id="categoryForm">
							<div class="srchWrp pull-right mrR1">
								<i class="fa fa-search"></i> <input type="text" name="category"
									value="<%=ServletUtility.getParameter("category", request)%>"
									placeholder="Search" /> <input type="submit"
									style="display: none" name="operation"
									value="<%=ManageCategoryCtl.OP_SEARCH%>" id="triggerSrch">

							</div>
						</form>
						<script type="text/javascript">
							$('#triggerSrch').keypress(function(e) {
								if (e.which == 13) {
									e.preventDefault();
									$('#categoryForm').submit();
								}
							});
						</script>
					</div>
				</div>
			</div>
		</div>

		<div class="catHash">
			<%
				List list = ServletUtility.getList(request);
				Iterator<CategoryBean> it = list.iterator();
				int size = list.size();
				while (it.hasNext()) {
					bean = it.next();
			%>
			<a href="ManageCategoryCtl?id=<%=bean.getId()%>"> #<%=bean.getCategory()%>
			</a>

			<%
				}
			%>
		</div>
	</div>


	<script>
		$('.nav>li:nth-child(2)').addClass('active');
	</script>
</body>
</html>
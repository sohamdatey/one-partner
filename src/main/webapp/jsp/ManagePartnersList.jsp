<%@page
	import="in.co.raystech.maven.project4.controller.ManagePartnersCtl"%>
<%@page import="in.co.raystech.maven.project4.controller.BaseCtl"%>
<%@page import="in.co.raystech.maven.project4.bean.UserBean"%>
<%@page import="in.co.raystech.maven.project4.controller.ORSView"%>
<%@page import="in.co.raystech.maven.project4.util.DataValidator"%>
<%@page import="in.co.raystech.maven.project4.util.DataUtility"%>
<%@page import="javax.mail.Header"%>
<%@page import="in.co.raystech.maven.project4.controller.UserListCtl"%>
<%@page import="in.co.raystech.maven.project4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.raystech.maven.project4.util.HTMLUtility"%>
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

<link rel="stylesheet" href="css/bootstrap.min.css" />
<link rel="stylesheet" href="css/select2.min.css" />
<link rel="stylesheet" href="css/font-awesome.min.css" />
<link rel="stylesheet" href="css/onepartner.css" />
<script src="js/jquery-2.2.4.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-select.min.js"></script>
<script src="js/select2.min.js"></script>

</head>
<body>
	<form action="<%=ORSView.MANAGE_PARTNERS_CTL%>" method="post"
		id="partnerForm">
		<jsp:useBean id="bean" scope="request"
			class="in.co.raystech.maven.project4.bean.UserBean"></jsp:useBean>

		<%@include file="Header.jsp"%>

		<div class="pd2 hdpad">
			<div class="clearfix tabTop">
				<div class="row">
					<div class="col-sm-6">
						<h4>My Partners</h4>
					</div>
					<div class="col-sm-6">
						<div class="clearfix">
							<div class="srchWrp pull-right mrR1">
								<i class="fa fa-search"></i> <input type="text" name="search" placeholder="Search"
									value="<%=ServletUtility.getParameter("search", request)%>">

								<input type="submit" name="operation" style="display: none"
									class="btn btn-info" value="<%=ManagePartnersCtl.OP_SEARCH%>">
							</div>
						</div>
					</div>
				</div>
			</div>
	</form>
	<form action="<%=ORSView.MANAGE_PARTNERS_CTL%>" method="post"
		id="partnerForm2">
		<div class="tableCnt">
			<table class="table partnerTbl">
				<thead>
					<tr>
						<th width="3%"></th>
						<th>Name</th>
						<th>Email ID</th>
						<th>Mobile Number</th>
						<th>Password</th>
						<th>Description</th>
						<th width="3%"></th>
					</tr>
				</thead>
				<tbody>
					<%
						List list = ServletUtility.getList(request);
						Iterator<UserBean> it = list.iterator();
						int size = list.size();
						while (it.hasNext()) {
							bean = it.next();
					%>
					<tr class="partTr">
						<%
							UserBean ubean = (UserBean) session.getAttribute("user");

								if (ubean.getId() == bean.getId()) {
						%>

						<%
							} else {
						%>

						<%
							}
						%>
						<td><input type="checkbox" name="ids" class="checkbox1"
							value="<%=bean.getId()%>"
							onclick="$(this).closest('.partTr').find('.fa-trash').fadeToggle()">
						</td>
						<td><%=bean.getName()%></td>
						<td><%=bean.getLogin()%></td>
						<td><%=bean.getMobileNo()%></td>
						<td><%=bean.getPassword()%></td>
						<td><textarea name="description"><%=DataUtility.getStringData(bean.getDescription())%></textarea>
							<a class="fa fa-check" onclick="$(this).prev().val()"
							href="ManagePartnersCtl?id=<%=bean.getId()%>&description="></a></td>
						<td><i style="display: none" class="fa fa-trash"
							onclick="$('#delPart').modal('show');"></i></td>

						<%
							}
						%>


						<script>
							$('.partTr .fa-check').click(function() {
								var getVal = $(this).prev().val();
								var _href = $(this).attr("href");
								$(this).attr("href", _href + getVal);
							})
						</script>
					</tr>
				</tbody>
			</table>
		</div>
		</div>


		<!--*******Delete confirmation popup******** -->
		<div id="delPart" class="prompt modal fade" role="dialog">
			<!--only for prompt action case, keep backdrop here-->
			<div class="modal-backdrop fade in"></div>
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title pull-left">Are you sure?</h4>
						<a class="iconCir pull-right" data-dismiss="modal"><i
							class="fa fa-times"></i></a>
					</div>
					<div class="modal-body" id="formCnt">
						<p>This partner's account will be permanently deleted.</p>

						<div class="clearfix mrT2">
							<input type="submit" name="operation"
								value="<%=BaseCtl.OP_DELETE%>"
								class="btn btn-lg btn-danger mrR1"> <input type="button"
								class="btn btn-lg btn-inverse" value="Cancel"
								data-dismiss="modal" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script>
		$('.nav>li:nth-child(1)').addClass('active');
	</script>
</body>
</html>
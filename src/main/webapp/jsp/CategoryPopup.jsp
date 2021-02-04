<%@page import="in.co.raystech.maven.project4.bean.CategoryBean"%>
<%@page import="in.co.raystech.maven.project4.bean.UserBean"%>
<%@page import="in.co.raystech.maven.project4.controller.ORSView"%>
<%@page import="in.co.raystech.maven.project4.util.DataValidator"%>
<%@page import="in.co.raystech.maven.project4.util.DataUtility"%>
<%@page import="javax.mail.Header"%>
<%@page import="in.co.raystech.maven.project4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.raystech.maven.project4.util.HTMLUtility"%><%@page
	import="in.co.raystech.maven.project4.controller.*"%>
<html>
<body><jsp:useBean id="bean"
		class="in.co.raystech.maven.project4.bean.CategoryBean"
		scope="request"></jsp:useBean>

	<jsp:useBean id="bean2"
		class="in.co.raystech.maven.project4.bean.CategoryBean"
		scope="request"></jsp:useBean>

	<form action="<%=ORSView.MANAGE_CATEGORY_CTL%>" method="post">
		<%
			bean2 = new CategoryBean();
			bean2 = (CategoryBean) request.getAttribute("catBean");
		%>

		<!-- /****Add Category Modal*****/ -->
		<div id="addCategory" class="mSlide right modal fade" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<div class="pull-right">
							<button class="iconCir" title="Cancel" data-dismiss="modal">
								<i class="fa fa-times"></i>
							</button>
						</div>
						<h4 class="modal-title">Add Category</h4>
					</div>
					<div class="modal-body" id="formCnt">
						<div class="form-group">

							<label>Category Name </label> <input required type="text"
								name="category" class="form-control"
								placeholder="Enter Category Name" />
						</div>


						<div class="highCat">
							<label><input type="checkbox" onchange="highlight()"
								id="marketPlaceId" name="highlightchk" class="mrR">
								Highlight on marketplace </label> <input type="hidden"
								name="marketPlaceId" id="hltAddHid" value="0">
						</div>


						<script>
							function highlight() {
								if ($('#marketPlaceId').is(':checked')) {
									$('#hltAddHid').val('1')
								} else {
									$('#hltAddHid').val('0')
								}
							}
						</script>

						<div class="clearfix">
							<input type="submit" name="operation" value="<%=BaseCtl.OP_ADD%>"
								class="btn btn-lg btn-primary mrR1"> <input
								type="button" class="btn btn-lg btn-inverse" value="Cancel"
								data-dismiss="modal" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<!-- //*******Add Category Modal******** -->


	<form action="<%=ORSView.MANAGE_CATEGORY_CTL%>" method="post">
		<%
			bean2 = new CategoryBean();
			bean2 = (CategoryBean) request.getAttribute("catBean");
		%>


		<!-- /****Edit Category Modal*****/ -->
		<div id="editCategory" class="mSlide right modal fade" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<div class="pull-right">

							<button type="button" class="iconCir" title="Delete"
								onclick="$('#delCatCon').modal('show');">
								<i class="fa fa-trash"></i>
							</button>
							<button type="button" class="iconCir" title="Cancel"
								data-dismiss="modal">
								<i class="fa fa-times"></i>
							</button>
						</div>
						<h4 class="modal-title">Edit Category</h4>
					</div>
					<div class="modal-body" id="formCnt">
						<div class="form-group">

							<label>Category Name </label> <input required type="text"
								<%if (bean2 != null) {%>
								value="<%=DataUtility.getStringData(bean2.getCategory())%>"
								<%} else {%>
								value="<%=DataUtility.getStringData(bean.getCategory())%>" <%}%>
								name="category" class="form-control"
								placeholder="Enter Category Name" />
						</div>

						<%
							if (bean2 != null) {
						%>
						<input type="hidden" name="id" value="<%=bean2.getId()%>">
						<input type="hidden" name="id" value="<%=bean2.getCategory()%>">

						<%
							}
						%>
						<div class="highCat">
							<label><input type="checkbox" onchange="edhighlit()"
								class="mrR" id="checkEditId"> Highlight on marketplace </label>
							<input type="hidden" name="marketPlaceId" id="hltEditHid">

							<%
								if (bean2 != null && bean2.getMarketPlaceId() == 1) {
							%>
							<script>
								$('#checkEditId').attr('checked', true)
							</script>

							<%
								} else {
							%>
							<script>
								$('#checkEditId').attr('checked', false)
							</script>

							<%
								}
							%>



						</div>

						<script>
							function edhighlit() {
								if ($('#checkEditId').is(':checked')) {
									$('#hltEditHid').val('1')
								} else {
									$('#hltEditHid').val('0')
								}
							}
						</script>

						<input type="submit" name="operation" value="<%=BaseCtl.OP_EDIT%>"
							class="btn btn-lg btn-primary mrR1"> <input type="button"
							class="btn btn-lg btn-inverse" value="Cancel"
							data-dismiss="modal" />
					</div>
				</div>
			</div>
		</div>

		<!-- //*******Add Category Modal******** -->


		<!--*******Delete confirmation popup******** -->
		<div id="delCatCon" class="prompt modal fade" role="dialog">
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
						<p>If you delete this category, it will be also removed from
							its assigned products.</p>

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
		$("#imgInp").change(function() {
			readURL(this);
		});
	</script>

</body>
</html>
<!-- //*******Delete confirmation popup End******** -->
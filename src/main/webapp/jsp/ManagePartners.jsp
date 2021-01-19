<%@page import="in.co.raystech.maven.project4.bean.CategoryBean"%>
<%@page import="in.co.raystech.maven.project4.bean.UserBean"%>
<%@page import="in.co.raystech.maven.project4.controller.ORSView"%>
<%@page import="in.co.raystech.maven.project4.util.DataValidator"%>
<%@page import="in.co.raystech.maven.project4.util.DataUtility"%>
<%@page import="javax.mail.Header"%>
<%@page import="in.co.raystech.maven.project4.controller.UserListCtl"%>
<%@page import="in.co.raystech.maven.project4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.raystech.maven.project4.util.HTMLUtility"%><%@page
	import="in.co.raystech.maven.project4.controller.*"%>
<html>
<body><jsp:useBean id="bean"
		class="in.co.raystech.maven.project4.bean.UserBean" scope="request"></jsp:useBean>



	<form action="<%=ORSView.ADD_CATEGORY_CTL%>" method="post">


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
						<h4 class="modal-title">Edit description</h4>
					</div>
					<div class="modal-body" id="formCnt">
						<div class="form-group">

							<label>Category Name </label> <input type="text"
								<%if (bean != null) {%>
								value="<%=DataUtility.getStringData(bean.getDescription())%>"
								<%} else {%>
								value="<%=DataUtility.getStringData(bean.getDescription())%>"
								<%}%> name="category" class="form-control"
								placeholder="Enter Category Name" />
						</div>

						<input type="submit" name="operation" value="<%=BaseCtl.OP_EDIT%>"
							class="btn btn-lg btn-primary mrR1"> <input type="button"
							class="btn btn-lg btn-inverse" value="Cancel"
							data-dismiss="modal" />
					</div>
				</div>
			</div>
		</div>

	</form>

	<script>
		$('.catHash a').click(function() {
			$('#addCategory').modal('show');
		});
	</script>

</body>
</html>
<!-- //*******Delete confirmation popup End******** -->
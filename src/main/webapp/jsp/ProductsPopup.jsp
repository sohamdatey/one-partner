<%@page import="in.onepartner.bean.ProductsBean"%>
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
<body><jsp:useBean id="bean"
		class="in.onepartner.bean.ProductsBean"
		scope="request"></jsp:useBean>

	<jsp:useBean id="categoryBean"
		class="in.onepartner.bean.CategoryBean"
		scope="request"></jsp:useBean>

	<jsp:useBean id="bean2"
		class="in.onepartner.bean.ProductsBean"
		scope="request"></jsp:useBean>



	<form id="addProductForm" action="<%=ORSView.MANAGE_PRODUCTS_CTL%>"
		method="post" enctype="multipart/form-data">
		
		<%
			List<CategoryBean> l = (List) request.getAttribute("categoryList");
		%>

		<!-- /****Add Product Modal*****/ -->
		<div id="addProduct" class="mSlide right modal fade" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<div class="pull-right">
							<a class="iconCir" title="Cancel" data-dismiss="modal"> <i
								class="fa fa-times"></i>
							</a>
						</div>

						<h4 class="modal-title">Add Product</h4>
					</div>


					<div class="modal-body" id="formCnt">

						<!-- File: <input type="file" required name="file" id="file" /> -->

						<div class="form-group clearfix">
							<div id="ppic">
								<img src="../images/default.png" alt="profile-pic" />
							</div>
							<div class="img-upload">
								<label for="file" class="img-upload__label">Upload Image</label>
								<input class="img-upload__input" required type="file"
									name="fileAdd" id="file">
							</div>

						</div>


						<div class="form-group">
							<label>Product Name </label> <input required type="text"
								name="productName" class="form-control"
								placeholder="Enter Product Name" />
						</div>
						<div class="form-group">
							<label>Description </label>
							<textarea class="form-control" required name="description"
								placeholder="Enter Description"></textarea>
						</div>

						<div class="form-group">
							<label>Partnership Offer</label> <input type="text"
								name="partnershipOffer" class="form-control"
								placeholder="Enter Offer Details" />
						</div>

						<div class="form-group">
							<label>Form Link </label> <input required type="text"
								name="formLink" class="form-control"
								placeholder="Enter Google form or Typeform link" />
						</div>

						<div class="form-group catForm">
							<label>Category</label>

							<%=HTMLUtility.getCategoryListForProducts("categoryId",
					DataUtility.getStringData(String.valueOf(bean.getId())), l)%>


							<aside onclick="$('#addCat').modal('show');">
								<i class="fa fa-plus"></i>
							</aside>

						</div>

						<div class="clearfix">
							<input type="submit" value="<%=BaseCtl.OP_ADD%>" name="operation"
								class="btn btn-lg btn-primary mrR1"> <input
								type="button" class="btn btn-lg btn-inverse" value="Cancel"
								data-dismiss="modal" />
						</div>
					</div>

				</div>
			</div>
		</div>
	</form>

	<form id="editProductForm" action="<%=ORSView.MANAGE_PRODUCTS_CTL%>"
		method="post" enctype="multipart/form-data">


		<%
			bean2 = new ProductsBean();
			bean2 = (ProductsBean) request.getAttribute("productBeanAttr");
			List listOfCats = (List) request.getAttribute("listOfCats");
			List allCategoriesList = (List) request.getAttribute("allCategoriesList");
		%>


		<!-- /****Edit Product Modal*****/ -->
		<div id="editProduct" class="mSlide right modal fade" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<div class="pull-right">


							<a class="iconCir" title="Delete"
								onclick="$('#delConfirm').modal('show');"> <i
								class="fa fa-trash"></i>
							</a> <a class="iconCir" title="Cancel" data-dismiss="modal"> <i
								class="fa fa-times"></i>
							</a>
						</div>

						<h4 class="modal-title">Edit Product</h4>
					</div>
					<div class="modal-body" id="formCnt">
						<div class="form-group clearfix">

							<div id="ppap">

								<img name="imageEdit" <%if (bean2 != null) {%>
									src="<%=DataUtility.getStringData(bean2.getImageURL())%>"
									<%} else {%>
									src="<%=DataUtility.getStringData(bean.getImageURL())%>" <%}%>
									alt="profile-pic" />
							</div>

							<%
								if (bean2 != null) {
							%>
							<input type="hidden" name="id" value="<%=bean2.getId()%>">

							<%
								} else {
							%>
							<input type="hidden" name="id" value="<%=bean.getId()%>">
							<%
								}
							%>


							<div class="img-upload">
								<label for="imgInp" class="img-upload__label">Change
									Image</label> <input class="img-upload__input" type="file" name="fileEdit"
									id="imgInp">
							</div>

						</div>


						<div class="form-group">
							<label>Product Name </label> <input required type="text"
								<%if (bean2 != null) {%>
								value="<%=DataUtility.getStringData(bean2.getProductName())%>"
								<%} else {%>
								value="<%=DataUtility.getStringData(bean.getProductName())%>"
								<%}%> class="form-control" name="productName"
								placeholder="Enter Product Name" />
						</div>

						<div class="form-group">
							<label>Description </label>

							<textarea class="form-control" required name="description"
								placeholder="Enter Description">
								<%
									if (bean2 != null) {
								%><%=DataUtility.getStringData(bean2.getDescription())%>
							<%
								} else {
							%><%=DataUtility.getStringData(bean.getDescription())%>
							<%
								}
							%>
							</textarea>

						</div>

						<div class="form-group">
							<label>Partnership Offer</label> <input type="text"
								<%if (bean2 != null) {%>
								value="<%=DataUtility.getStringData(bean2.getPartnershipOffer())%>"
								<%} else {%>
								value="<%=DataUtility.getStringData(bean.getPartnershipOffer())%>"
								<%}%> class="form-control" name="partnershipOffer"
								placeholder="Enter Offer Details" />
						</div>

						<div class="form-group">
							<label>Form Link </label> <input type="text"
								<%if (bean2 != null) {%>
								value="<%=DataUtility.getStringData(bean2.getFormLink())%>"
								<%} else {%>
								value="<%=DataUtility.getStringData(bean.getFormLink())%>" <%}%>
								class="form-control" name="formLink"
								placeholder="Enter Google form or Typeform link" />
						</div>
						<div class="form-group catForm">
							<label>Category</label>
							<%
								if (bean2 != null) {
							%>

							<%=HTMLUtility.getCategoryListForProductsSelected("categoryId", String.valueOf(bean.getId()),
						listOfCats, allCategoriesList)%>

							<%
								}
							%>
						</div>
						<div class="form-group catForm">
							<aside onclick="$('#addCat').modal('show');">
								<i class="fa fa-plus"></i>
							</aside>
						</div>

						<div class="clearfix">
							<input type="submit" value="<%=BaseCtl.OP_EDIT%>"
								name="operation" class="btn btn-lg btn-primary mrR1"> <input
								type="button" class="btn btn-lg btn-inverse" value="Cancel"
								data-dismiss="modal" />
						</div>

					</div>
				</div>
			</div>
		</div>



		<!--*******Delete confirmation popup******** -->
		<div id="delConfirm" class="prompt modal fade" role="dialog">
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
						<p>Are you sure you want to delete this product?</p>

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
		<!-- //*******Delete confirmation popup End******** -->

	</form>

	<script type="text/javascript">
		$('.catHash a').click(function() {
			$('#addCategory').modal('show');
		});
		$('.catForm').on('keyup keypress', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode === 13) {
				e.preventDefault();
				return false;
			}
		});
	</script>
</body>
</html>
<!-- //*******Delete confirmation popup End******** -->
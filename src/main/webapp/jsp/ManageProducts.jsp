<%@page import="in.co.raystech.maven.project4.bean.ProductsBean"%>
<%@page import="in.co.raystech.maven.project4.bean.CategoryBean"%>
<%@page import="java.io.Console"%>
<%@page import="in.co.raystech.maven.project4.bean.UserBean"%>
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
	<%@include file="ProductsPopup.jsp"%>
	<jsp:useBean id="beanP" scope="request"
		class="in.co.raystech.maven.project4.bean.ProductsBean"></jsp:useBean>
	<div class="pd2 hdpad">
		<div class="clearfix tabTop">
			<div class="row">
				<div class="col-sm-6">
					<h4>Manage Products</h4>
				</div>
				<div class="col-sm-6">
					<div class="clearfix">
						<button onclick="$('#addProduct').modal('show');"
							class="btn btn-primary pull-right">
							<i class="fa fa-plus mrR"> </i> Add Product
						</button>
						<%
							if (bean2 != null) {
						%>
						<script>
							$('#editProduct').modal('show')
						</script>
						<%
							}
						%>


						<form action="<%=ORSView.MANAGE_PRODUCTS_CTL%>" method="post"
							id="productsForm" enctype="multipart/form-data">
							<div class="srchWrp pull-right mrR1">
								<i class="fa fa-search"></i> <input type="text"
									name="productName"
									value="<%=DataUtility.getStringData(ServletUtility.getParameter("name", request))%>"
									placeholder="Search" /> <input type="submit"
									style="display: none" name="operation"
									value="<%=ManageProductsCtl.OP_SEARCH%>" id="triggerSrch">
							</div>
							<script type="text/javascript">
								$('.srchWrp input')
										.keypress(
												function(e) {
													if (e.which == 13
															&& $(this).value.length > 0) {
														e.preventDefault();
														$('#productsForm')
																.submit();
													}
												});
							</script>

						</form>
					</div>
				</div>
			</div>
		</div>


		<div class="prdTbl tblPntr">
			<table class="table">
				<thead>
					<tr>
						<th>Logo</th>
						<th>Product Name</th>
						<th width="40%">Description</th>
						<th>Category</th>
						<th>Partnership offer</th>
					</tr>
				</thead>

				<tbody>
					<%
						List categoryList = (List) request.getAttribute("categoryList");
					%>

					<!-- 				<%=HTMLUtility.getList("categoryId", String.valueOf(bean.getId()), categoryList)%>
 -->

					<%
						List list = ServletUtility.getList(request);
						Iterator<ProductsBean> it = list.iterator();
						int size = list.size();
						while (it.hasNext()) {
							beanP = it.next();
					%>

					<tr class="prdTr">
						<td><a
							href="ManageProductsCtl?id=<%=DataUtility.getStringData(beanP.getId())%>"></a>
							<img src="<%=DataUtility.getStringData(beanP.getImageURL())%>"></td>
						<td><b><%=DataUtility.getStringData(beanP.getProductName())%></b></td>
						<td><%=DataUtility.getStringData(beanP.getDescription())%></td>
						<td><i class="fa fa-tag mrR"></i> <%
 	for (CategoryBean category : beanP.getCategories()) {
 %> <%=category.getCategory()%>, <%
 	}
 %></td>

						<td><%=DataUtility.getStringData(beanP.getPartnershipOffer())%></td>
					</tr>
					<%
						}
					%>
				</tbody>


			</table>
		</div>
	</div>

	<script type="text/javascript">
		if ($('.prdTr img').attr('src') == '') {
			$(this).attr('src', '../images/default.png')
		}
		$('.prdTr').click(function() {
			var getVal = $(this).find('a').attr("href");
			var url = window.location.origin;
			url = url + "/OnePartner/ctl/" + getVal;
			window.location.href = url
		})
	</script>



	<script>
		function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#ppic img').attr('src', e.target.result);
					$('#ppap img').attr('src', e.target.result);
				}
				reader.readAsDataURL(input.files[0]);
			}
		}

		$("#file").change(function() {
			readURL(this);
		});

		$("#imgInp").change(function() {
			readURL(this);
		});

		$('.prdTbl tbody tr').click(function() {
			$('#editProduct').modal('show');
		});

		$('#select1,#select2').select2({
			placeholder : 'Choose'
		});

		$('textarea').each(function() {
			$(this).val($(this).val().trim());
		});

		$('.nav>li:nth-child(3)').addClass('active');
	</script>

</body>
</html>
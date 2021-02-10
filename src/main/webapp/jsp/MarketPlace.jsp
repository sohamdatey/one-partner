<%@page import="in.co.raystech.maven.project4.controller.BaseCtl"%>
<%@page import="in.co.raystech.maven.project4.bean.ProductsBean"%>
<%@page import="in.co.raystech.maven.project4.bean.CategoryBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@page import="in.co.raystech.maven.project4.bean.UserBean"%>
<%@page import="in.co.raystech.maven.project4.controller.ORSView"%>
<%@page import="in.co.raystech.maven.project4.util.DataValidator"%>
<%@page import="in.co.raystech.maven.project4.util.DataUtility"%>
<%@page import="in.co.raystech.maven.project4.controller.LoginCtl"%>
<%@page import="javax.mail.Header"%>
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
</head>
<body>

	

		<jsp:useBean id="userBean"
			class="in.co.raystech.maven.project4.bean.UserBean" scope="session"></jsp:useBean>

		<jsp:useBean id="categoryBean"
			class="in.co.raystech.maven.project4.bean.CategoryBean"
			scope="request"></jsp:useBean>

		<jsp:useBean id="productBean"
			class="in.co.raystech.maven.project4.bean.ProductsBean"
			scope="request"></jsp:useBean>


		<%
			userBean = (UserBean) session.getAttribute("user");

			boolean userLoggedIn = userBean != null;

			String welcomeMsg = "";

			if (userLoggedIn) {

				welcomeMsg += userBean.getName();
			} else {
				welcomeMsg += "Guest";
			}
		%>
		<%
			List l = (List) request.getAttribute("categoryList");
			List highlitedCategories = (List) request.getAttribute("highlitedCategories");
		%>

		<nav class="navbar navbar-blue navbar-white navbar-fixed-top">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<!-- <a class="navbar-brand" href="#">Brand</a> -->
				<a href="#" class="navbar-brand">
					<h3>
						<span>ONE</span>Partner
					</h3>
				</a>

			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">

				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#"
						class="profDrop dropdown-toggle" data-toggle="dropdown"> Hi <%=welcomeMsg%>
							<img src="../images/profile.png" /> <small class="caret"
							style="margin: -2px 0 0 9px"></small>
					</a>
						<ul class="dropdown-menu">


							<li>
								<%
									if (userLoggedIn) {
								%>
							
							<li><a href="<%=ORSView.MY_PROFILE_CTL%>"><i
									class="fa fa-user-circle mrR"></i> View Profile</a></li>
							<li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>"><i
									class="fa fa-key mrR"></i> Change Password</a></li>
							<li role="separator" class="divider"></li>

							<a class="btn btn-inverse mrL1 mrB"
								href="<%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>"><i
								class="fa fa-power-off mrR"></i> Logout</b></a>

							<%
								} else {
							%>
							<a class="btn btn-inverse mrL1 mrB" href="<%=ORSView.LOGIN_CTL%>">Login</b></a>
							<%
								}
							%>
						</li>
				</ul>
				</li>
				</ul>

				<div class="clearfix">
				<form action="<%=ORSView.MARKET_PLACE_CTL%>" method="post" id="marketPlaceForm">
					<!-- <button onclick="$('#editTeam').modal('show');" class="btn btn-inverse dropdown-toggle pull-right" type="button" data-toggle="dropdown"><i class="fa fa-plus-circle mrR"> </i> ADD PEOPLE</button>-->
					<div class="srchWrp pull-right">
						<i class="fa fa-search"></i> <input type="text" name="productName"
							value="<%=ServletUtility.getParameter("productName", request)%>"
							placeholder="Search" /> <input type="submit"
							style="display: none" name="operation"
							value="<%=BaseCtl.OP_SEARCH%>" id="triggerMkt" />
					</div>
					<script type="text/javascript">
						$('#triggerMkt').keypress(function(e) {
							if (e.which == 13) {
								e.preventDefault();
								$('#marketPlaceForm').submit();
							}
						});
					</script>
				</form>
				</div>


			</div>
		</nav>
		<!-- / .navbar-->

	<form action="<%=ORSView.MARKET_PLACE_CTL%>" method="post"
		id="applyForm">

		<div class="weltxt">
			<b>Welcome <%=welcomeMsg%></b>, Explore our marketplace & choose what
			suits you best.
		</div>



		<div class="catTag clearfix">

			<%=HTMLUtility.hilighterMethod("ids", String.valueOf(categoryBean.getId()), highlitedCategories, l)%>


			<a class="more" onclick="moreFunc()"><label><span
					style="display: block;">Show More</span><span
					style="display: none;">Show Less</span></label></a> <input type="submit"
				class="btn filter" name="operation" value="<%=BaseCtl.OP_APPLY%>"
				style="display: none;" />
		</div>

		<script>

			function moreFunc(){
				$('.default').toggle('fast');
				$('.more span').toggle();
			};

		     
			$(".catTag input[type=checkbox]").click(function() {
			    var legchecked = $('.catTag input[type=checkbox]:checked').length;
			    if (legchecked > 0){$("input.filter").show('fast');}
			}); 

			 $(function () {
		            if (localStorage && localStorage["checked"]) {
		                var localStoredData = JSON.parse(localStorage["checked"]);
		                var checkboxes = $("input[name='ids']");
		                for (var i = 0; i < checkboxes.length; i++) {
		                    for (var j = 0; j < localStoredData.length; j++) {
		                        if (checkboxes[i].value == localStoredData[j]) {
		                            checkboxes[i].checked = true;
		                        }
		                    }
		                }
		                localStorage.removeItem('checked');
		            }
		            $("input[type=checkbox]").click(function () {
		                var data = $("input[name='ids']:checked").map(function () {
		                    return this.value;
		                }).get();
		                localStorage['checked'] = JSON.stringify(data);
		            });

				    var legchecked = $('.catTag input[type=checkbox]:checked').length;
				    var leghidchecked = $('.default input[type=checkbox]:checked').length;
	                if (legchecked > 0){$("input.filter").show();}
				    else{$("input.filter").hide();}
	                if (leghidchecked > 0){$(".default").css({'display':'inline-block'});}
				    else{$(".default").hide();}
		        }); 
		</script>

		<div class="pd2 prodCnt">
			<div class="clearfix">
				<div class="row" id="grid" data-columns>
					<%
						List list = ServletUtility.getList(request);
						Iterator<ProductsBean> it = list.iterator();
						while (it.hasNext()) {
							productBean = it.next();
					%>
					<div class="prdBox">
						<img src="<%=productBean.getImageURL()%>" />
						<h4><%=productBean.getProductName()%></h4>
						<p><%=productBean.getDescription()%></p>
						<small><i class="fa fa-tag mrR"></i> <%
 	for (CategoryBean category : productBean.getCategories()) {
 %> <%=category.getCategory()%>, <%
 	}
 %> </small>

						<%
							if (productBean.getPartnershipOffer() != null) {
						%>
						<span class="offer"><b><i class="fa fa-gift mrR"></i>
								Partnership offer :</b>
							<p><%=productBean.getPartnershipOffer()%></p> </span>

						<%
							} else {
						%>

						<!-- No partnership offer -->

						<%
							}
						%>


						<a href="javascript:void(0)"
							onclick="window.open('<%=productBean.getFormLink()%>', '_blank');"
							class="btn btn-primary btn-lg btn-block">JOIN NOW</a>
					</div>
					<%
						}
					%>
				</div>
			</div>
		</div>

		<%
			if (DataValidator.isNotNull(DataUtility.getString(ServletUtility.getSuccessMessage(request)))) {
		%>
		<font color="#369a47"><%=ServletUtility.getSuccessMessage(request)%></font>
		<%
			} else if (DataUtility.getString(ServletUtility.getErrorMessage(request)) != null) {
		%>
		<font color="#ff3030"><%=ServletUtility.getErrorMessage(request)%></font>
		<%
			}
		%>



		<script>
			if ($('.offer p').is(':empty')) {
				$(this).parent().hide();
			}
		</script>
		<script src="../js/salvattore.min.js"></script>
	</form>
</body>
</html>
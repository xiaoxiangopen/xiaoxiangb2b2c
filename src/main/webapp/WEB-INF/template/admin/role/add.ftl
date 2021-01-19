<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.role.add")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $roleForm = $("#roleForm");
				var $roleGroup = $("#roleForm a.role-group");
				var $permissions = $("#roleForm input[name='permissions']");
				
				// 权限组
				$roleGroup.click(function() {
					var $element = $(this);
					var $groupPermissions = $element.closest("div.form-group").find("input[name='permissions']");
					
					$groupPermissions.prop("checked", $groupPermissions.filter(":checked").length < 1);
					$roleForm.validate().element($permissions);
					return false;
				});
				
				// 表单验证
				$roleForm.validate({
					rules: {
						name: "required",
						permissions: "required"
					}
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="admin">
	[#include "/admin/include/main_header.ftl" /]
	[#include "/admin/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<ol class="breadcrumb">
				<li>
					<a href="${base}/admin/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("admin.role.add")}</li>
			</ol>
			<form id="roleForm" class="ajax-form form-horizontal" action="${base}/admin/role/save" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.role.add")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("Role.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="description">${message("Role.description")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="description" name="description" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.storeGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="business" name="permissions" type="checkbox" value="admin:business">
									<label for="business">${message("admin.role.business")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="store" name="permissions" type="checkbox" value="admin:store">
									<label for="store">${message("admin.role.store")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="storeCategory" name="permissions" type="checkbox" value="admin:storeCategory">
									<label for="storeCategory">${message("admin.role.storeCategory")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="storeRank" name="permissions" type="checkbox" value="admin:storeRank">
									<label for="storeRank">${message("admin.role.storeRank")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="businessAttribute" name="permissions" type="checkbox" value="admin:businessAttribute">
									<label for="businessAttribute">${message("admin.role.businessAttribute")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="businessCash" name="permissions" type="checkbox" value="admin:businessCash">
									<label for="businessCash">${message("admin.role.businessCash")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="categoryApplication" name="permissions" type="checkbox" value="admin:categoryApplication">
									<label for="categoryApplication">${message("admin.role.categoryApplication")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="businessDeposit" name="permissions" type="checkbox" value="admin:businessDeposit">
									<label for="businessDeposit">${message("admin.role.businessDeposit")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.productGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="product" name="permissions" type="checkbox" value="admin:product">
									<label for="product">${message("admin.role.product")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="stock" name="permissions" type="checkbox" value="admin:stock">
									<label for="stock">${message("admin.role.stock")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="productCategory" name="permissions" type="checkbox" value="admin:productCategory">
									<label for="productCategory">${message("admin.role.productCategory")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="productTag" name="permissions" type="checkbox" value="admin:productTag">
									<label for="productTag">${message("admin.role.productTag")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="parameter" name="permissions" type="checkbox" value="admin:parameter">
									<label for="parameter">${message("admin.role.parameter")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="attribute" name="permissions" type="checkbox" value="admin:attribute">
									<label for="attribute">${message("admin.role.attribute")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="specification" name="permissions" type="checkbox" value="admin:specification">
									<label for="specification">${message("admin.role.specification")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="brand" name="permissions" type="checkbox" value="admin:brand">
									<label for="brand">${message("admin.role.brand")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.orderGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="order" name="permissions" type="checkbox" value="admin:order">
									<label for="order">${message("admin.role.order")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="print" name="permissions" type="checkbox" value="admin:print">
									<label for="print">${message("admin.role.print")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderPayment" name="permissions" type="checkbox" value="admin:orderPayment">
									<label for="orderPayment">${message("admin.role.orderPayment")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderRefunds" name="permissions" type="checkbox" value="admin:orderRefunds">
									<label for="orderRefunds">${message("admin.role.orderRefunds")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderShipping" name="permissions" type="checkbox" value="admin:orderShipping">
									<label for="orderShipping">${message("admin.role.orderShipping")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderReturns" name="permissions" type="checkbox" value="admin:orderReturns">
									<label for="orderReturns">${message("admin.role.orderReturns")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="deliveryCenter" name="permissions" type="checkbox" value="admin:deliveryCenter">
									<label for="deliveryCenter">${message("admin.role.deliveryCenter")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="deliveryTemplate" name="permissions" type="checkbox" value="admin:deliveryTemplate">
									<label for="deliveryTemplate">${message("admin.role.deliveryTemplate")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="aftersales" name="permissions" type="checkbox" value="admin:aftersales">
									<label for="aftersales">${message("admin.role.aftersales")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.memberGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="member" name="permissions" type="checkbox" value="admin:member">
									<label for="member">${message("admin.role.member")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="memberRank" name="permissions" type="checkbox" value="admin:memberRank">
									<label for="memberRank">${message("admin.role.memberRank")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="memberAttribute" name="permissions" type="checkbox" value="admin:memberAttribute">
									<label for="memberAttribute">${message("admin.role.memberAttribute")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="point" name="permissions" type="checkbox" value="admin:point">
									<label for="point">${message("admin.role.point")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="memberDeposit" name="permissions" type="checkbox" value="admin:memberDeposit">
									<label for="memberDeposit">${message("admin.role.memberDeposit")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="review" name="permissions" type="checkbox" value="admin:review">
									<label for="review">${message("admin.role.review")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="consultation" name="permissions" type="checkbox" value="admin:consultation">
									<label for="consultation">${message("admin.role.consultation")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="messageConfig" name="permissions" type="checkbox" value="admin:messageConfig">
									<label for="messageConfig">${message("admin.role.messageConfig")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.distributorGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="distributor" name="permissions" type="checkbox" value="admin:distributor">
									<label for="distributor">${message("admin.role.distributor")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="distributionCash" name="permissions" type="checkbox" value="admin:distributionCash">
									<label for="distributionCash">${message("admin.role.distributionCash")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="distributionCommission" name="permissions" type="checkbox" value="admin:distributionCommission">
									<label for="distributionCommission">${message("admin.role.distributionCommission")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.contentGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="navigationGroup" name="permissions" type="checkbox" value="admin:navigationGroup">
									<label for="navigationGroup">${message("admin.role.navigationGroup")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="navigation" name="permissions" type="checkbox" value="admin:navigation">
									<label for="navigation">${message("admin.role.navigation")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="article" name="permissions" type="checkbox" value="admin:article">
									<label for="article">${message("admin.role.article")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="articleCategory" name="permissions" type="checkbox" value="admin:articleCategory">
									<label for="articleCategory">${message("admin.role.articleCategory")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="articleTag" name="permissions" type="checkbox" value="admin:articleTag">
									<label for="articleTag">${message("admin.role.articleTag")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="friendLink" name="permissions" type="checkbox" value="admin:friendLink">
									<label for="friendLink">${message("admin.role.friendLink")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="adPosition" name="permissions" type="checkbox" value="admin:adPosition">
									<label for="adPosition">${message("admin.role.adPosition")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="ad" name="permissions" type="checkbox" value="admin:ad">
									<label for="ad">${message("admin.role.ad")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="template" name="permissions" type="checkbox" value="admin:template">
									<label for="template">${message("admin.role.template")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="cache" name="permissions" type="checkbox" value="admin:cache">
									<label for="cache">${message("admin.role.cache")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.marketingGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">

								<div class="checkbox checkbox-inline">
									<input id="seo" name="permissions" type="checkbox" value="admin:seo">
									<label for="seo">${message("admin.role.seo")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.systemGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="setting" name="permissions" type="checkbox" value="admin:setting">
									<label for="setting">${message("admin.role.setting")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="area" name="permissions" type="checkbox" value="admin:area">
									<label for="area">${message("admin.role.area")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="paymentMethod" name="permissions" type="checkbox" value="admin:paymentMethod">
									<label for="paymentMethod">${message("admin.role.paymentMethod")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="shippingMethod" name="permissions" type="checkbox" value="admin:shippingMethod">
									<label for="shippingMethod">${message("admin.role.shippingMethod")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="deliveryCorp" name="permissions" type="checkbox" value="admin:deliveryCorp">
									<label for="deliveryCorp">${message("admin.role.deliveryCorp")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="paymentPlugin" name="permissions" type="checkbox" value="admin:paymentPlugin">
									<label for="paymentPlugin">${message("admin.role.paymentPlugin")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="storagePlugin" name="permissions" type="checkbox" value="admin:storagePlugin">
									<label for="storagePlugin">${message("admin.role.storagePlugin")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="loginPlugin" name="permissions" type="checkbox" value="admin:loginPlugin">
									<label for="loginPlugin">${message("admin.role.loginPlugin")}</label>
								</div>

								<div class="checkbox checkbox-inline">
									<input id="admin" name="permissions" type="checkbox" value="admin:admin">
									<label for="admin">${message("admin.role.admin")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="role" name="permissions" type="checkbox" value="admin:role">
									<label for="role">${message("admin.role.role")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="auditLog" name="permissions" type="checkbox" value="admin:auditLog">
									<label for="auditLog">${message("admin.role.auditLog")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.statisticGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="orderStatistic" name="permissions" type="checkbox" value="admin:orderStatistic">
									<label for="orderStatistic">${message("admin.role.orderStatistic")}</label>
								</div>
								[#--<div class="checkbox checkbox-inline">--]
									[#--<input id="fundStatistic" name="permissions" type="checkbox" value="admin:fundStatistic">--]
									[#--<label for="fundStatistic">${message("admin.role.fundStatistic")}</label>--]
								[#--</div>--]
								<div class="checkbox checkbox-inline">
									<input id="registerStatistic" name="permissions" type="checkbox" value="admin:registerStatistic">
									<label for="registerStatistic">${message("admin.role.registerStatistic")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="productRanking" name="permissions" type="checkbox" value="admin:productRanking">
									<label for="productRanking">${message("admin.role.productRanking")}</label>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("common.submit")}</button>
								<button class="btn btn-default" type="button" data-action="back">${message("common.back")}</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>
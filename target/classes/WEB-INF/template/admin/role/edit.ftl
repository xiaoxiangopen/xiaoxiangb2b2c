<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.role.edit")} - 小象电商</title>
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
				<li class="active">${message("admin.role.edit")}</li>
			</ol>
			<form id="roleForm" class="ajax-form form-horizontal" action="${base}/admin/role/update" method="post">
				<input name="id" type="hidden" value="${role.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.role.edit")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("Role.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" value="${role.name}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="description">${message("Role.description")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="description" name="description" class="form-control" type="text" value="${role.description}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">
								<a class="role-group" href="javascript:;" title="${message("admin.role.roleGroupTitle")}" data-toggle="tooltip">${message("admin.role.storeGroup")}:</a>
							</label>
							<div class="col-xs-9 col-sm-10 permissions">
								<div class="checkbox checkbox-inline">
									<input id="business" name="permissions" type="checkbox" value="admin:business"[#if role.permissions?seq_contains("admin:business")] checked[/#if]>
									<label for="business">${message("admin.role.business")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="store" name="permissions" type="checkbox" value="admin:store"[#if role.permissions?seq_contains("admin:store")] checked[/#if]>
									<label for="store">${message("admin.role.store")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="storeCategory" name="permissions" type="checkbox" value="admin:storeCategory"[#if role.permissions?seq_contains("admin:storeCategory")] checked[/#if]>
									<label for="storeCategory">${message("admin.role.storeCategory")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="storeRank" name="permissions" type="checkbox" value="admin:storeRank"[#if role.permissions?seq_contains("admin:storeRank")] checked[/#if]>
									<label for="storeRank">${message("admin.role.storeRank")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="businessAttribute" name="permissions" type="checkbox" value="admin:businessAttribute"[#if role.permissions?seq_contains("admin:businessAttribute")] checked[/#if]>
									<label for="businessAttribute">${message("admin.role.businessAttribute")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="businessCash" name="permissions" type="checkbox" value="admin:businessCash"[#if role.permissions?seq_contains("admin:businessCash")] checked[/#if]>
									<label for="businessCash">${message("admin.role.businessCash")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="categoryApplication" name="permissions" type="checkbox" value="admin:categoryApplication"[#if role.permissions?seq_contains("admin:categoryApplication")] checked[/#if]>
									<label for="categoryApplication">${message("admin.role.categoryApplication")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="businessDeposit" name="permissions" type="checkbox" value="admin:businessDeposit"[#if role.permissions?seq_contains("admin:businessDeposit")] checked[/#if]>
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
									<input id="product" name="permissions" type="checkbox" value="admin:product"[#if role.permissions?seq_contains("admin:product")] checked[/#if]>
									<label for="product">${message("admin.role.product")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="stock" name="permissions" type="checkbox" value="admin:stock"[#if role.permissions?seq_contains("admin:stock")] checked[/#if]>
									<label for="stock">${message("admin.role.stock")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="productCategory" name="permissions" type="checkbox" value="admin:productCategory"[#if role.permissions?seq_contains("admin:productCategory")] checked[/#if]>
									<label for="productCategory">${message("admin.role.productCategory")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="productTag" name="permissions" type="checkbox" value="admin:productTag"[#if role.permissions?seq_contains("admin:productTag")] checked[/#if]>
									<label for="productTag">${message("admin.role.productTag")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="parameter" name="permissions" type="checkbox" value="admin:parameter"[#if role.permissions?seq_contains("admin:parameter")] checked[/#if]>
									<label for="parameter">${message("admin.role.parameter")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="attribute" name="permissions" type="checkbox" value="admin:attribute"[#if role.permissions?seq_contains("admin:attribute")] checked[/#if]>
									<label for="attribute">${message("admin.role.attribute")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="specification" name="permissions" type="checkbox" value="admin:specification"[#if role.permissions?seq_contains("admin:specification")] checked[/#if]>
									<label for="specification">${message("admin.role.specification")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="brand" name="permissions" type="checkbox" value="admin:brand"[#if role.permissions?seq_contains("admin:brand")] checked[/#if]>
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
									<input id="order" name="permissions" type="checkbox" value="admin:order"[#if role.permissions?seq_contains("admin:order")] checked[/#if]>
									<label for="order">${message("admin.role.order")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="print" name="permissions" type="checkbox" value="admin:print"[#if role.permissions?seq_contains("admin:print")] checked[/#if]>
									<label for="print">${message("admin.role.print")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderPayment" name="permissions" type="checkbox" value="admin:orderPayment"[#if role.permissions?seq_contains("admin:orderPayment")] checked[/#if]>
									<label for="orderPayment">${message("admin.role.orderPayment")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderRefunds" name="permissions" type="checkbox" value="admin:orderRefunds"[#if role.permissions?seq_contains("admin:orderRefunds")] checked[/#if]>
									<label for="orderRefunds">${message("admin.role.orderRefunds")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderShipping" name="permissions" type="checkbox" value="admin:orderShipping"[#if role.permissions?seq_contains("admin:orderShipping")] checked[/#if]>
									<label for="orderShipping">${message("admin.role.orderShipping")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="orderReturns" name="permissions" type="checkbox" value="admin:orderReturns"[#if role.permissions?seq_contains("admin:orderReturns")] checked[/#if]>
									<label for="orderReturns">${message("admin.role.orderReturns")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="deliveryCenter" name="permissions" type="checkbox" value="admin:deliveryCenter"[#if role.permissions?seq_contains("admin:deliveryCenter")] checked[/#if]>
									<label for="deliveryCenter">${message("admin.role.deliveryCenter")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="deliveryTemplate" name="permissions" type="checkbox" value="admin:deliveryTemplate"[#if role.permissions?seq_contains("admin:deliveryTemplate")] checked[/#if]>
									<label for="deliveryTemplate">${message("admin.role.deliveryTemplate")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="aftersales" name="permissions" type="checkbox" value="admin:aftersales"[#if role.permissions?seq_contains("admin:aftersales")] checked[/#if]>
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
									<input id="member" name="permissions" type="checkbox" value="admin:member"[#if role.permissions?seq_contains("admin:member")] checked[/#if]>
									<label for="member">${message("admin.role.member")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="memberRank" name="permissions" type="checkbox" value="admin:memberRank"[#if role.permissions?seq_contains("admin:memberRank")] checked[/#if]>
									<label for="memberRank">${message("admin.role.memberRank")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="memberAttribute" name="permissions" type="checkbox" value="admin:memberAttribute"[#if role.permissions?seq_contains("admin:memberAttribute")] checked[/#if]>
									<label for="memberAttribute">${message("admin.role.memberAttribute")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="point" name="permissions" type="checkbox" value="admin:point"[#if role.permissions?seq_contains("admin:point")] checked[/#if]>
									<label for="point">${message("admin.role.point")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="memberDeposit" name="permissions" type="checkbox" value="admin:memberDeposit"[#if role.permissions?seq_contains("admin:memberDeposit")] checked[/#if]>
									<label for="memberDeposit">${message("admin.role.memberDeposit")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="memberDeposit" name="permissions" type="checkbox" value="admin:memberDepositInfo"[#if role.permissions?seq_contains("admin:memberDepositInfo")] checked[/#if]>
									<label for="memberDeposit">${message("admin.role.memberDepositLog")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="review" name="permissions" type="checkbox" value="admin:review"[#if role.permissions?seq_contains("admin:review")] checked[/#if]>
									<label for="review">${message("admin.role.review")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="consultation" name="permissions" type="checkbox" value="admin:consultation"[#if role.permissions?seq_contains("admin:consultation")] checked[/#if]>
									<label for="consultation">${message("admin.role.consultation")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="messageConfig" name="permissions" type="checkbox" value="admin:messageConfig"[#if role.permissions?seq_contains("admin:messageConfig")] checked[/#if]>
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
									<input id="distributor" name="permissions" type="checkbox" value="admin:distributor"[#if role.permissions?seq_contains("admin:distributor")] checked[/#if]>
									<label for="distributor">${message("admin.role.distributor")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="distributionCash" name="permissions" type="checkbox" value="admin:distributionCash"[#if role.permissions?seq_contains("admin:distributionCash")] checked[/#if]>
									<label for="distributionCash">${message("admin.role.distributionCash")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="distributionCommission" name="permissions" type="checkbox" value="admin:distributionCommission"[#if role.permissions?seq_contains("admin:distributionCommission")] checked[/#if]>
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
									<input id="navigationGroup" name="permissions" type="checkbox" value="admin:navigationGroup"[#if role.permissions?seq_contains("admin:navigationGroup")] checked[/#if]>
									<label for="navigationGroup">${message("admin.role.navigationGroup")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="navigation" name="permissions" type="checkbox" value="admin:navigation"[#if role.permissions?seq_contains("admin:navigation")] checked[/#if]>
									<label for="navigation">${message("admin.role.navigation")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="article" name="permissions" type="checkbox" value="admin:article"[#if role.permissions?seq_contains("admin:article")] checked[/#if]>
									<label for="article">${message("admin.role.article")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="articleCategory" name="permissions" type="checkbox" value="admin:articleCategory"[#if role.permissions?seq_contains("admin:articleCategory")] checked[/#if]>
									<label for="articleCategory">${message("admin.role.articleCategory")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="articleTag" name="permissions" type="checkbox" value="admin:articleTag"[#if role.permissions?seq_contains("admin:articleTag")] checked[/#if]>
									<label for="articleTag">${message("admin.role.articleTag")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="friendLink" name="permissions" type="checkbox" value="admin:friendLink"[#if role.permissions?seq_contains("admin:friendLink")] checked[/#if]>
									<label for="friendLink">${message("admin.role.friendLink")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="adPosition" name="permissions" type="checkbox" value="admin:adPosition"[#if role.permissions?seq_contains("admin:adPosition")] checked[/#if]>
									<label for="adPosition">${message("admin.role.adPosition")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="ad" name="permissions" type="checkbox" value="admin:ad"[#if role.permissions?seq_contains("admin:ad")] checked[/#if]>
									<label for="ad">${message("admin.role.ad")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="template" name="permissions" type="checkbox" value="admin:template"[#if role.permissions?seq_contains("admin:template")] checked[/#if]>
									<label for="template">${message("admin.role.template")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="cache" name="permissions" type="checkbox" value="admin:cache"[#if role.permissions?seq_contains("admin:cache")] checked[/#if]>
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
									<input id="seo" name="permissions" type="checkbox" value="admin:seo"[#if role.permissions?seq_contains("admin:seo")] checked[/#if]>
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
									<input id="setting" name="permissions" type="checkbox" value="admin:setting"[#if role.permissions?seq_contains("admin:setting")] checked[/#if]>
									<label for="setting">${message("admin.role.setting")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="area" name="permissions" type="checkbox" value="admin:area"[#if role.permissions?seq_contains("admin:area")] checked[/#if]>
									<label for="area">${message("admin.role.area")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="paymentMethod" name="permissions" type="checkbox" value="admin:paymentMethod"[#if role.permissions?seq_contains("admin:paymentMethod")] checked[/#if]>
									<label for="paymentMethod">${message("admin.role.paymentMethod")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="shippingMethod" name="permissions" type="checkbox" value="admin:shippingMethod"[#if role.permissions?seq_contains("admin:shippingMethod")] checked[/#if]>
									<label for="shippingMethod">${message("admin.role.shippingMethod")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="deliveryCorp" name="permissions" type="checkbox" value="admin:deliveryCorp"[#if role.permissions?seq_contains("admin:deliveryCorp")] checked[/#if]>
									<label for="deliveryCorp">${message("admin.role.deliveryCorp")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="paymentPlugin" name="permissions" type="checkbox" value="admin:paymentPlugin"[#if role.permissions?seq_contains("admin:paymentPlugin")] checked[/#if]>
									<label for="paymentPlugin">${message("admin.role.paymentPlugin")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="storagePlugin" name="permissions" type="checkbox" value="admin:storagePlugin"[#if role.permissions?seq_contains("admin:storagePlugin")] checked[/#if]>
									<label for="storagePlugin">${message("admin.role.storagePlugin")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="loginPlugin" name="permissions" type="checkbox" value="admin:loginPlugin"[#if role.permissions?seq_contains("admin:loginPlugin")] checked[/#if]>
									<label for="loginPlugin">${message("admin.role.loginPlugin")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="admin" name="permissions" type="checkbox" value="admin:admin"[#if role.permissions?seq_contains("admin:admin")] checked[/#if]>
									<label for="admin">${message("admin.role.admin")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="role" name="permissions" type="checkbox" value="admin:role"[#if role.permissions?seq_contains("admin:role")] checked[/#if]>
									<label for="role">${message("admin.role.role")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="auditLog" name="permissions" type="checkbox" value="admin:auditLog"[#if role.permissions?seq_contains("admin:auditLog")] checked[/#if]>
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
									<input id="orderStatistic" name="permissions" type="checkbox" value="admin:orderStatistic"[#if role.permissions?seq_contains("admin:orderStatistic")] checked[/#if]>
									<label for="orderStatistic">${message("admin.role.orderStatistic")}</label>
								</div>
								[#--<div class="checkbox checkbox-inline">--]
									[#--<input id="fundStatistic" name="permissions" type="checkbox" value="admin:fundStatistic"[#if role.permissions?seq_contains("admin:fundStatistic")] checked[/#if]>--]
									[#--<label for="fundStatistic">${message("admin.role.fundStatistic")}</label>--]
								[#--</div>--]
								<div class="checkbox checkbox-inline">
									<input id="registerStatistic" name="permissions" type="checkbox" value="admin:registerStatistic"[#if role.permissions?seq_contains("admin:registerStatistic")] checked[/#if]>
									<label for="registerStatistic">${message("admin.role.registerStatistic")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input id="productRanking" name="permissions" type="checkbox" value="admin:productRanking"[#if role.permissions?seq_contains("admin:productRanking")] checked[/#if]>
									<label for="productRanking">${message("admin.role.productRanking")}</label>
								</div>
							</div>
						</div>
						[#if role.isSystem]
							<div class="form-group">
								<div class="col-xs-9 col-sm-4 col-xs-offset-3 col-sm-offset-2">
									<div class="alert alert-warning">${message("admin.role.editSystemNotAllowed")}</div>
								</div>
							</div>
						[/#if]
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit"[#if role.isSystem] disabled[/#if]>${message("common.submit")}</button>
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
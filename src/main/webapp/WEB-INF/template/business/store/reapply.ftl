<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.store.reapply")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/summernote.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/summernote.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $mainHeaderNav = $("header.main-header a").not("a.logout");
				var $mainSidebarNav = $("aside.main-sidebar a, aside.main-sidebar button");
				var $storeForm = $("#storeForm");
				var $storeRank = $("#storeRank");
				var $quantity = $("#quantity");
				var $serviceFee = $("#serviceFee");
				var $storeCategory = $("#storeCategory");
				var $bail = $("#bail");
				var quantity = $storeRank.find(":selected").data("quantity");
				var serviceFee = $storeRank.find(":selected").data("service-fee");
				var bail = $storeCategory.find(":selected").data("bail");
				
				// 顶部、侧边导航
				$mainHeaderNav.add($mainSidebarNav).click(function() {
					return false;
				});
				
				// 店铺等级
				$quantity.text(quantity);
				$serviceFee.text($.currency(serviceFee, true, true));
				$storeRank.change(function() {
					var $selected = $(this).find(":selected");
					var quantity = $selected.data("quantity");
					var serviceFee = $selected.data("service-fee");
					
					$quantity.velocity("fadeIn").text(quantity);
					$serviceFee.velocity("fadeIn").text($.currency(serviceFee, true, true));
				});
				
				// 店铺分类
				$bail.text($.currency(bail, true, true));
				$storeCategory.change(function() {
					var $element = $(this);
					var bail = $element.find(":selected").data("bail");
					
					$bail.velocity("fadeIn").text($.currency(bail, true, true));
				});
				
				// 表单验证
				$storeForm.validate({
					rules: {
						name: {
							required: true,
							remote: {
								url: "${base}/business/store/check_name",
								data: {
									id: ${currentStore.id}
								},
								cache: false
							}
						},
						mobile: {
							required: true,
							mobile: true
						},
						email: {
							required: true,
							email: true
						}
					},
					messages: {
						name: {
							remote: "${message("common.validator.exist")}"
						},
						mobile: {
							remote: "${message("common.validator.exist")}"
						},
						email: {
							remote: "${message("common.validator.exist")}"
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/index"
						});
					}
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="business">
	[#include "/business/include/main_header.ftl" /]
	[#include "/business/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<ol class="breadcrumb">
				<li class="active">${message("business.store.reapply")}</li>
			</ol>
			<form id="storeForm" class="form-horizontal" action="${base}/business/store/reapply" method="post">
				<input name="id" type="hidden" value="${currentStore.id}">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#info" data-toggle="tab">${message("business.store.info")}</a>
							</li>
							<li>
								<a href="#productCategories" data-toggle="tab">${message("Store.productCategories")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="info" class="tab-pane active">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("Store.name")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="name" name="name" class="form-control" type="text" value="${currentStore.name}" maxlength="200">
									</div>
								</div>
								<div class="form-group[#if !storeRanks??] hidden-element[/#if]">
									<label class="col-xs-3 col-sm-2 control-label">${message("Store.storeRank")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select id="storeRank" name="storeRankId" class="selectpicker form-control" data-size="5">
											[#list storeRanks as storeRank]
												[#if storeRank.isAllowRegister]
													<option value="${storeRank.id}" data-quantity="${(storeRank.quantity)!'${message("business.store.infiniteQuantity")}'}" data-service-fee="${storeRank.serviceFee}"[#if storeRank == currentStore.storeRank] selected[/#if]>${storeRank.name}</option>
												[/#if]
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("StoreRank.quantity")}:</label>
									<div class="col-xs-9 col-sm-4">
										<p id="quantity" class="form-control-static"></p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("StoreRank.serviceFee")}:</label>
									<div class="col-xs-9 col-sm-4">
										<p id="serviceFee" class="form-control-static text-red"></p>
									</div>
								</div>
								<div class="form-group[#if !storeCategories??] hidden-element[/#if]">
									<label class="col-xs-3 col-sm-2 control-label">${message("Store.storeCategory")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select id="storeCategory" name="storeCategoryId" class="selectpicker form-control" data-size="5">
											[#list storeCategories as storeCategory]
												<option value="${storeCategory.id}" data-bail="${storeCategory.bail}"[#if currentStore.storeCategory.id == storeCategory.id] selected[/#if]>${storeCategory.name}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("StoreCategory.bail")}:</label>
									<div class="col-xs-9 col-sm-4">
										<p id="bail" class="form-control-static text-red"></p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="mobile">${message("Store.mobile")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="mobile" name="mobile" class="form-control" type="text" value="${currentStore.mobile}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="email">${message("Store.email")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="email" name="email" class="form-control" type="text" value="${currentStore.email}" maxlength="200">
									</div>
								</div>
							</div>
							<div id="productCategories" class="tab-pane">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Store.storeCategory")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="productCategoryIds" class="selectpicker form-control" data-none-selected-text="${message("common.choose")}" multiple>
											[#list productCategoryTree as productCategory]
												<option name="${productCategory.generalRate}" value="${productCategory.id}" title="${productCategory.name}"[#list currentStore.productCategories as currentProductCategory][#if currentProductCategory.id == productCategory.id] selected[/#if][/#list]>
													[#if productCategory.grade != 0]
														[#list 1..productCategory.grade as i]
															&nbsp;&nbsp;
														[/#list]
													[/#if]
													${productCategory.name}
												</option>
											[/#list]
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("business.store.submit")}</button>
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
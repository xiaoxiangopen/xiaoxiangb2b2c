<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.stock.stockOut")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/ajax-bootstrap-select.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/ajax-bootstrap-select.js"></script>
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
			
				var $stockForm = $("#stockForm");
				var $skuSn = $("#skuSn");
				var $stock = $("#stock");
				var $quantity = $("#quantity");
				
				// SKU
				$skuSn.selectpicker({
					liveSearch: true
				}).ajaxSelectPicker({
					ajax: {
						url: "${base}/business/stock/sku_select",
						type: "GET",
						data: function() {
							return {
								keyword: "{{{q}}}"
							};
						},
						dataType: "json"
					},
					preprocessData: function(data) {
						return $.map(data, function(item) {
							return {
								value: item.sn,
								text: item.name,
								data: {
									subtext: item.specifications.length > 0 ? '<span class="text-gray">[' + _.escape(item.specifications.join(",")) + ']</span>' : null,
									stock: item.stock
								},
								disabled: false
							};
						});
					},
					preserveSelected: false
				}).change(function() {
					$stockForm.validate().element($skuSn);
				});
				
				// 库存
				$skuSn.on("hidden.bs.select", function() {
					var value = $skuSn.val();
					var stock = $skuSn.find("option:selected").data("stock");
					
					if ($.trim(value) != "") {
						$stock.text(stock);
						if ($stock.is(":hidden")) {
							$stock.closest("div.form-group").velocity("slideDown");
							$quantity.prop("disabled", false).closest("div.form-group").velocity("slideDown");
						}
					} else {
						$stock.text("-");
						if ($stock.is(":visible")) {
							$stock.closest("div.form-group").velocity("slideUp");
							$quantity.prop("disabled", true).closest("div.form-group").velocity("slideUp");
						}
					}
					$stockForm.validate().element($skuSn);
				});
				
				// 表单验证
				$stockForm.validate({
					rules: {
						skuSn: "required",
						quantity: {
							required: true,
							integer: true,
							min: 1
						}
					},
					submitHandler: function(form) {
						var stock = parseInt($stock.text());
						var quantity = parseInt($quantity.val());
						
						if (stock != null && stock - quantity < 0) {
							$.bootstrapGrowl("${message("business.stock.insufficientStock")}", {
								type: "warning"
							});
							return false;
						}
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/stock/log"
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
				<li>
					<a href="${base}/business/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("business.stock.stockOut")}</li>
			</ol>
			<form id="stockForm" class="form-horizontal" action="${base}/business/stock/stock_out" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.stock.stockOut")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("business.stock.skuSelect")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select id="skuSn" name="skuSn" class="form-control" title="${message("business.stock.skuTitle")}">
									[#if sku??]
										<option value="${sku.sn}" selected>${sku.name}</option>
									[/#if]
								</select>
							</div>
						</div>
						<div class="[#if !sku.sn?has_content]hidden-element[/#if] form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("Sku.stock")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p id="stock" class="text-red form-control-static">${(sku.stock)!}</p>
							</div>
						</div>
						<div class="[#if !sku.sn?has_content]hidden-element[/#if] form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="quantity">${message("business.stock.stockOutQuantity")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="quantity" name="quantity" class="form-control" type="text" maxlength="9"[#if !sku.sn?has_content] disabled[/#if]>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="memo">${message("business.stock.memo")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="memo" name="memo" class="form-control" type="text" maxlength="200">
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
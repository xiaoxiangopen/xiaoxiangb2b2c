<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.businessDeposit.adjust")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/ajax-bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
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
	<script src="${base}/resources/admin/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $businessDepositForm = $("#businessDepositForm");
				var $businessId = $("#businessId");
				var $availableBalance = $("#availableBalance");
				var $amount = $("#amount");
				
				// 商家选择
				$businessId.selectpicker({
					liveSearch: true
				}).ajaxSelectPicker({
					ajax: {
						url: "${base}/admin/business_deposit/business_select",
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
								value: item.id,
								text: item.name,
								data: {
									availablebalance: item.availableBalance
								}
							};
						});
					},
					preserveSelected: false
				}).change(function() {
					$businessDepositForm.validate().element($businessId);
				});
				
				// 余额
				$businessId.on("hidden.bs.select", function() {
					var value = $businessId.val();
					var availableBalance = $businessId.find("option:selected").data("availablebalance");
					
					if ($.trim(value) != "") {
						$availableBalance.text($.currency(availableBalance, true, true));
						if ($availableBalance.is(":hidden")) {
							$availableBalance.closest("div.form-group").velocity("slideDown");
							$amount.prop("disabled", false).closest("div.form-group").velocity("slideDown");
						}
					} else {
						$availableBalance.text("-");
						if ($availableBalance.is(":visible")) {
							$availableBalance.closest("div.form-group").velocity("slideUp");
							$amount.prop("disabled", true).closest("div.form-group").velocity("slideUp");
						}
					}
					$businessDepositForm.validate().element($businessId);
				});
				
				$.validator.addMethod("nonzero",
					function(value, element) {
						return !$.isNumeric(value) || parseFloat(value) != 0;
					},
					"${message("admin.businessDeposit.nonzero")}"
				);
				
				$.validator.addMethod("insufficientBalance",
					function(value, element) {
						var availableBalance = parseFloat($businessId.find("option:selected").data("availablebalance"));
						
						return availableBalance + parseFloat(value) >= 0
					},
					"${message("admin.businessDeposit.insufficientBalance")}"
				);
				
				// 表单验证
				$businessDepositForm.validate({
					rules: {
						businessId: "required",
						amount: {
							required: true,
							number: true,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							},
							nonzero: true,
							insufficientBalance: true
						}
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
				<li class="active">${message("admin.businessDeposit.adjust")}</li>
			</ol>
			<form id="businessDepositForm" class="ajax-form form-horizontal" action="${base}/admin/business_deposit/adjust" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.businessDeposit.adjust")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.businessDeposit.business")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select id="businessId" name="businessId" class="form-control" title="${message("admin.businessDeposit.businessTitle")}"></select>
							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("admin.businessDeposit.availableBalance")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p id="availableBalance" class="text-red form-control-static"></p>
							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="amount">${message("admin.businessDeposit.amount")}:</label>
							<div class="col-xs-9 col-sm-4" title="${message("admin.businessDeposit.amountTitle")}" data-toggle="tooltip">
								<input id="amount" name="amount" class="form-control" type="text" maxlength="16" disabled>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="memo">${message("admin.businessDeposit.memo")}:</label>
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
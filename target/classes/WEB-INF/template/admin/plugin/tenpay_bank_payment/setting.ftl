<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.plugin.tenpayBankPayment.setting")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-fileinput.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
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
			
				var $tenpayBankPaymentForm = $("#tenpayBankPaymentForm");
				
				// 表单验证
				$tenpayBankPaymentForm.validate({
					rules: {
						displayName: "required",
						partner: "required",
						key: "required",
						fee: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						order: "digits"
					}
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body>
	[#include "/admin/include/main_header.ftl" /]
	[#include "/admin/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<ol class="breadcrumb">
				<li>
					<a href="${base}/business/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("admin.plugin.tenpayBankPayment.setting")}</li>
			</ol>
			<form id="tenpayBankPaymentForm" class="ajax-form form-horizontal" action="${base}/admin/plugin/tenpay_bank_payment/update" method="post">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="displayName">${message("PaymentPlugin.displayName")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="displayName" name="displayName" class="form-control" type="text" value="${pluginConfig.getAttribute("displayName")}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="partner">${message("admin.plugin.tenpayBankPayment.partner")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="partner" name="partner" class="form-control" type="text" value="${pluginConfig.getAttribute("partner")}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="key">${message("admin.plugin.tenpayBankPayment.key")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="key" name="key" class="form-control" type="text" value="${pluginConfig.getAttribute("key")}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("PaymentPlugin.feeType")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select name="feeType" class="selectpicker form-control" data-size="10">
									[#list feeTypes as feeType]
										<option value="${feeType}"[#if feeType == pluginConfig.getAttribute("feeType")] selected[/#if]>${message("PaymentPlugin.FeeType." + feeType)}</option>
									[/#list]
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="fee">${message("PaymentPlugin.fee")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="fee" name="fee" class="form-control" type="text" value="${pluginConfig.getAttribute("fee")}" maxlength="16">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("PaymentPlugin.logo")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input name="logo" type="hidden" value="${pluginConfig.getAttribute("logo")}" data-provide="fileinput" data-file-type="IMAGE">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="description">${message("PaymentPlugin.description")}:</label>
							<div class="col-xs-9 col-sm-4">
								<textarea id="description" name="description" class="form-control" rows="5">${pluginConfig.getAttribute("description")}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="checkbox">
									<input name="_isEnabled" type="hidden" value="false">
									<input id="isEnabled" name="isEnabled" type="checkbox" value="true"[#if pluginConfig.isEnabled] checked[/#if]>
									<label for="isEnabled">${message("PaymentPlugin.isEnabled")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="order">${message("common.order")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="order" name="order" class="form-control" type="text" value="${pluginConfig.order}" maxlength="9">
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
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.shippingMethod.defaultFreightConfigEdit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
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
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $shippingMethodForm = $("#shippingMethodForm");
				
				// 表单验证
				$shippingMethodForm.validate({
					rules: {
						firstWeight: {
							required: true,
							digits: true
						},
						continueWeight: {
							required: true,
							integer: true,
							min: 1
						},
						firstPrice: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						continuePrice: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
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
				<li class="active">${message("business.shippingMethod.defaultFreightConfigEdit")}</li>
			</ol>
			<form id="shippingMethodForm" class="ajax-form form-horizontal" action="${base}/business/shipping_method/update" method="post">
				<input name="shippingMethodId" type="hidden" value="${shippingMethod.id}">
				<input name="id" type="hidden" value="${defaultFreightConfig.id}">
				<input name="defaultFreightConfigId" type="hidden" value="${defaultFreightConfig.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.shippingMethod.defaultFreightConfigEdit")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="firstWeight">${message("FreightConfig.firstWeight")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="input-group">
									<input id="firstWeight" name="firstWeight" class="form-control" type="text" value="${defaultFreightConfig.firstWeight}" maxlength="9">
									<span class="input-group-addon">${message("business.shippingMethod.weightTitle")}</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="continueWeight">${message("FreightConfig.continueWeight")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="input-group">
									<input id="continueWeight" name="continueWeight" class="form-control" type="text" value="${defaultFreightConfig.continueWeight}" maxlength="9">
									<span class="input-group-addon">${message("business.shippingMethod.weightTitle")}</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="firstPrice">${message("FreightConfig.firstPrice")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="firstPrice" name="firstPrice" class="form-control" type="text" value="${defaultFreightConfig.firstPrice}" maxlength="16">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="continuePrice">${message("FreightConfig.continuePrice")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="continuePrice" name="continuePrice" class="form-control" type="text" value="${defaultFreightConfig.continuePrice}" maxlength="16">
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
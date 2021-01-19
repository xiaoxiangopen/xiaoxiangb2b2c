<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.areaFreightConfig.edit")} - 小象电商</title>
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
	<script src="${base}/resources/common/js/jquery.lSelect.js"></script>
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
				
				var $areaFreightConfigForm = $("#areaFreightConfigForm");
				var $areaId = $("input[name='areaId']");
				
				$areaId.lSelect({
					url: "${base}/common/area"
				});
				
				// 表单验证
				$areaFreightConfigForm.validate({
					rules: {
						areaId: {
							required: true,
							remote: {
								url: "${base}/business/area_freight_config/check_area?id=${areaFreightConfig.id}&shippingMethodId=${areaFreightConfig.shippingMethod.id}",
								cache: false
							}
						},
						firstWeight: {
							required: true,
							digits: true
						},
						continueWeight: {
							required: true,
							digits: true,
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
					},
					messages: {
						areaId: {
							remote: "${message("business.areaFreightConfig.areaExists")}"
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
				<li class="active">${message("business.areaFreightConfig.edit")}</li>
			</ol>
			<form id="areaFreightConfigForm" class="ajax-form form-horizontal" action="${base}/business/area_freight_config/update" method="post">
				<input name="areaFreightConfigId" type="hidden" value="${areaFreightConfig.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.areaFreightConfig.edit")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("FreightConfig.shippingMethod")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p class="form-control-static">${areaFreightConfig.shippingMethod.name}</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("AreaFreightConfig.area")}:</label>
							<div class="col-xs-9 col-sm-10">
								<input name="areaId" type="hidden" value="${areaFreightConfig.area.id}" treePath="${areaFreightConfig.area.treePath}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="firstWeight">${message("FreightConfig.firstWeight")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="input-group">
									<input id="firstWeight" name="firstWeight" class="form-control" type="text" value="${areaFreightConfig.firstWeight}" maxlength="9">
									<span class="input-group-addon">${message("business.areaFreightConfig.weightTitle")}</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="continueWeight">${message("FreightConfig.continueWeight")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="input-group">
									<input id="continueWeight" name="continueWeight" class="form-control" type="text" value="${areaFreightConfig.continueWeight}" maxlength="9">
									<span class="input-group-addon">${message("business.areaFreightConfig.weightTitle")}</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="firstPrice">${message("FreightConfig.firstPrice")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="firstPrice" name="firstPrice" class="form-control" type="text" value="${areaFreightConfig.firstPrice}" maxlength="16">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="continuePrice">${message("FreightConfig.continuePrice")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="continuePrice" name="continuePrice" class="form-control" type="text" value="${areaFreightConfig.continuePrice}" maxlength="16">
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
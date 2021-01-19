<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.shippingMethod.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
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
			
				var $shippingMethodForm = $("#shippingMethodForm");
				
				// 表单验证
				$shippingMethodForm.validate({
					rules: {
						name: "required",
						order: "digits"
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
				<li class="active">${message("admin.shippingMethod.edit")}</li>
			</ol>
			<form id="shippingMethodForm" class="ajax-form form-horizontal" action="${base}/admin/shipping_method/update" method="post">
				<input name="id" type="hidden" value="${shippingMethod.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.shippingMethod.edit")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("ShippingMethod.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" value="${shippingMethod.name}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("ShippingMethod.defaultDeliveryCorp")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select name="defaultDeliveryCorpId" class="selectpicker form-control" data-size="10">
									<option value="">${message("common.choose")}</option>
									[#list deliveryCorps as deliveryCorp]
										<option value="${deliveryCorp.id}"[#if deliveryCorp == shippingMethod.defaultDeliveryCorp] selected[/#if]>${deliveryCorp.name}</option>
									[/#list]
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("ShippingMethod.icon")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input name="icon" type="hidden" value="${shippingMethod.icon}" data-provide="fileinput" data-file-type="IMAGE">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("ShippingMethod.paymentMethods")}:</label>
							<div class="col-xs-9 col-sm-10">
								[#list paymentMethods as paymentMethod]
									<div class="checkbox checkbox-inline">
										<input id="paymentMethod_${paymentMethod.id}" name="paymentMethodIds" type="checkbox" value="${paymentMethod.id}"[#if shippingMethod.paymentMethods?seq_contains(paymentMethod)] checked[/#if]>
										<label for="paymentMethod_${paymentMethod.id}">${paymentMethod.name}</label>
									</div>
								[/#list]
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="description">${message("ShippingMethod.description")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="description" name="description" class="form-control" type="text" value="${shippingMethod.description}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="order">${message("common.order")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="order" name="order" class="form-control" type="text" value="${shippingMethod.order}" maxlength="9">
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
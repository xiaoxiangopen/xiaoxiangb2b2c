<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.profile.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-datetimepicker.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-fileinput.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
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
				
				var $profileForm = $("#profileForm");
				
				// 表单验证
				$profileForm.validate({
					rules: {
						email: {
							required: true,
							email: true,
							remote: {
								url: "${base}/business/profile/check_email",
								cache: false
							}
						},
						mobile: {
							required: true,
							mobile: true,
							remote: {
								url: "${base}/business/profile/check_mobile",
								cache: false
							}
						}
						[@business_attribute_list]
							[#list businessAttributes as businessAttribute]
								[#if businessAttribute.isRequired || businessAttribute.pattern?has_content]
									,"businessAttribute_${businessAttribute.id}": {
										[#if businessAttribute.isRequired]
											required: true
											[#if businessAttribute.pattern?has_content],[/#if]
										[/#if]
										[#if businessAttribute.pattern?has_content]
											pattern: new RegExp("${businessAttribute.pattern}")
										[/#if]
									}
								[/#if]
							[/#list]
						[/@business_attribute_list]
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/profile/edit"
						});
					},
					messages: {
						email: {
							remote: "${message("common.validator.exist")}"
						},
						mobile: {
							remote: "${message("common.validator.exist")}"
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
				<li class="active">${message("business.profile.edit")}</li>
			</ol>
			<form id="profileForm" class="ajax-form form-horizontal" action="${base}/business/profile/update" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.profile.edit")}</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-xs-12 col-sm-6">
								<dl class="items dl-horizontal clearfix">
									<dt>${message("Business.username")}:</dt>
									<dd>${currentUser.username}</dd>
									<dt>${message("Business.balance")}:</dt>
									<dd>
										<span class="text-red">${currency(currentUser.balance, true, true)}</span>
									</dd>
									[#if currentUser.frozenAmount > 0]
										<dt>${message("Business.frozenAmount")}:</dt>
										<dd>
											<span class="text-gray">${currency(currentUser.frozenAmount, true, true)}</span>
										</dd>
									[/#if]
									<dt>${message("Store.bailPaid")}:</dt>
									<dd>${currency(currentStore.bailPaid, true, true)}</dd>
									<dt>${message("common.createdDate")}:</dt>
									<dd>${currentUser.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
								</dl>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="email">${message("Business.email")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="email" name="email" class="form-control" type="text" value="${currentUser.email}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="mobile">${message("Business.mobile")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="mobile" name="mobile" class="form-control" type="text" value="${currentUser.mobile}" maxlength="200">
							</div>
						</div>
						[@business_attribute_list]
							[#list businessAttributes as businessAttribute]
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label[#if businessAttribute.isRequired] item-required[/#if]" for="businessAttribute_${businessAttribute.id}">${businessAttribute.name}:</label>
									[#if businessAttribute.type == "NAME" || businessAttribute.type == "LICENSE_NUMBER" || businessAttribute.type == "LEGAL_PERSON" || businessAttribute.type == "ID_CARD" || businessAttribute.type == "PHONE" || businessAttribute.type == "ORGANIZATION_CODE" || businessAttribute.type == "IDENTIFICATION_NUMBER" || businessAttribute.type == "BANK_NAME" || businessAttribute.type == "BANK_ACCOUNT" || businessAttribute.type == "TEXT"]
										<div class="col-xs-9 col-sm-4">
											<input id="businessAttribute_${businessAttribute.id}" name="businessAttribute_${businessAttribute.id}" class="form-control" type="text" value="${currentUser.getAttributeValue(businessAttribute)}" maxlength="200">
										</div>
									[#elseif businessAttribute.type == "LICENSE_IMAGE" || businessAttribute.type == "ID_CARD_IMAGE" || businessAttribute.type == "ORGANIZATION_IMAGE" || businessAttribute.type == "TAX_IMAGE" || businessAttribute.type == "IMAGE"]
										<div class="col-xs-9 col-sm-4">
											<input name="businessAttribute_${businessAttribute.id}" type="hidden" value="${currentUser.getAttributeValue(businessAttribute)}" data-provide="fileinput" data-file-type="IMAGE">
										</div>
									[#elseif businessAttribute.type == "SELECT"]
										<div class="col-xs-9 col-sm-4">
											<select id="businessAttribute_${businessAttribute.id}" name="businessAttribute_${businessAttribute.id}" class="selectpicker form-control" data-size="5">
												<option value="">${message("common.choose")}</option>
												[#list businessAttribute.options as option]
													<option value="${option}"[#if option == currentUser.getAttributeValue(businessAttribute)] selected[/#if]>${option}</option>
												[/#list]
											</select>
										</div>
									[#elseif businessAttribute.type == "CHECKBOX"]
										<div class="col-xs-9 col-sm-10">
											[#list businessAttribute.options as option]
												<div class="checkbox checkbox-inline">
													<input id="${option}_${businessAttribute.id}" name="businessAttribute_${businessAttribute.id}" type="checkbox" value="${option}"[#if (currentUser.getAttributeValue(businessAttribute)?seq_contains(option))!] checked[/#if]>
													<label for="${option}_${businessAttribute.id}">${option}</label>
												</div>
											[/#list]
										</div>
									[#elseif businessAttribute.type == "DATE"]
										<div class="col-xs-9 col-sm-4">
											<div class="input-group">
												<input id="businessAttribute_${businessAttribute.id}" name="businessAttribute_${businessAttribute.id}" class="form-control" type="text" value="${currentUser.getAttributeValue(businessAttribute)}" data-provide="datetimepicker">
												<span class="input-group-addon">
													<i class="iconfont icon-calendar"></i>
												</span>
											</div>
										</div>
									[/#if]
								</div>
							[/#list]
						[/@business_attribute_list]
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
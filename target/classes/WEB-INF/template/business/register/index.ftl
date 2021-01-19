<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.register.title")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-datetimepicker.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-fileinput.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/shop/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/register.css" rel="stylesheet">
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
				
				var $document = $(document);
				var $registerForm = $("#registerForm");
				var $username = $("#username");
				var $captcha = $("#captcha");
				var $captchaImage = $("[data-toggle='captchaImage']");
				var $agree = $("#agree");
				var $submit = $("#registerForm button:submit");
				
				// 同意注册协议
				$agree.change(function() {
					$submit.prop("disabled", !$agree.prop("checked"));
				});
				
				// 表单验证
				$registerForm.validate({
					rules: {
						username: {
							required: true,
							minlength: 4,
							username: true,
							notAllNumber: true,
							remote: {
								url: "${base}/business/register/check_username",
								cache: false
							}
						},
						password: {
							required: true,
							minlength: 4,
							normalizer: function(value) {
								return value;
							}
						},
						rePassword: {
							required: true,
							equalTo: "#password",
							normalizer: function(value) {
								return value;
							}
						},
						email: {
							required: true,
							email: true,
							remote: {
								url: "${base}/business/register/check_email",
								cache: false
							}
						},
						mobile: {
							required: true,
							mobile: true,
							remote: {
								url: "${base}/business/register/check_mobile",
								cache: false
							}
						},
						captcha: "required"
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
					messages: {
						username: {
							remote: "${message("business.register.usernameExist")}"
						},
						email: {
							remote: "${message("business.register.emailExist")}"
						},
						mobile: {
							remote: "${message("business.register.mobileExist")}"
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successMessage: false,
							successRedirectUrl: "${base}/"
						});
					}
				});
				
				// 用户注册成功
				$registerForm.on("success.xiaoxiangshop.ajaxSubmit", function() {
					$document.trigger("registered.xiaoxiangshop.user", [{
						type: "business",
						username: $username.val()
					}]);
				});
				
				// 验证码图片
				$registerForm.on("error.xiaoxiangshop.ajaxSubmit", function() {
					$captchaImage.captchaImage("refresh");
				});
				
				// 验证码图片
				$captchaImage.on("refreshed.xiaoxiangshop.captchaImage", function() {
					$captcha.val("");
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="business register">
	[#include "/shop/include/main_header.ftl" /]
	<main>
		<div class="container">
			<form id="registerForm" class="form-horizontal" action="${base}/business/register/submit" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">
							<h1 class="text-blue">${message("business.register.title")}</h1>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 control-label item-required" for="username">${message("business.register.username")}:</label>
							<div class="col-xs-6">
								<input id="username" name="username" class="form-control" type="text" maxlength="20" autocomplete="off">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label item-required" for="password">${message("business.register.password")}:</label>
							<div class="col-xs-6">
								<input id="password" name="password" class="form-control" type="password" maxlength="20" autocomplete="off">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label item-required" for="rePassword">${message("business.register.rePassword")}:</label>
							<div class="col-xs-6">
								<input id="rePassword" name="rePassword" class="form-control" type="password" maxlength="20" autocomplete="off">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label item-required" for="email">${message("business.register.email")}:</label>
							<div class="col-xs-6">
								<input id="email" name="email" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label item-required" for="mobile">${message("business.register.mobile")}:</label>
							<div class="col-xs-6">
								<input id="mobile" name="mobile" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						[@business_attribute_list]
							[#list businessAttributes as businessAttribute]
								<div class="form-group">
									<label class="col-xs-3 control-label[#if businessAttribute.isRequired] item-required[/#if]" for="businessAttribute_${businessAttribute.id}">${businessAttribute.name}:</label>
									[#if businessAttribute.type == "NAME" || businessAttribute.type == "LICENSE_NUMBER" || businessAttribute.type == "LEGAL_PERSON" || businessAttribute.type == "ID_CARD" || businessAttribute.type == "PHONE" || businessAttribute.type == "ORGANIZATION_CODE" || businessAttribute.type == "IDENTIFICATION_NUMBER" || businessAttribute.type == "BANK_NAME" || businessAttribute.type == "BANK_ACCOUNT" || businessAttribute.type == "TEXT"]
										<div class="col-xs-6">
											<input id="businessAttribute_${businessAttribute.id}" name="businessAttribute_${businessAttribute.id}" class="form-control" type="text" maxlength="200">
										</div>
									[#elseif businessAttribute.type == "LICENSE_IMAGE" || businessAttribute.type == "ID_CARD_IMAGE" || businessAttribute.type == "ORGANIZATION_IMAGE" || businessAttribute.type == "TAX_IMAGE" || businessAttribute.type == "IMAGE"]
										<div class="col-xs-6">
											<input name="businessAttribute_${businessAttribute.id}" type="hidden" data-provide="fileinput" data-file-type="IMAGE">
										</div>
									[#elseif businessAttribute.type == "SELECT"]
										<div class="col-xs-6">
											<select name="businessAttribute_${businessAttribute.id}" class="selectpicker form-control" data-size="5">
												<option value="">${message("common.choose")}</option>
												[#list businessAttribute.options as option]
													<option value="${option}">${option}</option>
												[/#list]
											</select>
										</div>
									[#elseif businessAttribute.type == "CHECKBOX"]
										<div class="col-xs-6">
											[#list businessAttribute.options as option]
												<div class="checkbox checkbox-inline">
													<input id="${option}_${businessAttribute.id}" name="businessAttribute_${businessAttribute.id}" type="checkbox" value="${option}">
													<label for="${option}_${businessAttribute.id}">${option}</label>
												</div>
											[/#list]
										</div>
									[#elseif businessAttribute.type == "DATE"]
										<div class="col-xs-6">
											<div class="input-group">
												<input id="businessAttribute_${businessAttribute.id}" name="businessAttribute_${businessAttribute.id}" class="form-control" type="text" data-provide="datetimepicker">
												<span class="input-group-addon">
													<i class="iconfont icon-calendar"></i>
												</span>
											</div>
										</div>
									[/#if]
								</div>
							[/#list]
						[/@business_attribute_list]
						[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("BUSINESS_REGISTER")]
							<div class="form-group">
								<label class="col-xs-3 control-label item-required" for="captcha">${message("common.captcha.name")}:</label>
								<div class="col-xs-6">
									<div class="input-group">
										<input id="captcha" name="captcha" class="captcha form-control" type="text" maxlength="4" autocomplete="off">
										<div class="input-group-btn">
											<img class="captcha-image" src="${base}/resources/common/images/transparent.png" title="${message("common.captcha.imageTitle")}" data-toggle="captchaImage">
										</div>
									</div>
								</div>
							</div>
						[/#if]
					</div>
					<div class="panel-footer">
						<div class="form-group">
							<div class="col-xs-6 col-xs-offset-3">
								<div class="checkbox">
									<input id="agree" name="agree" type="checkbox" value="true" checked>
									<label for="agree">${message("business.register.agree")}</label>
									<a class="text-red" href="${base}/article/detail/35_1" target="_blank">${message("business.register.agreement", setting.siteName)}</a>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-xs-6 col-xs-offset-3">
								<button class="btn btn-primary btn-lg btn-block" type="submit">${message("business.register.submit")}</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</main>
	[#include "/shop/include/main_footer.ftl" /]
</body>
</html>
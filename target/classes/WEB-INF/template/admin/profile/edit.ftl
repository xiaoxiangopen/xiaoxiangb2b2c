<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.profile.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
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
			
				var $profileForm = $("#profileForm");
				var $password = $("#password");
				
				// 表单验证
				$profileForm.validate({
					rules: {
						currentPassword: {
							required: function(element) {
								return $password.val() != "";
							},
							remote: {
								url: "${base}/admin/profile/check_current_password",
								cache: false
							},
							normalizer: function(value) {
								return value;
							}
						},
						password: {
							minlength: 4,
							normalizer: function(value) {
								return value;
							}
						},
						rePassword: {
							equalTo: "#password",
							normalizer: function(value) {
								return value;
							}
						},
						email: {
							required: true,
							email: true
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/admin/profile/edit"
						});
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
				<li class="active">${message("admin.profile.edit")}</li>
			</ol>
			<form id="profileForm" class="ajax-form form-horizontal" action="${base}/admin/profile/update" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.profile.edit")}</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-xs-12 col-sm-6">
								<dl class="items dl-horizontal">
									<dt>${message("Admin.username")}:</dt>
									<dd>${admin.username}</dd>
									<dt>${message("Admin.name")}:</dt>
									<dd>${admin.name}</dd>
									<dt>${message("Admin.department")}:</dt>
									<dd>${admin.department}</dd>
								</dl>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="currentPassword">${message("admin.profile.currentPassword")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="currentPassword" name="currentPassword" class="form-control" type="password" maxlength="20" autocomplete="off">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="password">${message("admin.profile.password")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="password" name="password" class="form-control" type="password" maxlength="20" autocomplete="off">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="rePassword">${message("admin.profile.rePassword")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="rePassword" name="rePassword" class="form-control" type="password" maxlength="20" autocomplete="off">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="email">${message("Admin.email")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="email" name="email" class="form-control" type="text" value="${admin.email}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<div class="col-xs-9 col-sm-4 col-xs-offset-3 col-sm-offset-2">
								<div class="alert alert-warning">${message("admin.profile.tips")}</div>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("common.submit")}</button>
								<a class="btn btn-default" href="${base}/admin/index">${message("common.back")}</a>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>
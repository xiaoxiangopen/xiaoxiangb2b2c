<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.admin.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
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
			
				var $adminForm = $("#adminForm");
				
				// 表单验证
				$adminForm.validate({
					rules: {
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
							email: true,
							remote: {
								url: "${base}/admin/admin/check_email?id=${admin.id}",
								cache: false
							}
						},
						mobile: {
							required: true,
							mobile: true,
							remote: {
								url: "${base}/admin/admin/check_mobile?id=${admin.id}",
								cache: false
							}
						},
						roleIds: "required"
					},
					messages: {
						email: {
							remote: "${message("common.validator.exist")}"
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
				<li class="active">${message("admin.admin.edit")}</li>
			</ol>
			<form id="adminForm" class="ajax-form form-horizontal" action="${base}/admin/admin/update" method="post">
				<input name="id" type="hidden" value="${admin.id}">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#base" data-toggle="tab">${message("admin.admin.base")}</a>
							</li>
							<li>
								<a href="#profile" data-toggle="tab">${message("admin.admin.profile")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="base" class="tab-pane active">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Admin.username")}:</label>
									<div class="col-xs-9 col-sm-4">
										<p class="form-control-static">${admin.username}</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="password">${message("Admin.password")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="password" name="password" class="form-control" type="password" maxlength="20" autocomplete="off">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="rePassword">${message("admin.admin.rePassword")}:</label>
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
									<label class="col-xs-3 col-sm-2 control-label item-required" for="mobile">${message("Admin.mobile")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="mobile" name="mobile" class="form-control" type="text" value="${admin.mobile}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("Admin.roles")}:</label>
									<div class="col-xs-9 col-sm-10">
										[#list roles as role]
											<div class="checkbox checkbox-inline">
												<input id="role_${role.id}" name="roleIds" type="checkbox" value="${role.id}"[#if admin.roles?seq_contains(role)] checked[/#if]>
												<label for="role_${role.id}">${role.name}</label>
											</div>
										[/#list]
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox checkbox-inline">
											<input name="_isEnabled" type="hidden" value="false">
											<input id="isEnabled" name="isEnabled" type="checkbox" value="true"[#if admin.isEnabled] checked[/#if]>
											<label for="isEnabled">${message("User.isEnabled")}</label>
										</div>
										[#if admin.isLocked]
											<div class="checkbox checkbox-inline">
												<input name="_unlock" type="hidden" value="false">
												<input id="unlock" name="unlock" type="checkbox" value="true"[#if !admin.isLocked] checked[/#if]>
												<label for="unlock">${message("admin.admin.unlock")}</label>
											</div>
										[/#if]
									</div>
								</div>
							</div>
							<div id="profile" class="tab-pane">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="department">${message("Admin.department")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="department" name="department" class="form-control" type="text" value="${admin.department}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="name">${message("Admin.name")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="name" name="name" class="form-control" type="text" value="${admin.name}" maxlength="200">
									</div>
								</div>
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
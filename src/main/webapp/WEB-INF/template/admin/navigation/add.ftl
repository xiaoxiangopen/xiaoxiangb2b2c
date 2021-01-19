<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.navigation.add")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
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
			
				var $navigationForm = $("#navigationForm");
				var $systemUrl = $("#systemUrl");
				var $url = $("#url");
				
				// 系统内容
				$systemUrl.change(function() {
					$url.val($(this).val());
				});
				
				// 链接地址
				$url.keypress(function() {
					$systemUrl.selectpicker("val", "");
				});
				
				// 表单验证
				$navigationForm.validate({
					rules: {
						name: "required",
						url: {
							required: true,
							url2: true
						},
						navigationGroupId: "required",
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
				<li class="active">${message("admin.navigation.add")}</li>
			</ol>
			<form id="navigationForm" class="ajax-form form-horizontal" action="${base}/admin/navigation/save" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.navigation.add")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("Navigation.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("admin.navigation.systemUrl")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select id="systemUrl" class="selectpicker form-control" data-live-search="true" data-size="10">
									<option value="">-</option>
									<option value="${setting.siteUrl}/">${message("admin.navigation.home")}</option>
									<option value="${setting.siteUrl}/product_category">${message("admin.navigation.productCategory")}</option>
									<option value="${setting.siteUrl}/friend_link">${message("admin.navigation.friendLink")}</option>
									<option value="${setting.siteUrl}/member/index">${message("admin.navigation.member")}</option>
									<option value="${setting.siteUrl}/business/index">${message("admin.navigation.business")}</option>
									[#list articleCategoryTree as articleCategory]
										<option value="${setting.siteUrl}${articleCategory.path}" title="${articleCategory.name}">
											[#if articleCategory.grade != 0]
												[#list 1..articleCategory.grade as i]
													&nbsp;&nbsp;
												[/#list]
											[/#if]
											${articleCategory.name}
										</option>
									[/#list]
									[#list productCategoryTree as productCategory]
										<option value="${setting.siteUrl}${productCategory.path}" title="${productCategory.name}">
											[#if productCategory.grade != 0]
												[#list 1..productCategory.grade as i]
													&nbsp;&nbsp;
												[/#list]
											[/#if]
											${productCategory.name}
										</option>
									[/#list]
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="url">${message("Navigation.url")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="url" name="url" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("Navigation.navigationGroup")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select name="navigationGroupId" class="selectpicker form-control" data-size="10">
									[#list navigationGroups as navigationGroup]
										<option value="${navigationGroup.id}">${navigationGroup.name}</option>
									[/#list]
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="checkbox">
									<input name="_isBlankTarget" type="hidden" value="false">
									<input id="isBlankTarget" name="isBlankTarget" type="checkbox" value="true">
									<label for="isBlankTarget">${message("Navigation.isBlankTarget")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="order">${message("common.order")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="order" name="order" class="form-control" type="text" maxlength="9">
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
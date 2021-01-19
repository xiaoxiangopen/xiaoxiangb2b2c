<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.store.setting")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
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
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $storeForm = $("#storeForm");
				
				// 表单验证
				$storeForm.validate({
					rules: {
						name: {
							required: true,
							remote: {
								url: "${base}/business/store/check_name?id=${currentStore.id}",
								cache: false
							}
						},
						email: {
							required: true,
							email: true
						},
						zipCode: "zipCode",
						phone: "phone",
						mobile: {
							required: true,
							mobile: true
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/store/setting"
						});
					},
					messages: {
						name: {
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
				<li class="active">${message("business.store.setting")}</li>
			</ol>
			<form id="storeForm" class="ajax-form form-horizontal" action="${base}/business/store/setting" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.store.setting")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("Store.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" value="${currentStore.name}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("Store.storeRank")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p class="form-control-static">${currentStore.storeRank.name}</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("Store.logo")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input name="logo" type="hidden" value="${currentStore.logo}" data-provide="fileinput" data-file-type="IMAGE">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="email">${message("Store.email")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="email" name="email" class="form-control" type="text" value="${currentStore.email}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="mobile">${message("Store.mobile")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="mobile" name="mobile" class="form-control" type="text" value="${currentStore.mobile}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="phone">${message("Store.phone")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="phone" name="phone" class="form-control" type="text" value="${currentStore.phone}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="address">${message("Store.address")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="address" name="address" class="form-control" type="text" value="${currentStore.address}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="zipCode">${message("Store.zipCode")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="zipCode" name="zipCode" class="form-control" type="text" value="${currentStore.zipCode}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="introduction">${message("Store.introduction")}:</label>
							<div class="col-xs-9 col-sm-4">
								<textarea id="introduction" name="introduction" class="form-control" maxlength="200" rows="5">${currentStore.introduction}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="keyword">${message("Store.keyword")}:</label>
							<div class="col-xs-9 col-sm-4" title="${message("business.store.keywordTitle")}" data-toggle="tooltip">
								<input id="keyword" name="keyword" class="form-control" type="text" value="${currentStore.keyword}" maxlength="200">
							</div>
						</div>
						[#if currentStore.productCategories?has_content]
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("Store.productCategories")}:</label>
								<div class="col-xs-9 col-sm-4">
									<div class="table-responsive">
										<table class="table table-bordered">
											<thead>
												<tr>
													<th>${message("ProductCategory.name")}</th>
													[#if currentStore.isSelf()]
														<th>${message("ProductCategory.selfRate")}</th>
													[#else]
														<th>${message("ProductCategory.generalRate")}</th>
													[/#if]
												</tr>
											</thead>
											<tbody>
												[#list currentStore.productCategories as productCategory]
													<tr>
														<td>${productCategory.name}</td>
														[#if currentStore.isSelf()]
															<td>${productCategory.selfRate}</td>
														[#else]
															<td>${productCategory.generalRate}</td>
														[/#if]
													</tr>
												[/#list]
											</tbody>
										</table>
									</div>
								</div>
							</div>
						[/#if]
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
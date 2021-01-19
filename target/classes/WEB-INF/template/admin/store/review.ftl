<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.store.review")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-treeview.css" rel="stylesheet">
	<link href="${base}/resources/common/css/zoom.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootstrap-treeview.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/zoom.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				[#macro productCategoryTree productCategories]
					[
						[#if productCategories?has_content]
							[#list productCategories as productCategory]
								{
									text: "${productCategory.name}",
									[#if allowedProductCategories?seq_contains(productCategory)]
										icon: "iconfont icon-check pull-right",
									[/#if]
									selectable: false,
									color: "#666666",
									state: {
										[#if !allowedProductCategories?seq_contains(productCategory) && !allowedProductCategoryParents?seq_contains(productCategory)]
											disabled: true,
										[/#if]
										expanded: ${allowedProductCategoryParents?seq_contains(productCategory)?string("true", "false")}
									}
									[#if productCategory.children?has_content]
										,nodes: [@productCategoryTree productCategories = productCategory.children /]
									[/#if]
								}[#if productCategory_has_next],[/#if]
							[/#list]
						[/#if]
					]
				[/#macro]
				
				var $storeForm = $("#storeForm");
				var $passed = $("[name='passed']");
				var $tree = $("#tree");
				var $content = $("#content");
				
				$tree.treeview({
					collapseIcon: "iconfont icon-unfold",
					expandIcon: "iconfont icon-right",
					data: [@productCategoryTree productCategories = productCategoryRoots /]
				});
				
				// 是否审核成功
				$passed.change(function() {
					var isText = $(this).val() == "true";
					
					$content.prop("disabled", isText).closest("div.form-group").velocity(isText ? "slideUp" : "slideDown");
				});
				
				// 表单验证
				$storeForm.validate({
					rules: {
						passed: "required",
						content: {
							required: true,
							maxlength: 200
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
				<li class="active">${message("admin.store.review")}</li>
			</ol>
			<form id="storeForm" class="ajax-form form-horizontal" action="${base}/admin/store/review" method="post">
				<input name="id" type="hidden" value="${store.id}">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#store" data-toggle="tab">${message("admin.store.store")}</a>
							</li>
							<li>
								<a href="#productCategories" data-toggle="tab">${message("Store.productCategories")}</a>
							</li>
							<li>
								<a href="#business" data-toggle="tab">${message("admin.store.business")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="store" class="tab-pane active">
								<div class="row">
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											<dt>${message("Store.name")}:</dt>
											<dd>${store.name}</dd>
											<dt>${message("Store.type")}:</dt>
											<dd>${message("Store.Type." + store.type)}</dd>
											<dt>${message("Store.status")}:</dt>
											<dd>
												<span class="[#if store.status == "PENDING"]text-orange[#elseif store.status == "FAILED"]text-red[#else]text-green[/#if]">${message("Store.Status." + store.status)}</span>
											</dd>
											<dt>${message("Store.logo")}:</dt>
											<dd class="col-xs-4">
												[#if store.logo?has_content]
													<img class="img-thumbnail" src="${store.logo}" alt="${store.name}" width="50" data-action="zoom">
												[/#if]
											</dd>
											<dt>${message("Store.email")}:</dt>
											<dd>${store.email}</dd>
											<dt>${message("Store.mobile")}:</dt>
											<dd>${store.mobile}</dd>
											<dt>${message("Store.phone")}:</dt>
											<dd>${store.phone}</dd>
											<dt>${message("Store.address")}:</dt>
											<dd>${store.address}</dd>
											<dt>${message("Store.zipCode")}:</dt>
											<dd>${store.zipCode}</dd>
											<dt>${message("Store.introduction")}:</dt>
											<dd>${store.introduction}</dd>
											<dt>${message("Store.keyword")}:</dt>
											<dd>${store.keyword}</dd>
											<dt>${message("Store.storeRank")}:</dt>
											<dd>${store.storeRank.name}</dd>
											<dt>${message("Store.storeCategory")}:</dt>
											<dd>${store.storeCategory.name}</dd>
										</dl>
									</div>
								</div>
							</div>
							<div id="productCategories" class="tab-pane">
								<div class="row">
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											<dt>${message("Store.productCategories")}:</dt>
											<dd id="tree" style="height: 600px; overflow-y: auto"></dd>
										</dl>
									</div>
								</div>
							</div>
							<div id="business" class="tab-pane">
								[@business_attribute_list]
									[#list businessAttributes as businessAttribute]
										<div class="form-group">
											<label class="col-xs-3 col-sm-2 control-label">${businessAttribute.name}:</label>
											[#if businessAttribute.type == "IMAGE" || businessAttribute.type == "LICENSE_IMAGE" || businessAttribute.type == "ID_CARD_IMAGE" || businessAttribute.type == "ORGANIZATION_IMAGE" || businessAttribute.type == "TAX_IMAGE"]
												<div class="col-xs-9 col-sm-4">
													<p class="form-control-static">
														<img class="img-thumbnail" src="${store.business.getAttributeValue(businessAttribute)}" alt="${businessAttribute.name}" width="50" data-action="zoom">
													</p>
												</div>
											[#else]
												<div class="col-xs-9 col-sm-4">
													<p class="form-control-static">${store.business.getAttributeValue(businessAttribute)}</p>
												</div>
											[/#if]
										</div>
									[/#list]
								[/@business_attribute_list]
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.store.review")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="radio radio-inline">
											<input id="passed" name="passed" type="radio" value="true" checked>
											<label for="passed">${message("admin.store.approved")}</label>
										</div>
										<div class="radio radio-inline">
											<input id="failed" name="passed" type="radio" value="false">
											<label for="failed">${message("admin.store.failed")}</label>
										</div>
									</div>
								</div>
								<div class="hidden-element form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="content">${message("admin.store.content")}:</label>
									<div class="col-xs-9 col-sm-4">
										<textarea id="content" name="content" class="form-control" rows="5" disabled></textarea>
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
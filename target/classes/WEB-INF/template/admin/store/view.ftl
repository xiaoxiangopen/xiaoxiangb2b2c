<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.store.view")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
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
				
				var $tree = $("#tree");
				
				// 树形结构
				$tree.treeview({
					collapseIcon: "iconfont icon-unfold",
					expandIcon: "iconfont icon-right",
					data: [@productCategoryTree productCategories = productCategoryRoots /]
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
				<li class="active">${message("admin.store.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#store" data-toggle="tab">${message("admin.store.store")}</a>
						</li>
						<li>
							<a href="#productCategories" data-toggle="tab">${message("Store.productCategories")}</a>
						</li>
					</ul>
					<div class="tab-content">
						<div id="store" class="tab-pane active">
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("Store.storeNo")}:</dt>
										<dd>${store.storeNo}</dd>
										<dt>${message("Store.name")}:</dt>
										<dd>${store.name}</dd>
										<dt>${message("Store.type")}:</dt>
										<dd>${message("Store.Type." + store.type)}</dd>
										<dt>${message("admin.store.username")}:</dt>
										<dd>
											${store.business.username}
											<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/business/view?id=${store.business.id}" title="${message("common.view")}" data-toggle="tooltip">
												<i class="iconfont icon-search"></i>
											</a>
										</dd>
										<dt>${message("Store.status")}:</dt>
										<dd>
											<span class="[#if store.status == "PENDING"]text-orange[#elseif store.status == "FAILED"]text-red[#else]text-green[/#if]">${message("Store.Status." + store.status)}</span>
										</dd>
										<dt>${message("Store.logo")}:</dt>
										<dd>
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
					</div>
				</div>
				<div class="panel-footer">
					<div class="row">
						<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
							<button class="btn btn-default" type="button" data-action="back">${message("common.back")}</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>
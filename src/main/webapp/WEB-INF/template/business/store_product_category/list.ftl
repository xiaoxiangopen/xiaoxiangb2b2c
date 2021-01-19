<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.storeProductCategory.list")} - 小象电商++</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/bootbox.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
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
			
				var $delete = $("a.delete");
				
				// 删除
				$delete.delete({
					url: "${base}/business/store_product_category/delete",
					data: function($element) {
						return {
							storeProductCategoryId: $element.data("id")
						}
					}
				}).on("success.xiaoxiangshop.delete", function(event) {
					var $element = $(event.target);
					
					$element.closest("tr").velocity("fadeOut", {
						complete: function() {
							$(this).remove();
							if ($("a.delete").length < 1) {
								location.reload(true);
							}
						}
					});
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
				<li class="active">${message("business.storeProductCategory.list")}</li>
			</ol>
			<form id="storeProductCategoryForm" action="${base}/business/store_product_category/list" method="get">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="btn-group">
							<a class="btn btn-default" href="${base}/business/store_product_category/add" data-redirect-url="${base}/business/store_product_category/list">
								<i class="iconfont icon-add"></i>
								${message("common.add")}
							</a>
							<button class="btn btn-default" type="button" data-action="refresh">
								<i class="iconfont icon-refresh"></i>
								${message("common.refresh")}
							</button>
						</div>
					</div>
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>${message("StoreProductCategory.name")}</th>
										<th>${message("common.order")}</th>
										<th>${message("common.action")}</th>
									</tr>
								</thead>
								<tbody>
									[#list storeProductCategoryTree as storeProductCategory]
										<tr>
											<td>
												<span style="margin-left: ${storeProductCategory.grade * 20}px;[#if storeProductCategory.grade == 0] color: #000000;[/#if]">${storeProductCategory.name}</span>
											</td>
											<td>${storeProductCategory.order}</td>
											<td>
												<a class="btn btn-default btn-xs btn-icon" href="${base}${storeProductCategory.path}" title="${message("common.view")}" data-toggle="tooltip" target="_blank">
													<i class="iconfont icon-search"></i>
												</a>
												<a class="btn btn-default btn-xs btn-icon" href="${base}/business/store_product_category/edit?storeProductCategoryId=${storeProductCategory.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
													<i class="iconfont icon-write"></i>
												</a>
												<a class="delete btn btn-default btn-xs btn-icon" href="javascript:;" title="${message("common.delete")}" data-toggle="tooltip" data-id="${storeProductCategory.id}">
													<i class="iconfont icon-close"></i>
												</a>
											</td>
										</tr>
									[/#list]
								</tbody>
							</table>
							[#if !storeProductCategoryTree?has_content]
								<p class="no-result">${message("common.noResult")}</p>
							[/#if]
						</div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>
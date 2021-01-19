<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.productCategory.list")} - 小象电商</title>
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
	<script src="${base}/resources/common/js/bootbox.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $delete = $("a.delete");
				
				// 删除
				$delete.delete({
					url: "${base}/admin/product_category/delete",
					data: function($element) {
						return {
							id: $element.data("id")
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
				<li class="active">${message("admin.productCategory.list")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="btn-group">
						<a class="btn btn-default" href="${base}/admin/product_category/add" data-redirect-url="${base}/admin/product_category/list">
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
									<th>${message("ProductCategory.name")}</th>
									<th>${message("ProductCategory.generalRate")}</th>
									<th>${message("ProductCategory.selfRate")}</th>
									<th>${message("common.order")}</th>
									<th>是否有效</th>
									<th>${message("common.action")}</th>

								</tr>
							</thead>
							<tbody>
								[#list productCategoryTree as productCategory]
									<tr>
										<td>
											<span style="margin-left: ${productCategory.grade * 20}px;[#if productCategory.grade == 0] color: #000000;[/#if]">${productCategory.name}</span>
										</td>
										<td>${productCategory.generalRate}</td>
										<td>${productCategory.selfRate}</td>
										<td>${productCategory.order}</td>
										<td>
											[#if productCategory.effective]
												是
											[#else]
												否
											[/#if]
										</td>
										<td>
											<a class="btn btn-default btn-xs btn-icon" href="${base}${productCategory.path}" title="${message("common.view")}" data-toggle="tooltip" target="_blank">
												<i class="iconfont icon-search"></i>
											</a>
											<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/product_category/edit?id=${productCategory.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
												<i class="iconfont icon-write"></i>
											</a>
											<a class="delete btn btn-default btn-xs btn-icon" href="javascript:;" title="${message("common.delete")}" data-toggle="tooltip" data-id="${productCategory.id}">
												<i class="iconfont icon-close"></i>
											</a>
										</td>
									</tr>
								[/#list]
							</tbody>
						</table>
						[#if !productCategoryTree?has_content]
							<p class="no-result">${message("common.noResult")}</p>
						[/#if]
					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>
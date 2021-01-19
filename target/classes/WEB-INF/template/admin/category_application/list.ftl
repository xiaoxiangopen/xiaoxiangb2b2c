<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.categoryApplication.list")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
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
			
				var $review = $("a.review");
				
				// 审核
				$review.click(function() {
					var $element = $(this);
					
					bootbox.prompt({
						title: "${message("common.bootbox.title")}",
						inputType: "select",
						value: "APPROVED",
						inputOptions: [
							{
								text: "${message("admin.categoryApplication.reviewApproved")}",
								value: "APPROVED"
							},
							{
								text: "${message("admin.categoryApplication.reviewFailed")}",
								value: "FAILED"
							}
						],
						callback: function(result) {
							if (result == null) {
								return;
							}
							
							$.ajax({
								url: "${base}/admin/category_application/review",
								type: "POST",
								data: {
									id: $element.data("id"),
									isPassed: result == "APPROVED" ? "true" : "false"
								},
								dataType: "json",
								cache: false,
								success: function() {
									location.reload(true);
								}
							});
						}
					}).find("select").selectpicker();
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
				<li class="active">${message("admin.categoryApplication.list")}</li>
			</ol>
			<form action="${base}/admin/category_application/list" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<input name="status" type="hidden" value="${status}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-9">
								<div class="btn-group">
									<button class="btn btn-default" type="button" data-action="refresh">
										<i class="iconfont icon-refresh"></i>
										${message("common.refresh")}
									</button>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("admin.categoryApplication.filter")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if status?? && status == "PENDING"] class="active"[/#if] data-filter-property="status" data-filter-value="PENDING">
												<a href="javascript:;">${message("CategoryApplication.Status.PENDING")}</a>
											</li>
											<li[#if status?? && status == "FAILED"] class="active"[/#if] data-filter-property="status" data-filter-value="FAILED">
												<a href="javascript:;">${message("CategoryApplication.Status.FAILED")}</a>
											</li>
											<li[#if status?? && status == "APPROVED"] class="active"[/#if] data-filter-property="status" data-filter-value="APPROVED">
												<a href="javascript:;">${message("CategoryApplication.Status.APPROVED")}</a>
											</li>
										</ul>
									</div>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("common.pageSize")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if page.pageSize == 10] class="active"[/#if] data-page-size="10">
												<a href="javascript:;">10</a>
											</li>
											<li[#if page.pageSize == 20] class="active"[/#if] data-page-size="20">
												<a href="javascript:;">20</a>
											</li>
											<li[#if page.pageSize == 50] class="active"[/#if] data-page-size="50">
												<a href="javascript:;">50</a>
											</li>
											<li[#if page.pageSize == 100] class="active"[/#if] data-page-size="100">
												<a href="javascript:;">100</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-3">
								<div id="search" class="input-group">
									<div class="input-group-btn">
										<button class="btn btn-default" type="button" data-toggle="dropdown">
											[#switch page.searchProperty]
												[#case "store.name"]
													<span>${message("CategoryApplication.store")}</span>
													[#break /]
												[#default]
													<span>${message("CategoryApplication.productCategory")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "productCategory.name"] class="active"[/#if] data-search-property="productCategory.name">
												<a href="javascript:;">${message("CategoryApplication.productCategory")}</a>
											</li>
											<li[#if page.searchProperty == "store.name"] class="active"[/#if] data-search-property="store.name">
												<a href="javascript:;">${message("CategoryApplication.store")}</a>
											</li>
										</ul>
									</div>
									<input name="searchValue" class="form-control" type="text" value="${page.searchValue}" placeholder="${message("common.search")}" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search">
									<div class="input-group-btn">
										<button class="btn btn-default" type="submit">
											<i class="iconfont icon-search"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>
											<a href="javascript:;" data-order-property="product_category.name">
												${message("CategoryApplication.productCategory")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="rate">
												${message("CategoryApplication.rate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="store.name">
												${message("CategoryApplication.store")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="status">
												${message("CategoryApplication.status")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>${message("common.action")}</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as categoryApplication]
											<tr>
												<td>${categoryApplication.productCategory.name}</td>
												<td>${categoryApplication.rate}</td>
												<td>${categoryApplication.store.name}</td>
												<td>
													<span class="[#if categoryApplication.status == "PENDING"]text-orange[#elseif categoryApplication.status == "FAILED"]text-red[#else]text-green[/#if]">${message("CategoryApplication.Status." + categoryApplication.status)}</span>
												</td>
												<td>
													[#if categoryApplication.status == "PENDING"]
														<a class="review btn btn-default btn-xs btn-icon" href="javascript:;" title="${message("common.review")}" data-toggle="tooltip" data-id="${categoryApplication.id}">
															<i class="iconfont icon-comment"></i>
														</a>
													[#else]
														<button class="btn btn-default btn-xs btn-icon" type="button" title="${message("CategoryApplication.Status." + categoryApplication.status)}" data-toggle="tooltip" disabled>
															<i class="iconfont icon-comment"></i>
														</button>
													[/#if]
												</td>
											</tr>
										[/#list]
									</tbody>
								[/#if]
							</table>
							[#if !page.content?has_content]
								<p class="no-result">${message("common.noResult")}</p>
							[/#if]
						</div>
					</div>
					[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
						[#if totalPages > 1]
							<div class="panel-footer text-right">
								[#include "/admin/include/pagination.ftl" /]
							</div>
						[/#if]
					[/@pagination]
				</div>
			</form>
		</div>
	</main>
</body>
</html>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.productNotify.list")} - 小象电商</title>
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
				
				var $send = $("#send");
				var $delete = $("[data-action='delete']");
				var $ids = $("input[name='ids']");
				
				// 发送到货通知
				$send.click(function() {
					bootbox.confirm("${message("business.productNotify.sendConfirm")}", function(result) {
						if (result == null || !result) {
							return;
						}
						
						$.post("${base}/business/product_notify/send", $("input[name='ids']").serialize(), function() {
							location.reload(true);
						});
					});
				});
				
				// 删除
				$delete.on("success.xiaoxiangshop.delete", function() {
					$send.attr("disabled", true);
				});
				
				// ID多选框
				$ids.change(function() {
					$send.attr("disabled", $("input[name='ids']:checked").length < 1);
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
				<li class="active">${message("business.productNotify.list")}</li>
			</ol>
			<form action="${base}/business/product_notify/list" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<input name="isMarketable" type="hidden" value="[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]">
				<input name="isOutOfStock" type="hidden" value="[#if isOutOfStock??]${isOutOfStock?string("true", "false")}[/#if]">
				<input name="hasSent" type="hidden" value="[#if hasSent??]${hasSent?string("true", "false")}[/#if]">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-9">
								<div class="btn-group">
									<button id="send" class="btn btn-default" type="button" disabled>
										<i class="iconfont icon-mail"></i>
										${message("business.productNotify.send")}
									</button>
									<button class="btn btn-default" type="button" data-action="delete" disabled>
										<i class="iconfont icon-close"></i>
										${message("common.delete")}
									</button>
									<button class="btn btn-default" type="button" data-action="refresh">
										<i class="iconfont icon-refresh"></i>
										${message("common.refresh")}
									</button>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("business.productNotify.filter")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if isMarketable] class="active"[/#if] data-filter-property="isMarketable" data-filter-value="true">
												<a href="javascript:;">${message("business.productNotify.marketable")}</a>
											</li>
											<li[#if isMarketable?? && !isMarketable] class="active"[/#if] data-filter-property="isMarketable" data-filter-value="false">
												<a href="javascript:;">${message("business.productNotify.notMarketable")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isOutOfStock] class="active"[/#if] data-filter-property="isOutOfStock" data-filter-value="true">
												<a href="javascript:;">${message("business.productNotify.outOfStock")}</a>
											</li>
											<li[#if isOutOfStock?? && !isOutOfStock] class="active"[/#if] data-filter-property="isOutOfStock" data-filter-value="false">
												<a href="javascript:;">${message("business.productNotify.inStock")}</a>
											</li>
											<li class="divider"></li>
											<li[#if hasSent] class="active"[/#if] data-filter-property="hasSent" data-filter-value="true">
												<a href="javascript:;">${message("business.productNotify.hasSent")}</a>
											</li>
											<li[#if hasSent?? && !hasSent] class="active"[/#if] data-filter-property="hasSent" data-filter-value="false">
												<a href="javascript:;">${message("business.productNotify.hasNotSent")}</a>
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
												[#case "sku.product.name"]
													<span>${message("business.productNotify.sku")}</span>
													[#break /]
												[#case "member.username"]
													<span>${message("ProductNotify.member")}</span>
													[#break /]
												[#default]
													<span>${message("ProductNotify.email")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "email"] class="active"[/#if] data-search-property="productnotify.email">
												<a href="javascript:;">${message("ProductNotify.email")}</a>
											</li>
											<li[#if page.searchProperty == "sku.product.name"] class="active"[/#if] data-search-property="product.name">
												<a href="javascript:;">${message("business.productNotify.sku")}</a>
											</li>
											<li[#if page.searchProperty == "member.username"] class="active"[/#if] data-search-property="member.username">
												<a href="javascript:;">${message("ProductNotify.member")}</a>
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
											<div class="checkbox">
												<input type="checkbox" data-toggle="checkAll">
												<label></label>
											</div>
										</th>
										<th>
											<a href="javascript:;" data-order-property="skuId">
												${message("business.productNotify.skuName")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="memberId">
												${message("ProductNotify.member")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="email">
												${message("ProductNotify.email")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="createdDate">
												${message("business.productNotify.createdDate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="lastModifiedDate">
												${message("business.productNotify.notifyDate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>${message("business.productNotify.status")}</th>
										<th>
											<a href="javascript:;" data-order-property="hasSent">
												${message("business.productNotify.hasSent")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as productNotify]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" value="${productNotify.id}">
														<label></label>
													</div>
												</td>
												<td>
													<a href="${base}${productNotify.sku.path}" target="_blank">${productNotify.sku.name}</a>
													[#if productNotify.sku.specifications?has_content]
														<span class="text-gray">[${productNotify.sku.specifications?join(", ")}]</span>
													[/#if]
												</td>
												<td>
													[#if productNotify.member??]
														${productNotify.member.username}
													[#else]
														-
													[/#if]
												</td>
												<td>${productNotify.email}</td>
												<td>
													<span title="${productNotify.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${productNotify.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
												<td>
													[#if productNotify.hasSent]
														<span title="${productNotify.lastModifiedDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${productNotify.lastModifiedDate?string("yyyy-MM-dd HH:mm:ss")}</span>
													[#else]
														-
													[/#if]
												</td>
												<td>
													[#if productNotify.sku.isMarketable]
														<span class="text-green">${message("business.productNotify.marketable")}</span>
													[#else]
														<span class="text-red">${message("business.productNotify.notMarketable")}</span>
													[/#if]
													[#if productNotify.sku.isOutOfStock]
														<span class="text-red">${message("business.productNotify.outOfStock")}</span>
													[#else]
														<span class="text-green">${message("business.productNotify.inStock")}</span>
													[/#if]
												</td>
												<td>
													[#if productNotify.hasSent?has_content]
														<i class="text-green iconfont icon-check"></i>
													[#else]
														<i class="text-red iconfont icon-close"></i>
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
								[#include "/business/include/pagination.ftl" /]
							</div>
						[/#if]
					[/@pagination]
				</div>
			</form>
		</div>
	</main>
</body>
</html>
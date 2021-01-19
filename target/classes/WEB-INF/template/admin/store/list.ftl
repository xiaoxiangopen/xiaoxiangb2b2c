<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.store.list")} - 小象电商</title>
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
				<li class="active">${message("admin.store.list")}</li>
			</ol>
			<form action="${base}/admin/store/list" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<input name="type" type="hidden" value="${type}">
				<input name="status" type="hidden" value="${status}">
				<input name="isEnabled" type="hidden" value="[#if isEnabled??]${isEnabled?string("true", "false")}[/#if]">
				<input name="hasExpired" type="hidden" value="[#if hasExpired??]${hasExpired?string("true", "false")}[/#if]">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-9">
								<div class="btn-group">
									<a class="btn btn-default" href="${base}/admin/store/add" data-redirect-url="${base}/admin/store/list">
										<i class="iconfont icon-add"></i>
										${message("common.add")}
									</a>
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
											${message("admin.store.filter")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if type?? && type == "GENERAL"] class="active"[/#if] data-filter-property="type" data-filter-value="GENERAL">
												<a href="javascript:;">${message("Store.Type.GENERAL")}</a>
											</li>
											<li[#if type?? && type == "SELF"] class="active"[/#if] data-filter-property="type" data-filter-value="SELF">
												<a href="javascript:;">${message("Store.Type.SELF")}</a>
											</li>
											<li class="divider"></li>
											<li[#if status?? && status == "PENDING"] class="active"[/#if] data-filter-property="status" data-filter-value="PENDING">
												<a href="javascript:;">${message("Store.Status.PENDING")}</a>
											</li>
											<li[#if status?? && status == "FAILED"] class="active"[/#if] data-filter-property="status" data-filter-value="FAILED">
												<a href="javascript:;">${message("Store.Status.FAILED")}</a>
											</li>
											<li[#if status?? && status == "APPROVED"] class="active"[/#if] data-filter-property="status" data-filter-value="APPROVED">
												<a href="javascript:;">${message("Store.Status.APPROVED")}</a>
											</li>
											<li[#if status?? && status == "SUCCESS"] class="active"[/#if] data-filter-property="status" data-filter-value="SUCCESS">
												<a href="javascript:;">${message("Store.Status.SUCCESS")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isEnabled?? && isEnabled] class="active"[/#if] data-filter-property="isEnabled" data-filter-value="true">
												<a href="javascript:;">${message("admin.store.enabled")}</a>
											</li>
											<li[#if isEnabled?? && !isEnabled] class="active"[/#if] data-filter-property="isEnabled" data-filter-value="false">
												<a href="javascript:;">${message("admin.store.disabled")}</a>
											</li>
											<li class="divider"></li>
											<li[#if hasExpired?? && hasExpired] class="active"[/#if] data-filter-property="hasExpired" data-filter-value="true">
												<a href="javascript:;">${message("admin.store.hasExpired")}</a>
											</li>
											<li[#if hasExpired?? && !hasExpired] class="active"[/#if] data-filter-property="hasExpired" data-filter-value="false">
												<a href="javascript:;">${message("admin.store.unexpired")}</a>
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
												[#default]
													<span>${message("Store.name")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "name"] class="active"[/#if] data-search-property="name">
												<a href="javascript:;">${message("Store.name")}</a>
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
											<a href="javascript:;" data-order-property="name">
												${message("Store.storeNo")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="name">
												${message("Store.name")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="type">
												${message("Store.type")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="businessId">
												${message("Store.business")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="storeRankId">
												${message("Store.storeRank")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="storeCategoryId">
												${message("Store.storeCategory")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="isEnabled">
												${message("Store.isEnabled")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="createdDate">
												${message("common.createdDate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="endDate">
												${message("Store.endDate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="status">
												${message("Store.status")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>${message("common.action")}</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as store]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" value="${store.id}">
														<label></label>
													</div>
												</td>
												<td>${store.storeNo}</td>
												<td>${store.name}</td>
												<td>${message("Store.Type." + store.type)}</td>
												<td>${store.business.username}</td>
												<td>${store.storeRank.name}</td>
												<td>${store.storeCategory.name}</td>
												<td>
													<span class="[#if store.status == "PENDING"]text-orange[#elseif store.status == "FAILED"]text-red[#else]text-green[/#if]">${message("Store.Status." + store.status)}</span>
													[#if store.status == "SUCCESS" && store.hasExpired()]
														<span class="gray-dark">(${message("admin.store.hasExpired")})</span>
													[/#if]
												</td>
												<td>
													<span title="${store.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${store.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
												<td>
													[#if store.endDate??]
														<span title="${store.endDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${store.endDate?string("yyyy-MM-dd HH:mm:ss")}</span>
													[#else]
														-
													[/#if]
												</td>
												<td>
													[#if store.isEnabled]
														<i class="text-green iconfont icon-check"></i>
													[#else]
														<i class="text-red iconfont icon-close"></i>
													[/#if]
												</td>
												<td>
													<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/store/view?id=${store.id}" title="${message("common.view")}" data-toggle="tooltip">
														<i class="iconfont icon-search"></i>
													</a>
													<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/store/edit?id=${store.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
														<i class="iconfont icon-write"></i>
													</a>
													[#if store.status == "PENDING"]
														<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/store/review?id=${store.id}" title="${message("common.review")}" data-toggle="tooltip" data-redirect-url>
															<i class="iconfont icon-comment"></i>
														</a>
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
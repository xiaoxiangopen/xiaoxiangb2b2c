<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.businessDeposit.log")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
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
				<li class="active">${message("business.businessDeposit.log")}</li>
			</ol>
			<form action="${base}/business/business_deposit/log" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="btn-group">
							<button class="btn btn-default" type="button" data-action="refresh">
								<i class="iconfont icon-refresh"></i>
								${message("common.refresh")}
							</button>
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
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>${message("BusinessDepositLog.type")}</th>
										<th>${message("BusinessDepositLog.credit")}</th>
										<th>${message("BusinessDepositLog.debit")}</th>
										<th>${message("BusinessDepositLog.balance")}</th>
										<th>${message("common.createdDate")}</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as businessDepositLog]
											<tr>
												<td>${message("BusinessDepositLog.Type." + businessDepositLog.type)}</td>
												<td>${currency(businessDepositLog.credit, true)}</td>
												<td>${currency(businessDepositLog.debit, true)}</td>
												<td>${currency(businessDepositLog.balance, true)}</td>
												<td>
													<span title="${businessDepositLog.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${businessDepositLog.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
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
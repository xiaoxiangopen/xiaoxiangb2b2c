<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.aftersales.view")} - 小象电商</title>
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
	<script src="${base}/resources/common/js/jquery.lSelect.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	<style>
		.returns-items-quantity, .shipping-items-quantity {
			width: 50px;
		}
		
		div.modal .table tbody tr td {
			vertical-align: middle;
		}
	</style>
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
				<li class="active">${message("admin.aftersales.view")}</li>
			</ol>
			<form class="form-horizontal">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#aftersalesInfo" data-toggle="tab">${message("admin.aftersales.aftersalesInfo")}</a>
							</li>
							<li>
								<a href="#aftersalesItem" data-toggle="tab">${message("admin.aftersales.aftersalesItemInfo")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="aftersalesInfo" class="tab-pane active">
								<div class="row">
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											<dt>${message("Aftersales.member")}:</dt>
											<dd>${aftersales.member.username}</dd>
											<dt>${message("Aftersales.store")}:</dt>
											<dd>${aftersales.store.name}</dd>
											<dt>${message("admin.aftersales.type")}:</dt>
											<dd>${message("Aftersales.Type." + aftersales.type)}</dd>
											<dt>${message("Aftersales.status")}:</dt>
											<dd>
												<span class="[#if aftersales.status == "FAILED"]text-red[#elseif aftersales.status == "PENDING"]text-orange[#else]text-green[/#if]">${message("Aftersales.Status." + aftersales.status)}</span>
											</dd>
											[#if aftersales.type == "AFTERSALES_REPAIR" || aftersales.type == "AFTERSALES_REPLACEMENT"]
												<dt>${message("AftersalesRepair.consignee")}:</dt>
												<dd>${aftersales.consignee!"-"}</dd>
												<dt>${message("AftersalesRepair.address")}:</dt>
												<dd>${aftersales.area}${aftersales.address}</dd>
												<dt>${message("AftersalesRepair.phone")}:</dt>
												<dd>${aftersales.phone!"-"}</dd>
											[/#if]
											[#if aftersales.type == "AFTERSALES_RETURNS"]
												<dt>${message("AftersalesReturns.method")}:</dt>
												<dd>[#if aftersales.method?has_content]${message("AftersalesReturns.Method." + aftersales.method)}[#else]-[/#if]</dd>
												<dt>${message("AftersalesReturns.bank")}:</dt>
												<dd>${aftersales.bank!"-"}</dd>
												<dt>${message("AftersalesReturns.account")}:</dt>
												<dd>${aftersales.account!"-"}</dd>
											[/#if]
											<dt>${message("Aftersales.reason")}:</dt>
											<dd>${aftersales.reason}</dd>
										</dl>
									</div>
								</div>
							</div>
							<div id="aftersalesItem" class="tab-pane">
								<div class="table-responsive">
									<table class="table table-hover">
										<thead>
											<tr>
												<th>${message("OrderItem.sn")}</th>
												<th>${message("OrderItem.name")}</th>
												<th>${message("OrderItem.price")}</th>
												<th>${message("OrderItem.quantity")}</th>
											</tr>
										</thead>
										<tbody>
											[#list aftersales.aftersalesItems as aftersalesItem]
												<tr>
													<td>${aftersalesItem.orderItem.sn}</td>
													<td>
														[#if aftersalesItem.orderItem.sku??]
															<a href="${base}${aftersalesItem.orderItem.sku.path}" target="_blank">${aftersalesItem.orderItem.name}</a>
														[#else]
															${aftersalesItem.orderItem.name}
														[/#if]
														[#if aftersalesItem.orderItem.specifications?has_content]
															<span class="text-gray">[${aftersalesItem.orderItem.specifications?join(", ")}]</span>
														[/#if]
														[#if aftersalesItem.orderItem.type != "GENERAL"]
															<span class="text-red">[${message("Product.Type." + aftersalesItem.orderItem.type)}]</span>
														[/#if]
													</td>
													<td>
														[#if aftersalesItem.orderItem.type == "GENERAL"]
															${currency(aftersalesItem.orderItem.price, true)}
														[#else]
															-
														[/#if]
													</td>
													<td>${aftersalesItem.quantity}</td>
												</tr>
											[/#list]
										</tbody>
									</table>
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
			</form>
		</div>
	</main>
</body>
</html>
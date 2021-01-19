<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.orderShipping.view")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
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
				<li class="active">${message("admin.orderShipping.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#base" data-toggle="tab">${message("admin.orderShipping.base")}</a>
						</li>
						<li>
							<a href="#orderShippingItem" data-toggle="tab">${message("admin.orderShipping.orderShippingItem")}</a>
						</li>
					</ul>
					<div class="tab-content">
						<div id="base" class="tab-pane active">
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("OrderShipping.sn")}:</dt>
										<dd>${shipping.sn}</dd>
										<dt>${message("common.createdDate")}:</dt>
										<dd>${shipping.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
										<dt>${message("OrderShipping.shippingMethod")}:</dt>
										<dd>${shipping.shippingMethod!"-"}</dd>
										<dt>${message("OrderShipping.deliveryCorp")}:</dt>
										<dd>${shipping.deliveryCorp!"-"}</dd>
										<dt>${message("OrderShipping.trackingNo")}:</dt>
										<dd>${shipping.trackingNo!"-"}</dd>
										<dt>${message("OrderShipping.freight")}:</dt>
										<dd>${currency(shipping.freight, true, true)!"-"}</dd>
										<dt>${message("OrderShipping.consignee")}:</dt>
										<dd>${shipping.consignee!"-"}</dd>
										<dt>${message("OrderShipping.phone")}:</dt>
										<dd>${shipping.phone!"-"}</dd>
										<dt>${message("OrderShipping.area")}:</dt>
										<dd>${shipping.area!"-"}</dd>
										<dt>${message("OrderShipping.address")}:</dt>
										<dd>${shipping.address!"-"}</dd>
										<dt>${message("OrderShipping.zipCode")}:</dt>
										<dd>${shipping.zipCode!"-"}</dd>
										<dt>${message("OrderShipping.order")}:</dt>
										<dd>${shipping.order.sn}</dd>
										<dt>${message("OrderShipping.memo")}:</dt>
										<dd>${shipping.memo!"-"}</dd>
									</dl>
								</div>
							</div>
						</div>
						<div id="orderShippingItem" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("admin.orderShippingItem.sn")}</th>
											<th>${message("admin.orderShippingItem.name")}</th>
											<th>${message("OrderShippingItem.quantity")}</th>
											<th>${message("OrderShippingItem.isDelivery")}</th>
										</tr>
									</thead>
									<tbody>
										[#list shipping.orderShippingItems as shippingItem]
											<tr>
												<td>${shippingItem.sn}</td>
												<td>${shippingItem.name}</td>
												<td>${shippingItem.quantity}</td>
												<td>
													[#if shippingItem.isDelivery]
														<i class="text-green iconfont icon-check"></i>
													[#else]
														<i class="text-red iconfont icon-close"></i>
													[/#if]
												</td>
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
		</div>
	</main>
</body>
</html>
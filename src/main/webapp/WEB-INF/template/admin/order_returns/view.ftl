<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.orderReturns.view")} - 小象电商</title>
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
				<li class="active">${message("admin.orderReturns.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#base" data-toggle="tab">${message("admin.orderReturns.base")}</a>
						</li>
						<li>
							<a href="#orderReturnsItem" data-toggle="tab">${message("admin.orderReturns.orderReturnsItem")}</a>
						</li>
					</ul>
					<div class="tab-content">
						<div id="base" class="tab-pane active">
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("OrderReturns.sn")}:</dt>
										<dd>${returns.sn}</dd>
										<dt>${message("common.createdDate")}:</dt>
										<dd>${returns.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
										<dt>${message("OrderReturns.shippingMethod")}:</dt>
										<dd>${returns.shippingMethod!"-"}</dd>
										<dt>${message("OrderReturns.deliveryCorp")}:</dt>
										<dd>${returns.deliveryCorp!"-"}</dd>
										<dt>${message("OrderReturns.trackingNo")}:</dt>
										<dd>${returns.trackingNo!"-"}</dd>
										<dt>${message("OrderReturns.freight")}:</dt>
										<dd>${currency(returns.freight, true, true)!"-"}</dd>
										<dt>${message("OrderReturns.shipper")}:</dt>
										<dd>${returns.shipper!"-"}</dd>
										<dt>${message("OrderReturns.phone")}:</dt>
										<dd>${returns.phone!"-"}</dd>
										<dt>${message("OrderReturns.area")}:</dt>
										<dd>${returns.area!"-"}</dd>
										<dt>${message("OrderReturns.address")}:</dt>
										<dd>${returns.address!"-"}</dd>
										<dt>${message("OrderReturns.zipCode")}:</dt>
										<dd>${returns.zipCode!"-"}</dd>
										<dt>${message("OrderReturns.order")}:</dt>
										<dd>${returns.order.sn}</dd>
										<dt>${message("OrderReturns.memo")}:</dt>
										<dd>${returns.memo!"-"}</dd>
									</dl>
								</div>
							</div>
						</div>
						<div id="orderReturnsItem" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("OrderReturnsItem.sn")}</th>
											<th>${message("OrderReturnsItem.name")}</th>
											<th>${message("OrderReturnsItem.quantity")}</th>
										</tr>
									</thead>
									<tbody>
										[#list returns.orderReturnsItems as returnsItem]
											<tr>
												<td>${returnsItem.sn}</td>
												<td>${returnsItem.name}</td>
												<td>${returnsItem.quantity}</td>
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
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.orderPayment.view")} - 小象电商</title>
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
				<li class="active">${message("admin.orderPayment.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-heading">${message("admin.orderPayment.view")}</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-xs-12 col-sm-6">
							<dl class="items dl-horizontal">
								<dt>${message("OrderPayment.sn")}:</dt>
								<dd>${orderPayment.sn}</dd>
								<dt>${message("common.createdDate")}:</dt>
								<dd>${orderPayment.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
								<dt>${message("OrderPayment.method")}:</dt>
								<dd>${message("OrderPayment.Method." + orderPayment.method)}</dd>
								<dt>${message("OrderPayment.paymentMethod")}:</dt>
								<dd>${orderPayment.paymentMethod!"-"}</dd>
								<dt>${message("OrderPayment.bank")}:</dt>
								<dd>${orderPayment.bank!"-"}</dd>
								<dt>${message("OrderPayment.account")}:</dt>
								<dd>${orderPayment.account!"-"}</dd>
								<dt>${message("OrderPayment.amount")}:</dt>
								<dd>
									<span class="text-red">${currency(orderPayment.amount, true, true)}</span>
									[#if orderPayment.fee > 0]${message("OrderPayment.fee")}: (${currency(orderPayment.fee, true, true)})[/#if]
								</dd>
								<dt>${message("OrderPayment.payer")}:</dt>
								<dd>${orderPayment.payer!"-"}</dd>
								<dt>${message("OrderPayment.order")}:</dt>
								<dd>${orderPayment.order.sn}</dd>
								<dt>${message("OrderPayment.memo")}:</dt>
								<dd>${orderPayment.memo!"-"}</dd>
							</dl>
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
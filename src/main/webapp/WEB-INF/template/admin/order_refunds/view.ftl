<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.orderRefunds.view")} - 小象电商</title>
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
				<li class="active">${message("admin.orderRefunds.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-heading">${message("admin.orderRefunds.view")}</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-xs-12 col-sm-6">
							<dl class="items dl-horizontal">
								<dt>${message("OrderRefunds.sn")}:</dt>
								<dd>${refunds.sn}</dd>
								<dt>${message("common.createdDate")}:</dt>
								<dd>${refunds.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
								<dt>${message("OrderRefunds.method")}:</dt>
								<dd>${message("OrderRefunds.Method." + refunds.method)}</dd>
								<dt>${message("OrderRefunds.paymentMethod")}:</dt>
								<dd>${refunds.paymentMethod!"-"}</dd>
								<dt>${message("OrderRefunds.bank")}:</dt>
								<dd>${refunds.bank!"-"}</dd>
								<dt>${message("OrderRefunds.account")}:</dt>
								<dd>${refunds.account!"-"}</dd>
								<dt>${message("OrderRefunds.amount")}:</dt>
								<dd>
									<span class="text-red">${currency(refunds.amount, true, true)}</span>
								</dd>
								<dt>${message("OrderRefunds.payee")}:</dt>
								<dd>${refunds.payee!"-"}</dd>
								<dt>${message("OrderRefunds.order")}:</dt>
								<dd>${refunds.order.sn}</dd>
								<dt>${message("OrderRefunds.memo")}:</dt>
								<dd>${refunds.memo!"-"}</dd>
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
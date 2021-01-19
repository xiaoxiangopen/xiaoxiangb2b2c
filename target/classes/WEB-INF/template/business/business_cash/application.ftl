<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.businessCash.application")} - 小象电商</title>
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
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $businessCashForm = $("#businessCashForm");
				
				// 表单验证
				$businessCashForm.validate({
					rules: {
						amount: {
							required: true,
							positive: true,
							min: parseFloat(${setting.businessMinimumCashAmount}),
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							},
							remote: {
								url: "${base}/business/business_cash/check_balance",
								cache: false
							}
						},
						bank: "required",
						account: "required"
					},
					messages: {
						amount: {
							remote: "${message("business.businessCash.notCurrentAccountBalance")}"
						}
					}
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
				<li class="active">${message("business.businessCash.application")}</li>
			</ol>
			<form id="businessCashForm" class="ajax-form form-horizontal" action="${base}/business/business_cash/save" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.businessCash.application")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("Business.availableBalance")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p class="form-control-static text-red">${currency(currentUser.availableBalance, true, true)}</p>
							</div>
						</div>
						[#if currentUser.frozenAmount > 0]
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("Business.frozenAmount")}:</label>
								<div class="col-xs-9 col-sm-4">
									<p class="form-control-static text-gray">${currency(currentUser.frozenAmount, true, true)}</p>
								</div>
							</div>
						[/#if]
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="amount">${message("BusinessCash.amount")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="amount" name="amount" class="form-control" type="text" maxlength="16">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="bank">${message("BusinessCash.bank")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="bank" name="bank" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="account">${message("BusinessCash.account")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="account" name="account" class="form-control" type="text" maxlength="200">
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("common.submit")}</button>
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
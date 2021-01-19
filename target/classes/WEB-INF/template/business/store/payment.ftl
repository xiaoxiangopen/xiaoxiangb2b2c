<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.store.payment")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-spinner.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/jquery.spinner.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
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
				
				var $payConfirmModal = $("#payConfirmModal");
				var $mainHeaderNav = $("header.main-header a:not(.logout)");
				var $mainSidebarNav = $("aside.main-sidebar a, aside.main-sidebar button");
				var $storeForm = $("#storeForm");
				var $paymentPluginId = $("#paymentPluginId");
				var $spinner = $("[data-trigger='spinner']");
				var $years = $("#years");
				var $feeItem = $("#feeItem");
				var $fee = $("#fee");
				var $amount = $("#amount");
				var $paymentPlugin = $("#paymentPlugin");
				var $paymentPluginItem = $("#paymentPlugin div.media");
				var currentEndDate = ${currentStore.endDate?long};
				
				[#if currentStore.status == "APPROVED" || currentStore.hasExpired()]
					// 主顶部导航
					$mainHeaderNav.click(function() {
						return false;
					});
					
					// 主侧边导航
					$mainSidebarNav.click(function() {
						return false;
					});
				[/#if]
				
				[#if currentStore.status == "APPROVED"]
					// 检查店铺状态
					setInterval(function() {
						$.ajax({
							url: "${base}/business/store/store_status",
							type: "GET",
							dataType: "json",
							success: function(data) {
								if (data.status != "APPROVED") {
									location.href = "${base}/business/index";
								}
							}
						});
					}, 10000);
				[#else]
					// 检查店铺日期
					setInterval(function() {
						$.ajax({
							url: "${base}/business/store/end_date",
							type: "GET",
							dataType: "json",
							success: function(data) {
								if (moment(data.endDate).isAfter(currentEndDate)) {
									location.href = "${base}/business/index";
								}
							}
						});
					}, 10000);
				[/#if]
				
				// 购买时长
				$spinner.spinner("changing", function(event, newValue, oldValue) {
					calculate();
				});
				
				// 支付插件
				$paymentPluginItem.click(function() {
					var $element = $(this);
					var paymentPluginId = $element.data("payment-plugin-id");
					
					$element.addClass("active").siblings().removeClass("active");
					$paymentPluginId.val(paymentPluginId);
					calculate();
				});
				
				// 计算
				var calculate = _.debounce(function() {
					if (!$storeForm.valid()) {
						return;
					}
					$.ajax({
						url: "${base}/business/store/calculate",
						type: "GET",
						data: {
							paymentPluginId: $paymentPluginId.val(),
							years: $years.val()
						},
						dataType: "json",
						success: function(data) {
							$amount.text($.currency(data.amount, true, true));
							if (data.fee > 0) {
								$fee.text($.currency(data.fee, true, true));
								if ($feeItem.is(":hidden")) {
									$feeItem.velocity("slideDown");
								}
							} else {
								if ($feeItem.is(":visible")) {
									$feeItem.velocity("slideUp");
								}
							}
							if (data.amount > 0) {
								if ($paymentPlugin.is(":hidden")) {
									$paymentPlugin.velocity("slideDown");
								}
							} else {
								if ($paymentPlugin.is(":visible")) {
									$paymentPlugin.velocity("slideUp");
								}
							}
						}
					});
				}, 200);
				
				calculate();
				
				// 表单验证
				$storeForm.validate({
					submitHandler: function(form) {
						$.ajax({
							url: "${base}/business/store/payment",
							type: "POST",
							data: {
								years: $years.val()
							},
							dataType: "json",
							async: false,
							success: function(data) {
								if (data.platformSvcSn != null || (data.bail != null && data.bail > 0)) {
									$payConfirmModal.modal({
										backdrop: "static",
										keyboard: false
									}).modal("show");
									
									var i = 0;
									if (data.platformSvcSn != null) {
										$('<input name="paymentItemList[' + i + '].type" type="hidden">').appendTo($storeForm).val("SVC_PAYMENT");
										$('<input name="paymentItemList[' + i + '].svcSn" type="hidden">').appendTo($storeForm).val(data.platformSvcSn);
										i++;
									}
									if (data.bail != null && data.bail > 0) {
										$('<input name="paymentItemList[' + i + '].type" type="hidden">').appendTo($storeForm).val("BAIL_PAYMENT");
										$('<input name="paymentItemList[' + i + '].amount" type="hidden">').appendTo($storeForm).val(data.bail);
									}
									form.submit();
								} else {
									location.reload(true);
								}
							}
						});
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
			<div id="payConfirmModal" class="pay-confirm-modal modal fade" tabindex="-1">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title">${message("PayConfirmModal.payConfirm")}</h5>
						</div>
						<div class="modal-body">
							<p>
								<i class="iconfont icon-warn"></i>
								[#noautoesc]${message("PayConfirmModal.payPrimary")}[/#noautoesc]
							</p>
						</div>
						<div class="modal-footer">
							<a class="btn btn-primary" href="${base}/business/index">${message("PayConfirmModal.complete")}</a>
							<a class="btn btn-default" href="${base}/">${message("PayConfirmModal.problem")}</a>
						</div>
					</div>
				</div>
			</div>
			<ol class="breadcrumb">
				<li>
					<a href="${base}/business/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("business.store.payment")}</li>
			</ol>
			<form id="storeForm" class="form-horizontal" action="${base}/payment" method="post" target="_blank">
				<input id="paymentPluginId" name="paymentPluginId" type="hidden" value="${defaultPaymentPlugin.id}">
				<input name="rePayUrl" type="hidden" value="${base}/business/store/payment">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.store.payment")}</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-xs-12 col-sm-6">
								<dl class="items dl-horizontal clearfix">
									<dt>${message("Store.name")}:</dt>
									<dd>${currentStore.name}</dd>
									<dt>${message("Store.storeRank")}:</dt>
									<dd>${currentStore.storeRank.name}</dd>
									[#if currentStore.status == "SUCCESS"]
										<dt>${message("Store.endDate")}:</dt>
										<dd>
											<span class="text-orange">${currentStore.endDate?string("yyyy-MM-dd HH:mm:ss")}</span>
											[#if currentStore.hasExpired()]
												<span class="text-red">(${message("business.store.hasExpired")})</span>
											[/#if]
										</dd>
									[/#if]
									<dt>${message("StoreRank.serviceFee")}:</dt>
									<dd>${message("business.store.serviceFee", currency(currentStore.storeRank.serviceFee, true, true))}</dd>
									[#if currentStore.status == "APPROVED"]
										<dt>${message("StoreCategory.bail")}:</dt>
										<dd>${currency(currentStore.bailPayable, true, true)}</dd>
									[/#if]
								</dl>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="years">${message("business.store.years")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="spinner input-group input-group-sm" title="${message("business.store.yearsTitle")}" data-trigger="spinner" data-toggle="tooltip">
									<input id="years" class="form-control" type="text" maxlength="5" data-rule="quantity" data-min="1" data-max="99">
									<span class="input-group-addon">
										<a class="spin-up" href="javascript:;" data-spin="up">
											<i class="fa fa-caret-up"></i>
										</a>
										<a class="spin-down" href="javascript:;" data-spin="down">
											<i class="fa fa-caret-down"></i>
										</a>
									</span>
								</div>
							</div>
						</div>
						<div id="feeItem" class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("business.store.fee")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p id="fee" class="form-control-static text-red"></p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("business.store.amount")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p id="amount" class="form-control-static text-red"></p>
							</div>
						</div>
						[#if paymentPlugins?has_content]
							<div id="paymentPlugin" class="payment-plugin hidden-element">
								<div class="payment-plugin-heading">${message("common.paymentPlugin")}</div>
								<div class="payment-plugin-body clearfix">
									[#list paymentPlugins as paymentPlugin]
										<div class="media[#if paymentPlugin == defaultPaymentPlugin] active[/#if]" data-payment-plugin-id="${paymentPlugin.id}">
											<div class="media-left media-middle">
												<i class="iconfont icon-roundcheck"></i>
											</div>
											<div class="media-body media-middle">
												<div class="media-object">
													[#if paymentPlugin.logo?has_content]
														<img src="${paymentPlugin.logo}" alt="${paymentPlugin.displayName}">
													[#else]
														${paymentPlugin.displayName}
													[/#if]
												</div>
											</div>
										</div>
									[/#list]
								</div>
							</div>
						[/#if]
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								[#if currentStore.endDate?? || currentStore.status != "SUCCESS"]
									<button class="btn btn-primary" type="submit">${message("common.submit")}</button>
								[/#if]
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
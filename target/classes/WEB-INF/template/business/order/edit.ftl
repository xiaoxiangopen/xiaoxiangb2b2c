<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.order.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
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
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $orderForm = $("#orderForm");
				var $amount = $("#amount");
				var $freight = $("#freight");
				var $offsetAmount = $("#offsetAmount");
				var $isInvoice = $("input[name='isInvoice']");
				var $invoiceTitle = $("#invoiceTitle");
				var $invoiceTaxNumber = $("#invoiceTaxNumber");
				var $tax = $("#tax");
				var $areaId = $("[name='areaId']");
				var isLocked = false;
				var timeouts = {};
				
				// 地区选择
				$areaId.lSelect({
					url: "${base}/common/area"
				});
				
				[#--// 检查锁定--]
				[#--function acquireLock() {--]
				[#--	$.ajax({--]
				[#--		url: "${base}/business/order/acquire_lock",--]
				[#--		type: "POST",--]
				[#--		data: {--]
				[#--			orderId: '${order.id}'--]
				[#--		},--]
				[#--		dataType: "json",--]
				[#--		cache: false,--]
				[#--		success: function(data) {--]
				[#--			if (!data) {--]
				[#--				$orderForm.find(":input:not(:button)").prop("disabled", true);--]
				[#--				isLocked = true;--]
				[#--			}--]
				[#--		}--]
				[#--	});--]
				[#--}--]
				
				// 检查锁定
				// acquireLock();
				//
				// setInterval(function() {
				// 	acquireLock();
				// }, 50000);
				
				// 开具发票
				$isInvoice.change(function() {
					var checked = $(this).prop("checked");
					
					$invoiceTitle.prop("disabled", !checked);
					$invoiceTaxNumber.prop("disabled", !checked);
					[#if order.status == "PENDING_PAYMENT" && order.amountPaid <= 0]
						$tax.prop("disabled", !checked);
					[/#if]
				});
				
				// 计算
				$amount.add($freight).add($isInvoice).add($offsetAmount).add($tax).on("input propertychange change", function(event) {
					if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
						calculate($(this));
					}
				});
				
				// 计算
				function calculate($input) {
					var name = $input.attr("name");
					
					clearTimeout(timeouts[name]);
					timeouts[name] = setTimeout(function() {
						if ($input.valid()) {
							$.ajax({
								url: "${base}/business/order/calculate",
								type: "POST",
								data: {
									orderId: '${order.id}',
									freight: $freight.val(),
									offsetAmount: $offsetAmount.val(),
									tax: $tax.prop("disabled") ? 0 : $tax.val()
								},
								dataType: "json",
								cache: false,
								success: function(data) {
									$amount.text($.currency(data.amount, true, true));
								}
							});
						}
					}, 500);
				}
				
				// 表单验证
				$orderForm.validate({
					rules: {
						freight: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						offsetAmount: {
							required: true,
							number: true,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						rewardPoint: {
							required: true,
							digits: true
						},
						invoiceTitle: "required",
						invoiceTaxNumber: "required",
						tax: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						consignee: "required",
						areaId: "required",
						address: "required",
						zipCode: {
							required: true,
							zipCode: true
						},
						phone: {
							required: true,
							phone: true
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
				<li class="active">${message("business.order.edit")}</li>
			</ol>
			<form id="orderForm" class="ajax-form form-horizontal" action="${base}/business/order/update" method="post">
				<input name="orderId" type="hidden" value="${order.id}">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#orderInfo" data-toggle="tab">${message("business.order.orderInfo")}</a>
							</li>
							<li>
								<a href="#productInfo" data-toggle="tab">${message("business.order.productInfo")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="orderInfo" class="tab-pane active">
								<div class="row">
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											<dt>${message("Order.sn")}:</dt>
											<dd>${order.sn}</dd>
											<dt>${message("Order.type")}:</dt>
											<dd>${message("Order.Type." + order.type)}</dd>
											<dt>${message("Order.member")}:</dt>
											<dd>${order.member.username}</dd>
											<dt>${message("Order.price")}:</dt>
											<dd>${currency(order.price, true, true)}</dd>
											<dt>${message("Order.amount")}:</dt>
											<dd id="amount">${currency(order.amount, true, true)}</dd>
											<dt>${message("Order.weight")}:</dt>
											<dd>${order.weight}</dd>


											<dt>${message("Order.fee")}:</dt>
											<dd>${currency(order.fee, true, true)}</dd>
										</dl>
									</div>
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											<dt>${message("common.createdDate")}:</dt>
											<dd>${order.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
											<dt>${message("Order.status")}:</dt>
											<dd>
												<span class="text-green">${message("Order.Status." + order.status)}</span>
												[#if order.expire??]
													<span class="text-orange">(${message("Order.expire")}: ${order.expire?string("yyyy-MM-dd HH:mm:ss")})</span>
												[/#if]
											</dd>
											<dt>${message("Member.memberRank")}:</dt>
											<dd>${order.member.memberRank.name}</dd>
											<dt>${message("Order.exchangePoint")}:</dt>
											<dd>${order.exchangePoint}</dd>
											<dt>${message("Order.amountPaid")}:</dt>
											<dd>
												<span class="text-red">${currency(order.amountPaid, true, true)}</span>
												[#if order.amountPayable > 0]
													(${message("Order.amountPayable")}: ${currency(order.amountPayable, true, true)})
												[/#if]
											</dd>
											<dt>${message("Order.quantity")}:</dt>
											<dd>${order.quantity}</dd>

										</dl>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label item-required" for="freight">${message("Order.freight")}:</label>
											<div class="col-xs-9 col-sm-4">
												<input id="freight" name="freight" class="form-control" type="text" value="${order.freight}" maxlength="16"[#if order.status == "PENDING_REVIEW" || order.amountPaid > 0] disabled[/#if]>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label item-required" for="offsetAmount">${message("Order.offsetAmount")}:</label>
											<div class="col-xs-9 col-sm-4">
												<input id="offsetAmount" name="offsetAmount" class="form-control" type="text" value="${order.offsetAmount}" maxlength="16"[#if order.status == "PENDING_REVIEW" || order.amountPaid > 0] disabled[/#if]>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label item-required" for="rewardPoint">${message("Order.rewardPoint")}:</label>
											<div class="col-xs-9 col-sm-4">
												<input id="rewardPoint" name="rewardPoint" class="form-control" type="text" value="${order.rewardPoint}" maxlength="9">
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label">${message("Order.paymentMethod")}:</label>
											<div class="col-xs-9 col-sm-4">
												<select name="paymentMethodId" class="selectpicker form-control" data-size="5">
													<option value="">${message("common.choose")}</option>
													[#list paymentMethods as paymentMethod]
														<option value="${paymentMethod.id}"[#if paymentMethod == order.paymentMethod] selected[/#if]>${paymentMethod.name}</option>
													[/#list]
												</select>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label">${message("Order.shippingMethod")}:</label>
											<div class="col-xs-9 col-sm-4">
												[#if order.isDelivery]
													<select name="shippingMethodId" class="selectpicker form-control" data-size="5">
														<option value="">${message("common.choose")}</option>
														[#list shippingMethods as shippingMethod]
															<option value="${shippingMethod.id}"[#if shippingMethod == order.shippingMethod] selected[/#if]>${shippingMethod.name}</option>
														[/#list]
													</select>
												[#else]
													-
												[/#if]
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label item-required" for="invoiceTitle">${message("Invoice.title")}:</label>
											<div class="col-xs-9 col-sm-4">
												<div class="input-group">
													<input id="invoiceTitle" name="invoiceTitle" class="form-control" type="text" value="${(order.invoice.title)!}" maxlength="200"[#if !order.invoice??] disabled[/#if]>
													<span class="input-group-addon">
														<div class="checkbox">
															<input name="isInvoice" type="checkbox" value="true"[#if order.invoice??] checked[/#if]>
															<label></label>
														</div>
													</span>
												</div>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label item-required" for="invoiceTaxNumber">${message("Invoice.taxNumber")}:</label>
											<div class="col-xs-9 col-sm-4">
												<input id="invoiceTaxNumber" name="invoiceTaxNumber" class="form-control" type="text" value="${(order.invoice.taxNumber)!}" maxlength="200"[#if !order.invoice??] disabled[/#if]>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-xs-3 col-sm-4 control-label item-required" for="tax">${message("Order.tax")}:</label>
											<div class="col-xs-9 col-sm-4">
												<input id="tax" name="tax" class="form-control" type="text" value="${order.tax}" maxlength="16"[#if order.status == "PENDING_REVIEW" || order.amountPaid > 0 || !order.invoice??] disabled[/#if]>
											</div>
										</div>
									</div>
									[#if order.isDelivery]
										<div class="col-xs-12 col-sm-6">
											<div class="form-group">
												<label class="col-xs-3 col-sm-4 control-label item-required" for="consignee">${message("Order.consignee")}:</label>
												<div class="col-xs-9 col-sm-4">
													<input id="consignee" name="consignee" class="form-control" type="text" value="${order.consignee}" maxlength="200">
												</div>
											</div>
										</div>
										<div class="col-xs-12 col-sm-6">
											<div class="form-group">
												<label class="col-xs-3 col-sm-4 control-label item-required">${message("Order.area")}:</label>
												<div class="col-xs-9 col-sm-8">
													<input name="areaId" type="hidden" value="${(order.area.id)!}" treePath="${(order.area.treePath)!}">
												</div>
											</div>
										</div>
										<div class="col-xs-12 col-sm-6">
											<div class="form-group">
												<label class="col-xs-3 col-sm-4 control-label item-required" for="address">${message("Order.address")}:</label>
												<div class="col-xs-9 col-sm-4">
													<input id="address" name="address" class="form-control" type="text" value="${order.address}" maxlength="200">
												</div>
											</div>
										</div>
										<div class="col-xs-12 col-sm-6">
											<div class="form-group">
												<label class="col-xs-3 col-sm-4 control-label item-required" for="zipCode">${message("Order.zipCode")}:</label>
												<div class="col-xs-9 col-sm-4">
													<input id="zipCode" name="zipCode" class="form-control" type="text" value="${order.zipCode}" maxlength="200">
												</div>
											</div>
										</div>
										<div class="col-xs-12 col-sm-6">
											<div class="form-group">
												<label class="col-xs-3 col-sm-4 control-label item-required" for="phone">${message("Order.phone")}:</label>
												<div class="col-xs-9 col-sm-4">
													<input id="phone" name="phone" class="form-control" type="text" value="${order.phone}" maxlength="200">
												</div>
											</div>
										</div>
										<div class="col-xs-12 col-sm-6">
											<div class="form-group">
												<label class="col-xs-3 col-sm-4 control-label" for="memo">${message("Order.memo")}:</label>
												<div class="col-xs-9 col-sm-4">
													<input id="memo" name="memo" class="form-control" type="text" value="${order.memo}" maxlength="200">
												</div>
											</div>
										</div>
									[/#if]
								</div>
							</div>
							<div id="productInfo" class="tab-pane">
								<div class="table-responsive">
									<table class="table table-hover">
										<thead>

												<th>${message("Product.internalNumber")}</th>
[#--												<th>${message("OrderItem.sn")}</th>--]
												<th>${message("OrderItem.name")}</th>
												<th>${message("OrderItem.price")}</th>
												<th>${message("OrderItem.quantity")}</th>
												<th>${message("OrderItem.subtotal")}</th>
											</tr>
										</thead>
										<tbody>
											[#list order.orderItems as orderItem]
													<td>${orderItem.sku.product.internalNumber}</td>
[#--													<td>${orderItem.sn}</td>--]
													<td>
														[#if orderItem.sku??]
															<a href="${base}${orderItem.sku.path}" target="_blank">${orderItem.name}</a>
														[#else]
															${orderItem.name}
														[/#if]
														[#if orderItem.specifications?has_content]
															<span class="text-gray">[${orderItem.specifications?join(", ")}]</span>
														[/#if]
														[#if orderItem.type != "GENERAL"]
															<span class="text-red">[${message("Product.Type." + orderItem.type)}]</span>
														[/#if]
													</td>
													<td>
														[#if orderItem.type == "GENERAL"]
															${currency(orderItem.price, true)}
														[#else]
															-
														[/#if]
													</td>
													<td>${orderItem.quantity}</td>
													<td>
														[#if orderItem.type == "GENERAL"]
															${currency(orderItem.subtotal, true)}
														[#else]
															-
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
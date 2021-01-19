<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.order.view")} - 小象电商</title>
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
	<script id="transitStepTemplate" type="text/template">
		<%if (_.isEmpty(data.transitSteps)) {%>
			<p class="text-gray">${message("admin.order.noResult")}</p>
		<%} else {%>
			<div class="list-group">
				<%_.each(data.transitSteps, function(transitStep, i) {%>
					<div class="list-group-item">
						<p class="text-gray"><%-transitStep.time%></p>
						<p><%-transitStep.context%></p>
					</div>
				<%});%>
			</div>
		<%}%>
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $transitStep = $("a.transit-step");
				var transitStepTemplate = _.template($("#transitStepTemplate").html());
				var $transitStepModal = $("#transitStepModal");
				var $transitStepModalBody = $("#transitStepModal div.modal-body");
				
				// 物流动态
				$transitStep.click(function() {
					var $element = $(this);
					
					$.ajax({
						url: "${base}/admin/order/transit_step",
						type: "GET",
						data: {
							shippingId: $element.data("shipping-id")
						},
						dataType: "json",
						beforeSend: function() {
							$transitStepModal.modal();
						},
						success: function(data) {
							$transitStepModalBody.html(transitStepTemplate({
								data: data
							}));
						}
					});
					return false;
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="admin">
	[#include "/admin/include/main_header.ftl" /]
	[#include "/admin/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<div id="transitStepModal" class="transit-step-modal modal fade" tabindex="-1">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">&times;</button>
							<h5 class="modal-title">${message("admin.order.transitStep")}</h5>
						</div>
						<div class="modal-body"></div>
						<div class="modal-footer">
							<button class="btn btn-default" type="button" data-dismiss="modal">${message("common.cancel")}</button>
						</div>
					</div>
				</div>
			</div>
			<ol class="breadcrumb">
				<li>
					<a href="${base}/admin/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("admin.order.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#orderInfo" data-toggle="tab">${message("admin.order.orderInfo")}</a>
						</li>
						<li>
							<a href="#productInfo" data-toggle="tab">${message("admin.order.productInfo")}</a>
						</li>
						<li>
							<a href="#paymentInfo" data-toggle="tab">${message("admin.order.paymentInfo")}</a>
						</li>
						<li>
							<a href="#orderRefundsInfo" data-toggle="tab">${message("admin.order.orderRefundsInfo")}</a>
						</li>
						<li>
							<a href="#orderShippingInfo" data-toggle="tab">${message("admin.order.orderShippingInfo")}</a>
						</li>
						<li>
							<a href="#orderReturnsInfo" data-toggle="tab">${message("admin.order.orderReturnsInfo")}</a>
						</li>
						<li>
							<a href="#orderLog" data-toggle="tab">${message("admin.order.orderLog")}</a>
						</li>
					</ul>
					<div class="tab-content">
						<div id="orderInfo" class="tab-pane active">
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("Order.sn")}:</dt>
										<dd>${order.sn}</dd>
										<dt>${message("admin.orderPayment.transaction")}:</dt>
										<dd>
											[#list order.paymentTransactions as paymentTransaction]
												[#if paymentTransaction.isSuccess]
													${paymentTransaction.sn}
												[/#if]
											[/#list]
										</dd>
										<dt>${message("Order.type")}:</dt>
										<dd>${message("Order.Type." + order.type)}</dd>
										<dt>${message("Order.member")}:</dt>
										<dd>
											<a href="${base}/admin/member/view?id=${order.member.id}">${order.member.username}</a>
										</dd>
										<dt>${message("common.captcha.vipCard")}:</dt>
										<dd>${order.member.attributeValue0}</dd>
										<dt>${message("Order.store")}:</dt>
										<dd>${order.store.name}</dd>
										<dt>${message("Order.price")}:</dt>
										<dd>${currency(order.price, true, true)}</dd>
										<dt>${message("Order.amount")}:</dt>
										<dd>${currency(order.amount, true, true)}</dd>
										[#if order.invoice??]
											<dt>${message("Invoice.title")}:</dt>
											<dd>${order.invoice.title}</dd>
											<dt>${message("Order.tax")}:</dt>
											<dd>${currency(order.tax, true, true)}</dd>
										[/#if]
										[#if order.refundAmount > 0 || order.refundableAmount > 0]
											<dt>${message("Order.refundAmount")}:</dt>
											<dd>${currency(order.refundAmount, true, true)}</dd>
										[/#if]
										<dt>${message("Order.weight")}:</dt>
										<dd>${order.weight}</dd>
										<dt>${message("Order.shippedQuantity")}:</dt>
										<dd>${order.shippedQuantity}</dd>
										<dt>${message("admin.order.promotion")}:</dt>
										<dd>
											[#if order.promotionNames?has_content]
												${order.promotionNames?join(", ")}
											[#else]
												-
											[/#if]
										</dd>
										<dt>${message("Order.fee")}:</dt>
										<dd>${currency(order.fee, true, true)}</dd>
										<dt>${message("Order.offsetAmount")}:</dt>
										<dd>${currency(order.offsetAmount, true, true)}</dd>
										<dt>${message("Order.paymentMethod")}:</dt>
										<dd>${order.paymentMethodName!"-"}</dd>
										<dt>${message("Order.consignee")}:</dt>
										<dd>${order.consignee}</dd>
										<dt>${message("Order.address")}:</dt>
										<dd>${order.address}</dd>
										<dt>${message("Order.phone")}:</dt>
										<dd>${order.phone}</dd>


									</dl>
								</div>
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("common.createdDate")}:</dt>
										<dd>${order.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
										<dt>${message("admin.orderPayment.channels")}:</dt>
										<dd>
											[#list order.paymentTransactions as paymentTransaction]
												[#if paymentTransaction.isSuccess]
													${paymentTransaction.paymentPluginName}
												[/#if]
											[/#list]
										</dd>
										<dt>${message("Order.status")}:</dt>
										<dd>
											<span class="[#if order.status == "PENDING_SHIPMENT" || order.status == "PENDING_REVIEW" || order.status == "PENDING_PAYMENT"]text-orange[#elseif order.status == "FAILED" || order.status == "DENIED"]text-red[#elseif order.status == "CANCELED"]text-gray-dark[#else]text-green[/#if]">${message("Order.Status." + order.status)}</span>
											[#if order.hasExpired()]
												<span class="text-gray-dark">(${message("admin.order.hasExpired")})</span>
											[#else]
												[#if order.expire??]
													<span class="text-orange">(${message("Order.expire")}: ${order.expire?string("yyyy-MM-dd HH:mm:ss")})</span>
												[/#if]
											[/#if]
										</dd>
										<dt>${message("Member.memberRank")}:</dt>
										<dd>${order.member.memberRank.name}</dd>
										<dt>${message("Store.storeRank")}:</dt>
										<dd>${order.store.storeRank.name}</dd>
										<dt>${message("Order.exchangePoint")}:</dt>
										<dd>${order.exchangePoint}</dd>
										<dt>${message("Order.amountPaid")}:</dt>
										<dd>
											<span class="text-red">${currency(order.amountPaid, true, true)}</span>
											[#if order.amountPayable > 0]
												(${message("Order.amountPayable")}: ${currency(order.amountPayable, true, true)})
											[/#if]
										</dd>
										[#if order.invoice??]
											<dt>${message("Invoice.taxNumber")}:</dt>
											<dd>${order.invoice.taxNumber}</dd>
										[/#if]
										[#if order.refundAmount > 0 || order.refundableAmount > 0]
											<dt>${message("Order.refundableAmount")}:</dt>
											<dd>
												<span class="text-orange">${currency(order.refundableAmount, true, true)}</span>
											</dd>
										[/#if]
										<dt>${message("Order.quantity")}:</dt>
										<dd>${order.quantity}</dd>
										<dt>${message("Order.returnedQuantity")}:</dt>
										<dd>${order.returnedQuantity}</dd>
										<dt>${message("Order.freight")}:</dt>
										<dd>${currency(order.freight, true, true)}</dd>
										<dt>${message("Order.rewardPoint")}:</dt>
										<dd>${order.rewardPoint}</dd>
										<dt>${message("Order.shippingMethod")}:</dt>
										<dd>${order.shippingMethodName!"-"}</dd>
										<dt>${message("Order.area")}:</dt>
										<dd>${order.areaName}</dd>
										<dt>${message("Order.zipCode")}:</dt>
										<dd>${order.zipCode}</dd>
										<dt>${message("Order.memo")}:</dt>
										<dd>${order.memo}</dd>
									</dl>
								</div>
							</div>
						</div>
						<div id="productInfo" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("Product.internalNumber")}</th>
[#--											<th>${message("OrderItem.sn")}</th>--]
											<th>${message("OrderItem.name")}</th>
											<th>原价</th>
											<th>${message("OrderItem.price")}</th>
											<th>${message("OrderItem.quantity")}</th>
											<th>${message("OrderItem.subtotal")}</th>
										</tr>
									</thead>
									<tbody>
										[#list order.orderItems as orderItem]
											<tr>
												<td>${orderItem.sku.product.internalNumber}</td>
[#--												<td>${orderItem.sn}</td>--]
												<td width="400">
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
													${currency(orderItem.sku.price, true)}
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
						<div id="paymentInfo" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("OrderPayment.sn")}</th>
											<th>${message("OrderPayment.method")}</th>
											<th>${message("OrderPayment.paymentMethod")}</th>
											<th>${message("OrderPayment.fee")}</th>
											<th>${message("OrderPayment.amount")}</th>
											<th>${message("common.createdDate")}</th>
										</tr>
									</thead>
									<tbody>
										[#list order.orderPayments as orderPayment]
											<tr>
												<td>${orderPayment.sn}</td>
												<td>${message("OrderPayment.Method." + orderPayment.method)}</td>
												<td>${orderPayment.paymentMethod!"-"}</td>
												<td>${currency(orderPayment.fee, true)}</td>
												<td>${currency(orderPayment.amount, true)}</td>
												<td>
													<span title="${orderPayment.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${orderPayment.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
											</tr>
										[/#list]
									</tbody>
								</table>
								[#if !order.orderPayments?has_content]
									<p class="no-result">${message("common.noResult")}</p>
								[/#if]
							</div>
						</div>
						<div id="orderRefundsInfo" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("OrderRefunds.sn")}</th>
											<th>${message("OrderRefunds.method")}</th>
											<th>${message("OrderRefunds.paymentMethod")}</th>
											<th>${message("OrderRefunds.amount")}</th>
											<th>${message("common.createdDate")}</th>
										</tr>
									</thead>
									<tbody>
										[#list order.orderRefunds as orderRefunds]
											<tr>
												<td>${orderRefunds.sn}</td>
												<td>${message("OrderRefunds.Method." + orderRefunds.method)}</td>
												<td>${orderRefunds.paymentMethod!"-"}</td>
												<td>${currency(orderRefunds.amount, true)}</td>
												<td>
													<span title="${orderRefunds.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${orderRefunds.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
											</tr>
										[/#list]
									</tbody>
								</table>
								[#if !order.orderRefunds?has_content]
									<p class="no-result">${message("common.noResult")}</p>
								[/#if]
							</div>
						</div>
						<div id="orderShippingInfo" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("OrderShipping.sn")}</th>
											<th>${message("OrderShipping.shippingMethod")}</th>
											<th>${message("OrderShipping.deliveryCorp")}</th>
											<th>${message("OrderShipping.trackingNo")}</th>
											<th>${message("OrderShipping.consignee")}</th>
											<th>${message("OrderShipping.isDelivery")}</th>
											<th>${message("common.createdDate")}</th>
										</tr>
									</thead>
									<tbody>
										[#list order.orderShippings as orderShipping]
											<tr>
												<td>${orderShipping.sn}</td>
												<td>${orderShipping.shippingMethod!"-"}</td>
												<td>${orderShipping.deliveryCorp!"-"}</td>
												<td width="260">
													${orderShipping.trackingNo!"-"}
													[#if isKuaidi100Enabled && orderShipping.deliveryCorpCode?has_content && orderShipping.trackingNo?has_content]
														<a class="transit-step" href="javascript:;" data-shipping-id="${orderShipping.id}">[${message("admin.order.transitStep")}]</a>
													[/#if]
												</td>
												<td>${orderShipping.consignee!"-"}</td>
												<td>
													[#if orderShipping.isDelivery]
														<i class="text-green iconfont icon-check"></i>
													[#else]
														<i class="text-red iconfont icon-close"></i>
													[/#if]
												</td>
												<td>
													<span title="${orderShipping.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${orderShipping.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
											</tr>
										[/#list]
									</tbody>
								</table>
								[#if !order.orderShippings?has_content]
									<p class="no-result">${message("common.noResult")}</p>
								[/#if]
							</div>
						</div>
						<div id="orderReturnsInfo" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("OrderReturns.sn")}</th>
											<th>${message("OrderReturns.shippingMethod")}</th>
											<th>${message("OrderReturns.deliveryCorp")}</th>
											<th>${message("OrderReturns.trackingNo")}</th>
											<th>${message("OrderReturns.shipper")}</th>
											<th>${message("common.createdDate")}</th>
										</tr>
									</thead>
									<tbody>
										[#list order.orderReturns as orderReturns]
											<tr>
												<td>${orderReturns.sn}</td>
												<td>${orderReturns.shippingMethod!"-"}</td>
												<td>${orderReturns.deliveryCorp!"-"}</td>
												<td>${orderReturns.trackingNo!"-"}</td>
												<td>${orderReturns.shipper}</td>
												<td>
													<span title="${orderReturns.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${orderReturns.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
											</tr>
										[/#list]
									</tbody>
								</table>
								[#if !order.orderReturns?has_content]
									<p class="no-result">${message("common.noResult")}</p>
								[/#if]
							</div>
						</div>
						<div id="orderLog" class="tab-pane">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>${message("OrderLog.type")}</th>
											<th>${message("OrderLog.detail")}</th>
											<th>${message("common.createdDate")}</th>
										</tr>
									</thead>
									<tbody>
										[#list order.orderLogs as orderLog]
											<tr>
												<td>${message("OrderLog.Type." + orderLog.type)}</td>
												<td>
													[#if orderLog.detail??]
														<span title="${orderLog.detail}">${abbreviate(orderLog.detail, 30, "...")}</span>
													[#else]
														-
													[/#if]
												</td>
												<td>
													<span title="${orderLog.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${orderLog.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
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
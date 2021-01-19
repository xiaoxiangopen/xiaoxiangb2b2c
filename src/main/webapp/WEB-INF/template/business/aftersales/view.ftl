<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.aftersales.view")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
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
	<script src="${base}/resources/business/js/base.js"></script>
	<script id="transitStepTemplate" type="text/template">
		<%if (_.isEmpty(data.transitSteps)) {%>
			<p class="text-gray">${message("common.noResult")}</p>
		<%} else {%>
			<%_.each(data.transitSteps, function(transitStep, i) {%>
				<div class="list-item">
					<p class="text-gray"><%-transitStep.time%></p>
					<p><%-transitStep.context%></p>
				</div>
			<%});%>
		<%}%>
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $review = $("button.review");
				var $complete = $("button.complete");
				var $transitStep = $("a.transit-step");
				var $transitStepModal = $("#transitStepModal");
				var $transitStepModalBody = $("#transitStepModal div.modal-body");
				var transitStepTemplate = _.template($("#transitStepTemplate").html());
				
				// 审核
				$review.click(function() {
					var $element = $(this);
					
					bootbox.prompt({
						title: "${message("common.bootbox.title")}",
						inputType: "select",
						value: "APPROVED",
						inputOptions: [
							{
								text: "${message("business.aftersales.reviewApproved")}",
								value: "APPROVED"
							},
							{
								text: "${message("business.aftersales.reviewFailed")}",
								value: "FAILED"
							}
						],
						callback: function(result) {
							if (result == null) {
								return;
							}
							
							$.ajax({
								url: "${base}/business/aftersales/review",
								type: "POST",
								data: {
									aftersalesId: $element.data("id"),
									passed: result == "APPROVED" ? "true" : "false"
								},
								dataType: "json",
								cache: false,
								success: function() {
									location.reload(true);
								}
							});
						}
					}).find("select").selectpicker();
				});
				
				// 完成
				$complete.add().click(function() {
					var $element = $(this);
					
					bootbox.confirm("${message("business.aftersales.completeConfirm")}", function(result) {
						if (result == null || !result) {
							return;
						}
						
						$.ajax({
							url: "${base}/business/aftersales/complete",
							type: "POST",
							data: {
								aftersalesId: $element.data("id")
							},
							dataType: "json",
							cache: false,
							success: function() {
								location.reload(true);
							}
						});
					});
				});
				
				// 物流动态
				$transitStep.click(function() {
					var $element = $(this);
					
					$.ajax({
						url: "${base}/business/aftersales/transit_step",
						type: "GET",
						data: {
							aftersalesId: $element.data("id")
						},
						dataType: "json",
						beforeSend: function() {
							$transitStepModalBody.empty();
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
<body class="business">
	[#include "/business/include/main_header.ftl" /]
	[#include "/business/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<div id="transitStepModal" class="modal fade" tabindex="-1">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">&times;</button>
							<h5 class="modal-title">${message("business.aftersales.transitStep")}</h5>
						</div>
						<div class="modal-body"></div>
						<div class="modal-footer">
							<button class="btn btn-default btn-sm" type="button" data-dismiss="modal">${message("common.close")}</button>
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
				<li class="active">${message("business.aftersales.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#aftersalesInfo" data-toggle="tab">${message("business.aftersales.aftersalesInfo")}</a>
						</li>
						<li>
							<a href="#aftersalesItem" data-toggle="tab">${message("business.aftersales.aftersalesItem")}</a>
						</li>
					</ul>
					<div class="tab-content">
						<div id="aftersalesInfo" class="tab-pane active">
							<div class="row">
								<div class="col-xs-6 col-xs-offset-2">
									<div class="form-group">
										<button class="review btn btn-default" type="button" data-id="${aftersales.id}"[#if aftersales.status != "PENDING"] disabled[/#if]>${message("business.aftersales.review")}</button>
										<button class="complete btn btn-default" type="button" data-id="${aftersales.id}"[#if aftersales.status != "APPROVED"] disabled[/#if]>${message("business.aftersales.complete")}</button>
									</div>
								</div>
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("business.aftersales.order")}:</dt>
										<dd>
											${aftersales.orderItems[0].order.sn}
											<a class="text-gray" href="${base}/business/order/view?orderId=${aftersales.orderItems[0].order.id}">[${message("common.view")}]</a>
										</dd>
										<dt>${message("Aftersales.member")}:</dt>
										<dd>${aftersales.member.username}</dd>
										<dt>${message("business.aftersales.type")}:</dt>
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
										[#if aftersales.trackingNo?has_content && aftersales.deliveryCorp?has_content]
											<dt>${message("business.aftersales.trackingNo")}:</dt>
											<dd>
												${aftersales.trackingNo}
												[#if isKuaidi100Enabled]
													<a class="transit-step text-orange" href="javascript:;" data-id="${aftersales.id}">[${message("business.aftersales.viewTransitStep")}]</a>
												[/#if]
											</dd>
											<dt>${message("business.aftersales.deliveryCorp")}:</dt>
											<dd>${aftersales.deliveryCorp}</dd>
										[/#if]
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
		</div>
	</main>
</body>
</html>
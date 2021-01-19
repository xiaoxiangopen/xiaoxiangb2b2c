<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.print.order")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/print.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $print = $("#print");
				
				// 打印
				$print.click(function() {
					window.print();
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="admin print">
	<div class="bar hidden-print">
		<button id="print" class="btn btn-default" type="button">
			<i class="iconfont icon-punch"></i>
			${message("admin.print.print")}
		</button>
	</div>
	<main>
		<table class="table table-bordered">
			<tr>
				<td>
					<img src="${setting.logo}" alt="${setting.siteName}">
				</td>
				<td>
					<p>${setting.siteName}</p>
					<p>${setting.siteUrl}</p>
				</td>
				<td>
					<p>${message("Order.consignee")}: ${order.consignee}</p>
					<p>${message("Order.member")}: ${order.member.username}</p>
				</td>
			</tr>
			<tr>
				<td>${message("Order.sn")}: ${order.sn}</td>
				<td>${message("common.createdDate")}: ${order.createdDate?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td>${message("admin.print.printDate")}: ${.now?string("yyyy-MM-dd")}</td>
			</tr>
			[#if order.isDelivery]
				<tr>
					<td>${message("Order.consignee")}: ${order.consignee}</td>
					<td>${message("Order.zipCode")}: ${order.zipCode}</td>
					<td>${message("Order.phone")}: ${order.phone}</td>
				</tr>
				<tr>
					<td colspan="3">${message("Order.address")}: ${order.areaName}${order.address}</td>
				</tr>
			[/#if]
			<tr>
				<td>${message("Order.memo")}: ${order.memo}</td>
				<td colspan="2">
					[#if order.paymentMethodName??]
						<p>${message("Order.paymentMethod")}: ${order.paymentMethodName}</p>
					[/#if]
					[#if order.shippingMethodName??]
						<p>${message("Order.shippingMethod")}: ${order.shippingMethodName}</p>
					[/#if]
					<p>${message("Order.price")}: ${currency(order.price, true, true)}</p>
					[#if order.fee > 0]
						<p>${message("Order.fee")}: ${currency(order.fee, true, true)}</p>
					[/#if]
					[#if order.freight > 0]
						<p>${message("Order.freight")}: ${currency(order.freight, true, true)}</p>
					[/#if]
					[#if order.tax > 0]
						<p>${message("Order.tax")}: ${currency(order.tax, true, true)}</p>
					[/#if]

					[#if order.offsetAmount != 0]
						${message("Order.offsetAmount")}: ${currency(order.offsetAmount, true, true)}</p>
					[/#if]
					<p>
						${message("Order.amount")}: <strong>${currency(order.amount, true, true)}</strong>
					</p>
				</td>
			</tr>
		</table>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>${message("admin.print.number")}</th>
					<th>${message("OrderItem.sn")}</th>
					<th>${message("OrderItem.name")}</th>
					<th>${message("OrderItem.price")}</th>
					<th>${message("OrderItem.quantity")}</th>
					<th>${message("OrderItem.subtotal")}</th>
				</tr>
			</thead>
			<tbody>
				[#list order.orderItems as orderItem]
					<tr>
						<td>${orderItem_index + 1}</td>
						<td>${orderItem.sn}</td>
						<td>
							${orderItem.name}
							[#if orderItem.specifications?has_content]
								<span class="text-gray">[${orderItem.specifications?join(", ")}]</span>
							[/#if]
							[#if orderItem.type != "GENERAL"]
								<span class="text-red">[${message("Product.Type." + orderItem.type)}]</span>
							[/#if]
						</td>
						<td>
							[#if orderItem.type == "GENERAL"]
								${currency(orderItem.price, true, true)}
							[#else]
								-
							[/#if]
						</td>
						<td>${orderItem.quantity}</td>
						<td>
							[#if orderItem.type == "GENERAL"]
								${currency(orderItem.subtotal, true, true)}
							[#else]
								-
							[/#if]
						</td>
					</tr>
				[/#list]
			</tbody>
		</table>
		<div class="text-right">小象电商.net</div>
	</main>
</body>
</html>
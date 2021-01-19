<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.index.title")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/index.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/g2.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
	<script src="${base}/resources/common/js/echarts.min.js"></script>

	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $document = $(document);
				var $modal = $("#modal");
				var $logout = $("#logout");
				
				[#if !currentStore?? || !currentStore.isActive()]
					$modal.modal({
						backdrop: "static",
						keyboard: false
					}).modal("show");
				[/#if]
				
				// 用户注销
				$logout.click(function() {
					$document.trigger("loggedOut.xiaoxiangshop.user", $.getCurrentUser());
				});
				
				[#--// 订单完成数图表--]
				[#--var completeOrderCountChart = new G2.Chart({--]
					[#--id: "completeOrderCountChart",--]
					[#--height: 200,--]
					[#--forceFit: true,--]
					[#--plotCfg: {--]
						[#--margin: [20, 20, 30, 80]--]
					[#--}--]
				[#--});--]
				
				[#--completeOrderCountChart.source([], {--]
					[#--date: {--]
						[#--type: "time",--]
						[#--tickCount: 10,--]
						[#--formatter: function(value) {--]
							[#--return moment(value).format("YYYY-MM-DD");--]
						[#--}--]
					[#--},--]
					[#--value: {--]
						[#--alias: "${message("Statistic.Type.COMPLETE_ORDER_COUNT")}"--]
					[#--}--]
				[#--});--]
				[#--completeOrderCountChart.axis("date", {--]
					[#--title: null,--]
					[#--formatter: function(value) {--]
						[#--return moment(value).format("MM-DD");--]
					[#--}--]
				[#--});--]
				[#--completeOrderCountChart.axis("value", {--]
					[#--title: null--]
				[#--});--]
				[#--completeOrderCountChart.line().position("date*value").color("#66baff");--]
				[#--completeOrderCountChart.render();--]
				
				[#--$.ajax({--]
					[#--url: "${base}/business/order_statistic/data",--]
					[#--type: "get",--]
					[#--data: {--]
						[#--type: "COMPLETE_ORDER_COUNT"--]
					[#--},--]
					[#--dataType: "json",--]
					[#--success: function(data) {--]
						[#--debugger--]
						[#--completeOrderCountChart.changeData(data);--]
					[#--}--]
				[#--});--]
				
				[#--// 订单完成金额图表--]
				[#--var completeOrderAmountChart = new G2.Chart({--]
					[#--id: "completeOrderAmountChart",--]
					[#--height: 200,--]
					[#--forceFit: true,--]
					[#--plotCfg: {--]
						[#--margin: [20, 20, 30, 80]--]
					[#--}--]
				[#--});--]
				
				[#--completeOrderAmountChart.source([], {--]
					[#--date: {--]
						[#--type: "time",--]
						[#--tickCount: 10,--]
						[#--formatter: function(value) {--]
							[#--return moment(value).format("YYYY-MM-DD");--]
						[#--}--]
					[#--},--]
					[#--value: {--]
						[#--alias: "${message("Statistic.Type.COMPLETE_ORDER_AMOUNT")}",--]
						[#--formatter: function(value) {--]
							[#--return $.currency(value, true);--]
						[#--}--]
					[#--}--]
				[#--});--]
				[#--completeOrderAmountChart.axis("date", {--]
					[#--title: null,--]
					[#--formatter: function(value) {--]
						[#--return moment(value).format("MM-DD");--]
					[#--}--]
				[#--});--]
				[#--completeOrderAmountChart.axis("value", {--]
					[#--title: null--]
				[#--});--]
				[#--completeOrderAmountChart.line().position("date*value").color("#ffab66");--]
				[#--completeOrderAmountChart.render();--]
				
				[#--$.ajax({--]
					[#--url: "${base}/business/order_statistic/data",--]
					[#--type: "get",--]
					[#--data: {--]
						[#--type: "COMPLETE_ORDER_AMOUNT"--]
					[#--},--]
					[#--dataType: "json",--]
					[#--success: function(data) {--]
						[#--completeOrderAmountChart.changeData(data);--]
					[#--}--]
				[#--});--]

				var beginDate = $('#beginDate').datetimepicker({
					format: 'YYYY-MM-DD',
					locale: moment.locale('zh-cn')
				});
				var endDate = $('#endDate').datetimepicker({
					format: 'YYYY-MM-DD',
					locale: moment.locale('zh-cn'),
					maxDate: new Date()
				});
				getChartData();
				$('#queryChartData').click(function() {
					getChartData();
					$.bootstrapGrowl('请稍等...');
				})
              function getChartData() {
                var   beginDate=$('#beginDate').val();
				  var endDate=$('#endDate').val();
				  $.ajax({
					  url: "${base}/business/order_statistic/orderData?beginDate="+beginDate+"&endDate="+endDate,
					  type: "GET",
					  dataType: "json",
					  cache: false,
					  success: function(data) {
						  initChart(data);
					  }
				  });
			  }


               function initChart(data){
				   var averageOrderAmounts=data.averageOrderAmounts;
				   var orderNums=data.orderNums;
				   var totalOrderAmounts=data.totalOrderAmounts;
				   var xAxisDatas=data.xAxisDatas;
				   var myChart = echarts.init(document.getElementById('completeOrderCountChart'));
				   //百度统计图
				   var colors = ['#5793f3', '#d14a61', '#675bba'];

				   option = {
					   color: colors,

					   tooltip: {
						   trigger: 'axis',
						   axisPointer: {
							   type: 'cross'
						   }
					   },
					   grid: {
						   right: '20%'
					   },
					   toolbox: {
						   feature: {
							   restore: {show: true},
							   saveAsImage: {show: true}
						   }
					   },
					   legend: {
						   data: ['当日订单数', '当日订单金额', '当日订单金额']
					   },
					   xAxis: [
						   {
							   type: 'category',
							   axisTick: {
								   alignWithLabel: true
							   },
							   data: xAxisDatas
						   }
					   ],
					   yAxis: [
						   {
							   type: 'value',
							   name: '当日订单数',
							   min: 0,
							   position: 'right',
							   axisLine: {
								   lineStyle: {
									   color: colors[0]
								   }
							   },
							   axisLabel: {
								   formatter: '{value} 单'
							   }
						   },
						   {
							   type: 'value',
							   name: '当日订单金额',
							   min: 0,
							   position: 'right',
							   offset: 80,
							   axisLine: {
								   lineStyle: {
									   color: colors[1]
								   }
							   },
							   axisLabel: {
								   formatter: '{value} 元'
							   }
						   },
						   {
							   type: 'value',
							   name: '当日平均订单金额',
							   min: 0,
							   position: 'left',
							   axisLine: {
								   lineStyle: {
									   color: colors[2]
								   }
							   },
							   axisLabel: {
								   formatter: '{value} 元'
							   }
						   }
					   ],
					   series: [
						   {
							   name: '当日订单数',
							   type: 'bar',
							   data:orderNums
						   },
						   {
							   name: '当日订单金额',
							   type: 'bar',
							   yAxisIndex: 1,
							   data:totalOrderAmounts
						   },
						   {
							   name: '当日平均订单金额',
							   type: 'line',
							   yAxisIndex: 2,
							   data:averageOrderAmounts
						   }
					   ]
				   };
				   myChart.setOption(option);

			   }

			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="business index">
	[#include "/business/include/main_header.ftl" /]
	[#include "/business/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<div id="modal" class="modal fade" tabindex="-1">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title">${message("business.index.modalTitle")}</h5>
						</div>
						<div class="modal-body">
							[#if !currentStore??]
								${message("business.index.storeNotExists")}
							[#elseif currentStore.status == "PENDING"]
								${message("business.index.storePending")}
							[#elseif currentStore.status == "FAILED"]
								${message("business.index.storeFailed")}
							[#elseif currentStore.status == "APPROVED"]
								${message("business.index.storeApproved")}
							[#elseif currentStore.hasExpired()]
								${message("business.index.storeHasExpired")}
							[#else]
								${message("business.index.storeClose")}
							[/#if]
						</div>
						<div class="modal-footer">
							[#if !currentStore??]
								<a class="btn btn-primary" href="${base}/business/store/register">${message("business.index.storeRegister")}</a>
							[#elseif currentStore.status == "PENDING"]
								<a class="btn btn-primary" href="${base}/">${message("business.index.home")}</a>
							[#elseif currentStore.status == "FAILED"]
								<a class="btn btn-primary" href="${base}/business/store/reapply">${message("business.index.storeReapply")}</a>
							[#elseif currentStore.status == "APPROVED"]
								<a class="btn btn-primary" href="${base}/business/store/payment">${message("business.index.storePayment")}</a>
							[#elseif currentStore.hasExpired()]
								<a class="btn btn-primary" href="${base}/business/store/payment">${message("business.index.storeRenewal")}</a>
							[/#if]
							<a id="logout" class="btn btn-default" href="${base}/business/logout">${message("business.index.logout")}</a>
						</div>
					</div>
				</div>
			</div>
			<ol class="breadcrumb">
				<li class="active">${message("business.index.title")}</li>
			</ol>
			[#if currentStore?? && currentStore.status == "SUCCESS"]
				<div class="store-info panel panel-default">
					<div class="panel-body">
						<div class="row">
							<div class="col-xs-12 col-sm-2">
								<a class="logo" href="${base}${currentStore.path}" target="_blank">
									<img src="${currentStore.logo!setting.defaultStoreLogo}" alt="${currentStore.name}">
								</a>
							</div>
							<div class="col-xs-12 col-sm-4">
								<p>${message("Store.name")}: ${currentStore.name}</p>
								<p>${message("Store.storeRank")}: ${currentStore.storeRank.name}</p>
							</div>
							<div class="col-xs-12 col-sm-6">
								<p>
									${message("Business.balance")}:
									<span class="text-red">${currency(currentUser.balance, true, true)}</span>
									[#if currentUser.frozenAmount > 0]
										<span class="text-gray">(${message("Business.frozenAmount")}: ${currency(currentUser.frozenAmount, true, true)})</span>
									[/#if]
								</p>
								<p>
									${message("Store.endDate")}:
									<span class="text-red"[#if currentStore.endDate??] title="${currentStore.endDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip"[/#if]>${(currentStore.endDate?string("yyyy-MM-dd HH:mm:ss"))!message("business.index.infinite")}</span>
								</p>
							</div>
						</div>
					</div>
				</div>
			[/#if]
			<div class="row">
				<div class="col-xs-12 col-sm-12">
					<div class="order-info panel panel-default">
						<div class="panel-heading">
							<h5>${message("business.index.orderInfo")}</h5>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-2">
									<a href="${base}/business/order/list?status=PENDING_PAYMENT&hasExpired=false">
										<div class="media">
											<div class="media-left hidden-xs hidden-sm hidden-md">
												<i class="bg-blue-light iconfont icon-pay"></i>
											</div>
											<div class="media-body media-middle">
												[@order_count storeId = currentStore.id status = "PENDING_PAYMENT" hasExpired = false]
													<h2 class="text-blue-light">
														${count}
														<small>${message("business.index.countUnit")}</small>
													</h2>
												[/@order_count]
												<p>${message("Order.Status.PENDING_PAYMENT")}</p>
											</div>
										</div>
									</a>
								</div>
								<div class="col-xs-2">
									<a href="${base}/business/order/list?status=PENDING_REVIEW&hasExpired=false">
										<div class="media">
											<div class="media-left hidden-xs hidden-sm hidden-md">
												<i class="bg-orange-light iconfont icon-time"></i>
											</div>
											<div class="media-body media-middle">
												[@order_count storeId = currentStore.id status = "PENDING_REVIEW" hasExpired = false]
													<h2 class="text-orange-light">
														${count}
														<small>${message("business.index.countUnit")}</small>
													</h2>
												[/@order_count]
												<p>${message("Order.Status.PENDING_REVIEW")}</p>
											</div>
										</div>
									</a>
								</div>
								<div class="col-xs-2">
									<a href="${base}/business/order/list?status=PENDING_SHIPMENT">
										<div class="media">
											<div class="media-left hidden-xs hidden-sm hidden-md">
												<i class="bg-yellow-light iconfont icon-deliver"></i>
											</div>
											<div class="media-body media-middle">
												[@order_count storeId = currentStore.id status = "PENDING_SHIPMENT"]
													<h2 class="text-yellow-light">
														${count}
														<small>${message("business.index.countUnit")}</small>
													</h2>
												[/@order_count]
												<p>${message("Order.Status.PENDING_SHIPMENT")}</p>
											</div>
										</div>
									</a>
								</div>
								<div class="col-xs-2">
									<a href="${base}/business/order/list?isPendingRefunds=true">
										<div class="media">
											<div class="media-left hidden-xs hidden-sm hidden-md">
												<i class="bg-purple-light iconfont icon-refund"></i>
											</div>
											<div class="media-body media-middle">
												[@order_count storeId = currentStore.id isPendingRefunds = true]
													<h2 class="text-purple-light">
														${count}
														<small>${message("business.index.countUnit")}</small>
													</h2>
												[/@order_count]
												<p>${message("business.index.pendingRefunds")}</p>
											</div>
										</div>
									</a>
								</div>
								<!--售后申请-->
								<div class="col-xs-2">
									<a href="${base}/business/aftersales/list?status=PENDING">
										<div class="media">
											<div class="media-left hidden-xs hidden-sm hidden-md">
												<i class="bg-cyan-light iconfont icon-goods"></i>
											</div>
											<div class="media-body media-middle">
												[@aftersales_count storeId = currentStore.id status = "PENDING"]
													<h2 class="text-purple-light">
														${count}
														<small>${message("business.index.countUnit")}</small>
													</h2>
												[/@aftersales_count]
												<p>${message("business.index.pendingftersales")}</p>
											</div>
										</div>
									</a>
								</div>
								<!--退货申请-->
								<div class="col-xs-2">
									<a href="${base}/business/aftersales/list?status=PENDING&type=AFTERSALES_RETURNS">
										<div class="media">
											<div class="media-left hidden-xs hidden-sm hidden-md">
												<i class="bg-red-light iconfont icon-wang_light"></i>
											</div>
											<div class="media-body media-middle">
												[@aftersales_count storeId = currentStore.id type="AFTERSALES_RETURNS" status = "PENDING"]
													<h2 class="text-purple-light">
														${count}
														<small>${message("business.index.countUnit")}</small>
													</h2>
												[/@aftersales_count]
												<p>${message("business.index.pendingRefundsAftersales")}</p>
											</div>
										</div>
									</a>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-sm-12">
					<div class="product-info panel panel-default">
						<div class="panel-heading">
							<h5>${message("business.index.productInfo")}</h5>
						</div>
						<div class="panel-body">
							<a class="btn btn-default" href="${base}/business/product/list?isMarketable=true">
								${message("business.index.marketable")}
								[@product_count storeId = currentStore.id isMarketable = true]
									<span class="badge" title="${count}">[#if count > 99]99+[#else]${count}[/#if]</span>
								[/@product_count]
							</a>
							<a class="btn btn-default" href="${base}/business/product/list?isOutOfStock=true">
								${message("business.index.outOfStack")}
								[@product_count storeId = currentStore.id isOutOfStock = true]
									<span class="badge" title="${count}">[#if count > 99]99+[#else]${count}[/#if]</span>
								[/@product_count]
							</a>
							<a class="btn btn-default" href="${base}/business/product/list?isActive=false">
								${message("business.index.inactive")}
								[@product_count storeId = currentStore.id isActive = false]
									<span class="badge" title="${count}">[#if count > 99]99+[#else]${count}[/#if]</span>
								[/@product_count]
							</a>
							<a class="btn btn-default" href="${base}/business/product/list?isTop=true">
								${message("business.index.top")}
								[@product_count storeId = currentStore.id isTop = true]
									<span class="badge" title="${count}">[#if count > 99]99+[#else]${count}[/#if]</span>
								[/@product_count]
							</a>
							<a class="btn btn-default" href="${base}/business/product/list?isStockAlert=true">
								${message("business.index.stockAlert")}
								[@product_count storeId = currentStore.id isStockAlert = true]
									<span class="badge" title="${count}">[#if count > 99]99+[#else]${count}[/#if]</span>
								[/@product_count]
							</a>
						</div>
					</div>
				</div>
			</div>
[#--			隐藏订单统计--]
			<div class="row" style="display: none">
				<div class="col-xs-24 col-sm-12">
					<div class="panel panel-default">
						<div class="panel-heading" style="height: 50px;">
							<div class="col-xs-5 col-sm-3" style="padding-top: 10px;">
							<h5>${message("business.index.completeOrderCountStatistic")}</h5>
							</div>
							<div class="col-xs-7 col-sm-7">
								<div class="input-group" data-provide="datetimerangepicker" data-date-format="YYYY-MM-DD">
									<input id="beginDate" name="beginDate" autocomplete="off" class="form-control" type="text" placeholder="请选择开始日期">
									<span class="input-group-addon">-</span>
									<input id="endDate" name="endDate" autocomplete="off" class="form-control" type="text" placeholder="请选择结束日期">
									<span class="input-group-addon iconfont icon-search" style="cursor:pointer" id="queryChartData"></span>
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div id="completeOrderCountChart" style="width: 100%; height: 300px;"></div>
						</div>
					</div>
				</div>
				[#--<div class="col-xs-12 col-sm-6">--]
					[#--<div class="panel panel-default">--]
						[#--<div class="panel-heading">--]
							[#--<h5>${message("business.index.completeOrderCountStatistic")}</h5>--]
						[#--</div>--]
						[#--<div class="panel-body">--]
							[#--<div id="completeOrderCountChart"></div>--]
						[#--</div>--]
					[#--</div>--]
				[#--</div>--]
				[#--<div class="col-xs-12 col-sm-6">--]
					[#--<div class="panel panel-default">--]
						[#--<div class="panel-heading">--]
							[#--<h5>${message("business.index.completeOrderAmountStatistic")}</h5>--]
						[#--</div>--]
						[#--<div class="panel-body">--]
							[#--<div id="completeOrderAmountChart"></div>--]
						[#--</div>--]
					[#--</div>--]
				[#--</div>--]
			</div>
		</div>
	</main>
</body>
</html>
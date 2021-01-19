<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.orderStatistic.list")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-datetimepicker.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/g2.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	<script src="${base}/resources/common/js/echarts.min.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
				$().ready(function() {
					var $sourceItem = $("[data-type]");
					var $source = $("[name='source']");
					// 类型
					$sourceItem.click(function() {
						var $element = $(this);

						var source = $element.data("type");
						var sourceName = $element.data("typeName");

						$element.addClass("active").siblings().removeClass("active");
						$source.val(source);
						$('#sourceName').html(sourceName)

					});

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
					$('#queryChartData').click(function () {
						getChartData();
						$.bootstrapGrowl('请稍等...');
					})

					function getChartData() {
						var beginDate = $('#beginDate').val();
						var endDate = $('#endDate').val();
						var source = $('#source').val();
						$.ajax({
							url: "${base}/admin/order_statistic/orderSourceDatas?source="+source+"&beginDate=" + beginDate + "&endDate=" + endDate,
							type: "GET",
							dataType: "json",
							cache: false,
							success: function (data) {
								initChart(data);
							}
						});
					}


					function initChart(data){
						var averageOrderAmounts=data.averageOrderAmounts;
						var orderNums=data.orderNums;
						var totalOrderAmounts=data.totalOrderAmounts;
						var xAxisDatas=data.xAxisDatas;
						var myChart = echarts.init(document.getElementById('chart'));
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
			<li class="active">${message("admin.orderStatistic.list")}</li>
		</ol>
		<form id="orderStatisticForm"  method="get">
			<input name="source" id="source" type="hidden" value="0">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-12 col-sm-2">
							<div class="btn-group">
								<div class="btn-group" style="width:180px;">
									<button class="btn btn-default dropdown-toggle" style="width:180px;" type="button" data-toggle="dropdown">
										${message("Data.source.desc.row")}:<span id="sourceName">${message("Data.source.desc.0")}</span>
										<span class="caret" style="float: right;margin-top: 6px;"></span>
									</button>
									<ul class="dropdown-menu">
										<li  class="active"  data-type="0" data-type-name="${message("Data.source.desc.0")}">
											<a href="javascript:;">${message("Data.source.desc.0")}</a>
										</li>
										<li  data-type="1" data-type-name="${message("Data.source.desc.1")}">
											<a href="javascript:;">${message("Data.source.desc.1")}</a>
										</li>
										<li data-type="2" data-type-name="${message("Data.source.desc.2")}">
											<a href="javascript:;">${message("Data.source.desc.2")}</a>
										</li>
										<li  data-type="3" data-type-name="${message("Data.source.desc.3")}">
											<a href="javascript:;">${message("Data.source.desc.3")}</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-4">
							<div class="input-group" data-provide="datetimerangepicker">
								<span class="input-group-addon">${message("common.dateRange")}</span>
								<input name="beginDate" id="beginDate" class="form-control" type="text" >
								<span class="input-group-addon">-</span>
								<input name="endDate" id="endDate" class="form-control" type="text">
								<span class="input-group-addon iconfont icon-search" style="cursor:pointer" id="queryChartData"></span>
							</div>
						</div>
					</div>
				</div>
				<div class="panel-body">
					<div id="chart" style="width: 100%; height: 300px;"></div>
				</div>
			</div>
		</form>
	</div>
</main>
</body>
</html>
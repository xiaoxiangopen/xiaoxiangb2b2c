<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.fundStatistic.list")} - 小象电商</title>
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
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $fundStatisticForm = $("#fundStatisticForm");
				var $type = $("[name='type']");
				var $period = $("[name='period']");
				var $export = $("#export");
				var $typeItem = $("[data-type]");
				var $periodItem = $("[data-period]");
				var $beginDate = $("[name='beginDate']");
				var $endDate = $("[name='endDate']");
				var dateFormat = "YYYY-MM-DD";
				
				// 类型
				$typeItem.click(function() {
					var $element = $(this);
					var type = $element.data("type");
					
					$element.addClass("active").siblings().removeClass("active");
					$type.val(type);
					
					loadData();
				});
				
				// 周期
				$periodItem.click(function() {
					var $element = $(this);
					var period = $element.data("period");
					
					switch (period) {
						case "YEAR":
							dateFormat = "YYYY";
							break;
						case "MONTH":
							dateFormat = "YYYY-MM";
							break;
						default:
							dateFormat = "YYYY-MM-DD";
					}
					
					$element.addClass("active").siblings().removeClass("active");
					$period.val(period);
					$beginDate.data("DateTimePicker").format(dateFormat);
					$endDate.data("DateTimePicker").format(dateFormat);
					
					loadData();
				});
				
				// 日期
				$beginDate.add($endDate).on("dp.change", function() {
					loadData();
				});
				
				// 图表
				var chart = new G2.Chart({
					id: "chart",
					height: 400,
					forceFit: true
				});
				
				chart.source([], {
					date: {
						type: "time",
						alias: "${message("Statistic.date")}",
						tickCount: 10,
						formatter: function(value) {
							return moment(value).format(dateFormat);
						}
					},
					value: {}
				});
				chart.line().position("date*value").color("#66baff");
				chart.render();
				
				// 加载数据
				function loadData() {
					$fundStatisticForm.ajaxSubmit({
						success: function(data, textStatus, xhr, $form) {
							chart.col("value", {
								alias: $typeItem.filter(".active").data("type-name")
							});
							chart.changeData(data);
						}
					});
				};
				
				loadData();
				
				// 导出
				$export.click(function() {
					setTimeout(function() {
						chart.downloadImage();
					}, 1000);
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
			<ol class="breadcrumb">
				<li>
					<a href="${base}/admin/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("admin.fundStatistic.list")}</li>
			</ol>
			<form id="fundStatisticForm" action="${base}/admin/fund_statistic/data" method="get">
				<input name="type" type="hidden" value="${type}">
				<input name="period" type="hidden" value="${period}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-3">
								<div class="btn-group">
									<button id="export" class="btn btn-default" type="button">
										<i class="iconfont icon-upload"></i>
										${message("admin.fundStatistic.export")}
									</button>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("admin.fundStatistic.type")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											[#list types as value]
												<li[#if value == type] class="active"[/#if] data-type="${value}" data-type-name="${message("Statistic.Type." + value)}">
													<a href="javascript:;">${message("Statistic.Type." + value)}</a>
												</li>
											[/#list]
										</ul>
									</div>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("admin.fundStatistic.period")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu" role="menu">
											[#list periods as value]
												<li[#if value == period] class="active"[/#if] data-period="${value}">
													<a href="javascript:;">${message("Statistic.Period." + value)}</a>
												</li>
											[/#list]
										</ul>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-4">
								<div class="input-group" data-provide="datetimerangepicker">
									<span class="input-group-addon">${message("common.dateRange")}</span>
									<input name="beginDate" class="form-control" type="text" value="${beginDate?string("yyyy-MM-dd")}">
									<span class="input-group-addon">-</span>
									<input name="endDate" class="form-control" type="text" value="${endDate?string("yyyy-MM-dd")}">
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div id="chart"></div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>
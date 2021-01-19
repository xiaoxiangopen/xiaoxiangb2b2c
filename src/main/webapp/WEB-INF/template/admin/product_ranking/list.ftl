<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.productRanking.list")} - 小象电商</title>
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
				
				var $productRankingForm = $("#productRankingForm");
				var $rankingType = $("[name='rankingType']");
				var $size = $("[name='size']");
				var $export = $("#export");
				var $rankingTypeItem = $("[data-ranking-type]");
				var $sizeItem = $("[data-size]");
				loadData();
				// 排名类型
				$rankingTypeItem.click(function() {
					var $element = $(this);
					var rankingType = $element.data("ranking-type");
					
					$element.addClass("active").siblings().removeClass("active");
					$rankingType.val(rankingType);
					
					loadData();
				});
				
				// 数量
				$sizeItem.click(function() {
					var $element = $(this);
					var size = $element.data("size");
					
					$element.addClass("active").siblings().removeClass("active");
					$size.val(size);
					
					loadData();
				});
				
				[#--// 图表--]
				[#--var Frame = G2.Frame;--]
				[#--var chart = new G2.Chart({--]
				[#--	id: "chart",--]
				[#--	height: 500,--]
				[#--	forceFit: true,--]
				[#--	plotCfg: {--]
				[#--		margin: [20, 50, 80, 150]--]
				[#--	}--]
				[#--});--]
				
				[#--chart.source([], {--]
				[#--	name: {--]
				[#--		type: "cat",--]
				[#--		alias: "${message("Product.name")}"--]
				[#--	},--]
				[#--	value: {}--]
				[#--});--]
				
				[#--chart.axis("name", {--]
				[#--	title: null--]
				[#--});--]
				[#--chart.coord("rect").transpose();--]
				[#--chart.interval().position("name*value").color("#ffab66");--]
				[#--chart.render();--]
				
				// 加载数据
				function loadData() {
					$productRankingForm.ajaxSubmit({
						success: function(data, textStatus, xhr, $form) {
							initChart(data);
							// var frame = new Frame(data);
							//
							// chart.col("value", {
							// 	alias: $rankingTypeItem.filter(".active").data("ranking-type-name")
							// });
							// frame = Frame.sort(frame, "value");
							// chart.changeData(frame);
							// chart.changeSize(1000, data.length * 40 + 100);
							// chart.forceFit();
						}
					});
				};
				
				function initChart(data){

					var seriesList=data.seriesList;

					var yAxisList=data.yAxisList;
					var myChart = echarts.init(document.getElementById('chart'));
					if(seriesList.length>10){
						myChart.resize({height:seriesList.length*40});
					}
					//百度统计图
					option = {
						color: ['#3398DB'],
						title: {
							text: '商品排行'
						},
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'shadow'
							}
						},
						legend: {
							data: []
						},
						grid: {
							left: '1%',
							right: '5%',
							bottom: '3%',
							containLabel: true
						},
						xAxis: {
							type: 'value',
							boundaryGap: [0, 0.01]
						},
						yAxis: {
							type: 'category',
							data: yAxisList,
							triggerEvent: true,
							axisLabel:{
								formatter: function (value) {
									if (value.length >10) {
										return value.substring(0, 10) + "...";
									} else {
										return value;
									}
								},
								textStyle: {
									baseline:'middle'

								}
							},

						},
						series: [
							{
								name: '',
								type: 'bar',
								data: seriesList
							}
						]
					};

					myChart.setOption(option);

				}
				// 导出
				// $export.click(function() {
				// 	setTimeout(function() {
				// 		chart.downloadImage();
				// 	}, 1000);
				// });
			
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
				<li class="active">${message("admin.productRanking.list")}</li>
			</ol>
			<form id="productRankingForm" action="${base}/admin/product_ranking/data" method="get">
				<input name="rankingType" type="hidden" value="${rankingType}">
				<input name="size" type="hidden" value="${size}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="btn-group">
[#--							<button id="export" class="btn btn-default" type="button">--]
[#--								<i class="iconfont icon-upload"></i>--]
[#--								${message("admin.productRanking.export")}--]
[#--							</button>--]
							<div class="btn-group">
								<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
									${message("admin.productRanking.type")}
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									[#list rankingTypes as value]
										[#if value =="MONTH_SALES"|| value =="SALES"]
										<li[#if value == rankingType] class="active"[/#if] data-ranking-type="${value}" data-ranking-type-name="${message("Product.RankingType." + value)}">
											<a href="javascript:;">${message("Product.RankingType." + value)}</a>
										</li>
										[/#if]
									[/#list]
								</ul>
							</div>
							<div class="btn-group">
								<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
									${message("admin.productRanking.size")}
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li[#if size == 10] class="active"[/#if] data-size="10">
										<a href="javascript:;">10</a>
									</li>
									<li[#if size == 20] class="active"[/#if] data-size="20">
										<a href="javascript:;">20</a>
									</li>
									<li[#if size == 30] class="active"[/#if] data-size="30">
										<a href="javascript:;">30</a>
									</li>
									<li[#if size == 50] class="active"[/#if] data-size="50">
										<a href="javascript:;">50</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div id="chart" style="width:100%;height:400px"></div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>
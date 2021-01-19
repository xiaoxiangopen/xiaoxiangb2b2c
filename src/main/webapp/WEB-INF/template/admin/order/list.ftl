<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.order.list")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $printModal = $("#printModal");
				var $printSelect = $("#printSelect");
				var $printButton = $("#printButton");
				var $print = $("a.print");
				var orderId;
				var $export = $("#export");
				var $shelf = $("#shelf");
				var $ids = $("input[name='ids']");
				// 打印
				$printButton.click(function() {
					if ($printSelect.val() == "order") {
						window.open("${base}/admin/print/order?id=" + orderId);
					}
				});
				
				// 打印
				$print.click(function() {
					var $element = $(this);
					
					orderId = $element.data("order-id");
					$printModal.modal();
				});
				var paramValues='';
				$export.click(function() {
					var ids = $("input[name='ids']:checked");
					if(ids!=null&&ids.length!==0){
						for(var i=0;i<ids.length;i++){
							var ordersn=ids.eq(i)[0].value;
							if(paramValues===''){
								paramValues="ids="+ordersn;
							}else{
								paramValues+="&ids="+ordersn;
							}
						}
					}else{
						paramValues=$('#orderForm').serialize();
					}

					var request = new XMLHttpRequest();
					var noetime=nowtime();
					var fileName = "sf"+noetime+".xls";
					request.open('GET', "${base}/admin/order/export_express?"+paramValues, true);
					request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
					request.responseType = 'blob';

					request.onload = function(e) {
						if (this.status === 200) {
							var blob = this.response;
							if(window.navigator.msSaveOrOpenBlob) {
								window.navigator.msSaveBlob(blob, fileName);
							}
							else{
								var downloadLink = window.document.createElement('a');
								var contentTypeHeader = request.getResponseHeader("Content-Type");
								downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
								downloadLink.download = fileName;
								document.body.appendChild(downloadLink);
								downloadLink.click();
								document.body.removeChild(downloadLink);
							}
						}
					};
					request.send();
				});

				// // ID多选框
				// $ids.change(function() {
				//
				// 	$export.add($shelf).attr("disabled", $("input[name='ids']:checked").length < 1);
				// });
			});
			function nowtime(){//将当前时间转换成yyyymmdd格式
				var mydate = new Date();
				var str = "" + mydate.getFullYear();
				var mm = mydate.getMonth()+1
				if(mydate.getMonth()>9){
					str += mm;
				}
				else{
					str += "0" + mm;
				}
				if(mydate.getDate()>9){
					str += mydate.getDate();
				}
				else{
					str += "0" + mydate.getDate();
				}
				return str;
			}

			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="admin">
	[#include "/admin/include/main_header.ftl" /]
	[#include "/admin/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<div id="printModal" class="modal fade" tabindex="-1">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">&times;</button>
							<h5 class="modal-title">${message("admin.order.print")}</h5>
						</div>
						<div class="modal-body text-center">
							<select id="printSelect" class="selectpicker">
								<option value="order">${message("admin.order.orderPrint")}</option>
							</select>
						</div>
						<div class="modal-footer">
							<button id="printButton" class="btn btn-primary" type="button">${message("common.ok")}</button>
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
				<li class="active">${message("admin.order.list")}</li>
			</ol>
			<form id="orderForm" action="${base}/admin/order/list" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<input name="isPendingReceive" type="hidden" value="[#if isPendingReceive??]${isPendingReceive?string("true", "false")}[/#if]">
				<input name="isPendingRefunds" type="hidden" value="[#if isPendingRefunds??]${isPendingRefunds?string("true", "false")}[/#if]">
				<input name="isAllocatedStock" type="hidden" value="[#if isAllocatedStock??]${isAllocatedStock?string("true", "false")}[/#if]">
				<input name="hasExpired" type="hidden" value="[#if hasExpired??]${hasExpired?string("true", "false")}[/#if]">
				<div id="filterModal" class="modal fade" tabindex="-1">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" type="button" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">${message("admin.order.moreOption")}</h5>
							</div>
							<div class="modal-body form-horizontal">
								<div class="form-group">
									<label class="col-xs-3 control-label">${message("Order.type")}:</label>
									<div class="col-xs-9 col-sm-7">
										<select name="type" class="selectpicker form-control" data-size="5">
											<option value="">${message("common.choose")}</option>
											[#list types as value]
												<option value="${value}"[#if value == type] selected[/#if]>${message("Order.Type." + value)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">${message("Order.status")}:</label>
									<div class="col-xs-9 col-sm-7">
										<select name="status" class="selectpicker form-control" data-size="5">
											<option value="">${message("common.choose")}</option>
											[#list statuses as value]
												<option value="${value}"[#if value == status] selected[/#if]>${message("Order.Status." + value)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label" for="memberUsername">${message("admin.order.memberUsername")}:</label>
									<div class="col-xs-9 col-sm-7">
										<input id="memberUsername" name="memberUsername" class="form-control" type="text" value="${memberUsername}" maxlength="200">
									</div>
								</div>

								<div class="form-group">
									<label class="col-xs-3 control-label" for="transactionSn">${message("admin.orderPayment.transaction")}:</label>
									<div class="col-xs-9 col-sm-7">
										<input id="transactionSn" name="transactionSn" class="form-control" type="text" value="${transactionSn}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label" for="beginDate">${message("common.dateRange")}:</label>
									<div class="col-xs-9 col-sm-7">
										<div class="input-group" data-provide="datetimerangepicker" data-date-format="YYYY-MM-DD HH:mm:ss">
											<input id="beginDate" name="beginDate" autocomplete="off" class="form-control" type="text" value="[#if beginDate??]${beginDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]">
											<span class="input-group-addon">-</span>
											<input id="endDate" name="endDate" autocomplete="off" class="form-control" type="text" value="[#if endDate??]${endDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]">
										</div>
									</div>
								</div>



							</div>
							<div class="modal-footer">
								<button class="btn btn-primary" type="submit">${message("common.ok")}</button>
								<button class="btn btn-default" type="button" data-dismiss="modal">${message("common.cancel")}</button>
							</div>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-9">
								<div class="btn-group">
									<button class="btn btn-default" type="button" data-action="refresh">
										<i class="iconfont icon-refresh"></i>
										${message("common.refresh")}
									</button>
									<a class="btn btn-default" id="export" data-action="export" >
										<i class="iconfont icon-add"></i>
										${message("business.order.exportsf")}
									</a>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("admin.order.filter")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if isPendingReceive?? && isPendingReceive] class="active"[/#if] data-filter-property="isPendingReceive" data-filter-value="true">
												<a href="javascript:;">${message("admin.order.pendingReceive")}</a>
											</li>
											<li[#if isPendingReceive?? && !isPendingReceive] class="active"[/#if] data-filter-property="isPendingReceive" data-filter-value="false">
												<a href="javascript:;">${message("admin.order.unPendingReceive")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isPendingRefunds?? && isPendingRefunds] class="active"[/#if] data-filter-property="isPendingRefunds" data-filter-value="true">
												<a href="javascript:;">${message("admin.order.pendingRefunds")}</a>
											</li>
											<li[#if isPendingRefunds?? && !isPendingRefunds] class="active"[/#if] data-filter-property="isPendingRefunds" data-filter-value="false">
												<a href="javascript:;">${message("admin.order.unPendingRefunds")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isAllocatedStock?? && isAllocatedStock] class="active"[/#if] data-filter-property="isAllocatedStock" data-filter-value="true">
												<a href="javascript:;">${message("admin.order.allocatedStock")}</a>
											</li>
											<li[#if isAllocatedStock?? && !isAllocatedStock] class="active"[/#if] data-filter-property="isAllocatedStock" data-filter-value="false">
												<a href="javascript:;">${message("admin.order.unAllocatedStock")}</a>
											</li>
											<li class="divider"></li>
											<li[#if hasExpired?? && hasExpired] class="active"[/#if] data-filter-property="hasExpired" data-filter-value="true">
												<a href="javascript:;">${message("admin.order.hasExpired")}</a>
											</li>
											<li[#if hasExpired?? && !hasExpired] class="active"[/#if] data-filter-property="hasExpired" data-filter-value="false">
												<a href="javascript:;">${message("admin.order.unexpired")}</a>
											</li>
										</ul>
									</div>
									<button class="btn btn-default" type="button" data-toggle="modal" data-target="#filterModal">${message("common.moreOption")}</button>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("common.pageSize")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if page.pageSize == 10] class="active"[/#if] data-page-size="10">
												<a href="javascript:;">10</a>
											</li>
											<li[#if page.pageSize == 20] class="active"[/#if] data-page-size="20">
												<a href="javascript:;">20</a>
											</li>
											<li[#if page.pageSize == 50] class="active"[/#if] data-page-size="50">
												<a href="javascript:;">50</a>
											</li>
											<li[#if page.pageSize == 100] class="active"[/#if] data-page-size="100">
												<a href="javascript:;">100</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-3">
								<div id="search" class="input-group">
									<div class="input-group-btn">
										<button class="btn btn-default" type="button" data-toggle="dropdown">
											[#switch page.searchProperty]
												[#case "consignee"]
													<span>${message("Order.consignee")}</span>
													[#break /]
												[#case "areaName"]
													<span>${message("Order.area")}</span>
													[#break /]
												[#case "address"]
													<span>${message("Order.address")}</span>
													[#break /]
												[#case "zipCode"]
													<span>${message("Order.zipCode")}</span>
													[#break /]
												[#case "phone"]
													<span>${message("Order.phone")}</span>
													[#break /]
												[#default]
													<span>${message("Order.sn")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "sn"] class="active"[/#if] data-search-property="sn">
												<a href="javascript:;">${message("Order.sn")}</a>
											</li>
											<li[#if page.searchProperty == "consignee"] class="active"[/#if] data-search-property="consignee">
												<a href="javascript:;">${message("Order.consignee")}</a>
											</li>
											<li[#if page.searchProperty == "areaName"] class="active"[/#if] data-search-property="areaName">
												<a href="javascript:;">${message("Order.area")}</a>
											</li>
											<li[#if page.searchProperty == "address"] class="active"[/#if] data-search-property="address">
												<a href="javascript:;">${message("Order.address")}</a>
											</li>
											<li[#if page.searchProperty == "zipCode"] class="active"[/#if] data-search-property="zipCode">
												<a href="javascript:;">${message("Order.zipCode")}</a>
											</li>
											<li[#if page.searchProperty == "phone"] class="active"[/#if] data-search-property="phone">
												<a href="javascript:;">${message("Order.phone")}</a>
											</li>
										</ul>
									</div>
									<input name="searchValue" class="form-control" type="text" value="${page.searchValue}" placeholder="${message("common.search")}" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search">
									<div class="input-group-btn">
										<button class="btn btn-default" type="submit">
											<i class="iconfont icon-search"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>
											<div class="checkbox">
												<input type="checkbox" data-toggle="checkAll">
												<label></label>
											</div>
										</th>
										<th>
											<a href="javascript:;" data-order-property="sn">
												${message("Order.sn")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="amount">
												${message("Order.amount")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="member">
												${message("Order.member")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="store">
												${message("Order.store")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="consignee">
												${message("Order.consignee")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>

										<th>
											<a href="javascript:;" >
												${message("admin.orderPayment.transaction")}
											</a>
										</th>
										<th>
											<a href="javascript:;" >
												${message("admin.orderPayment.channels")}
											</a>
										</th>


[#--										<th>--]
[#--											<a href="javascript:;" data-order-property="paymentMethodName">--]
[#--												${message("Order.paymentMethod")}--]
[#--												<i class="iconfont icon-biaotou-kepaixu"></i>--]
[#--											</a>--]
[#--										</th>--]
										<th>
											<a href="javascript:;" data-order-property="shippingMethodName">
												${message("Order.shippingMethod")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>

										<th>
											<a href="javascript:;" data-order-property="dataSource">
												${message("Data.source.desc.row")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>

										<th>
											支付状态
										</th>

										<th>
											<a href="javascript:;" data-order-property="status">
												订单${message("Order.status")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="createdDate">
												${message("common.createdDate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>${message("common.action")}</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as order]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" value="${order.id}">
														<label></label>
													</div>
												</td>
												<td>${order.sn}</td>
												<td>${currency(order.amount, true)}</td>
												<td>${order.member.username}</td>
												<td>${order.store.name}</td>
												<td>${order.consignee}</td>
												<td>
													[#list order.paymentTransactions as paymentTransaction]
														[#if paymentTransaction.isSuccess]
															${paymentTransaction.sn}
														[/#if]
													[/#list]
												</td>
												<td>

													[#if !order.paymentTransactions?has_content||order.paymentTransactions.size()==0]
														积分卡|0306
													[/#if]

													[#list order.paymentTransactions as paymentTransaction]
														[#if paymentTransaction.isSuccess]
															${paymentTransaction.paymentPluginName}
													    [#else]
															积分卡|0306
														[/#if]
													[/#list]
												</td>
[#--												<td>${order.paymentMethodName}</td>--]


												<td>${order.shippingMethodName}</td>
												<td>
													[#if order.dataSource]
														${message("Data.source.desc." + order.dataSource)}
													[/#if]
												</td>

												<td>
													[#if order.status == "PENDING_PAYMENT" ]
														未支付
													[#else]
														已支付
													[/#if]
												</td>

												<td>
													<span class="[#if order.status == "PENDING_REVIEW" || order.status == "PENDING_PAYMENT" || order.status == "PENDING_SHIPMENT"]text-orange[#elseif order.status == "FAILED" || order.status == "DENIED"]text-red[#elseif order.status == "CANCELED"]text-gray-dark[#else]text-green[/#if]">${message("Order.Status." + order.status)}</span>
													[#if order.hasExpired()]
														<span class="text-gray-dark">(${message("admin.order.hasExpired")})</span>
													[/#if]
												</td>




												<td>
													<span title="${order.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${order.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
												<td>
													<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/order/view?id=${order.id}" title="${message("common.view")}" data-toggle="tooltip">
														<i class="iconfont icon-search"></i>
													</a>
													[@has_permission_tag permission="admin:orderPayment"]
														[#if hasPermission]
															<a class="print btn btn-default btn-xs btn-icon" href="javascript:;" title="${message("admin.order.print")}" data-toggle="tooltip" data-order-id="${order.id}">
																<i class="iconfont icon-vipcard"></i>
															</a>
														[/#if]
													[/@has_permission_tag]
												</td>
											</tr>
										[/#list]
									</tbody>
								[/#if]
							</table>
							[#if !page.content?has_content]
								<p class="no-result">${message("common.noResult")}</p>
							[/#if]
						</div>
					</div>
					[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
						[#if totalPages > 1]
							<div class="panel-footer text-right">
								[#include "/admin/include/pagination.ftl" /]
							</div>
						[/#if]
					[/@pagination]
				</div>
			</form>
		</div>
	</main>
</body>
</html>
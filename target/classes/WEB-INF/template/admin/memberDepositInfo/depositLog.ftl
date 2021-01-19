<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.memberDeposit.log")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
				$().ready(function() {
					var $export = $("[data-action='export']");
					var $ids = $("input[name='ids']");
					var $shelf = $("#shelf");
					$export.click(function() {
						var ids = $("input[name='ids']:checked");
						var paramValues='';
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
							paramValues=$('#depositForm').serialize();
						}

						var request = new XMLHttpRequest();
						var noetime=nowtime();
						var fileName = "depositLog"+noetime+".xls";
						request.open('GET', "${base}/admin/member_deposit/export_log?"+paramValues, true);
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
					// ID多选框
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
			<ol class="breadcrumb">
				<li>
					<a href="${base}/admin/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("admin.mainSidebar.depositLog")}</li>
			</ol>
			<form id="depositForm"  action="${base}/admin/memberDepositInfo/depositLog" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				[#if member??]
					<input name="memberId" type="hidden" value="${member.id}">
				[/#if]
				<div id="filterModal" class="modal fade" tabindex="-1">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" type="button" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">${message("admin.order.moreOption")}</h5>
							</div>
							<div class="modal-body form-horizontal">
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
									<button class="btn btn-default" type="button" data-toggle="modal" data-target="#filterModal">${message("common.moreOption")}</button>

									<a class="btn btn-default" id="export" data-action="export" >
										<i class="iconfont icon-add"></i>
										${message("common.export")}
									</a>
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
												[#case "member_deposit_log.card_no"]
													<span>${message("MemberDepositLog.cardNo")}</span>
													[#break /]
												[#case "member.username"]
												<span>${message("MemberDepositLog.member")}</span>
													[#break /]
												[#case "orders.sn"]
													<span>${message("MemberDepositLog.orderSn")}</span>
													[#break /]
												[#default]
												<span>${message("MemberDepositLog.type")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "MemberDepositLog.type"] class="active"[/#if] data-search-property="member_deposit_log.type">
												<a href="javascript:;">${message("MemberDepositLog.type")}</a>
											</li>
											<li[#if !page.searchProperty?? || page.searchProperty == "member.username"] class="active"[/#if] data-search-property="member.username">
												<a href="javascript:;">${message("MemberDepositLog.member")}</a>
											</li>
											<li[#if !page.searchProperty?? || page.searchProperty == "orders.sn"] class="active"[/#if] data-search-property="orders.sn">
												<a href="javascript:;">${message("MemberDepositLog.orderSn")}</a>
											</li>
											<li[#if !page.searchProperty?? || page.searchProperty == "member_deposit_log.card_no"] class="active"[/#if] data-search-property="member_deposit_log.card_no">
												<a href="javascript:;">${message("MemberDepositLog.cardNo")}</a>
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
											<a href="javascript:;" data-order-property="type">
												${message("MemberDepositLog.type")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="orders">
												${message("MemberDepositLog.orderSn")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="credit">
												${message("MemberDepositLog.credit")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="debit">
												${message("MemberDepositLog.debit")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="balance">
												${message("MemberDepositLog.balance")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="memberId">
												${message("MemberDepositLog.member")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>

										<th>
											<a href="javascript:;" data-order-property="cardNo">
												${message("MemberDepositLog.cardNo")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="memo">
												${message("MemberDepositLog.memo")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="createdDate">
												${message("common.createdDate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as memberDepositLog]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" value="${memberDepositLog.id}">
														<label></label>
													</div>
												</td>
												<td>${message("MemberDepositLog.Type." + memberDepositLog.type)}</td>
												<td>
													[#if memberDepositLog.order??]
														${memberDepositLog.order.sn}
													[#else]
														-
													[/#if]
												</td>
												<td>${currency(memberDepositLog.credit, true)}</td>
												<td>${currency(memberDepositLog.debit, true)}</td>
												<td>${currency(memberDepositLog.balance, true)}</td>
												<td>
													[#if memberDepositLog.member??]
														<a href="${base}/admin/member/view?id=${memberDepositLog.member.id}&logType=info">${memberDepositLog.member.username}</a>
													[#else]
														-
													[/#if]
												</td>

												<td>
													[#if memberDepositLog.cardNo??]
														<span title="${memberDepositLog.cardNo}">${memberDepositLog.cardNo}</span>
													[/#if]
												</td>
												<td>
													[#if memberDepositLog.memo??]
														<span title="${memberDepositLog.memo}">${abbreviate(memberDepositLog.memo, 30, "...")}</span>
													[/#if]
												</td>
												<td>
													<span title="${memberDepositLog.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${memberDepositLog.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
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
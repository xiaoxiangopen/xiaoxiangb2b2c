<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.member.list")} - 小象电商</title>
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
	<script src="${base}/resources/common/js/bootbox.js"></script>
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
							paramValues=$('#memberForm').serialize();
							}

						var request = new XMLHttpRequest();
						var noetime=nowtime();
						var fileName = "member"+noetime+".xls";
						request.open('GET', "${base}/admin/member/export_member?"+paramValues, true);
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
				});

				function GetQueryString(name)
				{
					var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
					var r = window.location.search.substr(1).match(reg);
					if(r!=null)return  unescape(r[2]); return null;
				}
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
				<li class="active">${message("admin.member.list")}</li>
			</ol>
			<form action="${base}/admin/member/list" method="get" id="memberForm">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-9">
								<div class="btn-group">
									<a class="btn btn-default" href="${base}/admin/member/add" data-redirect-url="${base}/admin/member/list">
										<i class="iconfont icon-add"></i>
										${message("common.add")}
									</a>
									<button class="btn btn-default" type="button" data-action="delete" disabled>
										<i class="iconfont icon-close"></i>
										${message("common.delete")}
									</button>
									<button class="btn btn-default" type="button" data-action="refresh">
										<i class="iconfont icon-refresh"></i>
										${message("common.refresh")}
									</button>
									<a class="btn btn-default" id="export" data-action="export" title="不选择数据则导出当前页">
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
												[#case "email"]
													<span>${message("Member.email")}</span>
													[#break /]
												[#case "mobile"]
												<span>${message("Member.mobile")}</span>
												[#break /]
												[#case "attribute_value0"]
													<span>${message("common.captcha.vipCard")}</span>
													[#break /]
												[#default]
													<span>${message("Member.username")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "username"] class="active"[/#if] data-search-property="username">
												<a href="javascript:;">${message("Member.username")}</a>
											</li>
											<li[#if page.searchProperty == "attribute_value0"] class="active"[/#if] data-search-property="attribute_value0">
												<a href="javascript:;">${message("common.captcha.vipCard")}</a>
											</li>
											<li[#if page.searchProperty == "email"] class="active"[/#if] data-search-property="mobile">
												<a href="javascript:;">${message("Member.mobile")}</a>
											</li>
											<li[#if page.searchProperty == "email"] class="active"[/#if] data-search-property="email">
												<a href="javascript:;">${message("Member.email")}</a>
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
											<a href="javascript:;" data-order-property="username">
												${message("Member.username")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="email">
												${message("Member.email")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="memberRankId">
												${message("Member.memberRank")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="createdDate">
												${message("common.createdDate")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>

											<a href="javascript:;" data-order-property="dataSource">
												${message("Data.source.desc.row")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>

										</th>
										<th>${message("admin.member.status")}</th>
										<th>${message("common.action")}</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as member]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" value="${member.id}">
														<label></label>
													</div>
												</td>
												<td>${member.username}</td>
												<td>${member.email}</td>
												<td>${member.memberRank.name}</td>
												<td>
													<span title="${member.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${member.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>

												<td>
													[#if member.dataSource]
														${message("Data.source.desc." + member.dataSource)}
													[/#if]
												</td>

												<td>
													[#if !member.isEnabled]
														<span class="text-red">${message("admin.member.disabled")}</span>
													[#elseif member.isLocked]
														<span class="text-red">${message("admin.member.locked")}</span>
													[#else]
														<span class="text-green">${message("admin.member.normal")}</span>
													[/#if]
												</td>
												<td>
													<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/member/view?id=${member.id}" title="${message("common.view")}" data-toggle="tooltip">
														<i class="iconfont icon-search"></i>
													</a>
													<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/member/edit?id=${member.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
														<i class="iconfont icon-write"></i>
													</a>
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
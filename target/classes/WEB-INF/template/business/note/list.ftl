<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.article.list")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-datetimepicker.css" rel="stylesheet">
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
	<script src="${base}/resources/business/js/base.js"></script>
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>

	[#noautoesc]
	[#escape x as x?js_string]
	<script>

		$().ready(function() {



			var date = new Date();
			var month = date.getMonth() + 1;
			var strDate = date.getDate();
			if (month >= 1 && month <= 9) {
				month = "0" + month;
			}
			if (strDate >= 0 && strDate <= 9) {
				strDate = "0" + strDate;
			}
			var currentDate = date.getFullYear() + "-" + month + "-" + strDate
					+ " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
			//默认显示当前时间
			thisForm.beginDate.value=currentDate;
			//默认结束时间为2099
			thisForm.endDate.value="2099-" + month + "-" + strDate
						+ " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

			function getparamNameMultiValues(paramName){
				var sURL = window.document.URL.toString();
				var value =[];
				if (sURL.indexOf("?") > 0){
					var arrParams = sURL.split("?");
					var arrURLParams = arrParams[1].split("&");
					for (var i = 0; i<arrURLParams.length; i++){
						var sParam =  arrURLParams[i].split("=");
						console.log(sParam);
						if(sParam){
							if(sParam[0] == paramName){
								if(sParam.length>0){
									value.push(sParam[1].trim());
								}
							}
						}
					}
				}
				return decodeURI(value);
			}

			function getparamValues(){
				var sURL = window.document.URL.toString();
				if (sURL.indexOf("?") > 0){
					var arrParams = sURL.split("?");
					alert(arrParams[1]);
					var arrURLParams = arrParams[1];


				}
				return decodeURI(arrURLParams);
			}

			var $addnote = $("[data-action='addnote']");
			var $pids = $("#pids");
			var val_ids = unescape(getparamNameMultiValues('pids'));

			if(val_ids != "")
				$pids.attr("value",val_ids);
			else
				$pids.attr("value",getparamNameMultiValues('ids'));

			// alert(encodeURI($pids.attr("value")));
			// $("input[name='ids']").serialize()

			$addnote.click(function() {
				$.post("${base}/business/product/addnote?pids="+$pids.attr("value"), $("#thisForm").serialize(), function(result) {
					alert(JSON.stringify(result));
				});
			});
		});
	</script>
	[/#escape]
	[/#noautoesc]




	</script>
</head>
<body class="business">
	[#include "/business/include/main_header.ftl" /]
	[#include "/business/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<ol class="breadcrumb">
				<li>
					<a href="${base}/business/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("admin.article.list")}</li>
			</ol>
			<form id="thisForm" name="thisForm" action="${base}/business/note/list" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-9">
								<div class="btn-group">
									<a class="btn btn-default" href="${base}/business/note/add" data-redirect-url="${base}/business/note/list">
										<i class="iconfont icon-add"></i>
										${message("common.add")}
									</a>
									<button class="btn btn-default" type="button" data-action="addnote" >
										<i class="iconfont icon-save"></i>
										${message("common.addnote")}
									</button>
									<button class="btn btn-default" type="button" data-action="delete" disabled>
										<i class="iconfont icon-close"></i>
										${message("common.delete")}
									</button>
									<button class="btn btn-default" type="button" data-action="refresh">
										<i class="iconfont icon-refresh"></i>
										${message("common.refresh")}
									</button>
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
												[#default]
													<span>${message("Article.title")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "title"] class="active"[/#if] data-search-property="title">
												<a href="javascript:;">${message("Article.title")}</a>
											</li>
										</ul>
									</div>
									<input id="pids" name="pids" type="hidden">
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
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="beginDate">${message("common.dateRange")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="input-group" data-provide="datetimerangepicker" data-date-format="YYYY-MM-DD HH:mm:ss">
									<input id="beginDate" name="beginDate" class="form-control" type="text" >
									<span class="input-group-addon">-</span>
									<input id="endDate"  name="endDate" class="form-control" type="text" >
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
											<a href="javascript:;" data-order-property="title">
												${message("Article.title")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
[#--										<th>--]
[#--											<a href="javascript:;" data-order-property="articleCategoryId">--]
[#--												${message("Article.articleCategory")}--]
[#--												<i class="iconfont icon-biaotou-kepaixu"></i>--]
[#--											</a>--]
[#--										</th>--]
[#--										<th>--]
[#--											<a href="javascript:;" data-order-property="isPublication">--]
[#--												${message("Article.isPublication")}--]
[#--												<i class="iconfont icon-biaotou-kepaixu"></i>--]
[#--											</a>--]
[#--										</th>--]
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
										[#list page.content as article]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" value="${article.id}">
														<label></label>
													</div>
												</td>
												<td>${article.title}</td>
												<td>
													<span title="${article.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${article.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
												<td>

													<a class="btn btn-default btn-xs btn-icon" href="${base}/business/product/list?noteId=${article.id}" data-toggle="tooltip" data-redirect-url>
														关联商品
													</a>
													<a class="btn btn-default btn-xs btn-icon" href="${base}/business/note/edit?id=${article.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
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
								[#include "/business/include/pagination.ftl" /]
							</div>
						[/#if]
					[/@pagination]
				</div>
			</form>
		</div>
	</main>
</body>
</html>
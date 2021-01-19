<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.product.list")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/bootbox.js"></script>
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
	<script src="https://cdn.bootcdn.net/ajax/libs/jquery.fileDownload/1.4.2/jquery.fileDownload.js"></script>

	[#noautoesc]
		[#escape x as x?js_string]
			<script>


				$().ready(function() {

				function getparamValues(){
					var sURL = window.document.URL.toString();
					if (sURL.indexOf("?") > 0){
						var arrParams = sURL.split("?");
						var arrURLParams = arrParams[1];
					}

					return unescape(arrURLParams);
				}




				var $delete = $("[data-action='delete']");
				var $addnote = $("[data-action='addnote']");
				var $export = $("[data-action='export']");
				var $delallnote = $("[data-action='delallnote']");
				var $shelves = $("#shelves");
				var $shelf = $("#shelf");
				var $ids = $("input[name='ids']");
				var $exportAll = $("[data-action='exportAll']");

				var $findEditPerson = $("[data-action='findEditPerson']");
				var $ysDesc = $("#ysDesc");

					// 修改编辑人
					$findEditPerson.click(function() {

						var ids = $("input[name='ids']:checked");
						if(ids==null||ids.length===0){
							$.bootstrapGrowl("请选择商品.");
							return ;
						}

						if($ysDesc.val()==''){
							$.bootstrapGrowl("请输入人员工号.");
							return ;
						}

						var paramValues='';
						for(var i=0;i<ids.length;i++){
							var pid=ids.eq(i)[0].value;
							if(paramValues===''){
								paramValues=pid;
							}else{
								paramValues+=","+pid;
							}
						}

						$.post("${base}/business/product/addYsProduct",{
							ids:paramValues,
							ysDesc:$ysDesc.val()
						}, function() {
							location.reload(true);
						});

					});
				
				// 删除
				$delete.on("success.xiaoxiangshop.delete", function() {
					$shelves.add($shelf).attr("disabled", true);
				});

				$delallnote.click(function() {
					$.post("${base}/business/product/delallnote", $("input[name='ids']").serialize(), function(result) {
						alert(JSON.stringify(result));
						// location.reload(true);
					});
				});



				$export.click(function() {
					var ids = $("input[name='ids']:checked");

					if(ids==null||ids.length===0){
						return ;
					}
					var paramValues='';
					for(var i=0;i<ids.length;i++){
						var pid=ids.eq(i)[0].value;
						if(paramValues===''){
							paramValues="ids="+pid;
						}else{
							paramValues+="&ids="+pid;
						}
					}
					var request = new XMLHttpRequest();
					var noetime=nowtime();
					var fileName = "sf"+noetime+".xls";
					request.open('GET', "${base}/business/product/export?"+paramValues, true);
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


				$exportAll.click(function() {
					var request = new XMLHttpRequest();
					var noetime=nowtime();
					var fileName = "sf"+noetime+".xls";
					request.open('GET', "${base}/business/product/exportAll", true);
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
				
				// 上架
				$shelves.click(function() {
					bootbox.confirm("${message("business.product.shelvesConfirm")}", function(result) {
						if (result == null || !result) {
							return;
						}
						
						$.post("${base}/business/product/shelves", $("input[name='ids']").serialize(), function() {
							location.reload(true);

						});
					});
				});

				$addnote.click(function(){
					location.href="${base}/business/note/list?" + $("input[name='ids']").serialize();
				});
				
				// 下架
				$shelf.click(function() {
					bootbox.confirm("${message("business.product.shelfConfirm")}", function(result) {
						if (result == null || !result) {
							return;
						}
						
						$.post("${base}/business/product/shelf", $("input[name='ids']").serialize(), function() {
							location.reload(true);
						});
					});
				});
				
				// ID多选框
				$ids.change(function() {
					$shelves.add($shelf).add($export).attr("disabled", $("input[name='ids']:checked").length < 1);
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
			<ol class="breadcrumb">
				<li>
					<a href="${base}/business/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("business.product.list")}</li>
			</ol>
			<form action="${base}/business/product/list" method="get">
				<input name="pageSize" type="hidden" value="${page.pageSize}">
				<input name="searchProperty" type="hidden" value="${page.searchProperty}">
				<input name="orderProperty" type="hidden" value="${page.orderProperty}">
				<input name="orderDirection" type="hidden" value="${page.orderDirection}">
				<input name="isActive" type="hidden" value="[#if isActive??]${isActive?string("true", "false")}[/#if]">
				<input name="isMarketable" type="hidden" value="[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]">
				<input name="isList" type="hidden" value="[#if isList??]${isList?string("true", "false")}[/#if]">
				<input name="isPro" type="hidden" value="[#if isPro??]${isList?string("true", "false")}[/#if]">
				<input name="erpFlag" type="hidden" value="[#if erpFlag??]${isList?string("true", "false")}[/#if]">
				<input name="isTop" type="hidden" value="[#if isTop??]${isTop?string("true", "false")}[/#if]">
				<input name="isOutOfStock" type="hidden" value="[#if isOutOfStock??]${isOutOfStock?string("true", "false")}[/#if]">
				<input name="isStockAlert" type="hidden" value="[#if isStockAlert??]${isStockAlert?string("true", "false")}[/#if]">

				<div id="filterModal" class="modal fade" tabindex="-1">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" type="button" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">${message("common.moreOption")}</h5>
							</div>
							<div class="modal-body form-horizontal">
								<div class="form-group">
									<label class="col-xs-3 control-label">${message("Product.productCategory")}:</label>
									<div class="col-xs-9 col-sm-7">
										<select name="productCategoryId" class="selectpicker form-control" data-live-search="true" data-size="10">
											<option value="">${message("common.choose")}</option>
											[#list productCategoryTree as productCategory]
													<option value="${productCategory.id}" title="${productCategory.name}"[#if productCategory.id == productCategoryId] selected[/#if][#if productCategory.disabledFlag=='disabled']disabled[/#if]>
														[#if productCategory.grade != 0]
															[#list 1..productCategory.grade as i]
																&nbsp;&nbsp;
															[/#list]
														[/#if]
														${productCategory.name}
													</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">${message("Product.type")}:</label>
									<div class="col-xs-9 col-sm-7">
										<select name="type" class="selectpicker form-control" data-live-search="true" data-size="10">
											<option value="">${message("common.choose")}</option>
											[#list types as item]
												<option value="${item}"[#if item == type] selected[/#if]>${message("Product.Type." + item)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">${message("Product.brand")}:</label>
									<div class="col-xs-9 col-sm-7">
										<select name="brandId" class="selectpicker form-control" data-live-search="true" data-size="10">
											<option value="">${message("common.choose")}</option>
											[#list brands as brand]
												<option value="${brand.id}"[#if brand.id == brandId] selected[/#if]>${brand.name}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">${message("Product.productTags")}:</label>
									<div class="col-xs-9 col-sm-7">
										<select name="productTagId" class="selectpicker form-control" data-live-search="true" data-size="10">
											<option value="">${message("common.choose")}</option>
											[#list productTags as productTag]
												<option value="${productTag.id}"[#if productTag.id == productTagId] selected[/#if]>${productTag.name}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">${message("Product.storeProductTags")}:</label>
									<div class="col-xs-9 col-sm-7">
										<select name="storeProductTagId" class="selectpicker form-control" data-live-search="true" data-size="10">
											<option value="">${message("common.choose")}</option>
											[#list storeProductTags as storeProductTag]
												<option value="${storeProductTag.id}"[#if storeProductTag.id == storeProductTagId] selected[/#if]>${storeProductTag.name}</option>
											[/#list]
										</select>
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

				<div id="yuShouModal" class="modal fade" tabindex="-1">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" type="button" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">设置商品编辑人</h5>
							</div>
							<div class="modal-body form-horizontal">

								<div class="form-group">
									<label class="col-xs-3 control-label" for="ysDesc">人员工号:</label>
									<div class="col-xs-9 col-sm-7">
										<input name="ysDesc" id="ysDesc" class="form-control" type="text">
										<div class="col-xs-9 col-sm-4" style="margin-top: 7px;color: #ff0000;">
											例如y075973
										</div>
									</div>
								</div>

							</div>

							<div class="modal-footer">
								<button  class="btn btn-primary" type="button" style="width:86px;margin-left: -15px;" data-action="findEditPerson">${message("common.ok")}</button>
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
									<a class="btn btn-default" href="${base}/business/product/add" data-redirect-url="${base}/business/product/list">
										<i class="iconfont icon-add"></i>
										${message("common.add")}
									</a>
									<a class="btn btn-default" data-action="addnote">
										<i class="iconfont icon-add"></i>
										${message("common.addnote")}
									</a>
									<a class="btn btn-default" data-action="export">
										<i class="iconfont icon-add"></i>
										${message("common.export")}
									</a>

									<a class="btn btn-default" data-action="exportAll">
										<i class="iconfont icon-add"></i>
										导出全部
									</a>

									<a class="btn btn-default" data-action="delallnote">
										<i class="iconfont icon-add"></i>
										${message("common.delallnote")}
									</a>
									<button class="btn btn-default" type="button" data-action="delete" disabled>
										<i class="iconfont icon-close"></i>
										${message("common.delete")}
									</button>
									<button class="btn btn-default" type="button" data-action="refresh">
										<i class="iconfont icon-refresh"></i>
										${message("common.refresh")}
									</button>
									<button id="shelves" class="btn btn-default" type="button" disabled>
										<i class="iconfont icon-fold"></i>
										${message("business.product.shelves")}
									</button>
									<button id="shelf" class="btn btn-default" type="button" disabled>
										<i class="iconfont icon-unfold"></i>
										${message("business.product.shelf")}
									</button>
									<div class="btn-group">
										<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
											${message("business.product.filter")}
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if isActive?? && isActive] class="active"[/#if] data-filter-property="isActive" data-filter-value="true">
												<a href="javascript:;">${message("business.product.isActive")}</a>
											</li>
											<li[#if isActive?? && !isActive] class="active"[/#if] data-filter-property="isActive" data-filter-value="false">
												<a href="javascript:;">${message("business.product.notActive")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isMarketable?? && isMarketable] class="active"[/#if] data-filter-property="isMarketable" data-filter-value="true">
												<a href="javascript:;">${message("business.product.isMarketable")}</a>
											</li>
											<li[#if isMarketable?? && !isMarketable] class="active"[/#if] data-filter-property="isMarketable" data-filter-value="false">
												<a href="javascript:;">${message("business.product.notMarketable")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isList?? && isList] class="active"[/#if] data-filter-property="isList" data-filter-value="true">
												<a href="javascript:;">${message("business.product.isList")}</a>
											</li>
											<li[#if isList?? && !isList] class="active"[/#if] data-filter-property="isList" data-filter-value="false">
												<a href="javascript:;">${message("business.product.notList")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isTop?? && isTop] class="active"[/#if] data-filter-property="isTop" data-filter-value="true">
												<a href="javascript:;">${message("business.product.isTop")}</a>
											</li>
											<li[#if isTop?? && !isTop] class="active"[/#if] data-filter-property="isTop" data-filter-value="false">
												<a href="javascript:;">${message("business.product.notTop")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isOutOfStock?? && !isOutOfStock] class="active"[/#if] data-filter-property="isOutOfStock" data-filter-value="false">
												<a href="javascript:;">${message("business.product.isStack")}</a>
											</li>
											<li[#if isOutOfStock?? && isOutOfStock] class="active"[/#if] data-filter-property="isOutOfStock" data-filter-value="true">
												<a href="javascript:;">${message("business.product.isOutOfStack")}</a>
											</li>
											<li class="divider"></li>
											<li[#if isStockAlert?? && !isStockAlert] class="active"[/#if] data-filter-property="isStockAlert" data-filter-value="false">
												<a href="javascript:;">${message("business.product.normalStore")}</a>
											</li>
											<li[#if isStockAlert?? && isStockAlert] class="active"[/#if] data-filter-property="isStockAlert" data-filter-value="true">
												<a href="javascript:;">${message("business.product.isStockAlert")}</a>
											</li>
										</ul>
									</div>

									<button class="btn btn-default" type="button" data-toggle="modal" data-target="#yuShouModal">更换编辑人</button>


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
												[#case "name"]
													<span>${message("Product.name")}</span>
													[#break /]
												[#case "productCategory.name"]
													<span>${message("Product.productCategory")}</span>
													[#break /]
												[#case "productCategory.sn"]
											    	<span>${message("Product.sn")}</span>
													[#break /]
												[#default]
												    <span>${message("Product.internalNumber")}</span>
											[/#switch]
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li[#if !page.searchProperty?? || page.searchProperty == "product.internal_number"] class="active"[/#if] data-search-property="product.internal_number">
												<a href="javascript:;">${message("Product.internalNumber")}</a>
											</li>
											<li[#if page.searchProperty == "product.name"] class="active"[/#if] data-search-property="product.name">
												<a href="javascript:;">${message("Product.name")}</a>
											</li>
											<li[#if page.searchProperty == "productCategory.name"] class="active"[/#if] data-search-property="productCategory.name">
												<a href="javascript:;">${message("Product.productCategory")}</a>
											</li>
											<li[#if !page.searchProperty?? || page.searchProperty == "product.sn"] class="active"[/#if] data-search-property="product.sn">
												<a href="javascript:;">${message("Product.sn")}</a>
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
											<a href="javascript:;" data-order-property="internalNumber">
												${message("Product.internalNumber")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="name">
												${message("Product.name")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="productCategoryId">
												${message("Product.productCategory")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="price">
												${message("Product.price")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="isPro">
												${message("Product.isPro")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="isMarketable">
												${message("Product.isMarketable")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="isActive">
												${message("Product.isActive")}
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
											<a href="javascript:;" data-order-property="lastModifiedDate">
												更新时间
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>
											<a href="javascript:;" data-order-property="businessName">
												${message("common.operator")}
												<i class="iconfont icon-biaotou-kepaixu"></i>
											</a>
										</th>
										<th>${message("common.action")}</th>
									</tr>
								</thead>
								[#if page.content?has_content]
									<tbody>
										[#list page.content as product]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" value="${product.id}">
														<label></label>
													</div>
												</td>
												<td>
													<span class="[#if product.isOutOfStock] text-red[#elseif product.isStockAlert] text-blue[/#if]">${product.internalNumber}</span>
												</td>
												<td>
													${product.name}
													[#if product.type != "GENERAL"]
														<span class="text-red">*</span>
													[/#if]

												</td>
												<td>${product.productCategory.name}</td>
												<td>${currency(product.price, true)}</td>
												<td>
													[#if product.isPro]
														<i class="text-green iconfont icon-check"></i>
													[#else]
														<i class="text-red iconfont icon-close"></i>
													[/#if]
												</td>
												<td>
													[#if product.isMarketable]
														<i class="text-green iconfont icon-check"></i>
													[#else]
														<i class="text-red iconfont icon-close"></i>
													[/#if]
												</td>
												<td>
													[#if product.isActive]
														<i class="text-green iconfont icon-check"></i>
													[#else]
														<i class="text-red iconfont icon-close"></i>
													[/#if]
												</td>

												<td>
													<span title="${product.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${product.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>
												<td>
													[#if product.lastModifiedDate?has_content ]
														<span title="${product.lastModifiedDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${product.lastModifiedDate?string("yyyy-MM-dd HH:mm:ss")}</span>
													[/#if]
												</td>
												<td>
													<span title="" data-toggle="tooltip">${product.businessName}</span>
												</td>
												<td>

													<a class="btn btn-default btn-xs btn-icon" href="${base}/business/note/productnotelist?productId=${product.id}" data-toggle="tooltip" data-redirect-url>
														${message("common.editnote")}
													</a>

													[#if product.businessId == currentUser.id]
														<a class="btn btn-default btn-xs btn-icon" href="${base}/business/product/edit?productId=${product.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
															<i class="iconfont icon-write"></i>
														</a>
													[/#if]
													[#if product.isMarketable && product.isActive]
														<a class="btn btn-default btn-xs btn-icon" href="${base}${product.path}" title="${message("common.view")}" data-toggle="tooltip" target="_blank">
															<i class="iconfont icon-search"></i>
														</a>
													[#else]
														<button class="disabled btn btn-default btn-xs btn-icon" type="button" title="${message("business.product.notMarketable")}" data-toggle="tooltip">
															<i class="iconfont icon-search"></i>
														</button>
													[/#if]

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
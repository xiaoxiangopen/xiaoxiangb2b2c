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
			var productId = unescape(getparamNameMultiValues('productId'));
			var $editnote = $("[data-action='editnote']");
			var $ids = $("input[name='ids']");
			var $shelf = $("#shelf");
			$editnote.click(function() {
				var ids = $("input[name='ids']:checked");
				if(ids==null||ids.length===0){
					return ;
				}
				$.post("${base}/business/product/editproductnote?productId="+productId, $("input[name='ids']").serialize(), function(result) {
					alert(JSON.stringify(result));
					location.reload(true);
				});
			});
			// ID多选框
			$ids.change(function() {
				$editnote.add($shelf).attr("disabled", $("input[name='ids']:checked").length < 1);
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

				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12 col-sm-9">
								<div class="btn-group">
									<button class="btn btn-default" title="${message("common.editnotetitle")}" type="button" data-action="editnote" >
                                        <i class="iconfont icon-write"></i>
		                                ${message("common.editnote")}
									</button>

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
												<input type="checkbox" checked data-toggle="checkAll">
												<label></label>
											</div>
										</th>
										<th>
											<a href="javascript:;" data-order-property="title">
												${message("Article.title")}
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

									<tbody>
		                            [#if noteList?? && (noteList?size > 0)]
										[#list noteList as article]
											<tr>
												<td>
													<div class="checkbox">
														<input name="ids" type="checkbox" checked value="${article.id}">
														<label></label>
													</div>
												</td>
												<td>${article.title}</td>
												<td>
													<span title="${article.createdDate?string("yyyy-MM-dd HH:mm:ss")}" data-toggle="tooltip">${article.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
												</td>

											</tr>
										[/#list]
									</tbody>
		                         [/#if]
							</table>
		                    [#if (noteList?size = 0)]
								<p class="no-result">${message("common.noResult")}</p>
							[/#if]
						</div>
					</div>

				</div>
			</form>
		</div>
	</main>
</body>
</html>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.area.list")} - 小象电商</title>
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
			
				var $delete = $("a.delete");
				
				// 删除
				$delete.delete({
					url: "${base}/admin/area/delete",
					data: function($element) {
						return {
							id: $element.data("id")
						}
					}
				}).on("success.小象SHOP.delete", function(event) {
					var $element = $(event.target);
					
					$element.closest("td").velocity({
						opacity: 0
					}, {
						complete: function() {
							location.reload(true);
						}
					});
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
				<li class="active">${message("admin.area.list")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="btn-group">
						<a class="btn btn-default" href="${base}/admin/area/add[#if parent??]?parentId=${parent.id}[/#if]" data-redirect-url[#if !parent??]="${base}/admin/area/list"[/#if]>
							<i class="iconfont icon-add"></i>
							${message("common.add")}
						</a>
						[#if parent??]
							[#if parent.parent??]
								<a class="btn btn-default" href="${base}/admin/area/list?parentId=${parent.parent.id}">
									<i class="iconfont icon-back_android"></i>
									${message("admin.area.parent")}
								</a>
							[#else]
								<a class="btn btn-default" href="${base}/admin/area/list">
									<i class="iconfont icon-back_android"></i>
									${message("admin.area.parent")}
								</a>
							[/#if]
						[/#if]
					</div>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<th class="text-green text-center" colspan="5">
								[#if parent??]
									${message("admin.area.parent")} - ${parent.name}
								[#else]
									${message("admin.area.root")}
								[/#if]
							</th>
							[#list areas?chunk(5) as row]
								<tr>
									[#list row as area]
										<td>
											<a href="${base}/admin/area/list?parentId=${area.id}" title="${message("common.view")}" data-toggle="tooltip">${area.name}</a>
											<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/area/edit?id=${area.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
												<i class="iconfont icon-write"></i>
											</a>
											<a class="delete btn btn-default btn-xs btn-icon" href="javascript:;" title="${message("common.delete")}" data-toggle="tooltip" data-id="${area.id}">
												<i class="iconfont icon-close"></i>
											</a>
										</td>
									[/#list]
								</tr>
							[/#list]
							[#if !areas?has_content]
								<tr>
									<td class="text-center" colspan="5">
										<span class="text-red">${message("admin.area.emptyChildren")}</span>
										<a href="${base}/admin/area/add[#if parent??]?parentId=${parent.id}[/#if]" data-redirect-url>${message("common.add")}</a>
									</td>
								</tr>
							[/#if]
						</table>
					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>
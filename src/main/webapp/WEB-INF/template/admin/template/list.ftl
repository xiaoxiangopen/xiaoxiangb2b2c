<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.template.list")} - 小象电商</title>
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
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
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
				<li class="active">${message("admin.template.list")}</li>
			</ol>
			<form action="${base}/admin/template/list" method="get">
				<input name="type" type="hidden" value="${type}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="btn-group">
							<button class="btn btn-default" type="button" data-action="refresh">
								<i class="iconfont icon-refresh"></i>
								${message("common.refresh")}
							</button>
							<div class="btn-group">
								<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
									[#if type??]
										${message("TemplateConfig.Type." + type)}
									[#else]
										${message("admin.template.allType")}
									[/#if]
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li[#if !type??] class="active"[/#if] data-filter-property="type">
										<a href="javascript:;">${message("admin.template.allType")}</a>
									</li>
									[#list types as item]
										<li[#if item == type] class="active"[/#if] data-filter-property="type" data-filter-value="${item}">
											<a href="javascript:;">${message("TemplateConfig.Type." + item)}</a>
										</li>
									[/#list]
								</ul>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>${message("TemplateConfig.name")}</th>
										<th>${message("TemplateConfig.type")}</th>
										<th>${message("TemplateConfig.templatePath")}</th>
										<th>${message("TemplateConfig.description")}</th>
										<th>${message("common.action")}</th>
									</tr>
								</thead>
								<tbody>
									[#list templateConfigs as templateConfig]
										<tr>
											<td>${templateConfig.name}</td>
											<td>${message("TemplateConfig.Type." + templateConfig.type)}</td>
											<td>${templateConfig.templatePath}</td>
											<td>
												[#if templateConfig.description??]
													<span title="${templateConfig.description}">${abbreviate(templateConfig.description, 30, "...")}</span>
												[/#if]
											</td>
											<td>
												<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/template/edit?id=${templateConfig.id}" title="${message("common.edit")}" data-toggle="tooltip" data-redirect-url>
													<i class="iconfont icon-write"></i>
												</a>
											</td>
										</tr>
									[/#list]
								</tbody>
							</table>
							[#if !templateConfigs?has_content]
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
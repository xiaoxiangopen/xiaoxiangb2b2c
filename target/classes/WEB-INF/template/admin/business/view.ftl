<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.business.view")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/zoom.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/zoom.js"></script>
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
				<li class="active">${message("admin.business.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#base" data-toggle="tab">${message("admin.business.base")}</a>
						</li>
						[#if businessAttributes?has_content]
							<li>
								<a href="#profile" data-toggle="tab">${message("admin.business.profile")}</a>
							</li>
						[/#if]
					</ul>
					<div class="tab-content">
						<div id="base" class="tab-pane active">
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("Business.username")}:</dt>
										<dd>
											${business.username}
											[#if loginPlugin??]
												<span class="text-gray">[${loginPlugin.name}]</span>
											[/#if]
										</dd>
										<dt>${message("Business.email")}:</dt>
										<dd>${business.email}</dd>
										<dt>${message("Business.mobile")}:</dt>
										<dd>${business.mobile}</dd>
										<dt>${message("Business.balance")}:</dt>
										<dd>
											<span class="text-red">${currency(business.balance, true, true)}</span>
											<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/business_deposit/log?businessId=${business.id}" title="${message("common.view")}" data-toggle="tooltip">
												<i class="iconfont icon-search"></i>
											</a>
										</dd>
										[#if business.frozenAmount > 0]
											<dt>${message("Business.frozenAmount")}:</dt>
											<dd>
												<span class="text-gray">${currency(business.frozenAmount, true, true)}</span>
											</dd>
										[/#if]
										<dt>${message("admin.business.status")}:</dt>
										<dd>
											[#if !business.isEnabled]
												<span class="text-red">${message("admin.business.disabled")}</span>
											[#elseif business.isLocked]
												<span class="text-red"> ${message("admin.business.locked")} </span>
											[#else]
												<span class="text-green">${message("admin.business.normal")}</span>
											[/#if]
										</dd>
										<dt>${message("common.createdDate")}:</dt>
										<dd>${business.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
										<dt>${message("User.lastLoginIp")}:</dt>
										<dd>${business.lastLoginIp!"-"}</dd>
										<dt>${message("User.lastLoginDate")}:</dt>
										<dd>${(business.lastLoginDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}</dd>
									</dl>
								</div>
							</div>
						</div>
						[#if businessAttributes?has_content]
							<div id="profile" class="tab-pane">
								<div class="row">
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											[#list businessAttributes as businessAttribute]
												<dt>${businessAttribute.name}:</dt>
												[#if businessAttribute.type == "NAME"]
													<dd>${business.name}</dd>
												[#elseif businessAttribute.type == "TEXT" || businessAttribute.type == "NAME" || businessAttribute.type == "LICENSE_NUMBER" || businessAttribute.type == "LEGAL_PERSON" || businessAttribute.type == "ID_CARD" || businessAttribute.type == "PHONE" || businessAttribute.type == "ORGANIZATION_CODE" || businessAttribute.type == "IDENTIFICATION_NUMBER" || businessAttribute.type == "BANK_NAME" || businessAttribute.type == "BANK_ACCOUNT" || businessAttribute.type == "DATE"]
													<dd>${business.getAttributeValue(businessAttribute)}</dd>
												[#elseif businessAttribute.type == "IMAGE" || businessAttribute.type == "LICENSE_IMAGE" || businessAttribute.type == "ID_CARD_IMAGE" || businessAttribute.type == "ORGANIZATION_IMAGE" || businessAttribute.type == "TAX_IMAGE"]
													<dd>
														<img class="img-thumbnail" src="${business.getAttributeValue(businessAttribute)}" alt="${businessAttribute.name}" width="50" data-action="zoom">
													</dd>
												[#elseif businessAttribute.type == "SELECT"]
													<dd>${business.getAttributeValue(businessAttribute)}</dd>
												[#elseif businessAttribute.type == "CHECKBOX"]
													<dd>
														[#list business.getAttributeValue(businessAttribute) as option]
															${option}
														[/#list]
													</dd>
												[/#if]
											[/#list]
										</dl>
									</div>
								</div>
							</div>
						[/#if]
					</div>
				</div>
				<div class="panel-footer">
					<div class="row">
						<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
							<button class="btn btn-default" type="button" data-action="back">${message("common.back")}</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>
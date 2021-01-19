<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.member.view")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
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
				<li class="active">${message("admin.member.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#base" data-toggle="tab">${message("admin.member.base")}</a>
						</li>
						[#if memberAttributes?has_content]
							<li>
								<a href="#profile" data-toggle="tab">${message("admin.member.profile")}</a>
							</li>
						[/#if]
					</ul>
					<div class="tab-content">
						<div id="base" class="tab-pane active">
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<dl class="items dl-horizontal">
										<dt>${message("Member.username")}:</dt>
										<dd>${member.username}</dd>
										<dt>${message("Member.email")}:</dt>
										<dd>${member.email}</dd>
										<dt>${message("Member.mobile")}:</dt>
										<dd>${member.mobile}</dd>
										<dt>${message("Member.memberRank")}:</dt>
										<dd>${member.memberRank.name}</dd>
										<dt>${message("admin.member.status")}:</dt>
										<dd>
											[#if !member.isEnabled]
												<span class="text-red">${message("admin.member.disabled")}</span>
											[#elseif member.isLocked]
												<span class="text-red"> ${message("admin.member.locked")} </span>
											[#else]
												<span class="text-green">${message("admin.member.normal")}</span>
											[/#if]
										</dd>
										[#if member.isLocked]
											<dt>${message("User.lockDate")}:</dt>
											<dd>${member.lockDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
										[/#if]
										<dt>${message("Member.point")}:</dt>
										<dd>
											${member.point}
											<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/point/log?memberId=${member.id}" title="${message("common.view")}" data-toggle="tooltip">
												<i class="iconfont icon-search"></i>
											</a>
										</dd>
										<dt>${message("Member.balance")}:</dt>
										<dd>
											<span class="text-red">${currency(member.balance, true, true)}</span>
											[#if logType=="info"]
											<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/memberDepositInfo/depositLog?memberId=${member.id}" title="${message("common.view")}" data-toggle="tooltip">
												<i class="iconfont icon-search"></i>
											</a>
											[#else ]
											<a class="btn btn-default btn-xs btn-icon" href="${base}/admin/member_deposit/log?memberId=${member.id}" title="${message("common.view")}" data-toggle="tooltip">
												<i class="iconfont icon-search"></i>
											</a>
											[/#if]
										</dd>
										<dt>${message("Member.amount")}:</dt>
										<dd>${currency(member.amount, true, true)}</dd>
										<dt>${message("common.createdDate")}:</dt>
										<dd>${member.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
										<dt>${message("User.lastLoginIp")}:</dt>
										<dd>${member.lastLoginIp!"-"}</dd>
										<dt>${message("User.lastLoginDate")}:</dt>
										<dd>${(member.lastLoginDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}</dd>
										<dt>${message("admin.member.reviewCount")}:</dt>
										<dd>${member.reviews?size}</dd>
										<dt>${message("admin.member.consultationCount")}:</dt>
										<dd>${member.consultations?size}</dd>
										<dt>${message("admin.member.productFavoriteCount")}:</dt>
										<dd>${member.productFavorites?size}</dd>
									</dl>
								</div>
							</div>
						</div>
						[#if memberAttributes?has_content]
							<div id="profile" class="tab-pane">
								<div class="row">
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											[#list memberAttributes as memberAttribute]
												<dt>${memberAttribute.name}:</dt>
												[#if memberAttribute.type == "NAME"]
													<dd>${member.name}</dd>
												[#elseif memberAttribute.type == "GENDER"]
													[#if member.gender??]
														<dd>${message("Member.Gender." + member.gender)}</dd>
													[/#if]
												[#elseif memberAttribute.type == "BIRTH"]
													<dd>${member.birth}</dd>
												[#elseif memberAttribute.type == "AREA"]
													[#if member.area??]
														<dd>${member.area.fullName}</dd>
													[#else]
														<dd>${member.areaName}</dd>
													[/#if]
												[#elseif memberAttribute.type == "ADDRESS"]
													<dd>${member.address}</dd>
												[#elseif memberAttribute.type == "ZIP_CODE"]
													<dd>${member.zipCode}</dd>
												[#elseif memberAttribute.type == "PHONE"]
													<dd>${member.phone}</dd>
												[#elseif memberAttribute.type == "TEXT"]
													<dd>${member.getAttributeValue(memberAttribute)}</dd>
												[#elseif memberAttribute.type == "SELECT"]
													<dd>${member.getAttributeValue(memberAttribute)}</dd>
												[#elseif memberAttribute.type == "CHECKBOX"]
													<dd>
														[#list member.getAttributeValue(memberAttribute) as option]
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
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.member.add")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-datetimepicker.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/jquery.lSelect.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
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
			
				var $memberForm = $("#memberForm");
				var $areaId = $("#areaId");
				
				// 地区选择
				$areaId.lSelect({
					url: "${base}/common/area"
				});
				
				// 表单验证
				$memberForm.validate({
					rules: {
						username: {
							required: true,
							minlength: 4,
							username: true,
							notAllNumber: true,
							remote: {
								url: "${base}/admin/member/check_username",
								cache: false
							}
						},
						password: {
							required: true,
							minlength: 4,
							normalizer: function(value) {
								return value;
							}
						},
						rePassword: {
							required: true,
							equalTo: "#password",
							normalizer: function(value) {
								return value;
							}
						},
						email: {
							required: true,
							email: true,
							remote: {
								url: "${base}/admin/member/check_email",
								cache: false
							}
						},
						mobile: {
							required: true,
							mobile: true,
							remote: {
								url: "${base}/admin/member/check_mobile",
								cache: false
							}
						}
						[#list memberAttributes as memberAttribute]
							[#if memberAttribute.isRequired || memberAttribute.pattern?has_content]
								,"memberAttribute_${memberAttribute.id}": {
									[#if memberAttribute.isRequired]
										required: true
										[#if memberAttribute.pattern?has_content],[/#if]
									[/#if]
									[#if memberAttribute.pattern?has_content]
										pattern: new RegExp("${memberAttribute.pattern}")
									[/#if]
								}
							[/#if]
						[/#list]
					},
					messages: {
						username: {
							remote: "${message("common.validator.exist")}"
						},
						email: {
							remote: "${message("common.validator.exist")}"
						},
						mobile: {
							remote: "${message("common.validator.exist")}"
						}
					}
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
				<li class="active">${message("admin.member.add")}</li>
			</ol>
			<form id="memberForm" class="ajax-form form-horizontal" action="${base}/admin/member/save" method="post">
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
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="username">${message("Member.username")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="username" name="username" class="form-control" type="text" maxlength="20">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="password">${message("Member.password")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="password" name="password" class="form-control" type="password" maxlength="20" autocomplete="off">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="rePassword">${message("admin.member.rePassword")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="rePassword" name="rePassword" class="form-control" type="password" maxlength="20" autocomplete="off">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="email">${message("Member.email")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="email" name="email" class="form-control" type="text" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="mobile">${message("Member.mobile")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="mobile" name="mobile" class="form-control" type="text" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Member.memberRank")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="memberRankId" class="selectpicker form-control" data-size="10">
											[#list memberRanks as memberRank]
												<option value="${memberRank.id}"[#if memberRank.isDefault] selected[/#if]>${memberRank.name}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isEnabled" type="hidden" value="false">
											<input id="isEnabled" name="isEnabled" type="checkbox" value="true" checked>
											<label for="isEnabled">${message("User.isEnabled")}</label>
										</div>
									</div>
								</div>
							</div>
							[#if memberAttributes?has_content]
								<div id="profile" class="tab-pane">
									[#list memberAttributes as memberAttribute]
										<div class="form-group">
											<label class="col-xs-3 col-sm-2 control-label[#if memberAttribute.isRequired] item-required[/#if]" for="memberAttribute_${memberAttribute.id}">${memberAttribute.name}:</label>
											[#if memberAttribute.type == "NAME"]
												<div class="col-xs-9 col-sm-4">
													<input id="memberAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "GENDER"]
												<div class="col-xs-9 col-sm-10">
													[#list genders as gender]
														<div class="radio radio-inline">
															<input id="${gender}" name="memberAttribute_${memberAttribute.id}" type="radio" value="${gender}">
															<label for="${gender}">${message("Member.Gender." + gender)}</label>
														</div>
													[/#list]
												</div>
											[#elseif memberAttribute.type == "BIRTH"]
												<div class="col-xs-9 col-sm-4">
													<div class="input-group">
														<input name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" data-provide="datetimepicker">
														<span class="input-group-addon">
															<i class="iconfont icon-calendar"></i>
														</span>
													</div>
												</div>
											[#elseif memberAttribute.type == "AREA"]
												<div class="col-xs-9 col-sm-4">
													<input id="areaId" name="memberAttribute_${memberAttribute.id}" type="hidden">
												</div>
											[#elseif memberAttribute.type == "ADDRESS"]
												<div class="col-xs-9 col-sm-4">
													<input id="memberAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "ZIP_CODE"]
												<div class="col-xs-9 col-sm-4">
													<input id="memberAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "PHONE"]
												<div class="col-xs-9 col-sm-4">
													<input id="memberAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "TEXT"]
												<div class="col-xs-9 col-sm-4">
													<input id="memberAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "SELECT"]
												<div class="col-xs-9 col-sm-4">
													<select id="memberAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="selectpicker form-control" data-size="10">
														<option value="">${message("common.choose")}</option>
														[#list memberAttribute.options as option]
															<option value="${option}">${option}</option>
														[/#list]
													</select>
												</div>
											[#elseif memberAttribute.type == "CHECKBOX"]
												<div class="col-xs-9 col-sm-10">
													[#list memberAttribute.options as option]
														<div class="checkbox checkbox-inline">
															<input id="${option}_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" type="checkbox" value="${option}">
															<label for="${option}_${memberAttribute.id}">${option}</label>
														</div>
													[/#list]
												</div>
											[/#if]
										</div>
									[/#list]
								</div>
							[/#if]
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("common.submit")}</button>
								<button class="btn btn-default" type="button" data-action="back">${message("common.back")}</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>
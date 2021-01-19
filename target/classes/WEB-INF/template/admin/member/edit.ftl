<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.member.edit")} - 小象电商</title>
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
						password: {
							minlength: 4,
							normalizer: function(value) {
								return value;
							}
						},
						rePassword: {
							equalTo: "#password",
							normalizer: function(value) {
								return value;
							}
						},
						email: {
							required: true,
							email: true,
							remote: {
								url: "${base}/admin/member/check_email?id=${member.id}",
								cache: false
							}
						},
						mobile: {
							required: true,
							mobile: true,
							remote: {
								url: "${base}/admin/member/check_mobile?id=${member.id}",
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
				<li class="active">${message("admin.member.edit")}</li>
			</ol>
			<form id="memberForm" class="ajax-form form-horizontal" action="${base}/admin/member/update" method="post">
				<input name="id" type="hidden" value="${member.id}">
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
									<label class="col-xs-3 col-sm-2 control-label">${message("Member.username")}:</label>
									<div class="col-xs-9 col-sm-4">
										<p class="form-control-static">${member.username}</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="password">${message("Member.password")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="password" name="password" class="form-control" type="password" maxlength="20" autocomplete="off">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="rePassword">${message("admin.member.rePassword")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="rePassword" name="rePassword" class="form-control" type="password" maxlength="20" autocomplete="off">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="email">${message("Member.email")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="email" name="email" class="form-control" type="text" value="${member.email}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="mobile">${message("Member.mobile")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="mobile" name="mobile" class="form-control" type="text" value="${member.mobile}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Member.memberRank")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="memberRankId" class="selectpicker form-control" data-size="10">
											[#list memberRanks as memberRank]
												<option value="${memberRank.id}"[#if memberRank == member.memberRank] selected[/#if]>${memberRank.name}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-12 col-sm-6">
										<dl class="items dl-horizontal">
											<dt>${message("Member.point")}:</dt>
											<dd>${member.point}</dd>
											<dt>${message("Member.balance")}:</dt>
											<dd>
												<span class="text-red">${currency(member.balance, true, true)}</span>
											</dd>
											<dt>${message("Member.amount")}:</dt>
											<dd>${currency(member.amount, true, true)}</dd>
											<dt>${message("common.createdDate")}:</dt>
											<dd>${member.createdDate?string("yyyy-MM-dd HH:mm:ss")}</dd>
										</dl>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox checkbox-inline">
											<input name="_isEnabled" type="hidden" value="false">
											<input id="isEnabled" name="isEnabled" type="checkbox" value="true"[#if member.isEnabled] checked[/#if]>
											<label for="isEnabled">${message("User.isEnabled")}</label>
										</div>
										[#if member.isLocked]
											<div class="checkbox checkbox-inline">
												<input name="_unlock" type="hidden" value="false">
												<input id="unlock" name="unlock" type="checkbox" value="true"[#if !member.isLocked] checked[/#if]>
												<label for="unlock">${message("admin.member.unlock")}</label>
											</div>
										[/#if]
									</div>
								</div>
							</div>
							[#if memberAttributes?has_content]
								<div id="profile" class="tab-pane">
									[#list memberAttributes as memberAttribute]
										<div class="form-group">
											<label class="col-xs-3 col-sm-2 control-label[#if memberAttribute.isRequired] item-required[/#if]" for="businessAttribute_${memberAttribute.id}">${memberAttribute.name}:</label>
											[#if memberAttribute.type == "NAME"]
												<div class="col-xs-9 col-sm-4">
													<input id="businessAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" value="${member.name}" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "GENDER"]
												<div class="col-xs-9 col-sm-10">
													[#list genders as gender]
														<div class="radio radio-inline">
															<input id="${gender}" name="memberAttribute_${memberAttribute.id}" type="radio" value="${gender}"[#if gender == member.gender] checked[/#if]>
															<label for="${gender}">${message("Member.Gender." + gender)}</label>
														</div>
													[/#list]
												</div>
											[#elseif memberAttribute.type == "BIRTH"]
												<div class="col-xs-9 col-sm-4">
													<div class="input-group">
														<input id="businessAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" value="${member.birth}" data-provide="datetimepicker">
														<span class="input-group-addon">
															<i class="iconfont icon-calendar"></i>
														</span>
													</div>
												</div>
											[#elseif memberAttribute.type == "AREA"]
												<div class="col-xs-9 col-sm-4">
													<input id="areaId" name="memberAttribute_${memberAttribute.id}" type="hidden" value="${(member.area.id)!}" treePath="${(member.area.treePath)!}">
												</div>
											[#elseif memberAttribute.type == "ADDRESS"]
												<div class="col-xs-9 col-sm-4">
													<input id="businessAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" value="${member.address}" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "ZIP_CODE"]
												<div class="col-xs-9 col-sm-4">
													<input id="businessAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" value="${member.zipCode}" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "PHONE"]
												<div class="col-xs-9 col-sm-4">
													<input id="businessAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" value="${member.phone}" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "TEXT"]
												<div class="col-xs-9 col-sm-4">
													<input id="businessAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="form-control" type="text" value="${member.getAttributeValue(memberAttribute)}" maxlength="200">
												</div>
											[#elseif memberAttribute.type == "SELECT"]
												<div class="col-xs-9 col-sm-4">
													<select id="businessAttribute_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" class="selectpicker form-control" data-size="10">
														<option value="">${message("common.choose")}</option>
														[#list memberAttribute.options as option]
															<option value="${option}"[#if option == member.getAttributeValue(memberAttribute)] selected[/#if]>${option}</option>
														[/#list]
													</select>
												</div>
											[#elseif memberAttribute.type == "CHECKBOX"]
												<div class="col-xs-9 col-sm-10">
													[#list memberAttribute.options as option]
														<div class="checkbox checkbox-inline">
															<input id="${option}_${memberAttribute.id}" name="memberAttribute_${memberAttribute.id}" type="checkbox" value="${option}"[#if (member.getAttributeValue(memberAttribute)?seq_contains(option))!] checked[/#if]>
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
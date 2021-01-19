<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.businessAttribute.edit")} - 小象电商</title>
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
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	<script id="optionTemplate" type="text/template">
		<%_.each(options, function(option, i) {%>
			<div class="form-group">
				<div class="col-xs-9 col-sm-4 col-xs-offset-3 col-sm-offset-2">
					<div class="input-group">
						<span class="input-group-addon">${message("BusinessAttribute.options")}</span>
						<input name="options[<%-i%>]" class="options form-control" type="text" value="<%-option%>" maxlength="200">
						<div class="input-group-btn">
							<button class="remove btn btn-default" type="button"<%-options.length <= 1 ? " disabled" : ""%>>
								<i class="iconfont icon-close"></i>
							</button>
						</div>
					</div>
				</div>
			</div>
		<%});%>
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $businessAttributeForm = $("#businessAttributeForm");
				var $addOption = $("#addOption");
				var $option = $("#option");
				var optionTemplate = _.template($("#optionTemplate").html());
				var initOptions = [
					[#list businessAttribute.options as option]
						"${option}"[#if option_has_next],[/#if]
					[/#list]
				];
				
				// 生成可选项
				function buildOption(options) {
					$option.html(optionTemplate({
						options: options
					}));
				}
				
				// 获取当前可选项
				function getCurrentOptions() {
					return $option.find("input.options").map(function() {
						return $(this).val();
					}).get();
				}
				
				// 初始化可选项
				buildOption(initOptions.length > 0 ? initOptions : [""]);
				
				// 增加可选项
				$addOption.click(function() {
					var options = getCurrentOptions();
					
					options.push("");
					buildOption(options);
				});
				
				// 移除可选项
				$option.on("click", "button.remove", function() {
					var $element = $(this);
					
					$element.closest(".form-group").velocity("slideUp", {
						complete: function() {
							$(this).remove();
							
							buildOption(getCurrentOptions());
						}
					});
				});
				
				$.validator.addClassRules({
					options: {
						required: true
					}
				});
				
				// 表单验证
				$businessAttributeForm.validate({
					rules: {
						name: "required",
						pattern: {
							remote: {
								url: "${base}/admin/business_attribute/check_pattern",
								cache: false
							}
						},
						order: "digits"
					},
					messages: {
						pattern: {
							remote: "${message("admin.businessAttribute.syntaxError")}"
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
				<li class="active">${message("admin.businessAttribute.edit")}</li>
			</ol>
			<form id="businessAttributeForm" class="ajax-form form-horizontal" action="${base}/admin/business_attribute/update" method="post">
				<input name="id" type="hidden" value="${businessAttribute.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.businessAttribute.edit")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("BusinessAttribute.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" value="${businessAttribute.name}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("BusinessAttribute.type")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p class="form-control-static">${message("BusinessAttribute.Type." + businessAttribute.type)}</p>
							</div>
						</div>
						[#if businessAttribute.type != "SELECT" && businessAttribute.type != "CHECKBOX"]
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="pattern">${message("BusinessAttribute.pattern")}:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="pattern" name="pattern" class="form-control" type="text" value="${businessAttribute.pattern}" maxlength="200">
								</div>
							</div>
						[/#if]
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="checkbox checkbox-inline">
									<input name="_isRequired" type="hidden" value="false">
									<input id="isRequired" name="isRequired" type="checkbox" value="true"[#if businessAttribute.isRequired] checked[/#if]>
									<label for="isRequired">${message("BusinessAttribute.isRequired")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input name="_isEnabled" type="hidden" value="false">
									<input id="isEnabled" name="isEnabled" type="checkbox" value="true"[#if businessAttribute.isEnabled] checked[/#if]>
									<label for="isEnabled">${message("BusinessAttribute.isEnabled")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="order">${message("common.order")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="order" name="order" class="form-control" type="text" value="${businessAttribute.order}" maxlength="9">
							</div>
						</div>
						[#if businessAttribute.type?? && (businessAttribute.type == "SELECT" || businessAttribute.type == "CHECKBOX")]
							<div class="form-group">
								<div class="col-xs-9 col-sm-4 col-xs-offset-3 col-sm-offset-2">
									<button id="addOption" class="btn btn-default" type="button">
										<i class="iconfont icon-add"></i>
										${message("admin.businessAttribute.addOption")}
									</button>
								</div>
							</div>
							<div id="option"></div>
						[/#if]
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
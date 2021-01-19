<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.setting.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-fileinput.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/intl-messageformat-with-locales.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	<script id="distributionCommissionRateTableTemplate" type="text/template">
		<%if (!_.isEmpty(distributionCommissionRates)) {%>
			<%_.each(distributionCommissionRates, function(distributionCommissionRate, i) {%>
				<%
					var message = new IntlMessageFormat("${message("admin.setting.distributionRankDetail")}", "${locale}");
				%>
				<div class="form-group">
					<div class="input-group">
						<span class="input-group-addon"><%-message.format({index: i + 1})%></span>
						<input name="distributionCommissionRates[<%-i%>]" class="distribution-commission-rates form-control" type="text" value="<%-distributionCommissionRate%>" maxlength="9">
						<span class="input-group-addon">%</span>
						<div class="input-group-btn">
							<button class="remove btn btn-default" type="button">
								<i class="iconfont icon-close"></i>
							</button>
						</div>
					</div>
				</div>
			<%});%>
		<%} else {%>
			<span class="text-gray">${message("admin.setting.noDistributionCommissionRate")}</span>
		<%}%>
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $settingForm = $("#settingForm");
				var $watermarkImage = $("#watermarkImage");
				var $watermarkImageFile = $("#watermarkImageFile");
				var $distributionCommissionRateModal = $("#distributionCommissionRateModal");
				var $smtpHost = $("#smtpHost");
				var $smtpPort = $("#smtpPort");
				var $smtpUsername = $("#smtpUsername");
				var $smtpPassword = $("#smtpPassword");
				var $smtpSSLEnabled = $("#smtpSSLEnabled");
				var $smtpFromMail = $("#smtpFromMail");
				var $testSmtp = $("#testSmtp");
				var $toMail = $("#toMail");
				var $sendMail = $("#sendMail");
				var $addDistributionCommissionRate = $("#addDistributionCommissionRate");
				var $distributionCommissionRate = $("#distributionCommissionRate");
				var $smsAppId = $("#smsAppId");
				var $smsBalance = $("#smsBalance");
				var distributionCommissionRateTableTemplate = _.template($("#distributionCommissionRateTableTemplate").html());
				var initDistributionCommissionRates = [
					[#list setting.distributionCommissionRates as distributionCommissionRate]
						"${distributionCommissionRate}"[#if distributionCommissionRate_has_next],[/#if]
					[/#list]
				];
				
				// 水印图片
				$watermarkImageFile.fileinput({
					allowedFileExtensions: "${setting.uploadImageExtension}".split(","),
					[#if setting.uploadMaxSize != 0]
						maxFileSize: ${setting.uploadMaxSize} * 1024,
					[/#if]
					showUpload: false,
					showClose: false,
					[#if setting.watermarkImage?has_content]
						initialPreview: ["${base}${setting.watermarkImage}"],
					[/#if]
					initialPreviewAsData: true,
					initialPreviewShowDelete: false,
					previewClass: "single-file-preview",
					layoutTemplates: {
						footer: '<div class="file-thumbnail-footer">{actions}</div>',
						actions: '<div class="file-actions"><div class="file-footer-buttons">{upload} {download} {delete} {zoom} {other}</div>{drag}<div class="clearfix"></div></div>'
					},
					fileActionSettings: {
						showUpload: false,
						showRemove: false,
						showDrag: false
					},
					removeFromPreviewOnError: true
				}).on("filecleared fileerror fileuploaderror", function() {
					$watermarkImage.val("");
				});
				
				// 分销佣金比例模态框
				$distributionCommissionRateModal.on("hide.bs.modal", function() {
					var validator = $settingForm.validate();
					var isValid = true;
					
					$distributionCommissionRate.find("input.distribution-commission-rates").each(function() {
						if (!validator.element($(this))) {
							isValid = false;
							return false;
						}
					});
					return isValid;
				});
				
				// 邮件测试
				$testSmtp.click(function() {
					$testSmtp.closest("div.form-group").velocity("slideUp");
					$toMail.prop("disabled", false).closest("div.form-group").velocity("slideDown");
				});
				
				// 发送邮件
				$sendMail.click(function() {
					var $element = $(this);
					var validator = $settingForm.validate();
					var isValid = validator.element($smtpFromMail) & validator.element($smtpHost) & validator.element($smtpPort) & validator.element($smtpUsername) & validator.element($toMail);
					var loading = false;
					
					$.ajax({
						url: "${base}/admin/setting/test_smtp",
						type: "POST",
						data: {
							smtpHost: $smtpHost.val(),
							smtpPort: $smtpPort.val(),
							smtpUsername: $smtpUsername.val(),
							smtpPassword: $smtpPassword.val(),
							smtpSSLEnabled: $smtpSSLEnabled.val(),
							smtpFromMail: $smtpFromMail.val(),
							toMail: $toMail.val()
						},
						dataType: "json",
						cache: false,
						beforeSend: function() {
							loading = true;
							if (!isValid) {
								return false;
							}
							setTimeout(function() {
								if (loading) {
									$element.button("loading");
								}
							}, 500);
						},
						success: function(data) {
							bootbox.alert(data.mesaage);
						},
						complete: function() {
							loading = false;
							$element.button("reset");
						}
					});
				});
				
				// 短信余额查询
				$smsBalance.click(function() {
					var $element = $(this);
					var loading = false;
					
					$.ajax({
						url: "${base}/admin/setting/sms_balance",
						type: "GET",
						dataType: "json",
						cache: false,
						beforeSend: function() {
							loading = true;
							setTimeout(function() {
								if (loading) {
									$element.button("loading");
								}
							}, 500);
						},
						success: function(data) {
							bootbox.alert(data.mesaage);
						},
						complete: function() {
							loading = false;
							$element.button("reset");
						}
					});
					return false;
				});
				
				// 生成分销佣金比例Table
				function buildDistributionCommissionRateTable(distributionCommissionRates) {
					$distributionCommissionRate.html(distributionCommissionRateTableTemplate({
						distributionCommissionRates: distributionCommissionRates
					}));
				}
				
				// 获取当前分销佣金比例
				function getCurrentDistributionCommissionRates() {
					return $distributionCommissionRate.find("input.distribution-commission-rates").map(function() {
						return $(this).val();
					}).get();
				}
				
				// 初始化分销佣金比例Table
				buildDistributionCommissionRateTable(initDistributionCommissionRates);
				
				// 增加分销佣金比例
				$addDistributionCommissionRate.click(function() {
					var distributionCommissionRates = getCurrentDistributionCommissionRates();
					
					distributionCommissionRates.push("0");
					buildDistributionCommissionRateTable(distributionCommissionRates);
				});
				
				// 删除分销佣金比例
				$distributionCommissionRate.on("click", "button.remove", function() {
					var $element = $(this);
					
					$element.closest(".form-group").velocity("fadeOut", {
						complete: function() {
							$(this).remove();
							
							buildDistributionCommissionRateTable(getCurrentDistributionCommissionRates());
						}
					});
				});
				
				$.validator.addMethod("comparePointScale",
					function(value, element, param) {
						var parameterValue = $(param).val();
						if ($.trim(parameterValue) == "" || $.trim(value) == "") {
							return true;
						}
						try {
							return parseFloat(parameterValue) <= parseFloat(value);
						} catch(e) {
							return false;
						}
					},
					"${message("admin.setting.comparePointScale")}"
				);
				
				$.validator.addMethod("maxDistributionCommissionRate",
					function(value, element, param) {
						return _.reduce(getCurrentDistributionCommissionRates(), function(memo, value) {
							return $.isNumeric(value) ? parseFloat(value) + memo : memo;
						}, 0) <= 100;
					},
					"${message("admin.setting.maxDistributionCommissionRate")}"
				);
				
				$.validator.addClassRules({
					"distribution-commission-rates": {
						required: true,
						number: true,
						min: 0,
						maxDistributionCommissionRate: true
					}
				});
				
				// 表单验证
				$settingForm.validate({
					rules: {
						siteName: "required",
						siteUrl: {
							required: true,
							url2: true
						},
						logo: "required",
						phone: "phone",
						zipCode: "zipCode",
						email: "email",
						largeProductImageWidth: {
							required: true,
							integer: true,
							min: 1
						},
						largeProductImageHeight: {
							required: true,
							integer: true,
							min: 1
						},
						mediumProductImageWidth: {
							required: true,
							integer: true,
							min: 1
						},
						mediumProductImageHeight: {
							required: true,
							integer: true,
							min: 1
						},
						thumbnailProductImageWidth: {
							required: true,
							integer: true,
							min: 1
						},
						thumbnailProductImageHeight: {
							required: true,
							integer: true,
							min: 1
						},
						defaultLargeProductImage: "required",
						defaultMediumProductImage: "required",
						defaultThumbnailProductImage: "required",
						defaultStoreLogo: "required",
						watermarkAlpha: {
							required: true,
							digits: true,
							max: 100
						},
						watermarkImageFile: {
							extension: "${setting.uploadImageExtension}"
						},
						defaultMarketPriceScale: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 3,
								fraction: ${setting.priceScale}
							}
						},
						registerPoint: {
							required: true,
							digits: true
						},
						maxFailedLoginAttempts: {
							integer: true,
							min: 1
						},
						passwordLockTime: {
							required: true,
							digits: true
						},
						safeKeyExpiryTime: {
							required: true,
							digits: true
						},
						uploadMaxSize: {
							required: true,
							digits: true
						},
						imageUploadPath: "required",
						mediaUploadPath: "required",
						fileUploadPath: "required",
						smtpFromMail: {
							required: true,
							email: true
						},
						smtpHost: "required",
						smtpPort: {
							required: true,
							digits: true
						},
						smtpUsername: "required",
						toMail: {
							required: true,
							email: true
						},
						currencySign: "required",
						currencyUnit: "required",
						stockAlertCount: {
							required: true,
							digits: true
						},
						automaticReceiveTime: {
							required: true,
							digits: true
						},
						defaultPointScale: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 3,
								fraction: ${setting.priceScale}
							}
						},
						maxPointScale: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 3,
								fraction: ${setting.priceScale}
							},
							comparePointScale: "#defaultPointScale"
						},
						memberMinimumCashAmount: {
							required: true,
							positive: true,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						businessMinimumCashAmount: {
							required: true,
							positive: true,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						taxRate: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 3,
								fraction: ${setting.priceScale}
							}
						},
						cookiePath: "required",
						smsSecretKey: {
							required: function(element) {
								return $.trim($smsAppId.val()) != "";
							}
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/admin/setting/edit"
						});
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
				<li class="active">${message("admin.setting.edit")}</li>
			</ol>
			<form id="settingForm" class="form-horizontal" action="${base}/admin/setting/update" method="post" enctype="multipart/form-data">
				<div id="distributionCommissionRateModal" class="modal fade" tabindex="-1">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" type="button" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">${message("admin.setting.distributionCommissionRateSetting")}</h5>
							</div>
							<div class="modal-body">
								<div id="distributionCommissionRate" style="padding: 0px 20px;"></div>
							</div>
							<div class="modal-footer">
								<button id="addDistributionCommissionRate" class="btn btn-primary" type="button">${message("admin.setting.addDistributionCommissionRate")}</button>
								<button class="btn btn-default" type="button" data-dismiss="modal">${message("common.close")}</button>
							</div>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#base" data-toggle="tab">${message("admin.setting.base")}</a>
							</li>
							<li>
								<a href="#show" data-toggle="tab">${message("admin.setting.show")}</a>
							</li>
							<li>
								<a href="#registersecurity" data-toggle="tab">${message("admin.setting.registerSecurity")}</a>
							</li>
							<li>
								<a href="#mail" data-toggle="tab">${message("admin.setting.mail")}</a>
							</li>
							<li>
								<a href="#other" data-toggle="tab">${message("admin.setting.other")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="base" class="tab-pane active">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="siteName">${message("Setting.siteName")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="siteName" name="siteName" class="form-control" type="text" value="${setting.siteName}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="siteUrl">${message("Setting.siteUrl")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="siteUrl" name="siteUrl" class="form-control" type="text" value="${setting.siteUrl}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("Setting.logo")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input name="logo" type="hidden" value="${setting.logo}" data-provide="fileinput" data-file-type="IMAGE">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="hotSearch">${message("Setting.hotSearch")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.hotSearchTitle")}" data-toggle="tooltip">
										<input id="hotSearch" name="hotSearch" class="form-control" type="text" value="${setting.hotSearch}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="address">${message("Setting.address")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="address" name="address" class="form-control" type="text" value="${setting.address}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="phone">${message("Setting.phone")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="phone" name="phone" class="form-control" type="text" value="${setting.phone}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="zipCode">${message("Setting.zipCode")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="zipCode" name="zipCode" class="form-control" type="text" value="${setting.zipCode}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="email">${message("Setting.email")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="email" name="email" class="form-control" type="text" value="${setting.email}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="certtext">${message("Setting.certtext")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="certtext" name="certtext" class="form-control" type="text" value="${setting.certtext}" maxlength="200">
									</div>
								</div>
							</div>
							<div id="show" class="tab-pane">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.locale")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="locale" class="selectpicker form-control" data-size="10">
											[#list locales as locale]
												<option value="${locale}"[#if locale == setting.locale] selected[/#if]>${message("Setting.Locale." + locale)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.setting.largeProductImage")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="row">
											<div class="col-xs-12 col-sm-6">
												<div class="input-group">
													<span class="input-group-addon">${message("admin.setting.width")}</span>
													<input id="largeProductImageWidth" name="largeProductImageWidth" class="form-control" type="text" value="${setting.largeProductImageWidth}" maxlength="9">
													<span class="input-group-addon">${message("common.unit.pixel")}</span>
												</div>
											</div>
											<div class="col-xs-12 col-sm-6">
												<div class="input-group">
													<span class="input-group-addon">${message("admin.setting.height")}</span>
													<input id="largeProductImageHeight" name="largeProductImageHeight" class="form-control" type="text" value="${setting.largeProductImageHeight}" maxlength="9">
													<span class="input-group-addon">${message("common.unit.pixel")}</span>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.setting.mediumProductImage")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="row">
											<div class="col-xs-12 col-sm-6">
												<div class="input-group">
													<span class="input-group-addon">${message("admin.setting.width")}</span>
													<input id="mediumProductImageWidth" name="mediumProductImageWidth" class="form-control" type="text" value="${setting.mediumProductImageWidth}" maxlength="9">
													<span class="input-group-addon">${message("common.unit.pixel")}</span>
												</div>
											</div>
											<div class="col-xs-12 col-sm-6">
												<div class="input-group">
													<span class="input-group-addon">${message("admin.setting.height")}</span>
													<input id="mediumProductImageHeight" name="mediumProductImageHeight" class="form-control" type="text" value="${setting.mediumProductImageHeight}" maxlength="9">
													<span class="input-group-addon">${message("common.unit.pixel")}</span>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.setting.thumbnailProductImage")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="row">
											<div class="col-xs-12 col-sm-6">
												<div class="input-group">
													<span class="input-group-addon">${message("admin.setting.width")}</span>
													<input id="thumbnailProductImageWidth" name="thumbnailProductImageWidth" class="form-control" type="text" value="${setting.thumbnailProductImageWidth}" maxlength="9">
													<span class="input-group-addon">${message("common.unit.pixel")}</span>
												</div>
											</div>
											<div class="col-xs-12 col-sm-6">
												<div class="input-group">
													<span class="input-group-addon">${message("admin.setting.height")}</span>
													<input id="thumbnailProductImageHeight" name="thumbnailProductImageHeight" class="form-control" type="text" value="${setting.thumbnailProductImageHeight}" maxlength="9">
													<span class="input-group-addon">${message("common.unit.pixel")}</span>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.setting.defaultLargeProductImage")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input name="defaultLargeProductImage" type="hidden" value="${setting.defaultLargeProductImage}" data-provide="fileinput" data-file-type="IMAGE">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.setting.defaultMediumProductImage")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input name="defaultMediumProductImage" type="hidden" value="${setting.defaultMediumProductImage}" data-provide="fileinput" data-file-type="IMAGE">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.setting.defaultThumbnailProductImage")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input name="defaultThumbnailProductImage" type="hidden" value="${setting.defaultThumbnailProductImage}" data-provide="fileinput" data-file-type="IMAGE">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("Setting.defaultStoreLogo")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input name="defaultStoreLogo" type="hidden" value="${setting.defaultStoreLogo}" data-provide="fileinput" data-file-type="IMAGE">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="watermarkAlpha">${message("Setting.watermarkAlpha")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.watermarkAlphaTitle")}" data-toggle="tooltip">
										<input id="watermarkAlpha" name="watermarkAlpha" class="form-control" type="text" value="${setting.watermarkAlpha}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.watermarkImage")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="watermarkImage" name="watermarkImage" type="hidden" value="${setting.watermarkImage}">
										<input id="watermarkImageFile" name="watermarkImageFile" type="file">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.watermarkPosition")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="watermarkPosition" class="selectpicker form-control" data-size="10">
											[#list watermarkPositions as watermarkPosition]
												<option value="${watermarkPosition}"[#if watermarkPosition == setting.watermarkPosition] selected[/#if]>${message("Setting.WatermarkPosition." + watermarkPosition)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.priceScale")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="priceScale" class="selectpicker form-control" data-size="10">
											<option value="0"[#if setting.priceScale == 0] selected[/#if]>${message("admin.setting.priceScale0")}</option>
											<option value="1"[#if setting.priceScale == 1] selected[/#if]>${message("admin.setting.priceScale1")}</option>
											<option value="2"[#if setting.priceScale == 2] selected[/#if]>${message("admin.setting.priceScale2")}</option>
											<option value="3"[#if setting.priceScale == 3] selected[/#if]>${message("admin.setting.priceScale3")}</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.priceRoundType")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="priceRoundType" class="selectpicker form-control" data-size="10">
											[#list roundTypes as roundType]
												<option value="${roundType}"[#if roundType == setting.priceRoundType] selected[/#if]>${message("Setting.RoundType." + roundType)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isShowMarketPrice">${message("Setting.isShowMarketPrice")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isShowMarketPrice" type="hidden" value="false">
											<input id="isShowMarketPrice" name="isShowMarketPrice" type="checkbox" value="true"[#if setting.isShowMarketPrice] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="defaultMarketPriceScale">${message("Setting.defaultMarketPriceScale")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.defaultMarketPriceScaleTitle")}" data-toggle="tooltip">
										<input id="defaultMarketPriceScale" name="defaultMarketPriceScale" class="form-control" type="text" value="${setting.defaultMarketPriceScale}" maxlength="9">
									</div>
								</div>
							</div>
							<div id="registersecurity" class="tab-pane">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.allowedRegisterTypes")}:</label>
									<div class="col-xs-9 col-sm-10">
										[#list registerTypes as registerType]
											<div class="checkbox checkbox-inline">
												<input id="${registerType}" name="allowedRegisterTypes" type="checkbox" value="${registerType}"[#if setting.allowedRegisterTypes?? && setting.allowedRegisterTypes?seq_contains(registerType)] checked[/#if]>
												<label for="${registerType}">${message("Setting.RegisterType." + registerType)}</label>
											</div>
										[/#list]
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="registerPoint">${message("Setting.registerPoint")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="registerPoint" name="registerPoint" class="form-control" type="text" value="${setting.registerPoint}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.captchaTypes")}:</label>
									<div class="col-xs-9 col-sm-10">
										[#list captchaTypes as captchaType]
											<div class="checkbox checkbox-inline">
												<input id="captcha_${captchaType}" name="captchaTypes" type="checkbox" value="${captchaType}"[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains(captchaType)] checked[/#if]>
												<label for="captcha_${captchaType}">${message("Setting.CaptchaType." + captchaType)}</label>
											</div>
										[/#list]
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="maxFailedLoginAttempts">${message("Setting.maxFailedLoginAttempts")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.maxFailedLoginAttemptsTitle")}" data-toggle="tooltip">
										<input id="maxFailedLoginAttempts" name="maxFailedLoginAttempts" class="form-control" type="text" value="${setting.maxFailedLoginAttempts}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="passwordLockTime">${message("Setting.passwordLockTime")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.passwordLockTimeTitle")}" data-toggle="tooltip">
										<div class="input-group">
											<input id="passwordLockTime" name="passwordLockTime" class="form-control" type="text" value="${setting.passwordLockTime}" maxlength="9">
											<span class="input-group-addon">${message("common.unit.minute")}</span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="safeKeyExpiryTime">${message("Setting.safeKeyExpiryTime")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.safeKeyExpiryTimeTitle")}" data-toggle="tooltip">
										<div class="input-group">
											<input id="safeKeyExpiryTime" name="safeKeyExpiryTime" class="form-control" type="text" value="${setting.safeKeyExpiryTime}" maxlength="9">
											<span class="input-group-addon">${message("common.unit.minute")}</span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="uploadMaxSize">${message("Setting.uploadMaxSize")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.uploadMaxSizeTitle")}" data-toggle="tooltip">
										<div class="input-group">
											<input id="uploadMaxSize" name="uploadMaxSize" class="form-control" type="text" value="${setting.uploadMaxSize}" maxlength="9">
											<span class="input-group-addon">${message("common.unit.megaByte")}</span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="uploadImageExtension">${message("Setting.uploadImageExtension")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.uploadImageExtensionTitle")}" data-toggle="tooltip">
										<input id="uploadImageExtension" name="uploadImageExtension" class="form-control" type="text" value="${setting.uploadImageExtension}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="uploadMediaExtension">${message("Setting.uploadMediaExtension")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.uploadMediaExtensionTitle")}" data-toggle="tooltip">
										<input id="uploadMediaExtension" name="uploadMediaExtension" class="form-control" type="text" value="${setting.uploadMediaExtension}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="uploadFileExtension">${message("Setting.uploadFileExtension")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.uploadFileExtensionTitle")}" data-toggle="tooltip">
										<input id="uploadFileExtension" name="uploadFileExtension" class="form-control" type="text" value="${setting.uploadFileExtension}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="imageUploadPath">${message("Setting.imageUploadPath")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="imageUploadPath" name="imageUploadPath" class="form-control" type="text" value="${setting.imageUploadPath}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="mediaUploadPath">${message("Setting.mediaUploadPath")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="mediaUploadPath" name="mediaUploadPath" class="form-control" type="text" value="${setting.mediaUploadPath}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="fileUploadPath">${message("Setting.fileUploadPath")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="fileUploadPath" name="fileUploadPath" class="form-control" type="text" value="${setting.fileUploadPath}" maxlength="200">
									</div>
								</div>
							</div>
							<div id="mail" class="tab-pane">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="smtpHost">${message("Setting.smtpHost")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="smtpHost" name="smtpHost" class="form-control" type="text" value="${setting.smtpHost}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="smtpPort">${message("Setting.smtpPort")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.smtpPortTitle")}" data-toggle="tooltip">
										<input id="smtpPort" name="smtpPort" class="form-control" type="text" value="${setting.smtpPort}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="smtpUsername">${message("Setting.smtpUsername")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="smtpUsername" name="smtpUsername" class="form-control" type="text" value="${setting.smtpUsername}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="smtpPassword">${message("Setting.smtpPassword")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.smtpPasswordTitle")}" data-toggle="tooltip">
										<input id="smtpPassword" name="smtpPassword" class="form-control" type="password" maxlength="200" autocomplete="off">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="smtpSSLEnabled">${message("Setting.smtpSSLEnabled")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_smtpSSLEnabled" type="hidden" value="false">
											<input id="smtpSSLEnabled" name="smtpSSLEnabled" type="checkbox" value="true"[#if setting.smtpSSLEnabled] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="smtpFromMail">${message("Setting.smtpFromMail")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="smtpFromMail" name="smtpFromMail" class="form-control" type="text" value="${setting.smtpFromMail}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<div class="col-xs-9 col-sm-4 col-xs-offset-3 col-sm-offset-2">
										<button id="testSmtp" class="btn btn-default" type="button">${message("admin.setting.testSmtp")}</button>
									</div>
								</div>
								<div class="hidden-element form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="toMail">${message("admin.setting.toMail")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="input-group">
											<input id="toMail" name="toMail" class="form-control" type="text" maxlength="200" disabled>
											<div class="input-group-btn">
												<button id="sendMail" class="btn btn-default" type="button" data-loading-text="<i class='fa fa-spinner fa-pulse'></i> ${message('admin.setting.sendMail')}">${message("admin.setting.sendMail")}</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div id="other" class="tab-pane">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="currencySign">${message("Setting.currencySign")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="currencySign" name="currencySign" class="form-control" type="text" value="${setting.currencySign}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="currencyUnit">${message("Setting.currencyUnit")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="currencyUnit" name="currencyUnit" class="form-control" type="text" value="${setting.currencyUnit}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="stockAlertCount">${message("Setting.stockAlertCount")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="stockAlertCount" name="stockAlertCount" class="form-control" type="text" value="${setting.stockAlertCount}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="automaticReceiveTime">${message("Setting.automaticReceiveTime")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="input-group">
											<input id="automaticReceiveTime" name="automaticReceiveTime" class="form-control" type="text" value="${setting.automaticReceiveTime}" maxlength="9">
											<span class="input-group-addon">${message("common.unit.day")}</span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.stockAllocationTime")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="stockAllocationTime" class="selectpicker form-control" data-size="10">
											[#list stockAllocationTimes as stockAllocationTime]
												<option value="${stockAllocationTime}"[#if stockAllocationTime == setting.stockAllocationTime] selected[/#if]>${message("Setting.StockAllocationTime." + stockAllocationTime)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="defaultPointScale">${message("Setting.defaultPointScale")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.defaultPointScaleTitle")}" data-toggle="tooltip">
										<input id="defaultPointScale" name="defaultPointScale" class="form-control" type="text" value="${setting.defaultPointScale}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="maxPointScale">${message("Setting.maxPointScale")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="maxPointScale" name="maxPointScale" class="form-control" type="text" value="${setting.maxPointScale}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="memberMinimumCashAmount">${message("Setting.memberMinimumCashAmount")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="memberMinimumCashAmount" name="memberMinimumCashAmount" class="form-control" type="text" value="${setting.memberMinimumCashAmount}" maxlength="16">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="businessMinimumCashAmount">${message("Setting.businessMinimumCashAmount")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="businessMinimumCashAmount" name="businessMinimumCashAmount" class="form-control" type="text" value="${setting.businessMinimumCashAmount}" maxlength="16">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Setting.distributionCommissionRates")}:</label>
									<div class="col-xs-9 col-sm-4">
										<button class="btn btn-default" type="button" data-toggle="modal" data-target="#distributionCommissionRateModal">${message("common.setting")}</button>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isDevelopmentEnabled">${message("Setting.isDevelopmentEnabled")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isDevelopmentEnabled" type="hidden" value="false">
											<input id="isDevelopmentEnabled" name="isDevelopmentEnabled" type="checkbox" value="true"[#if setting.isDevelopmentEnabled] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isReviewEnabled">${message("Setting.isReviewEnabled")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isReviewEnabled" type="hidden" value="false">
											<input id="isReviewEnabled" name="isReviewEnabled" type="checkbox" value="true"[#if setting.isReviewEnabled] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isReviewCheck">${message("Setting.isReviewCheck")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isReviewCheck" type="hidden" value="false">
											<input id="isReviewCheck" name="isReviewCheck" type="checkbox" value="true"[#if setting.isReviewCheck] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isConsultationEnabled">${message("Setting.isConsultationEnabled")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isConsultationEnabled" type="hidden" value="false">
											<input id="isConsultationEnabled" name="isConsultationEnabled" type="checkbox" value="true"[#if setting.isConsultationEnabled] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isConsultationCheck">${message("Setting.isConsultationCheck")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isConsultationCheck" type="hidden" value="false">
											<input id="isConsultationCheck" name="isConsultationCheck" type="checkbox" value="true"[#if setting.isConsultationCheck] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isInvoiceEnabled">${message("Setting.isInvoiceEnabled")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox">
											<input name="_isInvoiceEnabled" type="hidden" value="false">
											<input id="isInvoiceEnabled" name="isInvoiceEnabled" type="checkbox" value="true"[#if setting.isInvoiceEnabled] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="isTaxPriceEnabled">${message("Setting.isTaxPriceEnabled")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("admin.setting.taxRateTitle")}" data-toggle="tooltip">
										<div class="checkbox">
											<input name="_isTaxPriceEnabled" type="hidden" value="false">
											<input id="isTaxPriceEnabled" name="isTaxPriceEnabled" type="checkbox" value="true"[#if setting.isTaxPriceEnabled] checked[/#if]>
											<label></label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="taxRate">${message("Setting.taxRate")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="taxRate" name="taxRate" class="form-control" type="text" value="${setting.taxRate}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="cookiePath">${message("Setting.cookiePath")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="cookiePath" name="cookiePath" class="form-control" type="text" value="${setting.cookiePath}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="cookieDomain">${message("Setting.cookieDomain")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="cookieDomain" name="cookieDomain" class="form-control" type="text" value="${setting.cookieDomain}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="kuaidi100Customer">${message("Setting.kuaidi100Customer")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="kuaidi100Customer" name="kuaidi100Customer" class="form-control" type="text" value="${setting.kuaidi100Customer}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="kuaidi100Key">${message("Setting.kuaidi100Key")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="kuaidi100Key" name="kuaidi100Key" class="form-control" type="text" value="${setting.kuaidi100Key}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="smsAppId">${message("Setting.smsAppId")}:</label>
									<div class="col-xs-9 col-sm-4">
										[#if setting.smsAppId?has_content && setting.smsSecretKey?has_content]
											<div class="input-group">
												<input id="smsAppId" name="smsAppId" class="form-control" type="text" value="${setting.smsAppId}" maxlength="200">
												<div class="input-group-btn">
													<a id="smsBalance" class="btn btn-default" href="javascript:;" data-loading-text="${message('admin.setting.smsBalance')} <i class='fa fa-spinner fa-pulse'></i>">${message("admin.setting.smsBalance")}</a>
												</div>
											</div>
										[#else]
											<input id="smsAppId" name="smsAppId" class="form-control" type="text" value="${setting.smsAppId}" maxlength="200">
										[/#if]
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="smsSecretKey">${message("Setting.smsSecretKey")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="smsSecretKey" name="smsSecretKey" class="form-control" type="text" value="${setting.smsSecretKey}" maxlength="200">
									</div>
								</div>
							</div>
						</div>
					</div>
[#--					<div class="panel-footer">--]
[#--						<div class="row">--]
[#--							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">--]
[#--								<button class="btn btn-primary" type="submit" disabled="disabled">${message("common.submit")}</button>--]
[#--								<button class="btn btn-default" type="button" data-action="back">${message("common.back")}</button>--]
[#--							</div>--]
[#--						</div>--]
[#--					</div>--]
				</div>
			</form>
		</div>
	</main>
</body>
</html>
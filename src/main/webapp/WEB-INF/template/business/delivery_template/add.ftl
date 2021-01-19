<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.deliveryTemplate.add")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-fileinput.css" rel="stylesheet">
	<link href="${base}/resources/common/css/summernote.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
	<script src="${base}/resources/common/js/summernote.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
	<script id="tagSelectTemplate" type="text/template">
		<select name="tagSelect" class="form-control">
			<option value="">${message("business.deliveryTemplate.addTags")}</option>
			<%_.each(tagOptions, function(tagOption, i) {%>
				<option value="<%-tagOption.value%>"><%-tagOption.name%></option>
			<%});%>
		</select>
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $deliveryTemplateForm = $("#deliveryTemplateForm");
				var $content = $("#content");
				var $background = $("#background");
				var $width = $("#width");
				var $height = $("#height");
				var tagSelectTemplate = _.template($("#tagSelectTemplate").html());
				var tagOptions = [
					[#list storeAttributes as storeAttribute]
						{
							name: "${message("DeliveryTemplate.StoreAttribute." + storeAttribute)}",
							value: "${storeAttribute.tagName}"
						},
					[/#list]
					[#list deliveryCenterAttributes as deliveryCenterAttribute]
						{
							name: "${message("DeliveryTemplate.DeliveryCenterAttribute." + deliveryCenterAttribute)}",
							value: "${deliveryCenterAttribute.tagName}"
						},
					[/#list]
					[#list orderAttributes as orderAttribute]
						{
							name: "${message("DeliveryTemplate.OrderAttribute." + orderAttribute)}",
							value: "${orderAttribute.tagName}"
						}
						[#if orderAttribute_has_next],[/#if]
					[/#list]
				];
				var summernoteOptions = {
					width: 1000,
					height: 400,
					tableClassName: "table table-dashed",
					insertTableMaxSize: {
						col: 30,
						row: 30
					},
					toolbar: [
						["addTags", ["addTags"]],
						["style", ["bold", "italic", "underline", "clear"]],
						["font", ["strikethrough", "superscript", "subscript"]],
						["fontname", ["fontname"]],
						["fontsize", ["fontsize"]],
						["color", ["color"]],
						["height", ["height"]],
						["para", ["ul", "ol", "paragraph"]],
						["insert", ["table", "picture", "hr"]],
						["view", ["codeview"]]
					],
					buttons: {
						addTags: function() {
							return tagSelectTemplate({
								tagOptions: tagOptions
							});
						}
					},
					hint: {
						words: $.map(tagOptions, function(tagOption) {
							return tagOption.value;
						}),
						match: /\B\{(\w*)$/,
						search: function(keyword, callback) {
							callback($.grep(this.words, function(item) {
								return item.indexOf(keyword) === 0;
							}));
						}
					},
					callbacks: {
						onInit: function() {
							$("[name='tagSelect']").selectpicker({
								width: "auto"
							}).change(function() {
								var $element = $(this);
								
								$content.summernote("insertText", $element.val());
							});
						},
						onImageUpload: function(files) {
							var $element = $(this);
							var $files = $(files);
							
							$files.each(function() {
								var file = this;
								var formData = new FormData();
								
								formData.append("fileType", "IMAGE");
								formData.append("file", file);
								$.ajax({
									url: "${base}/common/file/upload",
									type: "POST",
									data: formData,
									dataType: "json",
									contentType: false,
									cache: false,
									processData: false,
									success: function(data) {
										$element.summernote("insertImage", data.url);
									}
								});
							});
						}
					}
				}
				
				// 内容
				$content.summernote(summernoteOptions);
				
				// 背景图
				$background.data("file").on("fileuploaded", function(event, data, previewId, index) {
					refreshSummernote();
				});
				
				// 宽度、高度
				$width.add($height).on("input propertychange change", function() {
					refreshSummernote();
				});
				
				// 刷新文本编辑器
				function refreshSummernote() {
					var validator = $deliveryTemplateForm.validate();
					var code = $content.summernote("code");
					var width = $width.val();
					var height = $height.val();
					var background = $background.val();
					
					if (validator.element($width) && validator.element($height) && (summernoteOptions.width != width || summernoteOptions.height != height)) {
						$.extend(summernoteOptions, {
							width: width,
							height: height
						});
						if ($content.data("summernote") != null) {
							$content.summernote("destroy");
						}
						$content.summernote(summernoteOptions).summernote("code", code);
					}
					
					$content.data("summernote").layoutInfo.editable.css("background", "url(" + background + ") 0px 0px no-repeat");
				}
				
				$.validator.addMethod("notEmpty", function(value, element, param) {
					var $element = $(element);
					
					return $element.data("summernote") != null && !$element.summernote("isEmpty");
				}, "${message("business.deliveryTemplate.emptyNotAllow")}");
				
				// 表单验证
				$deliveryTemplateForm.validate({
					rules: {
						name: "required",
						content: "notEmpty",
						width: {
							required: true,
							integer: true,
							min: 1
						},
						height: {
							required: true,
							integer: true,
							min: 1
						},
						offsetX: {
							required: true,
							integer: true
						},
						offsetY: {
							required: true,
							integer: true
						}
					}
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
				<li class="active">${message("business.deliveryTemplate.add")}</li>
			</ol>
			<form id="deliveryTemplateForm" class="ajax-form form-horizontal" action="${base}/business/delivery_template/save" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.deliveryTemplate.add")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("DeliveryTemplate.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("DeliveryTemplate.content")}:</label>
							<div class="col-xs-9 col-sm-4">
								<textarea id="content" name="content" rows="5"></textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("DeliveryTemplate.background")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="background" name="background" type="hidden" data-provide="fileinput" data-file-type="IMAGE" data-show-preview="false">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="width">${message("DeliveryTemplate.width")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="width" name="width" class="form-control" type="text" value="1000" maxlength="9">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="height">${message("DeliveryTemplate.height")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="height" name="height" class="form-control" type="text" value="400" maxlength="9">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="offsetX">${message("DeliveryTemplate.offsetX")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="offsetX" name="offsetX" class="form-control" type="text" value="0" maxlength="9">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="offsetY">${message("DeliveryTemplate.offsetY")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="offsetY" name="offsetY" class="form-control" type="text" value="0" maxlength="9">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="memo">${message("DeliveryTemplate.memo")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="memo" name="memo" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="checkbox">
									<input name="_isDefault" type="hidden" value="false">
									<input id="isDefault" name="isDefault" type="checkbox" value="true">
									<label for="isDefault">${message("DeliveryTemplate.isDefault")}</label>
								</div>
							</div>
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
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.aftersalesSetting.view")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/summernote.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $aftersalesSettingForm = $("#aftersalesSettingForm");
				var $editor = $("textarea.editor");
				
				// 富文本编辑器
				$editor.summernote({
					height: 300,
					toolbar: [
						["style", ["bold", "italic", "underline", "clear"]],
						["font", ["strikethrough", "superscript", "subscript"]],
						["fontname", ["fontname"]],
						["fontsize", ["fontsize"]],
						["color", ["color"]],
						["height", ["height"]],
						["para", ["ul", "ol", "paragraph"]],
						["view", ["codeview"]]
					]
				});
				
				$aftersalesSettingForm.validate({
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/aftersales_setting/view"
						});
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
				<li class="active">${message("business.aftersalesSetting.view")}</li>
			</ol>
			<form id="aftersalesSettingForm" class="ajax-form form-horizontal" action="${base}/business/aftersales_setting/update" method="post">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#repairTips" data-toggle="tab">${message("business.aftersalesSetting.repairTips")}</a>
							</li>
							<li>
								<a href="#replacementTips" data-toggle="tab">${message("business.aftersalesSetting.replacementTips")}</a>
							</li>
							<li>
								<a href="#returnsTips" data-toggle="tab">${message("business.aftersalesSetting.returnsTips")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="repairTips" class="tab-pane active">
								<textarea name="repairTips" class="editor" rows="5">${aftersalesSetting.repairTips}</textarea>
							</div>
							<div id="replacementTips" class="tab-pane">
								<textarea name="replacementTips" class="editor" rows="5">${aftersalesSetting.replacementTips}</textarea>
							</div>
							<div id="returnsTips" class="tab-pane">
								<textarea name="returnsTips" class="editor" rows="5">${aftersalesSetting.returnsTips}</textarea>
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
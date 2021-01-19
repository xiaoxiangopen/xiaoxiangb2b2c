<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.consultation.reply")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
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
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $consultationForm = $("#consultationForm");
				
				// 表单验证
				$consultationForm.validate({
					rules: {
						content: {
							required: true,
							maxlength: 200
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/consultation/reply?consultationId=${consultation.id}"
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
				<li class="active">${message("business.consultation.reply")}</li>
			</ol>
			<form id="consultationForm" class="ajax-form form-horizontal" action="${base}/business/consultation/reply" method="post">
				<input name="consultationId" type="hidden" value="${consultation.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.consultation.reply")}</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-xs-12 col-sm-6">
								<dl class="items dl-horizontal">
									<dt>${message("Consultation.product")}:</dt>
									<dd>
										<a href="${base}${consultation.product.path}" target="_blank">${consultation.product.name}</a>
									</dd>
									<dt>${message("Consultation.member")}:</dt>
									<dd>
										[#if consultation.member??]
											${consultation.member.username}
										[#else]
											${message("business.consultation.anonymous")}
										[/#if]
									</dd>
									<dt>${message("Consultation.content")}:</dt>
									<dd>${consultation.content}</dd>
								</dl>
							</div>
						</div>
						[#if consultation.replyConsultations?has_content]
							[#list consultation.replyConsultations as replyConsultation]
								<div class="form-group">
									<div class="col-xs-4 col-xs-offset-2">
										<p class="form-control-static">${replyConsultation.content}</p>
									</div>
									<div class="col-xs-2">
										<p class="form-control-static text-gray">${replyConsultation.createdDate?string("yyyy-MM-dd HH:mm:ss")}</p>
									</div>
								</div>
							[/#list]
						[/#if]
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="content">${message("Consultation.replyConsultations")}:</label>
							<div class="col-xs-9 col-sm-4">
								<textarea id="content" name="content" class="form-control" rows="5"></textarea>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("common.submit")}</button>
								<a class="btn btn-default" href="${base}/business/consultation/list">${message("common.back")}</a>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>
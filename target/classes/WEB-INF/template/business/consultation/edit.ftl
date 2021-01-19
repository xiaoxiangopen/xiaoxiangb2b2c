<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.consultation.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/bootbox.js"></script>
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
			
				var $delete = $("button.delete");
				
				// 删除
				$delete.delete({
					url: "${base}/business/consultation/delete_reply",
					data: function($element) {
						return {
							consultationId: $element.data("id")
						}
					}
				}).on("success.xiaoxiangshop.delete", function(event) {
					var $element = $(event.target);
					
					$element.closest(".form-group").velocity("fadeOut", {
						complete: function() {
							$(this).remove();
							if ($("button.delete").length < 1) {
								location.reload(true);
							}
						}
					});
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
				<li class="active">${message("business.consultation.edit")}</li>
			</ol>
			<form id="consultationForm" class="ajax-form form-horizontal" action="${base}/business/consultation/update" method="post">
				<input name="consultationId" type="hidden" value="${consultation.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.consultation.edit")}</div>
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
									<dt>${message("Consultation.ip")}:</dt>
									<dd>${consultation.ip}</dd>
								</dl>
							</div>
						</div>
						[#if consultation.replyConsultations?has_content]
							[#list consultation.replyConsultations as replyConsultation]
								<div class="form-group">
									<div class="col-xs-3 col-xs-offset-2">
										<p class="form-control-static">${replyConsultation.content}</p>
									</div>
									<div class="col-xs-2">
										<p class="form-control-static text-gray">${replyConsultation.createdDate?string("yyyy-MM-dd HH:mm:ss")}</p>
									</div>
									<div class="col-xs-1">
										<p class="form-control-static">
											<button class="delete btn btn-default btn-xs btn-icon" type="button" title="${message("common.delete")}" data-toggle="tooltip" data-id="${replyConsultation.id}">
												<i class="iconfont icon-close"></i>
											</button>
										</p>
									</div>
								</div>
							[/#list]
						[/#if]
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
							<div class="col-xs-9 col-sm-10">
								<div class="checkbox">
									<input name="_isShow" type="hidden" value="false">
									<input id="isShow" name="isShow" type="checkbox" value="true"[#if consultation.isShow] checked[/#if]>
									<label for="isShow">${message("Consultation.isShow")}</label>
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
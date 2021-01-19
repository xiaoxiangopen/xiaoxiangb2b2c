<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.message.view")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/message.css" rel="stylesheet">
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
			
				var $inputForm = $("#inputForm");
				
				// 表单验证
				$inputForm.validate({
					rules: {
						content: {
							required: true,
							maxlength: 4000
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/message/view?messageGroupId=${messageGroup.id}"
						});
					}
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="business message">
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
				<li class="active">${message("business.message.view")}</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">${message("business.message.view")}</h3>
				</div>
				<div class="[#if !messages?has_content]hidden-element [/#if]panel-body">
					[#list messages as businessMessage]
						<div class="clearfix">
							<div class="content[#if businessMessage.fromUser == currentUser] pull-right[#else] pull-left[/#if]">
								${businessMessage.content}
								<p class="text-right text-gray">[${businessMessage.fromUser.getDisplayName()}] ${businessMessage.createdDate?string("yyyy-MM-dd HH:mm:ss")}</p>
								[#if businessMessage.fromUser == currentUser]
									[#if businessMessage.fromUser == currentUser && businessMessage.toUserMessageStatus.isRead == false]
										<span class="text-red">${message("business.message.unread")}</span>
									[#else]
										<span class="text-gray">${message("business.message.read")}</span>
									[/#if]
								[/#if]
							</div>
						</div>
					[/#list]
				</div>
				<div class="panel-footer">
					<form id="inputForm" class="ajax-form form-horizontal" action="${base}/business/message/reply" method="post">
						<input name="messageGroupId" type="hidden" value="${messageGroupId}">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="content">${message("business.message.reply")}:</label>
							<div class="col-xs-9 col-sm-4">
								<textarea id="content" name="content" class="form-control" rows="5"></textarea>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-9 col-sm-4 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("business.message.sendNow")}</button>
								<a class="btn btn-default" href="${base}/business/message_group/list">${message("common.back")}</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</main>
</body>
</html>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.message.send")} - 小象电商</title>
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
			
				var $messageForm = $("#messageForm");
				
				// 表单验证
				$messageForm.validate({
					rules: {
						username: {
							required: true,
							notEquals: {
								param: {
									value: "${currentUser.username}",
									ignoreCase: true
								},
								depends: function() {
									return $("input[name='type']:checked").val() === "BUSINESS";
								}
							},
							remote: {
								url: "${base}/business/message/check_username",
								cache: false,
								data: {
									type: function() {
										return $("input[name='type']:checked").val();
									}
								}
							}
						},
						content: {
							required: true,
							maxlength: 4000
						}
					},
					messages: {
						username: {
							notEquals: "${message("business.message.notAllowSelf")}",
							remote: "${message("business.message.userNotExist")}"
						}
					},
					submitHandler: function(form) {
						$(form).ajaxSubmit({
							successRedirectUrl: "${base}/business/message_group/list"
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
				<li class="active">${message("business.message.send")}</li>
			</ol>
			<form id="messageForm" class="ajax-form form-horizontal" action="${base}/business/message/send" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("business.message.send")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("business.message.toUser")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="radio radio-inline">
									<input id="member" name="type" type="radio" value="MEMBER" checked>
									<label for="member">${message("business.message.member")}</label>
								</div>
								<div class="radio radio-inline">
									<input id="business" name="type" type="radio" value="BUSINESS">
									<label for="business">${message("business.message.otherBusiness")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="username">${message("business.message.toUsername")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="username" name="username" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="content">${message("Message.content")}:</label>
							<div class="col-xs-9 col-sm-4">
								<textarea id="content" name="content" class="form-control" rows="5"></textarea>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("business.message.sendNow")}</button>
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
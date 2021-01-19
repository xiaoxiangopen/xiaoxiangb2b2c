<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.point.adjust")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/ajax-bootstrap-select.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/ajax-bootstrap-select.js"></script>
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
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $pointForm = $("#pointForm");
				var $memberId = $("#memberId");
				var $point = $("#point");
				var $amount = $("#amount");
				
				// 检查会员
				$memberId.selectpicker({
					liveSearch: true
				}).ajaxSelectPicker({
					ajax: {
						url: "${base}/admin/point/member_select",
						type: "get",
						data: function() {
							return {
								keyword: "{{{q}}}"
							};
						},
						dataType: "json"
					},
					preprocessData: function(data) {
						return $.map(data, function(item) {
							return {
								value: item.id,
								text: item.name,
								data: {
									point: item.point
								}
							};
						});
					},
					preserveSelected: false
				}).change(function() {
					$pointForm.validate().element($memberId);
				});
				
				// 积分
				$memberId.on("hidden.bs.select", function() {
					var value = $memberId.val();
					var point = $memberId.find("option:selected").data("point");
					
					if ($.trim(value) != "") {
						$point.text(point);
						if ($point.is(":hidden")) {
							$point.closest("div.form-group").velocity("slideDown");
							$amount.prop("disabled", false).closest("div.form-group").velocity("slideDown");
						}
					} else {
						$point.text("-");
						if ($point.is(":visible")) {
							$point.closest("div.form-group").velocity("slideUp");
							$amount.prop("disabled", true).closest("div.form-group").velocity("slideUp");
						}
					}
					$pointForm.validate().element($memberId);
				});
				
				$.validator.addMethod("nonzero",
					function(value, element) {
						return !$.isNumeric(value) || parseFloat(value) != 0;
					},
					"${message("admin.point.nonzero")}"
				);
				
				$.validator.addMethod("insufficientBalance",
					function(value, element) {
						var point = $memberId.find("option:selected").data("point");
						
						return point + parseFloat(value) >= 0
					},
					"${message("admin.point.insufficientBalance")}"
				);
				
				// 表单验证
				$pointForm.validate({
					rules: {
						memberId: "required",
						amount: {
							required: true,
							integer: true,
							nonzero: true,
							insufficientBalance: true
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
				<li class="active">${message("admin.point.adjust")}</li>
			</ol>
			<form id="pointForm" class="ajax-form form-horizontal" action="${base}/admin/point/adjust" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.point.adjust")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.point.member")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select id="memberId" name="memberId" class="form-control" title="${message("admin.point.memberTitle")}"></select>
							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("admin.point.point")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p id="point" class="text-red form-control-static"></p>
							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="amount">${message("admin.point.amount")}:</label>
							<div class="col-xs-9 col-sm-4" title="${message("admin.point.amountTitle")}" data-toggle="tooltip">
								<input id="amount" name="amount" class="form-control" type="text" maxlength="16" disabled>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="memo">${message("admin.point.memo")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="memo" name="memo" class="form-control" type="text" maxlength="200">
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
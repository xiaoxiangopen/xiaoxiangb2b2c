<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.memberRank.add")} - 小象电商</title>
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
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $memberRankForm = $("#memberRankForm");
				var $amount = $("#amount");
				var $isSpecial = $("#isSpecial");
				
				// 是否特殊
				$isSpecial.change(function() {
					var checked = $(this).prop("checked");
					
					$amount.val("").prop("disabled", checked).closest(".form-group").velocity(checked ? "slideUp" : "slideDown");
				});
				
				// 表单验证
				$memberRankForm.validate({
					rules: {
						name: "required",
						scale: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 3,
								fraction: ${setting.priceScale}
							}
						},
						amount: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							},
							remote: {
								url: "${base}/admin/member_rank/check_amount",
								cache: false
							}
						}
					},
					messages: {
						name: {
							remote: "${message("common.validator.exist")}"
						},
						amount: {
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
				<li class="active">${message("admin.memberRank.add")}</li>
			</ol>
			<form id="memberRankForm" class="ajax-form form-horizontal" action="${base}/admin/member_rank/save" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.memberRank.add")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("MemberRank.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="scale">${message("MemberRank.scale")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="scale" name="scale" class="form-control" type="text" value="1" maxlength="9">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="checkbox checkbox-inline">
									<input name="_isDefault" type="hidden" value="false">
									<input id="isDefault" name="isDefault" type="checkbox" value="true">
									<label for="isDefault">${message("MemberRank.isDefault")}</label>
								</div>
								<div class="checkbox checkbox-inline" title="${message("admin.memberRank.isSpecialTitle")}" data-toggle="tooltip">
									<input name="_isSpecial" type="hidden" value="false">
									<input id="isSpecial" name="isSpecial" type="checkbox" value="true">
									<label for="isSpecial">${message("MemberRank.isSpecial")}</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="amount">${message("MemberRank.amount")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="amount" name="amount" class="form-control" type="text" maxlength="16">
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label " for="erpRank">${message("MemberRank.erpRank")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="erpRank" name="erpRank" class="form-control" type="text" maxlength="16">
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label " for="erpNormalPriceColumn">${message("MemberRank.erpNormalPriceColumn")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="erpNormalPriceColumn" name="erpNormalPriceColumn" class="form-control" type="text" maxlength="16">
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="erpNormalPriceColumn">${message("MemberRank.erpPromotionPriceColumn")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="erpPromotionPriceColumn" name="erpPromotionPriceColumn" class="form-control" type="text" maxlength="16">
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label " for="erpNormalPriceColumn">${message("MemberRank.skuNormalPriceColumn")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="skuNormalPriceColumn" name="skuNormalPriceColumn" class="form-control" type="text" maxlength="16">
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label " for="erpNormalPriceColumn">${message("MemberRank.skuPromotionPriceColumn")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="skuPromotionPriceColumn" name="skuPromotionPriceColumn" class="form-control" type="text" maxlength="16">
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
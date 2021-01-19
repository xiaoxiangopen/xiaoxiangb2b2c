<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.memberDeposit.adjust")} - 小象电商</title>
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
			
				var $memberDepositForm = $("#memberDepositForm");
				var $memberId = $("#memberId");
				var $availableBalance = $("#availableBalance");
				var $amount = $("#amount");
				var $orderSn = $("#orderSn");
                var $orderAmount=$("#orderAmount");

				// 会员选择
				$memberId.selectpicker({
					liveSearch: true
				}).ajaxSelectPicker({
					ajax: {
						url: "${base}/admin/member_deposit/member_select",
						type: "GET",
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
									availablebalance: item.availableBalance
								}
							};
						});
					},
					preserveSelected: false
				}).change(function() {
					$memberDepositForm.validate().element($memberId);
				});
				
				// 余额
				$memberId.on("hidden.bs.select", function() {
					var value = $memberId.val();
					var availableBalance = $memberId.find("option:selected").data("availablebalance");
					if ($.trim(value) != "") {
						$availableBalance.text($.currency(availableBalance, true, true));
						if ($availableBalance.is(":hidden")) {
							$availableBalance.closest("div.form-group").velocity("slideDown");
							$orderAmount.prop("disabled", false).closest("div.form-group").velocity("slideDown");
							$orderSn.prop("disabled", false).closest("div.form-group").velocity("slideDown");
							$amount.prop("disabled", false).closest("div.form-group").velocity("slideDown");

						}
					} else {
						$availableBalance.text("-");
						if ($availableBalance.is(":visible")) {
							$availableBalance.closest("div.form-group").velocity("slideUp");
							$orderSn.prop("disabled", true).closest("div.form-group").velocity("slideUp");
							$orderAmount.prop("disabled", false).closest("div.form-group").velocity("slideUp");
							$amount.prop("disabled", true).closest("div.form-group").velocity("slideUp");

						}
					}
					$memberDepositForm.validate().element($memberId);
				});
				$orderSn.on('input propertychange', function() {
					var sn=$(this).val();
					//查询订单金额
					getOrderAmount(sn);


				});
				$.validator.addMethod("nonzero",
					function(value, element) {
						return !$.isNumeric(value) || parseFloat(value) != 0;
					},
					"${message("admin.memberDeposit.nonzero")}"
				);
				
				$.validator.addMethod("insufficientBalance",
					function(value, element) {
						var availableBalance = $memberId.find("option:selected").data("availablebalance");

						return availableBalance + parseFloat(value) >= 0
					},
					"${message("admin.memberDeposit.insufficientBalance")}"
				);
				
				// 表单验证
				$memberDepositForm.validate({
					rules: {
						memberId: "required",
						orderSn: "required",
						amount: {
							required: true,
							number: true,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							},
							nonzero: true,
							insufficientBalance: true
						}
					}
				});
			
			});
			function getOrderAmount(sn){

				$.ajax({
					url: "${base}/admin/member_deposit/getOrderAmount?sn="+sn+"&memberId="+$("#memberId").val(),
					type: "GET",
					dataType: "json",
					cache: false,
					success: function(data) {
						var code=data.code;
						if(code===100){
							$('#orderAmount').text(data.message);
							$('#orderAmounts').val('');
							$('#amount').removeAttr('max');
							$('#amount').removeAttr('min');
							$('#memo').val('');
						}else{
							$('#orderAmount').text($.currency(data.message, true, true));
							$('#orderAmounts').val(data.message);
							$('#amount').attr('max',data.message);
							$('#amount').attr('min',-data.message);
							var memo="关联订单号为："+sn+"，订单金额"+$.currency(data.message, true, true)+"。";
							$('#memo').val(memo);
						}
					}
				});
			}
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
				<li class="active">${message("admin.memberDeposit.adjust")}</li>
			</ol>
			<form id="memberDepositForm" class="ajax-form form-horizontal" action="${base}/admin/member_deposit/adjust" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.memberDeposit.adjust")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.memberDeposit.memberUsername")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select id="memberId" name="memberId" class="form-control" title="${message("admin.memberDeposit.memberTitle")}"></select>
							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("admin.memberDeposit.availableBalance")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p id="availableBalance" class="text-red form-control-static"></p>
							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="orderSn">${message("admin.memberDeposit.orderSn")}:</label>
							<div class="col-xs-9 col-sm-4" title="${message("admin.memberDeposit.orderSn")}" data-toggle="tooltip">
								<input id="orderSn" name="orderSn" class="form-control" type="text" maxlength="16" disabled>
							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("admin.memberDeposit.orderAmount")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p id="orderAmount" class="text-red form-control-static"></p>
								<input id="orderAmounts" name="orderAmounts" class="form-control"  type="hidden"  disabled>

							</div>
						</div>
						<div class="hidden-element form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="amount">${message("admin.memberDeposit.amount")}:</label>
							<div class="col-xs-9 col-sm-4" title="${message("admin.memberDeposit.amountTitle")}" data-toggle="tooltip">
								<input id="amount" name="amount" class="form-control"  max="1000" type="text" maxlength="16" disabled>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="memo">${message("admin.memberDeposit.memo")}:</label>
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
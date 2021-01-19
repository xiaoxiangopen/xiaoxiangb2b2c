<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("business.product.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/bootbox.js"></script>
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/sortable.js"></script>
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
	<script src="${base}/resources/common/js/summernote.js"></script>
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
	<script src="${base}/resources/business/js/base.js"></script>
	<script src="${base}/resources/common/js/moment.js"></script>
	<link href="${base}/resources/common/css/bootstrap-datetimepicker.css" rel="stylesheet">
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>
	<script id="receiverListItemTemplate" type="text/template">
		<li<%=receiver.id == currentReceiverId ? ' class="active"' : ""%> data-receiver-id="<%-receiver.id%>">
		<h5 class="text-overflow" title="<%-receiver.consignee%>"><%-receiver.consignee%></h5>
		<p class="text-gray-dark"><%-receiver.phone%></p>
		<p class="text-overflow" title="<%-receiver.areaName%><%-receiver.address%>"><%-receiver.areaName%><%-receiver.address%></p>
		<p class="text-gray-dark"><%-receiver.zipCode%></p>
		</li>
	</script>
	<style>
		.table tbody tr td {
			vertical-align: top;
		}
	</style>
	<script id="productImageInputTemplate" type="text/template">
		<input name="productImages[<%-productImageIndex%>].source" type="hidden" value="<%-productImage.source%>">
		<input name="productImages[<%-productImageIndex%>].large" type="hidden" value="<%-productImage.large%>">
		<input name="productImages[<%-productImageIndex%>].medium" type="hidden" value="<%-productImage.medium%>">
		<input name="productImages[<%-productImageIndex%>].thumbnail" type="hidden" value="<%-productImage.thumbnail%>">
		<input name="productImages[<%-productImageIndex%>].order" type="hidden" value="<%-productImage.order%>">
	</script>
	<script id="addParameterGroupTemplate" type="text/template">
		<div class="item" data-parameter-index="<%-parameterIndex%>">
			<div class="form-group">
				<div class="col-xs-3 col-sm-1 col-sm-offset-1 text-right">
					<p class="form-control-static">${message("Parameter.group")}:</p>
				</div>
				<div class="col-xs-6 col-sm-4">
					<input name="parameterValues[<%-parameterIndex%>].group" class="parameter-group form-control" type="text" maxlength="200">
				</div>
				<div class="col-xs-3 col-sm-6">
					<p class="form-control-static">
						<a class="remove group" href="javascript:;">[${message("common.delete")}]</a>
						<a class="add" href="javascript:;">[${message("common.add")}]</a>
					</p>
				</div>
			</div>
			<div class="form-group">
				<div class="col-xs-3 col-sm-1 col-sm-offset-1">
					<input name="parameterValues[<%-parameterIndex%>].entries[<%-parameterEntryIndex%>].name" class="parameter-entry-name form-control text-right" type="text" maxlength="200">
				</div>
				<div class="col-xs-6 col-sm-4">
					<input name="parameterValues[<%-parameterIndex%>].entries[<%-parameterEntryIndex%>].value" class="parameter-entry-value form-control" type="text" maxlength="200">
				</div>
				<div class="col-xs-3 col-sm-6">
					<p class="form-control-static">
						<a class="remove" href="javascript:;">[${message("common.delete")}]</a>
					</p>
				</div>
			</div>
		</div>
	</script>
	<script id="addParameterTemplate" type="text/template">
		<div class="form-group">
			<div class="col-xs-3 col-sm-1 col-sm-offset-1">
				<input name="parameterValues[<%-parameterIndex%>].entries[<%-parameterEntryIndex%>].name" class="parameter-entry-name form-control" type="text" maxlength="200">
			</div>
			<div class="col-xs-6 col-sm-4">
				<input name="parameterValues[<%-parameterIndex%>].entries[<%-parameterEntryIndex%>].value" class="parameter-entry-value form-control" type="text" maxlength="200">
			</div>
			<div class="col-xs-3 col-sm-6">
				<p class="form-control-static">
					<a class="remove" href="javascript:;">[${message("common.delete")}]</a>
				</p>
			</div>
		</div>
	</script>
	<script id="parameterGroupTemplate" type="text/template">
		<%_.each(parameters, function(parameter, i) {%>
		<div class="item" data-parameter-index="<%-parameterIndex%>">
			<div class="form-group">
				<div class="col-xs-3 col-sm-1 col-sm-offset-1 text-right">
					<p class="form-control-static">${message("Parameter.group")}:</p>
				</div>
				<div class="col-xs-6 col-sm-4">
					<input name="parameterValues[<%-parameterIndex%>].group" class="parameter-group form-control" type="text" value="<%-parameter.group%>" maxlength="200">
				</div>
				<div class="col-xs-3 col-sm-6">
					<p class="form-control-static">
						<a class="remove group" href="javascript:;">[${message("common.delete")}]</a>
						<a class="add" href="javascript:;">[${message("common.add")}]</a>
					</p>
				</div>
			</div>
			<% var parameterEntryIndex = 0 %>
			<%_.each(parameter.names, function(name, i) {%>
			<div class="form-group">
				<div class="col-xs-3 col-sm-1 col-sm-offset-1">
					<input name="parameterValues[<%-parameterIndex%>].entries[<%-parameterEntryIndex%>].name" class="parameter-entry-name form-control text-right" type="text" value="<%-name%>" maxlength="200">
				</div>
				<div class="col-xs-6 col-sm-4">
					<input name="parameterValues[<%-parameterIndex%>].entries[<%-parameterEntryIndex%>].value" class="parameter-entry-value form-control" type="text" maxlength="200">
				</div>
				<div class="col-xs-3 col-sm-6">
					<p class="form-control-static">
						<a class="remove" href="javascript:;">[${message("common.delete")}]</a>
					</p>
				</div>
			</div>
			<% parameterEntryIndex ++ %>
			<%});%>
			<% parameterIndex ++ %>
		</div>
		<%});%>
	</script>
	<script id="attributeTemplate" type="text/template">
		<%_.each(attributes, function(attribute, i) {%>
		<div class="form-group">
			<label class="col-xs-3 col-sm-2 control-label"><%-attribute.name%>:</label>
			<div class="col-xs-9 col-sm-4">
				<select name="attribute_<%-attribute.id%>" class="form-control">
					<option value="">${message("common.choose")}</option>
					<%_.each(attribute.options, function(option, i) {%>
					<option value="<%-option%>"><%-option%></option>
					<%});%>
				</select>
			</div>
		</div>
		<%});%>
	</script>
	<script id="specificationTemplate" type="text/template">
		<%_.each(specifications, function(specification, i) {%>
		<div class="specification-item form-group">
			<div class="col-xs-4 col-sm-2" style="margin: 5px 0px;">
				<input name="specificationItems[<%-i%>].name" class="specification-item-name form-control text-right" type="text" value="<%-specification.name%>" data-value=<%-specification.name%>>
			</div>
			<div class="col-xs-8 col-sm-10">
				<div class="row">
					<%_.each(specification.options, function(option, j) {%>
					<div class="col-xs-10 col-sm-2" style="margin: 5px 0px;">
						<div class="input-group">
									<span class="input-group-addon">
										<div class="checkbox">
											<input name="specificationItems[<%-i%>].entries[<%-j%>].isSelected" class="specification-item-check" type="checkbox" value="true">
											<label></label>
										</div>
									</span>
							<input name="specificationItems[<%-i%>].entries[<%-j%>].value" class="specification-item-entry-value form-control" type="text" value="<%-option%>" data-value=<%-specification.name%>>
							<input name="_specificationItems[<%-i%>].entries[<%-j%>].isSelected" type="hidden" value="false">
							<input name="specificationItems[<%-i%>].entries[<%-j%>].id" class="specification-item-entry-id" type="hidden" value="<%-_.uniqueId()%>">
						</div>
					</div>
					<%});%>
				</div>
			</div>
		</div>
		<%});%>
	</script>
	<script id="skuTemplate" type="text/template">
		<div class="table-responsive" style="width: 1500px;">
			<table class="table table-hover">
				<thead>
				<tr>
					<%_.each(specificationItems, function(specificationItem, i) {%>
					<th><%-specificationItem.name%></th>
					<%});%>

					<th>商品编码</th>

					[#if product.type == "GENERAL"]
						<th>${message("Sku.price")}</th>
					[/#if]
[#--					<th>${message("Sku.cost")}</th>--]
					<th>${message("Sku.marketPrice")}</th>
					<th>${message("Sku.skuNormalPrice0")}</th>
					<th>${message("Sku.skuNormalPrice1")}</th>
					<th>${message("Sku.skuNormalPrice2")}</th>
					<th>${message("Sku.skuNormalPrice3")}</th>
					<th>${message("Sku.skuPromotionPrice0")}</th>
					<th>${message("Sku.skuPromotionPrice1")}</th>
					<th>${message("Sku.skuPromotionPrice2")}</th>
					<th>${message("Sku.skuPromotionPrice3")}</th>
[#--					[#if product.type == "GENERAL"]--]
[#--						<th>${message("Sku.maxCommission")}</th>--]
[#--						<th>${message("Sku.rewardPoint")}</th>--]
[#--					[/#if]--]
[#--					[#if product.type == "EXCHANGE"]--]
[#--						<th>${message("Sku.exchangePoint")}</th>--]
[#--					[/#if]--]
					<th>${message("Sku.stock")}</th>

					<th>促销${message("Sku.promStartTime")}</th>
					<th>促销${message("Sku.promEndTime")}</th>
					<th>${message("Sku.isPro")}</th>
					<th>${message("Sku.isDefault")}</th>
					<th>${message("business.product.isEnabled")}</th>
				</tr>
				</thead>
				<tbody>
				<%_.each(skus, function(entries, i) {%>
				<%
					var ids = [];
					_.each(entries, function(entry, j) {
					ids.push(entry.id);
				});

				var initSkuValue = initSkuValues[ids.join(",")];
				%>
				<tr data-ids="<%-ids.join(",")%>"
				<%if (initSkuValue != null) {%>title="${message("Sku.sn")}: <%-initSkuValue.sn%>"<%}%>>
				<%
				var skuValue = skuValues[ids.join(",")];
				var price = skuValue != null && skuValue.price != null ? skuValue.price : "";
				var cost = skuValue != null && skuValue.cost != null ? skuValue.cost : "";
				var internalNumber = skuValue != null && skuValue.internalNumber != null ? skuValue.internalNumber : "";
				var marketPrice = skuValue != null && skuValue.marketPrice != null ? skuValue.marketPrice : "";
				var skuNormalPrice0 = skuValue != null && skuValue.skuNormalPrice0 != null ? skuValue.skuNormalPrice0 : "";
				var skuNormalPrice1 = skuValue != null && skuValue.skuNormalPrice1 != null ? skuValue.skuNormalPrice1 : "";
				var skuNormalPrice2 = skuValue != null && skuValue.skuNormalPrice2 != null ? skuValue.skuNormalPrice2 : "";
				var skuNormalPrice3 = skuValue != null && skuValue.skuNormalPrice3 != null ? skuValue.skuNormalPrice3 : "";
				var skuPromotionPrice0 = skuValue != null && skuValue.skuPromotionPrice0 != null ? skuValue.skuPromotionPrice0 : "";
				var skuPromotionPrice1 = skuValue != null && skuValue.skuPromotionPrice1 != null ? skuValue.skuPromotionPrice1 : "";
				var skuPromotionPrice2 = skuValue != null && skuValue.skuPromotionPrice2 != null ? skuValue.skuPromotionPrice2 : "";
				var skuPromotionPrice3 = skuValue != null && skuValue.skuPromotionPrice3 != null ? skuValue.skuPromotionPrice3 : "";
[#--				var maxCommission = skuValue != null && skuValue.maxCommission != null ? skuValue.maxCommission : "";--]
[#--				var rewardPoint = skuValue != null && skuValue.rewardPoint != null ? skuValue.rewardPoint : "";--]
[#--				var exchangePoint = skuValue != null && skuValue.exchangePoint != null ? skuValue.exchangePoint : "";--]
				var stock = skuValue != null && skuValue.stock != null ? skuValue.stock : "";
				var isDefault = skuValue != null && skuValue.isDefault != null ? skuValue.isDefault : false;
				var isPro = skuValue != null && skuValue.isPro != null ? skuValue.isPro : false;
				var promStartTime =  skuValue != null && skuValue.promStartTime != null ? skuValue.promStartTime : "";
				var promEndTime =  skuValue != null && skuValue.promEndTime != null ? skuValue.promEndTime : "";
				var isEnabled = skuValue != null && skuValue.isEnabled != null ? skuValue.isEnabled : false;
				%>

				<%_.each(entries, function(entry, j) {%>

				<td>
					<p class="form-control-static"><%-entry.value%></p>
					<input name="skuList[<%-i%>].specificationValues[<%-j%>].id" type="hidden" value="<%-entry.id%>">
					<input name="skuList[<%-i%>].specificationValues[<%-j%>].value" type="hidden" value="<%-entry.value%>">
				</td>
				<%});%>

				<td>
					<input name="skuList[<%-i%>].internalNumber" style="width: 130px;color: red;" class="internalNumber form-control" type="text" value="<%-internalNumber%>" maxlength="200"<%-!isEnabled ? " disabled" : ""%>>
				</td>

				[#if product.type == "GENERAL"]
					<td>
						<input name="skuList[<%-i%>].price" class="price form-control" type="text" value="<%-price%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
					</td>
				[/#if]

					<input name="skuList[<%-i%>].cost" type = "hidden" class="cost form-control"  value="<%-cost%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>

[#--				<td>--]
[#--					<input name="skuList[<%-i%>].cost" class="cost form-control" type="text" value="<%-cost%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>--]
[#--				</td>--]

				<td>
					<input name="skuList[<%-i%>].marketPrice" class="market-price form-control" type="text"  value="<%-marketPrice%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuNormalPrice0" class="sku-normal-price0 form-control" type="text" value="<%-skuNormalPrice0%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuNormalPrice1" class="sku-normal-price1 form-control" type="text" value="<%-skuNormalPrice1%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuNormalPrice2" class="sku-normal-price2 form-control" type="text" value="<%-skuNormalPrice2%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuNormalPrice3" class="sku-normal-price3 form-control" type="text" value="<%-skuNormalPrice3%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuPromotionPrice0" class="sku-promotion-price0 form-control" type="text" value="<%-skuPromotionPrice0%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuPromotionPrice1" class="sku-promotion-price1 form-control" type="text" value="<%-skuPromotionPrice1%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuPromotionPrice2" class="sku-promotion-price2 form-control" type="text" value="<%-skuPromotionPrice2%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].skuPromotionPrice3" class="sku-promotion-price3 form-control" type="text" value="<%-skuPromotionPrice3%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>
				</td>
[#--				[#if product.type == "GENERAL"]--]

[#--						<input name="skuList[<%-i%>].maxCommission" class="max-commission form-control"  type="hidden" value="<%-maxCommission%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>--]

[#--						<input name="skuList[<%-i%>].rewardPoint" class="reward-point form-control"  type="hidden"  value="<%-rewardPoint%>" maxlength="9"<%-!isEnabled ? " disabled" : ""%>>--]

[#--				[/#if]--]
[#--				[#if product.type == "EXCHANGE"]--]

[#--						<input name="skuList[<%-i%>].exchangePoint" class="exchange-point form-control"  type="hidden"   value="<%-exchangePoint%>" maxlength="9"<%-!isEnabled ? " disabled" : ""%>>--]

[#--				[/#if]--]
[#--				[#if product.type == "GENERAL"]--]
[#--					<td>--]
[#--						<input name="skuList[<%-i%>].maxCommission" class="max-commission form-control" type="text" value="<%-maxCommission%>" maxlength="16"<%-!isEnabled ? " disabled" : ""%>>--]
[#--					</td>--]
[#--					<td>--]
[#--						<input name="skuList[<%-i%>].rewardPoint" class="reward-point form-control" type="text" value="<%-rewardPoint%>" maxlength="9"<%-!isEnabled ? " disabled" : ""%>>--]
[#--					</td>--]
[#--				[/#if]--]
[#--				[#if product.type == "EXCHANGE"]--]
[#--					<td>--]
[#--						<input name="skuList[<%-i%>].exchangePoint" class="exchange-point form-control" type="text" value="<%-exchangePoint%>" maxlength="9"<%-!isEnabled ? " disabled" : ""%>>--]
[#--					</td>--]
[#--				[/#if]--]
				<td>
					<%if (initSkuValue != null) {%>
					<div class="input-group">
						<input name="skuList[<%-i%>].stock" class="stock form-control" type="text" value="<%-initSkuValue.stock%>" maxlength="9" title="${message("Sku.allocatedStock")}: <%-initSkuValue.allocatedStock%>" style="min-width: 70px;" readonly>
						<div class="input-group-btn">
							<a class="sn btn btn-default" href="${base}/business/stock/stock_in?skuSn=<%-initSkuValue.sn%>" title="${message("business.product.stockIn")}" data-toggle="tooltip">+</a>
							<a class="sn btn btn-default" href="${base}/business/stock/stock_out?skuSn=<%-initSkuValue.sn%>" title="${message("business.product.stockOut")}" data-toggle="tooltip">-</a>
						</div>
					</div>
					<%} else {%>
					<input name="skuList[<%-i%>].stock" class="stock form-control" type="text" value="<%-stock%>" maxlength="9"<%-!isEnabled ? " disabled" : ""%>>
					<%}%>
				</td>
				<td>
					<input name="skuList[<%-i%>].promStartTime" class="prom-start-time form-control" type="text" value="<%-promStartTime%>" style="width:140px;" maxlength="32"<%-!isEnabled ? " disabled" : ""%>>
				</td>
				<td>
					<input name="skuList[<%-i%>].promEndTime" class="prom-end-time form-control" type="text" value="<%-promEndTime%>" style="width:140px;" maxlength="32"<%-!isEnabled ? " disabled" : ""%>>
				</td>

				<td>
					<div class="checkbox">
						<input name="_skuList[<%-i%>].isPro" type="hidden" value="false">
						<input name="skuList[<%-i%>].isPro" class="is-pro" type="checkbox" value="true"<%-isPro ? " checked" : ""%><%-!isEnabled ? " disabled" : ""%>>
						<label></label>
					</div>
				</td>
				<td>
					<div class="checkbox">
						<input name="_skuList[<%-i%>].isDefault" type="hidden" value="false">
						<input name="skuList[<%-i%>].isDefault" class="is-default" type="checkbox" value="true"<%-isDefault ? " checked" : ""%><%-!isEnabled ? " disabled" : ""%>>
						<label></label>
					</div>
				</td>
				<td>
					<div class="checkbox">
						<input name="isEnabled" class="is-enabled" type="checkbox" value="true"<%-isEnabled ? " checked" : ""%>>
						<label></label>
					</div>
				</td>
				</tr>
				<%});%>
				</tbody>
			</table>
		</div>
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
				$().ready(function() {



					var date = new Date();
					var month = date.getMonth() + 1;
					var strDate = date.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}
					var currentDate = date.getFullYear() + "-" + month + "-" + strDate
							+ " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

					var endDate="2099-" + month + "-" + strDate
							+ " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

					if($("#promStartTime").val()==null){
						$("#promStartTime").val(currentDate);
					}
					if($("#promEndTime").val()==null){
						$("#promEndTime").val(endDate);
					}
					// 1,111:2,222:3,333
					// 格式解析
					function cn(zhekouId,discountRate){
						var discountRateStr=discountRate.split(":");
						if(discountRateStr.length>0){
							for (i = 0; i < discountRateStr.length; i++) {
								if(discountRateStr[i]!=null){
									var dis=discountRateStr[i].split(",");
									if(dis[0]==zhekouId){
										// alert(dis[1]);
										return dis[1];
									}
								}
							}
						}
					}
					$("#price").change(function() {

						var price= $("#price").val();

						var zhekouId=$("#zhekouId").val();
						var discountRate1=$("#discountRate1").val();
						var discountRate2=$("#discountRate2").val();
						var discountRate3=$("#discountRate3").val();
						var discountRate4=$("#discountRate4").val();
						// 普通会员价
						var price1=parseFloat((cn(zhekouId,discountRate1)*price)/100).toFixed(1);
						// 金卡会员价
						var price2=parseFloat((cn(zhekouId,discountRate2)*price)/100).toFixed(1);
						// 白金会员价
						var price3=parseFloat((cn(zhekouId,discountRate3)*price)/100).toFixed(1);
						// 钻石会员价
						var price4=parseFloat((cn(zhekouId,discountRate4)*price)/100).toFixed(1);

						$("#skuNormalPrice0").val(price1==0?'':price1);
						$("#skuNormalPrice1").val(price2==0?'':price2);
						$("#skuNormalPrice2").val(price3==0?'':price3);
						$("#skuNormalPrice3").val(price4==0?'':price4);

						$("#skuPromotionPrice0").val(price1==0?'':price1);
						$("#skuPromotionPrice1").val(price2==0?'':price2);
						$("#skuPromotionPrice2").val(price3==0?'':price3);
						$("#skuPromotionPrice3").val(price4==0?'':price4);

					})


					$("#OneClicktofillin").click(function() {

						/*var price= $("#price").val();
						var zhekouId=$("#zhekouId").val();
						var discountRate1=$("#discountRate1").val();
						var discountRate2=$("#discountRate2").val();
						var discountRate3=$("#discountRate3").val();
						var discountRate4=$("#discountRate4").val();
						// 普通会员价
						var price1=(cn(zhekouId,discountRate1)*price)/100;
						// 金卡会员价
						var price2=(cn(zhekouId,discountRate2)*price)/100;
						// 白金会员价
						var price3=(cn(zhekouId,discountRate3)*price)/100;
						// 钻石会员价
						var price4=(cn(zhekouId,discountRate4)*price)/100;*/


						var internalNumber=$("#internalNumber").val();
						if(internalNumber==''){
							bootbox.alert("请填写商品编号");
							return;
						}

						//商品编码
						var elements = document.getElementsByClassName('internalNumber form-control');
						Array.prototype.forEach.call(elements,function (element,index) {
							element.value= $("#internalNumber").val()+"_"+index;
						});

						//销售价
						var elements = document.getElementsByClassName('price form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#price").val();
						});

						//成本价
						var elements = document.getElementsByClassName('cost form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#cost").val();
						});

						//市场价
						var elements = document.getElementsByClassName('market-price form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#marketPrice").val();
						});

						//普通会员价
						var elements = document.getElementsByClassName('sku-normal-price0 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuNormalPrice0").val();
						});

						//金卡会员价
						var elements = document.getElementsByClassName('sku-normal-price1 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuNormalPrice1").val();
						});

						//白金会员价
						var elements = document.getElementsByClassName('sku-normal-price2 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuNormalPrice2").val();
						});

						//钻石会员价
						var elements = document.getElementsByClassName('sku-normal-price3 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuNormalPrice3").val();
						});

						//促销普通会员价
						var elements = document.getElementsByClassName('sku-promotion-price0 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuPromotionPrice0").val();
						});
						//促销金卡会员价
						var elements = document.getElementsByClassName('sku-promotion-price1 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuPromotionPrice1").val();
						});
						//促销白金会员价
						var elements = document.getElementsByClassName('sku-promotion-price2 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuPromotionPrice2").val();
						});
						//促销钻石会员价
						var elements = document.getElementsByClassName('sku-promotion-price3 form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#skuPromotionPrice3").val();
						});

						//库存
						var elements = document.getElementsByClassName('stock form-control');
						Array.prototype.forEach.call(elements, function (element) {
							element.value= $("#stock").val();
						});

						//开始时间
						var elements = document.getElementsByClassName('prom-start-time form-control');
						Array.prototype.forEach.call(elements, function (element) {
							var v=$("#promStartTime").val();
							if(v!=null){
								element.value=v;
							}else{
								element.value=currentDate;
							}
						});

						//结束时间
						var elements = document.getElementsByClassName('prom-end-time form-control');
						Array.prototype.forEach.call(elements, function (element) {
							var v=$("#promEndTime").val();
							if(v!=null){
								element.value=v;
							}else{
								element.value=endDate;
							}
						});

						//是否促销
						if($("#isPro").prop('checked')){
							var elements = document.getElementsByClassName('is-pro');
							Array.prototype.forEach.call(elements, function (element) {
								element.value= 'true';
								element.checked = 'true';
							});
						}else{
							element.value= 'false';
							element.checked = 'false';
						}
					});

					var $productForm = $("#productForm");
					var $isDefault = $("#isDefault");
					var $isPro = $("#isPro");
					var $productCategoryId = $("#productCategoryId");
					var $price = $("#price");
					var $cost = $("#cost");
					var $promStartTime = $("#promStartTime");
					var $promEndTime = $("#promEndTime");
					var $marketPrice = $("#marketPrice");
					var $skuNormalPrice0 = $("#skuNormalPrice0");
					var $skuNormalPrice1 = $("#skuNormalPrice1");
					var $skuNormalPrice2 = $("#skuNormalPrice2");
					var $skuNormalPrice3 = $("#skuNormalPrice3");
					var $skuPromotionPrice0 = $("#skuPromotionPrice0");
					var $skuPromotionPrice1 = $("#skuPromotionPrice1");
					var $skuPromotionPrice2 = $("#skuPromotionPrice2");
					var $skuPromotionPrice3 = $("#skuPromotionPrice3");

					var $promStartTime = $("#promStartTime");
					var $promEndTime = $("#promEndTime");
					// var $maxCommission = $("#maxCommission");
					// var $rewardPoint = $("#rewardPoint");
					// var $exchangePoint = $("#exchangePoint");
					var $stock = $("#stock");
					var $productImage = $("#productImage");
					var $productImageFile = $("#productImage input:file");
					var $addParameterButton = $("#addParameterButton");
					var $resetParameter = $("#resetParameter");
					var $parameterContent = $("#parameterContent");
					var $attribute = $("#attribute");
					var $resetSpecification = $("#resetSpecification");
					var $specificationContent = $("#specificationContent");
					var $sku = $("#sku");
					var productImageInputTemplate = _.template($("#productImageInputTemplate").html());
					var addParameterGroupTemplate = _.template($("#addParameterGroupTemplate").html());
					var addParameterTemplate = _.template($("#addParameterTemplate").html());
					var parameterGroupTemplate = _.template($("#parameterGroupTemplate").html());
					var attributeTemplate = _.template($("#attributeTemplate").html());
					var specificationTemplate = _.template($("#specificationTemplate").html());
					var skuTemplate = _.template($("#skuTemplate").html());
					var previousProductCategoryId = ${product.productCategory.id};
					var productImageIndex = 0;
					var parameterIndex = ${(product.parameterValues?size)!0};
					var parameterEntryIndex = 0;
					var hasSpecification = ${product.hasSpecification()?string("true", "false")};
					var productImages = [];
					var initSkuValues = {};

					productImages = [
						[#list product.productImages as productImage]
						{
							source: "${productImage.source}",
							large: "${productImage.large}",
							medium: "${productImage.medium}",
							thumbnail: "${productImage.thumbnail}",
							order: "${productImage.order}"
						}[#if productImage_has_next],[/#if]
						[/#list]
					];

					[#if !product.parameterValues?has_content]
					loadParameter();
					[/#if]

					[#if product.hasSpecification()]
					[#list product.skus as sku]
					initSkuValues["${sku.specificationValueIds?join(",")}"] = {
						id: ${sku.id},
						sn: "${sku.sn}",
						price: ${sku.price},
						cost: ${sku.cost!"null"},
						internalNumber: '${sku.internalNumber}',
						marketPrice: ${sku.marketPrice},
						skuNormalPrice0: ${sku.skuNormalPrice0},
						skuNormalPrice1: ${sku.skuNormalPrice1},
						skuNormalPrice2: ${sku.skuNormalPrice2},
						skuNormalPrice3: ${sku.skuNormalPrice3},
						skuPromotionPrice0: ${sku.skuPromotionPrice0},
						skuPromotionPrice1: ${sku.skuPromotionPrice1},
						skuPromotionPrice2: ${sku.skuPromotionPrice2},
						skuPromotionPrice3: ${sku.skuPromotionPrice3},
						[#--maxCommission: ${sku.maxCommission},--]
						[#--rewardPoint: ${sku.rewardPoint},--]
						[#--exchangePoint: ${sku.exchangePoint},--]
						stock: ${sku.stock},
						allocatedStock: ${sku.allocatedStock},
						isDefault: ${sku.isDefault?string("true", "false")},
						isPro: ${sku.isPro?string("true", "false")},
						promStartTime:"${sku.promStartTime?string("yyyy-MM-dd HH:mm:ss")}",
						promEndTime:"${sku.promEndTime?string("yyyy-MM-dd HH:mm:ss")}",
					 	isEnabled: true
					};
					[/#list]
					buildSkuTable(initSkuValues);
					[#else]
					loadSpecification();
					[/#if]

					// 商品分类
					$productCategoryId.change(function() {
						if (!isEmpty($attribute.find("select"))) {
							bootbox.confirm("${message("business.product.productCategoryChangeConfirm")}", function(result) {
								if (result) {
									if (isEmpty($parameterContent.find("input.parameter-entry-value"))) {
										loadParameter();
									}
									loadAttribute();
									if (isEmpty($sku.find("input:text"))) {
										hasSpecification = false;
										changeView();
										loadSpecification();
									}
									previousProductCategoryId = $productCategoryId.val();
								} else {
									$productCategoryId.selectpicker("val", previousProductCategoryId);
								}
							});
						} else {
							if (isEmpty($parameterContent.find("input.parameter-entry-value"))) {
								loadParameter();
							}
							loadAttribute();
							if (isEmpty($sku.find("input:text"))) {
								hasSpecification = false;
								changeView();
								loadSpecification();
							}
							previousProductCategoryId = $productCategoryId.val();
						}
						$("#zhekouId").val($("#productCategoryId").find("option:selected").attr("categorytype"));
					});

					// 判断是否为空
					function isEmpty($elements) {
						var isEmpty = true;

						$elements.each(function() {
							var $element = $(this);
							if ($.trim($element.val()) != "") {
								isEmpty = false;
								return false;
							}
						});
						return isEmpty;
					}

					// 修改视图
					function changeView() {
						$isDefault.prop("disabled", hasSpecification);
						$isPro.prop("disabled", hasSpecification);
						//.add($maxCommission)
						//.add($rewardPoint)
						// add($exchangePoint).
						$price.add($promStartTime).add($promEndTime).add($cost).add($skuNormalPrice0).add($skuNormalPrice1).add($skuNormalPrice2).add($skuNormalPrice3).add($skuPromotionPrice0).add($skuPromotionPrice1).add($skuPromotionPrice2).add($skuPromotionPrice3).add($marketPrice).add($stock).prop("disabled", hasSpecification).closest("div.form-group").velocity(hasSpecification ? "slideUp" : "slideDown");
					}

					// 商品图片
					$productImageFile.fileinput({
						uploadUrl: "${base}/business/product/upload_product_image",
						allowedFileExtensions: "${setting.uploadImageExtension}".split(","),
						[#if setting.uploadMaxSize != 0]
						maxFileSize: ${setting.uploadMaxSize} * 1024,
						[/#if]
						showRemove: false,
						showClose: false,
						dropZoneEnabled: false,
						overwriteInitial: false,
						initialPreviewAsData: true,
						initialPreview: $.map(productImages, function(productImage) {
							return productImage.large;
						}),
						initialPreviewConfig: $.map(productImages, function(productImage, i) {
							return {
								url: "${base}/business/product/delete_product_image",
								key: i
							}
						}),
						initialPreviewThumbTags: $.map(productImages, function(productImage) {
							return {
								"{inputs}": productImageInputTemplate({
									productImageIndex: productImageIndex ++,
									productImage: productImage
								})
							}
						}),
						previewClass: "multiple-file-preview",
						previewThumbTags: {
							"{inputs}": function() {
								return productImageInputTemplate({
									productImageIndex: productImageIndex ++
								});
							}
						},
						layoutTemplates: {
							footer: '<div class="file-thumbnail-footer">{inputs}{actions}</div>',
							actions: '<div class="file-actions"><div class="file-footer-buttons">{upload} {download} {delete} {zoom} {other}</div>{drag}<div class="clearfix"></div></div>'
						},
						fileActionSettings: {
							showUpload: false,
							showRemove: true,
							showDrag: false
						},
						removeFromPreviewOnError: true,
						showAjaxErrorDetails: false
					}).on("fileuploaded", function(event, data, previewId, index) {
						var $preview = $("#" + previewId);
						var productImage = data.response;

						$preview.find("input[name$='.source']").val(productImage.source);
						$preview.find("input[name$='.large']").val(productImage.large);
						$preview.find("input[name$='.medium']").val(productImage.medium);
						$preview.find("input[name$='.thumbnail']").val(productImage.thumbnail);
					});

					// 商品图片排序
					Sortable.create($productImage.find(".file-preview-thumbnails")[0], {
						animation: 300
					});

					// 增加参数
					$addParameterButton.click(function() {
						var parameterIndex = $parameterContent.children(".item").size();

						$parameterContent.append(addParameterGroupTemplate({
							parameterIndex: parameterIndex,
							parameterEntryIndex: parameterEntryIndex
						}));
					});

					// 重置参数
					$resetParameter.click(function() {
						bootbox.confirm("${message("business.product.resetParameterConfirm")}", function(result) {
							if (result) {
								loadParameter();
							}
						});
					});

					// 删除参数
					$parameterContent.on("click", "a.remove", function() {
						var $element = $(this);

						if ($element.hasClass("group")) {
							$element.closest("div.item").velocity("fadeOut", {
								complete: function() {
									$(this).remove();
								}
							});
						} else {
							if ($element.closest("div.item").find("div.form-group").size() <= 2) {
								$.bootstrapGrowl("${message("business.product.deleteAllNotAllowed")}", {
									type: "warning"
								});
								return false;
							}
							$element.closest("div.form-group").velocity("fadeOut", {
								complete: function() {
									$(this).remove();
								}
							});
						}
					});

					// 添加参数
					$parameterContent.on("click", "a.add", function() {
						var $element = $(this);
						var $item = $element.closest(".item");
						var parameterIndex = $item.data("parameter-index");
						var parameterEntryIndex = $item.find(".form-group").size() - 1;

						$item.append(addParameterTemplate({
							parameterIndex: parameterIndex,
							parameterEntryIndex: parameterEntryIndex
						}));
					});

					// 加载参数
					function loadParameter() {

						$.ajax({
							url: "${base}/business/product/parameters",
							type: "GET",
							data: {
								productCategoryId: $productCategoryId.val()
							},
							dataType: "json",
							success: function(parameters) {
								$parameterContent.html(parameterGroupTemplate({
									parameters: parameters,
									parameterIndex: parameterIndex
								}));
							}
						});
					}

					// 加载属性
					function loadAttribute() {
						$.ajax({
							url: "${base}/business/product/attributes",
							type: "GET",
							data: {
								productCategoryId: $productCategoryId.val()
							},
							dataType: "json",
							success: function(attributes) {
								$attribute.html(attributeTemplate({
									attributes: attributes
								})).find("select").selectpicker();
							}
						});
					}

					// 重置规格
					$resetSpecification.click(function() {
						bootbox.confirm("${message("business.product.resetSpecificationConfirm")}", function(result) {
							if (result) {
								hasSpecification = false;
								changeView();
								loadSpecification();
							}
						});
					});

					// 选择规格
					$specificationContent.on("change", "input.specification-item-check", function() {
						hasSpecification = $specificationContent.find("input:checkbox:checked").size() > 0;
						changeView();
						buildSkuTable();
						if ($sku.find("input.is-default:not(:disabled):checked").size() == 0) {
							$sku.find("input.is-default:not(:disabled):first").prop("checked", true);
						}
					});

					// 规格
					$specificationContent.on("change", "input:text", function() {
						var $element = $(this);
						var value = $.trim($element.val());

						if (value == "") {
							$element.val($element.data("value"));
							return false;
						}
						if ($element.hasClass("specification-item-entry-value")) {
							var values = $element.closest("div.specification-item").find("input.specification-item-entry-value").not($element).map(function() {
								return $.trim($(this).val());
							}).get();

							if ($.inArray(value, values) >= 0) {
								$.bootstrapGrowl("${message("business.product.specificationItemEntryValueRepeated")}", {
									type: "warning"
								});
								$element.val($element.data("value"));
								return false;
							}
						}
						$element.data("value", value);
						buildSkuTable();
					});

					// 是否默认
					$sku.on("change", "input.is-default", function() {
						var $element = $(this);

						if ($element.prop("checked")) {
							$sku.find("input.is-default").not($element).prop("checked", false);
						} else {
							$element.prop("checked", true);
						}
					});




					// 是否启用
					$sku.on("change", "input.is-enabled", function() {
						var $element = $(this);

						if ($element.prop("checked")) {
							$element.closest("tr").find("input:not(input.is-enabled)").prop("disabled", false);
						} else {
							$element.closest("tr").find("input:not(input.is-enabled)").prop("disabled", true).end().find("input.is-default").prop("checked", false);
						}
						if ($sku.find("input.is-default:not(:disabled):checked").size() == 0) {
							$sku.find("input.is-default:not(:disabled):first").prop("checked", true);
						}
					});

					// 生成SKU表
					function buildSkuTable(skuValues) {
						var specificationItems = [];


						if (!hasSpecification) {
							$sku.empty()
							return false;
						}

						$specificationContent.find("div.specification-item").each(function() {
							var $element = $(this);
							var $checked = $element.find("input:checkbox:checked");

							if ($checked.size() > 0) {
								var specificationItem = {};

								specificationItem.name = $element.find("input.specification-item-name").val();
								specificationItem.entries = $checked.map(function() {
									var $element = $(this);
									return {
										id: $element.closest("div.input-group").find("input.specification-item-entry-id").val(),
										value: $element.closest("div.input-group").find("input.specification-item-entry-value").val()
									};
								}).get();
								specificationItems.push(specificationItem);
							}
						});
						var skus = cartesianSkuOf($.map(specificationItems, function(specificationItem) {
							return [specificationItem.entries];
						}));
						if (skuValues == null) {
							skuValues = {};
							$sku.find("tr").each(function() {
								var $element = $(this);
								skuValues[$element.data("ids")] = {
									sn: $element.find("a.sn").data("id"),
									price: $element.find("input.price").val(),
									cost: $element.find("input.cost").val(),
									marketPrice: $element.find("input.market-price").val(),
									skuNormalPrice0: $element.find("input.sku-normal-price0").val(),
									// maxCommission: $element.find("input.max-commission").val(),
									// rewardPoint: $element.find("input.reward-point").val(),
									// exchangePoint: $element.find("input.exchange-point").val(),
									stock: $element.find("input.stock").val(),
									isDefault: $element.find("input.is-default").prop("checked"),
									isPro: $element.find("input.is-pro").prop("checked"),
									isEnabled: $element.find("input.is-enabled").prop("checked")
								};
							});
						}
						$sku.html(skuTemplate({
							initSkuValues: initSkuValues,
							specificationItems: specificationItems,
							skus: skus,
							skuValues: skuValues
						}));
					}

					// 笛卡尔积
					function cartesianSkuOf(array) {
						function addTo(current, args) {
							var i, copy;
							var rest = args.slice(1);
							var isLast = !rest.length;
							var result = [];

							for (i = 0; i < args[0].length; i++) {
								copy = current.slice();
								copy.push(args[0][i]);
								if (isLast) {
									result.push(copy);
								} else {
									result = result.concat(addTo(copy, rest));
								}
							}
							return result;
						}
						return addTo([], array);
					}

					// 加载规格
					function loadSpecification() {

						$.ajax({
							url: "${base}/business/product/specifications",
							type: "GET",
							data: {
								productCategoryId: $productCategoryId.val()
							},
							dataType: "json",
							success: function(specifications) {
								$specificationContent.html(specificationTemplate({
									specifications: specifications
								}));
								$sku.empty();
							}
						});
					}

					$.validator.addClassRules({
						"parameter-group": {
							required: true
						},
						price: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},

						[#--'cost': {--]
						[#--	required: true,--]
						[#--	number: true,--]
						[#--	min: 0,--]
						[#--	decimal: {--]
						[#--		integer: 12,--]
						[#--		fraction: ${setting.priceScale}--]
						[#--	}--]
						[#--},--]

						[#--'market-price': {--]
						[#--	required: true,--]
						[#--	number: true,--]
						[#--	min: 0,--]
						[#--	decimal: {--]
						[#--		integer: 12,--]
						[#--		fraction: ${setting.priceScale}--]
						[#--	}--]
						[#--}--]
						[#--,--]
						'sku-normal-price0': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
						,
						'sku-normal-price1': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
						,
						'sku-normal-price2': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
						,
						'sku-normal-price3': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
						,
						'sku-promotion-price0': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
						,
						'sku-promotion-price1': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
						,
						'sku-promotion-price2': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						}
						,
						'sku-promotion-price3': {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							}
						},
						internalNumber: {
							required: true
						},
						stock: {
							required: true
						}

					});

					// 表单验证
					$productForm.validate({
						rules: {
							productCategoryId: "required",
							name: "required",
							"sku.price": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							},

							[#--"sku.cost": {--]
							[#--	required: true,--]
							[#--	number: true,--]
							[#--	min: 0,--]
							[#--	decimal: {--]
							[#--		integer: 12,--]
							[#--		fraction: ${setting.priceScale}--]
							[#--	}--]
							[#--},--]
							[#--"sku.marketPrice": {--]
							[#--	required: true,--]
							[#--	number: true,--]
							[#--	min: 0,--]
							[#--	decimal: {--]
							[#--		integer: 12,--]
							[#--		fraction: ${setting.priceScale}--]
							[#--	}--]
							[#--}--]
							// ,
							"sku.skuNormalPrice0": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
							,
							"sku.skuNormalPrice1": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}

							,
							"sku.skuNormalPrice2": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
							,
							"sku.skuNormalPrice3": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
							,
							"sku.skuPromotionPrice0": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
							,
							"sku.skuPromotionPrice1": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
							,
							"sku.skuPromotionPrice2": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
							,
							"sku.skuPromotionPrice3": {
								required: true,
								number: true,
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							},

							"weight": { number: true,digits: true},
							"moreFreight": { number: true,digits: true},
							"sku.stock": {required: true},
							internalNumber: {required: true}
						},
						messages: {
							sn: {
								pattern: "${message("common.validator.illegal")}",
								remote: "${message("common.validator.exist")}"
							}
						},
						submitHandler: function(form) {
							if (hasSpecification && $sku.find("input.is-enabled:checked").size() < 1) {
								$.bootstrapGrowl("${message("business.product.specificationSkuRequired")}", {
									type: "warning"
								});
								return false;
							}
							if ($productImageFile.fileinput("getFileStack").length > 0) {
								$.bootstrapGrowl("${message("business.product.productImageUnupload")}", {
									type: "warning"
								});
								return false;
							}
							$productImage.find(".kv-preview-thumb input[name$='.order']").each(function(i) {
								$(this).val(i);
							});
							$productImage.find(".kv-zoom-thumb input").each(function() {
								$(this).prop("disabled", true);
							});
							localStorage.setItem("previousProductCategoryId", $productCategoryId.val());

							$(form).ajaxSubmit({

								successRedirectUrl:location.href=document.referrer
								//successRedirectUrl: "${base}/business/product/list"
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
			<li class="active">${message("business.product.edit")}</li>
		</ol>
		<form id="productForm" class="form-horizontal" action="${base}/business/product/update" method="post">
			<input name="productId" type="hidden" value="${product.id}">
			<input id="isDefault" name="sku.isDefault" type="hidden" value="true">
			<input id="isPro" name="sku.isPro" type="hidden" value="true">
			<input id="zhekouId" name="zhekouId" type="hidden" value="${product.productCategory.categoryType}" >
			<input id="discountRate1" name="discountRate1" type="hidden" value="${discountRate1}">
			<input id="discountRate2" name="discountRate2" type="hidden" value="${discountRate2}">
			<input id="discountRate3" name="discountRate3" type="hidden" value="${discountRate3}">
			<input id="discountRate4" name="discountRate4" type="hidden" value="${discountRate4}">

			<div class="panel panel-default">
				<div class="panel-body">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#base" data-toggle="tab">${message("business.product.base")}</a>
						</li>
						<li>
							<a href="#introduction" data-toggle="tab">${message("business.product.introduction")}</a>
						</li>
						<li>
							<a href="#productImage" data-toggle="tab">${message("business.product.productImage")}</a>
						</li>
						<li>
							<a href="#parameter" data-toggle="tab">${message("business.product.parameter")}</a>
						</li>
						<li>
							<a href="#attribute" data-toggle="tab">${message("business.product.attribute")}</a>
						</li>
						<li>
							<a href="#specification" data-toggle="tab">${message("business.product.specification")}</a>
						</li>
					</ul>
					<div class="tab-content">
						<div id="base" class="tab-pane active">
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required">${message("Product.productCategory")}:</label>
								<div class="col-xs-9 col-sm-4">
									<select id="productCategoryId" name="productCategoryId" class="selectpicker form-control" data-live-search="true" data-size="10">
										<option value="">${message("common.choose")}</option>
										[#list productCategoryTree as productCategory]
												<option value="${productCategory.id}" categorytype="${productCategory.categoryType}" title="${productCategory.name}"[#if productCategory == product.productCategory] selected[/#if][#if productCategory.disabledFlag=='disabled']disabled[/#if]>
													[#if productCategory.grade != 0]
														[#list 1..productCategory.grade as i]
															&nbsp;&nbsp;
														[/#list]
													[/#if]
													${productCategory.name}
												</option>
										[/#list]
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("Product.storeProductCategory")}:</label>
								<div class="col-xs-9 col-sm-4">
									<select name="storeProductCategoryId" class="selectpicker form-control" data-live-search="true" data-size="10">
										<option value="">${message("common.choose")}</option>
										[#list storeProductCategoryTree as storeProductCategory]
											<option value="${storeProductCategory.id}" title="${storeProductCategory.name}"[#if storeProductCategory == product.storeProductCategory] selected[/#if]>
												[#if storeProductCategory.grade != 0]
													[#list 1..storeProductCategory.grade as i]
														&nbsp;&nbsp;
													[/#list]
												[/#if]
												${storeProductCategory.name}
											</option>
										[/#list]
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("Product.type")}:</label>
								<div class="col-xs-9 col-sm-4">
									<p class="form-control-static">${message("Product.Type." + product.type)}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("Product.sn")}:</label>
								<div class="col-xs-9 col-sm-4">
									<p class="form-control-static">${product.sn}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("Product.name")}:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="name" name="name" class="form-control" type="text" value="${product.name}" maxlength="200">
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="caption">${message("Product.caption")}:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="caption" name="caption" class="form-control" type="text" value="${product.caption}" maxlength="200">
								</div>
							</div>
							[#if product.type == "GENERAL"]
								<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="price">${message("Sku.price")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="price" name="sku.price" class="form-control" type="text" value="${product.defaultSku.price}" maxlength="16"[#if product.hasSpecification()] disabled[/#if]>
									</div>
								</div>
							[/#if]
							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="cost">${message("Sku.cost")}:</label>
								<div class="col-xs-9 col-sm-4" title="${message("business.product.costTitle")}" data-toggle="tooltip">
									<input id="cost" name="sku.cost" class="form-control" type="text" value="${product.defaultSku.cost}" maxlength="16"[#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>
							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="marketPrice">${message("Sku.marketPrice")}:</label>
								<div class="col-xs-9 col-sm-4" title="${message("business.product.marketPriceTitle")}" data-toggle="tooltip">
									<input id="marketPrice" name="sku.marketPrice" class="form-control" type="text" value="${product.defaultSku.marketPrice}" maxlength="16"[#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>

							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuNormalPrice0">${message("Sku.skuNormalPrice0")}:</label>
								<div class="col-xs-9 col-sm-4"   data-toggle="tooltip">
									<input id="skuNormalPrice0" name="sku.skuNormalPrice0" class="form-control" type="text" value="${product.defaultSku.skuNormalPrice0}" maxlength="16"[#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>

							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuNormalPrice1">${message("Sku.skuNormalPrice1")}:</label>
								<div class="col-xs-9 col-sm-4"  data-toggle="tooltip">
									<input id="skuNormalPrice1" name="sku.skuNormalPrice1" class="form-control" type="text" value="${product.defaultSku.skuNormalPrice1}" maxlength="16" [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>
							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuNormalPrice2">${message("Sku.skuNormalPrice2")}:</label>
								<div class="col-xs-9 col-sm-4"  data-toggle="tooltip">
									<input id="skuNormalPrice2" name="sku.skuNormalPrice2" class="form-control" type="text" value="${product.defaultSku.skuNormalPrice2}" maxlength="16" [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>
							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuNormalPrice3">${message("Sku.skuNormalPrice3")}:</label>
								<div class="col-xs-9 col-sm-4"  data-toggle="tooltip">
									<input id="skuNormalPrice3" name="sku.skuNormalPrice3" class="form-control" type="text" value="${product.defaultSku.skuNormalPrice3}" maxlength="16" [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>
							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuPromotionPrice0">${message("Sku.skuPromotionPrice0")}:</label>
								<div class="col-xs-9 col-sm-4"  data-toggle="tooltip">
									<input id="skuPromotionPrice0" name="sku.skuPromotionPrice0" class="form-control" value="${product.defaultSku.skuPromotionPrice0}" type="text" maxlength="16" [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>
							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuPromotionPrice1">${message("Sku.skuPromotionPrice1")}:</label>
								<div class="col-xs-9 col-sm-4"  data-toggle="tooltip">
									<input id="skuPromotionPrice1" name="sku.skuPromotionPrice1" class="form-control" type="text" value="${product.defaultSku.skuPromotionPrice1}" maxlength="16" [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>
							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuPromotionPrice2">${message("Sku.skuPromotionPrice2")}:</label>
								<div class="col-xs-9 col-sm-4"  data-toggle="tooltip">
									<input id="skuPromotionPrice2" name="sku.skuPromotionPrice2" class="form-control" type="text" value="${product.defaultSku.skuPromotionPrice2}" maxlength="16" [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>


							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="skuPromotionPrice3">${message("Sku.skuPromotionPrice3")}:</label>
								<div class="col-xs-9 col-sm-4"  data-toggle="tooltip">
									<input id="skuPromotionPrice3" name="sku.skuPromotionPrice3" class="form-control" type="text" value="${product.defaultSku.skuPromotionPrice3}" maxlength="16" [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>


							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="counterNo">专柜编码:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="counterNo" name="sku.counterNo" class="form-control" type="text" value="${product.counterNo}" maxlength="200">
								</div>
							</div>

[#--
							[#if product.type == "GENERAL"]
								<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="maxCommission">${message("Sku.maxCommission")}:</label>
									<div class="col-xs-9 col-sm-4" title="${message("business.product.maxCommission")}" data-toggle="tooltip">
										<input id="maxCommission" name="sku.maxCommission" class="form-control" type="text" value="${product.defaultSku.maxCommission}" maxlength="16"[#if product.hasSpecification()] disabled[/#if]>
									</div>
								</div>
							[/#if]--]

							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="unit">${message("Product.unit")}:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="unit" name="unit" class="form-control" type="text" value="${product.unit}" maxlength="200">
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="weight">${message("Product.weight")}:</label>
								<div class="col-xs-9 col-sm-4">
									<div class="input-group">
										<input id="weight" name="weight" class="form-control" type="text" value="${product.weight}" maxlength="9">
										<span class="input-group-addon">${message("common.unit.gram")}</span>
									</div>
								</div>
							</div>


[#--							<div class="form-group">--]
[#--								<label class="col-xs-3 col-sm-2 control-label" for="moreFreight">额外运费:</label>--]
[#--								<div class="col-xs-9 col-sm-4">--]
[#--									<input id="moreFreight " name="moreFreight " class="form-control" type="text" value="${product.moreFreight}"  maxlength="200">--]
[#--								</div>--]
[#--							</div>--]
[#--							[#if product.type == "GENERAL"]--]
[#--								<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">--]
[#--									<label class="col-xs-3 col-sm-2 control-label" for="rewardPoint">${message("Sku.rewardPoint")}:</label>--]
[#--									<div class="col-xs-9 col-sm-4" title="${message("business.product.rewardPointTitle")}" data-toggle="tooltip">--]
[#--										<input id="rewardPoint" name="sku.rewardPoint" class="form-control" type="text" value="${product.defaultSku.rewardPoint}" maxlength="9"[#if product.hasSpecification()] disabled[/#if]>--]
[#--									</div>--]
[#--								</div>--]
[#--							[/#if]--]
[#--							[#if product.type == "EXCHANGE"]--]
[#--								<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">--]
[#--									<label class="col-xs-3 col-sm-2 control-label item-required" for="exchangePoint">${message("Sku.exchangePoint")}:</label>--]
[#--									<div class="col-xs-9 col-sm-4">--]
[#--										<input id="exchangePoint" name="sku.exchangePoint" class="form-control" type="text" value="${product.defaultSku.exchangePoint}" maxlength="9"[#if product.hasSpecification()] disabled[/#if]>--]
[#--									</div>--]
[#--								</div>--]
[#--							[/#if]--]
							[#if product.hasSpecification()]
								<div class="hidden-element form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="stock">${message("Sku.stock")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="stock" name="sku.stock" class="form-control" type="text" value="1" maxlength="9" disabled>
									</div>
								</div>
							[#else]
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="stock">${message("Sku.stock")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="input-group">
											<input id="stock" name="sku.stock" class="form-control" type="text" value="${product.defaultSku.stock}" maxlength="9" title="${message("Sku.allocatedStock")}: ${product.defaultSku.allocatedStock}" style="min-width: 70px;" readonly>
											<div class="input-group-btn">
												<a class="btn btn-default" href="${base}/business/stock/stock_in?skuSn=${product.defaultSku.sn}" title="${message("business.product.stockIn")}" data-toggle="tooltip">+</a>
												<a class="btn btn-default" href="${base}/business/stock/stock_out?skuSn=${product.defaultSku.sn}" title="${message("business.product.stockOut")}" data-toggle="tooltip">-</a>
											</div>
										</div>
									</div>
								</div>
							[/#if]
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("Product.brand")}:</label>
								<div class="col-xs-9 col-sm-4">
									<select name="brandId" class="selectpicker form-control" data-live-search="true" data-size="10">
										<option value="">${message("common.choose")}</option>
										[#list brands as brand]
											<option value="${brand.id}"[#if brand == product.brand] selected[/#if]>${brand.name}</option>
										[/#list]
									</select>
								</div>
							</div>

							[#if productTags?has_content]
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Product.productTags")}:</label>
									<div class="col-xs-9 col-sm-10">
										[#list productTags as productTag]
											<div class="checkbox checkbox-inline">
												<input id="productTag_${productTag.id}" name="productTagIds" type="checkbox" value="${productTag.id}"[#if product.productTags?seq_contains(productTag)] checked[/#if]>
												<label for="productTag_${productTag.id}">${productTag.name}</label>
											</div>
										[/#list]
									</div>
								</div>
							[/#if]
							[#if storeProductTags?has_content]
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Product.storeProductTags")}:</label>
									<div class="col-xs-9 col-sm-10">
										[#list storeProductTags as storeProductTag]
											<div class="checkbox checkbox-inline">
												<input id="storeProductTags_${storeProductTag.id}" name="storeProductTagIds" type="checkbox" value="${storeProductTag.id}"[#if product.storeProductTags?seq_contains(storeProductTag)] checked[/#if]>
												<label for="storeProductTags_${storeProductTag.id}">${storeProductTag.name}</label>
											</div>
										[/#list]
									</div>
								</div>
							[/#if]
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
								<div class="col-xs-9 col-sm-4">
									<div class="checkbox checkbox-inline">
										<input name="_isPro" type="hidden" value="false">
										<input id="isPro" name="isPro" type="checkbox" value="true"[#if product.isPro] checked[/#if] >
										<label for="isPro">${message("Product.isPro")}</label>
									</div>
									<div class="checkbox checkbox-inline">
										<input name="_erpFlag" type="hidden" value="false">
										<input id="erpFlag" name="erpFlag" type="checkbox" value="true"[#if product.erpFlag] checked[/#if]>
										<label for="erpFlag">${message("Product.erpFlag")}</label>
									</div>
									<div class="checkbox checkbox-inline">
										<input name="_isMarketable" type="hidden" value="false">
										<input id="isMarketable" name="isMarketable" type="checkbox" value="true"[#if product.isMarketable] checked[/#if]>
										<label for="isMarketable">${message("Product.isMarketable")}</label>
									</div>
									<div class="checkbox checkbox-inline">
										<input name="_isList" type="hidden" value="false">
										<input id="isList" name="isList" type="checkbox" value="true"[#if product.isList] checked[/#if]>
										<label for="isList">${message("Product.isList")}</label>
									</div>
[#--									<div class="checkbox checkbox-inline">--]
[#--										<input name="_isAreaLock" type="hidden" value="false">--]
[#--										<input id="isAreaLock" name="isAreaLock" type="checkbox" value="true" checked>--]
[#--										<label for="isAreaLock">${message("Product.isAreaLock")}</label>--]
[#--									</div>--]
									<div class="checkbox checkbox-inline">
										<input name="_isTop" type="hidden" value="false">
										<input id="isTop" name="isTop" type="checkbox" value="true"[#if product.isTop] checked[/#if]>
										<label for="isTop">${message("Product.isTop")}</label>
									</div>
									<div class="checkbox checkbox-inline">
										<input name="_isDelivery" type="hidden" value="false">
										<input id="isDelivery" name="isDelivery" type="checkbox" value="true"[#if product.isDelivery] checked[/#if]>
										<label for="isDelivery">${message("Product.isDelivery")}</label>
									</div>
								</div>
							</div>

							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="promStartTime">促销${message("Sku.promStartTime")}:</label>
								<div class="col-xs-9 col-sm-4" data-provide="datetimerangepicker" data-date-format="YYYY-MM-DD HH:mm:ss">
									<input id="promStartTime" name="sku.promStartTime" value="${product.defaultSku.promStartTime?string("yyyy-MM-dd HH:mm:ss")}"  class="form-control" type="text"  [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>

							<div class="[#if product.hasSpecification()]hidden-element[/#if] form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="promEndTime">促销${message("Sku.promEndTime")}:</label>
								<div class="col-xs-9 col-sm-4" data-provide="datetimerangepicker" data-date-format="YYYY-MM-DD HH:mm:ss">
									<input id="promEndTime" name="sku.promEndTime"  value="${product.defaultSku.promEndTime?string("yyyy-MM-dd HH:mm:ss")}"  class="form-control" type="text"  [#if product.hasSpecification()] disabled[/#if]>
								</div>
							</div>


							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label">${message("Product.areaLock")}:</label>
								<div class="col-xs-9 col-sm-4">
									<select name="areaIds" class="selectpicker form-control" title="${message("common.choose")}" data-live-search="true" data-size="10" multiple>
										[#list areaTree as productCategory]
											<option value="${productCategory.id}" title="${productCategory.name}"[#if (","+product.areaIds+",")?index_of(","+productCategory.id+",") > -1] selected[/#if]>
												[#if productCategory.grade != 0]
													[#list 1..productCategory.grade as i]
														&nbsp;&nbsp;
													[/#list]
												[/#if]
												${productCategory.name}
											</option>
										[/#list]
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="memo">${message("Product.memo")}:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="memo" name="memo" class="form-control" type="text" value="${product.memo}" maxlength="200">
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label item-required" for="internalNumber">${message("Product.internalNumber")}:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="internalNumber" name="internalNumber" class="form-control" type="text" value="${product.internalNumber}" maxlength="200">
								</div>
							</div>

							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="moreFreight">额外运费:</label>
								<div class="col-xs-9 col-sm-4">
									<input id="moreFreight" name="moreFreight" class="form-control" type="text" value="${product.moreFreight}" maxlength="200">
								</div>
							</div>

							<div class="form-group">
								<label class="col-xs-3 col-sm-2 control-label" for="keyword">${message("Product.keyword")}:</label>
								<div class="col-xs-9 col-sm-4" title="${message("business.product.keywordTitle")}" data-toggle="tooltip">
									<input id="keyword" name="keyword" class="form-control" type="text" value="${product.keyword}" maxlength="200">
								</div>
							</div>
						</div>
						<div id="introduction" class="tab-pane">
							<textarea name="introduction" data-provide="editor">${product.introduction}</textarea>
						</div>
						<div id="productImage" class="tab-pane">
							<div class="form-group">
								<div class="col-xs-12 col-sm-6">
									<input name="file" type="file" multiple>
								</div>
							</div>
						</div>
						<div id="parameter" class="tab-pane">
							<div class="form-group">
								<div class="col-xs-10 col-xs-offset-2">
									<button id="addParameterButton" class="btn btn-default" type="button">
										<i class="iconfont icon-add"></i>
										${message("business.product.addParameter")}
									</button>
									<button id="resetParameter" class="btn btn-default" type="button">
										<i class="iconfont icon-repeal"></i>
										${message("business.product.resetParameter")}
									</button>
								</div>
							</div>
							<div id="parameterContent">
								[#list product.parameterValues as parameterValue]
									<div class="item" data-parameter-index="${parameterValue_index}" data-parameter-entry-index="${parameterValue.entries?size}">
										<div class="form-group">
											<div class="col-xs-3 col-sm-1 col-sm-offset-1 text-right">
												<p class="form-control-static">${message("Parameter.group")}:</p>
											</div>
											<div class="col-xs-6 col-sm-4">
												<input name="parameterValues[${parameterValue_index}].group" class="parameter-group form-control" type="text" value="${parameterValue.group}" maxlength="200">
											</div>
											<div class="col-xs-3 col-sm-6">
												<p class="form-control-static">
													<a class="remove group" href="javascript:;">[${message("common.delete")}]</a>
													<a class="add" href="javascript:;">[${message("common.add")}]</a>
												</p>
											</div>
										</div>
										[#list parameterValue.entries as entry]
											<div class="form-group">
												<div class="col-xs-3 col-sm-1 col-sm-offset-1">
													<input name="parameterValues[${parameterValue_index}].entries[${entry_index}].name" class="parameter-entry-name form-control text-right" type="text" value="${entry.name}" maxlength="200">
												</div>
												<div class="col-xs-6 col-sm-4">
													<input name="parameterValues[${parameterValue_index}].entries[${entry_index}].value" class="parameter-entry-value form-control" type="text" value="${entry.value}" maxlength="200">
												</div>
												<div class="col-xs-3 col-sm-6">
													<p class="form-control-static">
														<a class="remove" href="javascript:;">[${message("common.delete")}]</a>
													</p>
												</div>
											</div>
										[/#list]
									</div>
								[/#list]
							</div>
						</div>
						<div id="attribute" class="tab-pane">
							[#list product.productCategory.attributes as attribute]
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${attribute.name}:</label>
									<div class="col-xs-9 col-sm-4">
										<select name="attribute_${attribute.id}" class="selectpicker form-control">
											<option value="">${message("common.choose")}</option>
											[#list attribute.options as option]
												<option value="${option}"[#if option == product.getAttributeValue(attribute)] selected[/#if]>${option}</option>
											[/#list]
										</select>
									</div>
								</div>
							[/#list]
						</div>
						<div id="specification" class="tab-pane">
							<div class="form-group">
								<div class="col-xs-4 col-xs-offset-2">
									<button id="resetSpecification" class="btn btn-default" type="button">
										<i class="iconfont icon-repeal"></i>
										${message("business.product.resetSpecification")}
									</button>

									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<button id="OneClicktofillin"  class="btn btn-default" type="button">
										一键填写
									</button>

								</div>
							</div>
							<div id="specificationContent">
								[#list product.specificationItems as specificationItem]
									<div class="specification-item form-group">
										<div class="col-xs-4 col-sm-2" style="margin: 5px 0px;">
											<input name="specificationItems[${specificationItem_index}].name" class="specification-item-name form-control text-right" type="text" value="${specificationItem.name}" data-value="${specificationItem.name}">
										</div>
										<div class="col-xs-8 col-sm-10">
											<div class="row">
												[#list specificationItem.entries as entry]
													<div class="col-xs-10 col-sm-2" style="margin: 5px 0px;">
														<div class="input-group">
																<span class="input-group-addon">
																	<div class="checkbox">
																		<input name="specificationItems[${specificationItem_index}].entries[${entry_index}].isSelected" class="specification-item-check" type="checkbox" value="true"[#if entry.isSelected] checked[/#if]>
																		<label></label>
																	</div>
																</span>
															<input name="specificationItems[${specificationItem_index}].entries[${entry_index}].value" class="specification-item-entry-value form-control" type="text" value="${entry.value}" data-value="${entry.value}">
															<input name="_specificationItems[${specificationItem_index}].entries[${entry_index}].isSelected" type="hidden" value="false">
															<input name="specificationItems[${specificationItem_index}].entries[${entry_index}].id" class="specification-item-entry-id" type="hidden" value="${entry.id}">
														</div>
													</div>
												[/#list]
											</div>
										</div>
									</div>
								[/#list]
							</div>
							<div id="sku"></div>
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

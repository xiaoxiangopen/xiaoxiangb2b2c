[#noautoesc]
	[#escape x as x?js_string]
		<script>
		$().ready(function() {
			
			var $window = $(window);
			var $body = $("body");
			var $mainSidebarCollapseToggle = $("[data-toggle='mainSidebarCollapse']");
			var $mainSidebar = $("#mainSidebar");
			var $searchForm = $("#mainSidebar .search form");
			var $searchValue = $("#mainSidebar .search input[name='searchValue']");
			var $panelCollapse = $("#mainSidebar .panel-collapse");
			
			// 主侧边栏折叠
			$mainSidebarCollapseToggle.click(function() {
				var niceScroll = $mainSidebar.getNiceScroll();
				var interval = setInterval(function() {
					niceScroll.resize();
				}, 10);
				
				if ($window.width() > 767) {
					$body.removeClass("main-sidebar-expanded").toggleClass("main-sidebar-mini");
				} else {
					$body.removeClass("main-sidebar-mini").toggleClass("main-sidebar-expanded");
				}
				
				$body.one("bsTransitionEnd", function() {
					niceScroll.resize();
					window.clearInterval(interval);
				}).emulateTransitionEnd(500);
			});
			
			// 主侧边栏滚动条
			$mainSidebar.niceScroll({
				cursorwidth: "4px",
				cursorcolor: "#ffffff",
				cursorborder: "0px",
				cursoropacitymax: 0.4
			});
			
			// 搜索
			$searchForm.submit(function() {
				if ($.trim($searchValue.val()) == "") {
					return false;
				}
			});
			
			// 面板折叠
			$panelCollapse.on("shown.bs.collapse hidden.bs.collapse", function() {
				$mainSidebar.getNiceScroll().resize();
			});
		
		});
		</script>
	[/#escape]
[/#noautoesc]
<aside id="mainSidebar" class="main-sidebar">
	<div class="search">
		<form action="${base}/business/product/list" method="get">
			<input name="searchProperty" type="hidden" value="name">
			<div class="input-group">
				<input name="searchValue" class="form-control" type="text" placeholder="${message("business.mainSidebar.search")}" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search">
				<div class="input-group-btn">
					<button class="btn btn-default" type="submit">
						<i class="iconfont icon-search"></i>
					</button>
				</div>
			</div>
		</form>
	</div>
	<div id="mainSidebarPanelGroup" class="panel-group">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a href="#productPanelCollapse" data-toggle="collapse" data-parent="#mainSidebarPanelGroup"[#if .main_template_name?matches("business/index.*|business/product/.*|business/stock/.*|business/product_notify/.*|business/consultation/.*|business/review/.*|business/note/.*")] aria-expanded="true"[/#if]>
						<i class="iconfont icon-goods"></i>
						<span>${message("business.mainSidebar.product")}</span>
						<i class="iconfont icon-unfold"></i>
					</a>
				</h4>
			</div>
			<div id="productPanelCollapse" class="panel-collapse collapse[#if .main_template_name?matches("business/index.*|business/product/.*|business/stock/.*|business/product_notify/.*|business/consultation/.*|business/review/.*|business/note/.*")] in[/#if]">
				<div class="panel-body">
					<ul class="list-group">
						<li class="list-group-item[#if .main_template_name?matches("business/product/.*")] active[/#if]">
							<a href="${base}/business/product/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.productList")}
								[@product_count storeId = currentStore.id isStockAlert = true]
									<span class="label label-warning" title="${message("business.mainSidebar.stockAlertProduct")}" data-toggle="tooltip">${count}</span>
								[/@product_count]
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/stock/.*")] active[/#if]">
							<a href="${base}/business/stock/log">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.stockLog")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/product_notify/.*")] active[/#if]">
							<a href="${base}/business/product_notify/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.productNotifyList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/consultation/.*")] active[/#if]">
							<a href="${base}/business/consultation/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.consultationList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/review/.*")] active[/#if]">
							<a href="${base}/business/review/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.reviewList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/note/.*")] active[/#if]">
							<a href="${base}/business/note/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.noteList")}
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a href="#orderPanelCollapse" data-toggle="collapse" data-parent="#mainSidebarPanelGroup"[#if .main_template_name?matches("business/order/.*|business/delivery_template/.*|business/delivery_center/.*|business/aftersales/.*")] aria-expanded="true"[/#if]>
						<i class="iconfont icon-form"></i>
						<span>${message("business.mainSidebar.order")}</span>
						<i class="iconfont icon-unfold"></i>
					</a>
				</h4>
			</div>
			<div id="orderPanelCollapse" class="panel-collapse collapse[#if .main_template_name?matches("business/order/.*|business/delivery_template/.*|business/delivery_center/.*|business/aftersales/.*")] in[/#if]">
				<div class="panel-body">
					<ul class="list-group">
						<li class="list-group-item[#if .main_template_name?matches("business/order/.*")] active[/#if]">
							<a href="${base}/business/order/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.orderList")}
								[@order_count status = "PENDING_REVIEW" storeId = currentStore.id hasExpired = false]
									<span class="label label-warning" title="${message("business.mainSidebar.pendingOrder")}" data-toggle="tooltip">${count}</span>
								[/@order_count]
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/delivery_template/.*")] active[/#if]">
							<a href="${base}/business/delivery_template/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.deliveryTemplateList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/delivery_center/.*")] active[/#if]">
							<a href="${base}/business/delivery_center/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.deliveryCenterList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/aftersales/.*")] active[/#if]">
							<a href="${base}/business/aftersales/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.aftersalesList")}
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a href="#storePanelCollapse" data-toggle="collapse" data-parent="#mainSidebarPanelGroup"[#if .main_template_name?matches("business/store/setting.*|business/store_product_category/.*|business/store_product_tag/.*|business/category_application/.*|business/store/payment.*|business/shipping_method/.*|business/area_freight_config/.*|business/store_ad_image/.*|business/aftersales_setting/.*")] aria-expanded="true"[/#if]>
						<i class="iconfont icon-shop"></i>
						<span>${message("business.mainSidebar.store")}</span>
						<i class="iconfont icon-unfold"></i>
					</a>
				</h4>
			</div>
			<div id="storePanelCollapse" class="panel-collapse collapse[#if .main_template_name?matches("business/store/setting.*|business/store_product_category/.*|business/store_product_tag/.*|business/category_application/.*|business/store/payment.*|business/shipping_method/.*|business/area_freight_config/.*|business/store_ad_image/.*|business/aftersales_setting/.*")] in[/#if]">
				<div class="panel-body">
					<ul class="list-group">
						<li class="list-group-item[#if .main_template_name?matches("business/store/setting.*")] active[/#if]">
							<a href="${base}/business/store/setting">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.storeSetting")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/store_product_category/.*")] active[/#if]">
							<a href="${base}/business/store_product_category/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.storeProductCategoryList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/store_product_tag/.*")] active[/#if]">
							<a href="${base}/business/store_product_tag/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.storeProductTagList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/category_application/.*")] active[/#if]">
							<a href="${base}/business/category_application/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.categoryApplicationList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/store/payment.*")] active[/#if]">
							<a href="${base}/business/store/payment">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.storePayment")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/shipping_method/.*|business/area_freight_config/.*")] active[/#if]">
							<a href="${base}/business/shipping_method/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.shippingMethodList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/store_ad_image/.*")] active[/#if]">
							<a href="${base}/business/store_ad_image/list">
								<i class="iconfont icon-weixuanzhong"></i>
								<span>${message("business.mainSidebar.storeAdImageList")}</span>
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/aftersales_setting/.*")] active[/#if]">
							<a href="${base}/business/aftersales_setting/view">
								<i class="iconfont icon-weixuanzhong"></i>
								<span>${message("business.mainSidebar.aftersalesTips")}</span>
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>

		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a href="#depositPanelCollapse" data-toggle="collapse" data-parent="#mainSidebarPanelGroup"[#if .main_template_name?matches("business/business_deposit/.*|business/business_cash/.*")] aria-expanded="true"[/#if]>
						<i class="iconfont icon-recharge"></i>
						<span>${message("business.mainSidebar.deposit")}</span>
						<i class="iconfont icon-unfold"></i>
					</a>
				</h4>
			</div>
			<div id="depositPanelCollapse" class="panel-collapse collapse[#if .main_template_name?matches("business/business_deposit/.*|business/business_cash/.*")] in[/#if]">
				<div class="panel-body">
					<ul class="list-group">
						<li class="list-group-item[#if .main_template_name?matches("business/business_deposit/recharge.*")] active[/#if]">
							<a href="${base}/business/business_deposit/recharge">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.depositRecharge")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/business_cash/.*")] active[/#if]">
							<a href="${base}/business/business_cash/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.cashList")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/business_deposit/log.*")] active[/#if]">
							<a href="${base}/business/business_deposit/log">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.depositLog")}
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a href="#profilePanelCollapse" data-toggle="collapse" data-parent="#mainSidebarPanelGroup"[#if .main_template_name?matches("business/profile/.*|business/password/.*")] aria-expanded="true"[/#if]>
						<i class="iconfont icon-people"></i>
						<span>${message("business.mainSidebar.profile")}</span>
						<i class="iconfont icon-unfold"></i>
					</a>
				</h4>
			</div>
			<div id="profilePanelCollapse" class="panel-collapse collapse[#if .main_template_name?matches("business/profile/.*|business/password/.*")] in[/#if]">
				<div class="panel-body">
					<ul class="list-group">
						<li class="list-group-item[#if .main_template_name?matches("business/profile/.*")] active[/#if]">
							<a href="${base}/business/profile/edit">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.profileEdit")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/password/.*")] active[/#if]">
							<a href="${base}/business/password/edit">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.passwordEdit")}
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>

		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a href="#messagePanelCollapse" data-toggle="collapse" data-parent="#mainSidebarPanelGroup"[#if .main_template_name?matches("business/message/.*|business/message_group/.*")] aria-expanded="true"[/#if]>
						<i class="iconfont icon-message"></i>
						<span>${message("business.mainSidebar.messageList")}</span>
						<i class="iconfont icon-unfold"></i>
					</a>
				</h4>
			</div>
			<div id="messagePanelCollapse" class="panel-collapse collapse[#if .main_template_name?matches("business/message/.*|business/message_group/.*")] in[/#if]">
				<div class="panel-body">
					<ul class="list-group">
						<li class="list-group-item[#if .main_template_name?matches("business/message/.*")] active[/#if]">
							<a href="${base}/business/message/send">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.messageSend")}
							</a>
						</li>
						<li class="list-group-item[#if .main_template_name?matches("business/message_group/.*")] active[/#if]">
							<a href="${base}/business/message_group/list">
								<i class="iconfont icon-weixuanzhong"></i>
								${message("business.mainSidebar.messageList")}
							</a>
						</li>
					</ul>
				</div>
			</div>

		</div>

	</div>
	<ul class="list-group">
		<li class="list-group-item[#if .main_template_name?matches("business/instant_message/.*")] active[/#if]">
			<a href="${base}/business/instant_message/list">
				<i class="iconfont icon-wang"></i>
				<span>${message("business.mainSidebar.instantMessageList")}</span>
			</a>
		</li>
		<li class="list-group-item">
			<p>${message("business.mainSidebar.statistics")}</p>
		</li>
		<li class="list-group-item[#if .main_template_name?matches("business/order_statistic/.*")] active[/#if]">
			<a href="${base}/business/order_statistic/list">
				<i class="iconfont icon-baobiao-xianxing text-blue-light"></i>
				<span>${message("business.mainSidebar.orderStatisticList")}</span>
			</a>
		</li>
		<li class="list-group-item[#if .main_template_name?matches("business/fund_statistic/.*")] active[/#if]">
			<a href="${base}/business/fund_statistic/list">
				<i class="iconfont icon-zhexiantu-xianxing text-green"></i>
				<span>${message("business.mainSidebar.fundStatisticList")}</span>
			</a>
		</li>
		<li class="list-group-item[#if .main_template_name?matches("business/product_ranking/.*")] active[/#if]">
			<a href="${base}/business/product_ranking/list">
				<i class="iconfont icon-tiaoxingtu-xianxing text-yellow-light"></i>
				<span>${message("business.mainSidebar.productRankingList")}</span>
			</a>
		</li>
	</ul>
</aside>
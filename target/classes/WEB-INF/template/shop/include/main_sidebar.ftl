<script id="historyProductTemplate" type="text/template">
	<h4>
		${message("shop.mainSidebar.historyProduct")}
		<%if (!_.isEmpty(data)) {%>
			<a class="clear pull-right" href="javascript:;">${message("shop.mainSidebar.clearHistoryProduct")}</a>
		<%}%>
	</h4>
	<%if (!_.isEmpty(data)) {%>
		<ul class="clearfix">
			<%_.each(data, function(product, i) {%>
				<li>
					<a href="${base}<%-product.path%>" target="_blank">
						<img class="img-responsive center-block" src="<%-product.thumbnail != null ? product.thumbnail : "${setting.defaultThumbnailProductImage}"%>" alt="<%-product.name%>">
						<h5 class="text-overflow" title="<%-product.name%>"><%-product.name%></h5>
					</a>
				</li>
			<%});%>
		</ul>
	<%} else {%>
		<p>${message("common.noResult")}</p>
	<%}%>
</script>
[#noautoesc]
	[#escape x as x?js_string]
		<script>
			$().ready(function() {
				
				var $window = $(window);
				var $document = $(document);
				var $body = $("body");
				var $mainSidebar = $("#mainSidebar");
				var $mainSidebarCartQuantity = $("#mainSidebarCart em");
				var $mainSidebarTooltipToggle = $("#mainSidebar [data-toggle='tooltip']");
				var $historyProductCollapse = $("#historyProductCollapse");
				var $mainSidebarQrcodeContent = $("#mainSidebarQrcode div.content");
				var $mainSidebarBackTop = $("#mainSidebarBackTop");
				var $mainSidebarHistoryProduct = $("#mainSidebarHistoryProduct");
				var mainSidebarBackTopHidden = true;
				var mainSidebarExpanded = false;
				var historyProductTemplate = _.template($("#historyProductTemplate").html());
				var historyProductIdsLocalStorageKey = "historyProductIds";
				
				// 购物车数量
				var currentCartQuantity = $.getCurrentCartQuantity();
				
				if (currentCartQuantity != null) {
					$mainSidebarCartQuantity.text(currentCartQuantity < 100 ? currentCartQuantity : "99+");
				}
				
				// 购物车数量
				$document.on("complete.xiaoxiangshop.setCurrentCartQuantity", function(event, quantity) {
					$mainSidebarCartQuantity.text(quantity < 100 ? quantity : "99+");
				});
				
				// 提示
				$mainSidebarTooltipToggle.tooltip();
				
				// 浏览记录展开/折叠
				$document.on("click", function(e) {
					if ($mainSidebar.data("collapse-disabled") === "true") {
						return;
					}
					if ($historyProductCollapse[0] === e.target || $.contains($historyProductCollapse[0], e.target) || (mainSidebarExpanded && !$.contains($mainSidebar[0], e.target))) {
						$mainSidebar.velocity({
							right: mainSidebarExpanded ? -300 : 0
						}, {
							begin: function() {
								$mainSidebar.data("collapse-disabled", "true");
								if (!mainSidebarExpanded) {
									loadHistoryProduct();
								}
							},
							complete: function() {
								$mainSidebar.removeData("collapse-disabled");
								mainSidebarExpanded = !mainSidebarExpanded;
							}
						}, 500);
					}
				});
				
				// 加载浏览记录
				function loadHistoryProduct() {
					var historyProductIdsLocalStorage = localStorage.getItem(historyProductIdsLocalStorageKey);
					var historyProductIds = historyProductIdsLocalStorage != null ? JSON.parse(historyProductIdsLocalStorage) : [];
					
					$.get("${base}/product/history", {
						productIds: historyProductIds
					}).done(function(data) {
						localStorage.setItem(historyProductIdsLocalStorageKey, JSON.stringify($.map(data, function(item) {
							return item.id
						})));
						$mainSidebarHistoryProduct.html(historyProductTemplate({
							data: data
						}));
					});
				}
				
				// 清空浏览记录
				$mainSidebarHistoryProduct.on("click", ".clear", function() {
					localStorage.removeItem(historyProductIdsLocalStorageKey);
					$mainSidebarHistoryProduct.html(historyProductTemplate({
						data: {}
					}));
					return false;
				});
				
				// 二维码
				$mainSidebarQrcodeContent.qrcode({
					width: 100,
					height: 100,
					text: location.href
				});
				
				// 返回顶部
				$window.scroll(_.throttle(function() {
					if ($window.scrollTop() > 500) {
						if (mainSidebarBackTopHidden) {
							mainSidebarBackTopHidden = false;
							$mainSidebarBackTop.velocity("fadeIn");
						}
					} else {
						if (!mainSidebarBackTopHidden) {
							mainSidebarBackTopHidden = true;
							$mainSidebarBackTop.velocity("fadeOut");
						}
					}
				}, 500));
				
				// 返回顶部
				$mainSidebarBackTop.click(function() {
					$body.velocity("stop").velocity("scroll", {
						duration: 1000
					});
				});
				
			});
		</script>
	[/#escape]
[/#noautoesc]
<aside id="mainSidebar" class="main-sidebar clearfix">
	<div class="main-sidebar-body">
		<ul>
			<li>
				<a id="mainSidebarCart" class="cart" href="${base}/cart/list">
					<i class="iconfont icon-cart"></i>
					<span>${message("shop.mainSidebar.cartList")}</span>
					<em>0</em>
				</a>
			</li>
			<li>
				<a href="${base}/member/order/list" title="${message("shop.mainSidebar.orderListTitle")}" data-toggle="tooltip" data-placement="left">
					<i class="iconfont icon-form"></i>
				</a>
			</li>
			<li>
				<a id="historyProductCollapse" href="javascript:;" title="${message("shop.mainSidebar.historyProductTitle")}" data-toggle="tooltip" data-placement="left">
					<i class="iconfont icon-footprint"></i>
				</a>
			</li>
			<li>
				<a href="${base}/member/product_favorite/list" title="${message("shop.mainSidebar.productFavoriteListTitle")}" data-toggle="tooltip" data-placement="left">
					<i class="iconfont icon-like"></i>
				</a>
			</li>

			<li>
				<a id="mainSidebarQrcode" class="qrcode" href="javascript:;">
					<i class="iconfont icon-scan"></i>
					<div class="content"></div>
				</a>
			</li>
		</ul>
		<a id="mainSidebarBackTop" class="back-top" href="javascript:;" title="${message("shop.mainSidebar.backTopTitle")}">
			<i class="iconfont icon-top"></i>
		</a>
	</div>
	<div class="main-sidebar-right">
		<div id="mainSidebarHistoryProduct" class="history-product"></div>
	</div>
</aside>
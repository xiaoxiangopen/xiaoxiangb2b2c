<script id="mainHeaderMemberInfoTemplate" type="text/template">
	<%if (currentUser != null && currentUser.type == "member") {%>
		<ul class="list-inline">
			<li>
				<a href="${base}/member/index"><%-currentUser.username%></a>
			</li>
			<li>
				<a class="logout" href="${base}/member/logout">${message("shop.mainHeader.memberLogout")}</a>
			</li>
		</ul>
	<%} else {%>
		<ul class="list-inline">
			<li>
				<a href="${base}/member/login">${message("shop.mainHeader.memberLogin")}</a>
			</li>
			<li>
				<a href="${base}/member/register">${message("shop.mainHeader.memberRegister")}</a>
			</li>
		</ul>
	<%}%>
</script>
<script id="mainHeaderCartDetailTemplate" type="text/template">
	<%if (!_.isEmpty(cart.cartItems)) {%>
		<div class="cart-detail-body">
			<ul>
				<%_.each(cart.cartItems, function(cartItem, i) {%>
					<li>
						<a href="${base}<%-cartItem.skuPath%>">
							<img src="<%-cartItem.skuThumbnail%>" alt="<%-cartItem.skuName%>">
							<span class="text-overflow" title="<%-cartItem.skuName%>"><%-cartItem.skuName%></span>
						</a>
						<span>
							<strong><%-$.currency(cartItem.price, true, false)%></strong>
							&times; <%-cartItem.quantity%>
						</span>
					</li>
				<%});%>
			</ul>
		</div>
		<div class="cart-detail-footer">
			${message("shop.mainHeader.cartPrice")}:
			<strong><%-$.currency(cart.effectivePrice, true, true)%></strong>
			<a class="pull-right" href="${base}/cart/list">${message("shop.mainHeader.cartCheckout")}</a>
		</div>
	<%} else {%>
		<p>${message("shop.mainHeader.cartEmpty")}</p>
	<%}%>
</script>
[#noautoesc]
	[#escape x as x?js_string]
		<script>
		$().ready(function() {
			
			var $document = $(document);
			var $mainHeaderTopAd = $("#mainHeaderTopAd");
			var $mainHeaderTopAdClose = $("#mainHeaderTopAd button.close");
			var $mainHeaderMemberInfo = $("#mainHeaderMemberInfo");
			var $mainHeaderProductSearchForm = $("#mainHeaderProductSearch form");
			var $searchType = $("#mainHeaderProductSearch [data-search-type]");
			var $mainHeaderProductSearchKeyword = $("#mainHeaderProductSearch input[name='keyword']");
			var $mainHeaderCart = $("#mainHeaderCart");
			var $mainHeaderCartQuantity = $("#mainHeaderCart em");
			var $mainHeaderCartDetail = $("#mainHeaderCart div.cart-detail");
			var $mainHeaderMainNavInkBar = $("#mainHeaderMainNav div.ink-bar");
			var $mainHeaderMainNavItem = $("#mainHeaderMainNav li");
			var $mainHeaderMainNavActiveItem = $("#mainHeaderMainNav li.active");
			var mainHeaderMemberInfoTemplate = _.template($("#mainHeaderMemberInfoTemplate").html());
			var mainHeaderCartDetailTemplate = _.template($("#mainHeaderCartDetailTemplate").html());
			
			// 顶部广告
			if (sessionStorage.getItem("mainHeaderTopAdHidden") == null) {
				$mainHeaderTopAd.show();
			}
			
			// 顶部广告
			$mainHeaderTopAdClose.click(function() {
				sessionStorage.setItem("mainHeaderTopAdHidden", "true");
				$mainHeaderTopAd.velocity("slideUp");
			});
			
			// 会员信息
			$mainHeaderMemberInfo.html(mainHeaderMemberInfoTemplate({
				currentUser: $.getCurrentUser()
			}));

			// 用户注销
			$mainHeaderMemberInfo.on("click", "a.logout", function() {
				$document.trigger("loggedOut.xiaoxiangshop.user", $.getCurrentUser());
			});
			
			// 搜索类型
			$searchType.click(function() {
				var $element = $(this);
				var searchType = $element.data("search-type");
				
				$element.closest("div.input-group").find("[data-toggle='dropdown'] span:not(.caret)").text($element.text());
				
				switch (searchType) {
					case "product":
						$mainHeaderProductSearchForm.attr("action", "${base}/product/search");
						break;
					case "store":
						$mainHeaderProductSearchForm.attr("action", "${base}/store/search");
						break;
				}
			});
			
			// 商品搜索
			$mainHeaderProductSearchForm.submit(function() {
				if ($.trim($mainHeaderProductSearchKeyword.val()) == "") {
					return false;
				}
			});

			// 购物车
			$mainHeaderCart.hover(function() {
				var loading = true;
				
				setTimeout(function() {
					if (loading) {
						$mainHeaderCartDetail.html('<div class="cart-loader"><span></span><span></span><span></span><span></span><span></span></div>');
					}
				}, 500);
				$.getCurrentCart().done(function(data) {
					loading = false;
					$mainHeaderCartDetail.html(mainHeaderCartDetailTemplate({
						cart: data
					}));
				});
			});
			
			// 购物车数量
			var currentCartQuantity = $.getCurrentCartQuantity();
			if (currentCartQuantity != null) {
				$mainHeaderCartQuantity.text(currentCartQuantity < 100 ? currentCartQuantity : "99+");
			}
			
			// 购物车数量
			$document.on("complete.xiaoxiangshop.setCurrentCartQuantity", function(event, quantity) {
				$mainHeaderCartQuantity.text(quantity < 100 ? quantity : "99+");
			});
			
			// 主导航
			if ($mainHeaderMainNavItem.length > 0) {
				if ($mainHeaderMainNavActiveItem.length < 1) {
					$mainHeaderMainNavActiveItem = $mainHeaderMainNavItem.first();
				}
				
				$mainHeaderMainNavInkBar.css({
					width: $mainHeaderMainNavActiveItem.outerWidth(),
					display: "block",
					left: $mainHeaderMainNavActiveItem.position().left
				});
				
				$mainHeaderMainNavItem.hover(function() {
					var $element = $(this);
					
					$mainHeaderMainNavInkBar.css({
						width: $element.outerWidth(),
						left: $element.position().left
					});
				}, function() {
					$mainHeaderMainNavInkBar.css({
						width: $mainHeaderMainNavActiveItem.outerWidth(),
						left: $mainHeaderMainNavActiveItem.position().left
					});
				});
			}
			
		});
		</script>


	<script>

		$().ready(function() {

			var old = $.getCurrentUser().username;
			var name = '${currentUser.username}';
			var tel = '${currentUser.mobile}';
			var email = '${currentUser.email}';

			var f = true;

			if((name!=''||tel!=''||email!='')&&old!=''){

				old=old.toUpperCase();

				if(name!=''&& old != '' ) {
					if (old == name.toUpperCase()) {
						f = false;
					}
				}

				if(tel!=''&& old != '' ) {
					if (old == tel.toUpperCase()) {
						f = false;
					}
				}

				if(email!=''&& old != '' ) {
					if (old == email.toUpperCase()) {
						f = false;
					}
				}

				if (f) {
					$.get("${base}/member/logout", function () {
						$.removeCurrentUser();
					});
				}
			}

		});
	</script>
	[/#escape]
[/#noautoesc]
<header class="main-header">
	[@ad_position id = 1]
		[#if adPosition??]
			[#noautoesc]${adPosition.resolveTemplate()}[/#noautoesc]
		[/#if]
	[/@ad_position]
	<div class="top-nav">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<div id="mainHeaderMemberInfo" class="pull-left"></div>

					<input type="hidden" value="">
					<input type="hidden" value="">
					<input type="hidden" value="">

					<ul class="list-inline pull-right">
						[@navigation_list navigationGroupId = 1]
							[#if navigations?has_content]
								[#list navigations as navigation]
									<li>
										<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
									</li>
								[/#list]
							[/#if]
						[/@navigation_list]
			[#--			<li class="top-nav-dropdown">
							<a href="javascript:;">
								${message("shop.mainHeader.business")}
								<span class="caret"></span>
							</a>
							<ul class="business">
								<li>
									<a href="${base}/business/index" target="_blank">${message("shop.mainHeader.businessIndex")}</a>
								</li>
								<li>
									<a href="${base}/business/register">${message("shop.mainHeader.businessRegister")}</a>
								</li>
							</ul>
						</li>--]
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-xs-3">
				<a href="${base}/">
					<img class="logo" src="${setting.logo}" alt="${setting.siteName}">
				</a>
			</div>
			<div class="col-xs-6">
				<div id="mainHeaderProductSearch" class="product-search clearfix">
					<form action="[#if searchType == "STORE"]${base}/store/search[#else]${base}/product/search[/#if]" method="get">
						<div class="input-group">
							<div class="input-group-btn search-type">
								<button class="btn btn-default" type="button" data-toggle="dropdown">
									[#if searchType == "STORE"]
										<span>${message("shop.mainHeader.store")}</span>
									[#else]
										<span>${message("shop.mainHeader.product")}</span>
									[/#if]
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li data-search-type="product">
										<a href="javascript:;">${message("shop.mainHeader.product")}</a>
									</li>
[#--									<li data-search-type="store">--]
[#--										<a href="javascript:;">${message("shop.mainHeader.store")}</a>--]
[#--									</li>--]
								</ul>
							</div>
							<input name="keyword" class="form-control" type="text"[#if !store??] value="[#if searchType == "STORE"]${storeKeyword}[#else]${productKeyword}[/#if]"[/#if] placeholder="${message("shop.mainHeader.productSearchKeywordPlaceholder")}" autocomplete="off" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search">
							<input name="isOutOfStock" id="isOutOfStock" value="false" type="hidden">

							<div class="input-group-btn">
								<button class="btn btn-default search-btn" type="submit">
									<i class="iconfont icon-search"></i>
									${message("shop.mainHeader.productSearchSubmit")}
								</button>
							</div>
						</div>
					</form>
					[#if setting.hotSearches?has_content]
						<dl>
							<dt>${message("shop.mainHeader.productSearchHotSearch")}:</dt>
							[#list setting.hotSearches as hotSearch]
								<dd>
									<a href="${base}/product/search?keyword=${hotSearch?url}">${hotSearch}</a>
								</dd>
							[/#list]
						</dl>
					[/#if]
				</div>
			</div>
			<div class="col-xs-3">
				<div id="mainHeaderCart" class="cart">
					<i class="iconfont icon-cart"></i>
					<a href="${base}/cart/list">${message("shop.mainHeader.cart")}</a>
					<em></em>
					<div class="cart-detail"></div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-2">
				<a class="product-category" href="${base}/product_category">
					<i class="iconfont icon-sort"></i>
					${message("shop.mainHeader.productCategory")}
				</a>
			</div>
			<div class="col-xs-10">
				<div id="mainHeaderMainNav" class="main-nav">
					<div class="ink-bar"></div>
					<ul>
						[@navigation_list navigationGroupId = 2]
							[#if navigations?has_content]
								[#list navigations as navigation]
									<li[#if navigation.url == requestContext.requestUri] class="active"[/#if]>
										<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
									</li>
								[/#list]
							[/#if]
						[/@navigation_list]
					</ul>
				</div>
			</div>
		</div>
	</div>
</header>
<header class="main-header">
	<div class="title hidden-xs">
		<a href="${base}/business/index">${currentStore.name}</a>
	</div>
	<button class="main-sidebar-toggle" type="button" data-toggle="mainSidebarCollapse">
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
		<span class="icon-bar"></span>
	</button>
	<ul class="nav pull-right">
		<li>
			<a href="${base}/business/store/setting">${currentUser.username}</a>
		</li>
		<li class="dropdown">
			<a class="dropdown-toggle" href="javascript:;" data-toggle="dropdown">
				<i class="iconfont icon-notice"></i>
				[@order_count storeId = currentStore.id status = "PENDING_PAYMENT" hasExpired = false]
					[#assign pendingPaymentOrderCount = count /]
				[/@order_count]
				[@order_count storeId = currentStore.id status = "PENDING_REVIEW" hasExpired = false]
					[#assign pendingReviewOrderCount = count /]
				[/@order_count]
				[@order_count storeId = currentStore.id status = "PENDING_SHIPMENT"]
					[#assign pendingShipmentOrderCount = count /]
				[/@order_count]
				[@order_count storeId = currentStore.id isPendingRefunds = true]
					[#assign pendingRefundsOrderCount = count /]
				[/@order_count]
				[#if pendingPaymentOrderCount + pendingReviewOrderCount + pendingShipmentOrderCount + pendingRefundsOrderCount > 0]
					<span class="circle"></span>
				[/#if]
			</a>
			<ul class="dropdown-menu dropdown-menu-right">
				<li>
					<a href="${base}/business/order/list?status=PENDING_PAYMENT&hasExpired=false">${message("business.mainHeader.pendingPaymentOrderCount", pendingPaymentOrderCount)}</a>
				</li>
				<li>
					<a href="${base}/business/order/list?status=PENDING_REVIEW&hasExpired=false">${message("business.mainHeader.pendingReviewOrderCount", pendingReviewOrderCount)}</a>
				</li>
				<li>
					<a href="${base}/business/order/list?status=PENDING_SHIPMENT">${message("business.mainHeader.pendingShipmentOrderCount", pendingShipmentOrderCount)}</a>
				</li>
				<li>
					<a href="${base}/business/order/list?isPendingRefunds=true">${message("business.mainHeader.pendingRefundsOrderCount", pendingRefundsOrderCount)}</a>
				</li>
			</ul>
		</li>
		<li>
			<a id="mainHeaderLogout" class="logout" href="${base}/business/logout">
				<i class="iconfont icon-exit"></i>
				${message("business.mainHeader.logout")}
			</a>
		</li>
	</ul>
</header>
[#noautoesc]
	[#escape x as x?js_string]
		<script>
		$().ready(function() {
			
			var $document = $(document);
			var $mainHeaderLogout = $("#mainHeaderLogout");
			
			// 用户注销
			$mainHeaderLogout.click(function() {
				$document.trigger("loggedOut.xiaoxiangshop.user", $.getCurrentUser());
			});
			
		});
		</script>
	[/#escape]
[/#noautoesc]
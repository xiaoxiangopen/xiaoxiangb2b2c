<footer class="main-footer">
	<div class="container">
		<div class="promise">

		</div>
		<div class="row">
			<div class="col-xs-4">
				<ul class="contact">
					<li>
						<i class="iconfont icon-phone"></i>
						<strong>${setting.phone}</strong>
					</li>
					<li>
						<i class="iconfont icon-mark"></i>
						${setting.email}
					</li>
					<li>
						<i class="iconfont icon-location"></i>
						${setting.address}
					</li>
				</ul>
			</div>
			<div class="col-xs-2">
				<dl class="help">
					<dt>购物指南</dt>
					<dd>
						<a href="${base}/article/detail/16_1">注册会员</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312279160680472578_1">使用VIP卡的办法</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312282953459556354_1">如何获得发票</a>
					</dd>
				</dl>
			</div>
			<div class="col-xs-2">
				<dl class="help">
					<dt>支付配送</dt>
					<dd>
						<a href="${base}/article/detail/1312636793887158273_1">支付方式</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312281802911637505_1">大家电配送服务</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312637465302953985_1">交易条款及服务协议</a>
					</dd>
				</dl>
			</div>
			<div class="col-xs-2">
				<dl class="help">
					<dt>购物保障</dt>
					<dd>
						<a href="${base}/article/detail/1312637672237330433_1">商品销售和售后服务</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312638051519852546_1">退换货政策细则及流程</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312281091347963905_1">证件公示</a>
					</dd>
				</dl>
			</div>
			<div class="col-xs-2">
				<dl class="help">
					<dt>常见问题</dt>
					<dd>
						<a href="${base}/article/detail/17_1">申领VIP卡</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312274983929634818_1">积分卡线上充值教程</a>
					</dd>
					<dd>
						<a href="${base}/article/detail/1312639740087922690_1">购物优惠及消费积分</a>
					</dd>
				</dl>
			</div>
		</div>
	</div>
	<div class="bottom-nav">
		[@navigation_list navigationGroupId = 3]
			[#if navigations?has_content]
				<ul class="clearfix">
					[#list navigations as navigation]
						<li>
							<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
							[#if navigation_has_next]|[/#if]
						</li>
					[/#list]
				</ul>
			[/#if]
		[/@navigation_list]
		<p>Copyright &copy; 2002-2020 小象电商 版权所有  粤ICP备：B2-00000000    <script src='https://s9.cnzz.com/stat.php?id=1111&web_id=1&show=pic1' language='JavaScript'></script></p>
	</div>
</footer>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="author" content="小象SHOP Team" />
	<meta name="copyright" content="小象SHOP" />
	<title>${message("common.error.unsupportedBrowser")}[#if showPowered] - 小象电商[/#if]</title>
	<link href="${base}/resources/common/css/base.css" rel="stylesheet" type="text/css" />
	<link href="${base}/resources/common/css/error.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/jquery.qrcode.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script type="text/javascript">
				$().ready(function() {
					
					var $qrcode = $("#qrcode");
					
					// 二维码
					$qrcode.qrcode({
						width: 100,
						height: 100,
						render: "table",
						text: window.top != window.self ? window.top.location.href : "${setting.siteUrl}"
					});
					
				});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body>
	<div class="unsupported-browser-backdrop"></div>
	<div class="unsupported-browser">
		[#noautoesc]
			<p>${message("common.error.unsupportedBrowserMessage")}</p>
		[/#noautoesc]
		<div id="qrcode" class="qrcode"></div>
		<p>
			<a href="https://support.microsoft.com/zh-cn/help/17621/internet-explorer-downloads" target="_blank">${message("common.error.unsupportedBrowserIeDownload")}</a>
			<a href="http://www.firefox.com.cn" target="_blank">${message("common.error.unsupportedBrowserFirefoxDownload")}</a>
		</p>
	</div>
</body>
</html>
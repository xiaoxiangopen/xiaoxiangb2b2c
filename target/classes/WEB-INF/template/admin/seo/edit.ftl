<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.seo.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
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
	<script id="tagTemplate" type="text/template">
		<div class="tag hidden-element form-group">
			<div class="col-xs-8 col-xs-offset-2">
				[#switch seo.type]
					[#case "ARTICLE_LIST"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${articleCategory.name}[/#noparse]">${message("admin.seo.articleCategoryName")}</button>
						[#break /]
					[#case "ARTICLE_SEARCH"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${articleKeyword}[/#noparse]">${message("admin.seo.articleKeyword")}</button>
						[#break /]
					[#case "ARTICLE_DETAIL"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${article.title}[/#noparse]">${message("admin.seo.articleTitle")}</button>
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${article.author}[/#noparse]">${message("admin.seo.articleAuthor")}</button>
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${article.pageNumber}[/#noparse]">${message("admin.seo.articlePageNumber")}</button>
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${article.articleCategory.name}[/#noparse]">${message("admin.seo.articleArticleCategoryName")}</button>
						[#break /]
					[#case "PRODUCTS"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${productCategory.name}[/#noparse]">${message("admin.seo.productCategoryName")}</button>
						[#break /]
					[#case "PRODUCT_SEARCH"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${productKeyword}[/#noparse]">${message("admin.seo.productKeyword")}</button>
						[#break /]
					[#case "PRODUCT_DETAIL"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${product.sn}[/#noparse]">${message("admin.seo.productSn")}</button>
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${product.name}[/#noparse]">${message("admin.seo.productName")}</button>
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${product.productCategory.name}[/#noparse]">${message("admin.seo.productProductCategoryName")}</button>
						[#break /]
					[#case "BRAND_CONTENT"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${brand.name}[/#noparse]">${message("admin.seo.brandName")}</button>
						[#break /]
					[#case "STORE_INDEX"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${store.name}[/#noparse]">${message("admin.seo.storeName")}</button>
						[#break /]
					[#case "STORE_SEARCH"]
						<button class="btn btn-default" type="button" data-tag-value="[#noparse]${storeKeyword}[/#noparse]">${message("admin.seo.storeKeyword")}</button>
						[#break /]
				[/#switch]
				<button class="btn btn-default" type="button" data-tag-value="[#noparse]${setting.siteName}[/#noparse]">${message("admin.seo.settingSiteName")}</button>
				<button class="btn btn-default" type="button" data-tag-value="[#noparse]${setting.siteUrl}[/#noparse]">${message("admin.seo.settingSiteUrl")}</button>
				<button class="btn btn-default" type="button" data-tag-value="[#noparse]${setting.address}[/#noparse]">${message("admin.seo.settingAddress")}</button>
				<button class="btn btn-default" type="button" data-tag-value="[#noparse]${setting.phone}[/#noparse]">${message("admin.seo.settingPhone")}</button>
				<button class="btn btn-default" type="button" data-tag-value="[#noparse]${setting.zipCode}[/#noparse]">${message("admin.seo.settingZipCode")}</button>
				<button class="btn btn-default" type="button" data-tag-value="[#noparse]${setting.email}[/#noparse]">${message("admin.seo.settingEmail")}</button>
			</div>
		</div>
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $seoForm = $("#seoForm");
				var $title = $("#title");
				var $keywords = $("#keywords");
				var $description = $("#description");
				var tagTemplate = _.template($("#tagTemplate").html());
				
				$title.add($keywords).add($description).closest("div.form-group").after(tagTemplate());
				
				$title.add($keywords).add($description).click(function() {
					var $element = $(this);
					var $tag = $element.closest("div.form-group").next("div.tag");
					
					if ($tag.is(":hidden")) {
						$tag.velocity("slideDown");
					}
					$tag.siblings("div.tag:visible").velocity("slideUp");
				});
				
				$seoForm.find(".tag button").click(function() {
					var $element = $(this);
					var $input = $element.closest("div.form-group").prev().find("input:text");
					
					$input.val($input.val() + $element.data("tag-value"));
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
				<li class="active">${message("admin.seo.edit")}</li>
			</ol>
			<form id="seoForm" class="ajax-form form-horizontal" action="${base}/admin/seo/update" method="post">
				<input name="id" type="hidden" value="${seo.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.seo.edit")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("Seo.type")}:</label>
							<div class="col-xs-9 col-sm-4">
								<p class="form-control-static">${message("Seo.Type." + seo.type)}</p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="title">${message("Seo.title")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="title" name="title" class="form-control" type="text" value="${seo.title}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="keywords">${message("Seo.keywords")}:</label>
							<div class="col-xs-9 col-sm-4" title="${message("admin.seo.keywordsTitle")}" data-toggle="tooltip">
								<input id="keywords" name="keywords" class="form-control" type="text" value="${seo.keywords}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="description">${message("Seo.description")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="description" name="description" class="form-control" type="text" value="${seo.description}" maxlength="200">
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
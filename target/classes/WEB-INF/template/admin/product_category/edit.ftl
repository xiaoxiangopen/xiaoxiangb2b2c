<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.productCategory.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/ajax-bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-fileinput.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/ajax-bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/underscore.js"></script>
	<script src="${base}/resources/common/js/url.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $productCategoryForm = $("#productCategoryForm");
				

				
				// 表单验证
				$productCategoryForm.validate({
					rules: {
						name: "required",
						generalRate: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 3,
								fraction: ${setting.priceScale}
							}
						},
						selfRate: {
							required: true,
							number: true,
							min: 0,
							decimal: {
								integer: 3,
								fraction: ${setting.priceScale}
							}
						},
						order: "digits"
					}
					,
					rules: {
						name: "required",
						deductStock: "digits"
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
				<li class="active">${message("admin.productCategory.edit")}</li>
			</ol>
			<form id="productCategoryForm" class="ajax-form form-horizontal" action="${base}/admin/product_category/update" method="post">
				<input name="id" type="hidden" value="${productCategory.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.productCategory.edit")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("ProductCategory.name")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="name" name="name" class="form-control" type="text" value="${productCategory.name}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("ProductCategory.parent")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select name="parentId" class="selectpicker form-control" data-live-search="true" data-size="10">
									<option value="">${message("admin.productCategory.root")}</option>
									[#list productCategoryTree as category]
										[#if category != productCategory && !children?seq_contains(category)]
											<option value="${category.id}" title="${category.name}"[#if category == productCategory.parent] selected[/#if]>
												[#if category.grade != 0]
													[#list 1..category.grade as i]
														&nbsp;&nbsp;
													[/#list]
												[/#if]
												${category.name}
											</option>
										[/#if]
									[/#list]
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="generalRate">${message("ProductCategory.generalRate")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="input-group">
									<input id="generalRate" name="generalRate" class="form-control" type="text" value="${productCategory.generalRate}" maxlength="9">
									<span class="input-group-addon">%</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="selfRate">${message("ProductCategory.selfRate")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="input-group">
									<input id="selfRate" name="selfRate" class="form-control" type="text" value="${productCategory.selfRate}" maxlength="9">
									<span class="input-group-addon">%</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("ProductCategory.brands")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select name="brandIds" class="selectpicker form-control" title="${message("common.choose")}" data-live-search="true" data-size="10" multiple>
									[#list brands as brand]
										<option value="${brand.id}"[#if productCategory.brands?seq_contains(brand)] selected[/#if]>${brand.name}</option>
									[/#list]
								</select>
							</div>
						</div>


						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="deductStock">库存核减数:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="deductStock" name="deductStock" class="form-control" type="text" maxlength="9" value="${productCategory.deductStock}">
							</div>
						</div>


						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="categoryType">会员等级分类:</label>
							<div class="col-xs-9 col-sm-4">

								<select name="categoryType" class="selectpicker form-control" data-live-search="true" data-size="10">
									[#list dictList as dict]
										<option value="${dict.dictId}" title="${dict.dictValue}" [#if dict.dictId == productCategory.categoryType] selected[/#if]>
											${dict.dictValue}
										</option>
									[/#list]
								</select>
							</div>
						</div>


						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="seoTitle">${message("ProductCategory.seoTitle")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="seoTitle" name="seoTitle" class="form-control" type="text" value="${productCategory.seoTitle}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="seoKeywords">${message("ProductCategory.seoKeywords")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="seoKeywords" name="seoKeywords" class="form-control" type="text" value="${productCategory.seoKeywords}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="seoDescription">${message("ProductCategory.seoDescription")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="seoDescription" name="seoDescription" class="form-control" type="text" value="${productCategory.seoDescription}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="order">${message("common.order")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="order" name="order" class="form-control" type="text" value="${productCategory.order}" maxlength="9">
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">图片:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="imgUrl" name="imgUrl" type="hidden" value="${productCategory.imgUrl}"  data-provide="fileinput" data-file-type="IMAGE">
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label" for="isEffective">是否生效:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="checkbox checkbox-inline">
									<input id="effective" name="effective" type="checkbox" value="true" [#if productCategory.effective] checked [/#if]>
								</div>

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
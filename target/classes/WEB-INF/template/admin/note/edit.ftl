<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="小象SHOP Team">
	<meta name="copyright" content="小象SHOP">
	<title>${message("admin.article.edit")} - 小象电商</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/summernote.css" rel="stylesheet">
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
	<script src="${base}/resources/common/js/summernote.js"></script>
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
			
				var $articleForm = $("#articleForm");
				
				// 表单验证
				$articleForm.validate({
					rules: {
						title: "required",
						articleCategoryId: "required"
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
				<li class="active">${message("admin.article.edit")}</li>
			</ol>
			<form id="articleForm" class="ajax-form form-horizontal" action="${base}/admin/note/update" method="post">
				<input name="id" type="hidden" value="${article.id}">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.article.edit")}</div>
					<div class="panel-body">
						<div class="form-group"  >
							<label class="col-xs-3 col-sm-2 control-label item-required" for="title">${message("Article.title")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="title" name="title" class="form-control" type="text" value="${article.title}" maxlength="200">
							</div>
						</div>
						<div class="form-group" style="visibility: hidden">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("Article.articleCategory")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select name="articleCategoryId" class="selectpicker form-control" data-size="10">
									[#list articleCategoryTree as articleCategory]
										<option value="${articleCategory.id}" title="${articleCategory.name}"[#if articleCategory.id == article.articleCategory.id] selected[/#if]>
											[#if articleCategory.grade != 0]
												[#list 1..articleCategory.grade as i]
													&nbsp;&nbsp;
												[/#list]
											[/#if]
											${articleCategory.name}
										</option>
									[/#list]
								</select>
							</div>
						</div>
						<div class="form-group" style="visibility: hidden">
							<label class="col-xs-3 col-sm-2 control-label" for="author">${message("Article.author")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="author" name="author" class="form-control" type="text" value="${article.author}" maxlength="200">
							</div>
						</div>
						<div class="form-group" style="visibility: hidden">
							<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
							<div class="col-xs-9 col-sm-4">
								<div class="checkbox checkbox-inline">
									<input name="_isPublication" type="hidden" value="false">
									<input id="isPublication" name="isPublication" type="checkbox" value="true"[#if article.isPublication] checked[/#if]>
									<label for="isPublication">${message("Article.isPublication")}</label>
								</div>
								<div class="checkbox checkbox-inline">
									<input name="_isTop" type="hidden" value="false">
									<input id="isTop" name="isTop" type="checkbox" value="true"[#if article.isTop] checked[/#if]>
									<label for="isTop">${message("Article.isTop")}</label>
								</div>
							</div>
						</div>
						<div class="form-group" style="visibility: hidden">
							<label class="col-xs-3 col-sm-2 control-label">${message("Article.articleTags")}:</label>
							<div class="col-xs-9 col-sm-10">
								[#list articleTags as articleTag]
									<div class="checkbox checkbox-inline">
										<input id="articleTag_${articleTag.id}" name="articleTagIds" type="checkbox" value="1">
										<label for="articleTag_${articleTag.id}">${articleTag.name}</label>
									</div>
								[/#list]
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label">${message("Article.content")}:</label>
							<div class="col-xs-9 col-sm-10">
								<textarea name="content" class="form-control" data-provide="editor">${article.content}</textarea>
							</div>
						</div>
						<div class="form-group" style="visibility: hidden">
							<label class="col-xs-3 col-sm-2 control-label" for="seoTitle">${message("Article.seoTitle")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="seoTitle" name="seoTitle" class="form-control" type="text" value="${article.seoTitle}" maxlength="200">
							</div>
						</div>
						<div class="form-group" style="visibility: hidden">
							<label class="col-xs-3 col-sm-2 control-label" for="seoKeywords">${message("Article.seoKeywords")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="seoKeywords" name="seoKeywords" class="form-control" type="text" value="${article.seoKeywords}" maxlength="200">
							</div>
						</div>
						<div class="form-group" style="visibility: hidden">
							<label class="col-xs-3 col-sm-2 control-label" for="seoDescription">${message("Article.seoDescription")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="seoDescription" name="seoDescription" class="form-control" type="text" value="${article.seoDescription}" maxlength="200">
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
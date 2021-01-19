$().ready(function() {

	var $pageSize = $("input[name='pageSize']");
	var $searchProperty = $("input[name='searchProperty']");
	var $orderProperty = $("input[name='orderProperty']");
	var $orderDirection = $("input[name='orderDirection']");
	var $button = $(".btn");
	var $tooltipToggle = $("[data-toggle='tooltip']");
	var $deleteAction = $("[data-action='delete']");
	var $filterPropertyItem = $("[data-filter-property]");
	var $pageSizeItem = $("[data-page-size]");
	var $searchPropertyItem = $("[data-search-property]");
	var $searchValue = $("#search input[name='searchValue']");
	var $searchSubmit = $("#search :submit");
	var $checkAllToggle = $("[data-toggle='checkAll']");
	var $ids = $("input[name='ids']");
	var $orderPropertyItem = $("[data-order-property]");
	var $pageNumberItem = $("[data-page-number]");

	// 重定向登录页面
	if ($.redirectLogin != null) {
		$.extend($.redirectLogin.defaults, {
			loginType: "business"
		});
	}

	// 按钮
	$button.click(function() {
		var $element = $(this);

		if ($.support.transition) {
			$element.addClass("btn-clicked").one("bsTransitionEnd", function() {
				$(this).removeClass("btn-clicked");
			}).emulateTransitionEnd(300);
		}
	});

	// 提示
	$tooltipToggle.tooltip();

	// 删除
	$deleteAction.delete({
		url: "delete",
		data: function() {
			return $("input[name='ids']").serialize();
		}
	}).on("success.xiaoxiangshop.delete", function(event) {
		var $element = $(event.target);

		if ($.fn.velocity == null) {
			throw new Error("Delete requires velocity.js");
		}

		$("input[name='ids']:checked").closest("tr").velocity("fadeOut", {
			complete: function() {
				$(this).remove();
				$element.attr("disabled", true);
				$checkAllToggle.checkAll("uncheck");
				if ($("input[name='ids']").length < 1) {
					location.reload(true);
				}
			}
		});
	});

	// 筛选
	$filterPropertyItem.click(function() {
		var $element = $(this);
		var filterProperty = $element.data("filter-property");
		var filterValue = $element.data("filter-value");

		$("input[name='" + filterProperty + "']").val($element.hasClass("active") ? "" : filterValue).closest("form").submit();
	});

	// 每页显示
	$pageSizeItem.click(function() {
		var $element = $(this);

		$pageSize.val($element.data("page-size")).closest("form").submit();
	});

	// 搜索属性
	$searchPropertyItem.click(function() {
		var $element = $(this);

		$element.addClass("active").siblings().removeClass("active");
		$element.closest("div.input-group").find("[data-toggle='dropdown'] span:not(.caret)").text($element.text());
	});

	// 搜索值
	$searchValue.keypress(function(event) {
		if (event.which == 13) {
			$searchSubmit.click();
			return false;
		}
	});

	// 搜索提交
	$searchSubmit.click(function() {
		$searchProperty.val($searchPropertyItem.filter(".active").data("search-property"));
	});

	// ID多选框
	$ids.change(function() {
		$deleteAction.attr("disabled", $("input[name='ids']:checked").length < 1);
	});

	// 排序
	$("[data-order-property='" + $orderProperty.val() + "'] .iconfont").removeClass("icon-biaotou-kepaixu").addClass($orderDirection.val() == "ASC" ? "icon-biaotou-zhengxu" : "icon-biaotou-daoxu");
	$orderPropertyItem.click(function() {
		var $element = $(this);

		$orderProperty.val($element.data("order-property"));
		$orderDirection.val($orderDirection.val() == "ASC" ? "DESC" : "ASC");
		$orderProperty.closest("form").submit();
		return false;
	});

	// 页码
	$pageNumberItem.click(function() {
		var $element = $(this);
		var $form = $element.closest("form");
		var $pageNumber = $form.find("input[name='pageNumber']");
		var pageNumber = $element.data("page-number");

		if ($pageNumber.length > 0) {
			$pageNumber.val(pageNumber);
		} else {
			$form.append('<input name="pageNumber" type="hidden" value="' + pageNumber + '">');
		}
		$form.submit();
		return false;
	});

});
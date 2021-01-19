/*
 * C o p y r i g h t  2 0 0 5 - 2 0 1 6  s h o p x x . n e t .  A l l  r i g h t s  r e s e r v e d .
 * S u p p o r t :  h t t p : / / w w w . s h o p x x . n e t
 * L i c e n s e :  h t t p : / / w w w . s h o p x x . n e t / l i c e n s e
 */

(function($) {
	$.fn.extend({
		lSelect: function(options) {
			var settings = {
				choose: "请选择...",
				emptyValue: "",
				cssStyle: {
					"height": "31px",
					"margin": "3px 6px 3px 0px",
					"padding": "6px 7px",
					"font-size": "12px",
					"color": "#555",
					"border-radius": "4px",
					"border-color": "#d8d8d8",
					"background-color": "#ffffff"
				},
				url: null,
				type: "GET"
			};
			$.extend(settings, options);
			
			var cache = {};
			return this.each(function() {
				var lSelectId = new Date().getTime();
				var $input = $(this);
				var treePath = $input.attr("treePath");
				
				if (treePath != null && treePath != "") {
					var ids = (treePath + $input.val() + ",").split(",");
					var $position = $input;
					for (var i = 1; i < ids.length; i ++) {
						$position = addSelect($position, ids[i - 1], ids[i]);
					}
				} else {
					addSelect($input, null, null);
				}
				
				function addSelect($position, parentId, currentId) {
					$position.nextAll("select[name=" + lSelectId + "]").remove();
					if ($position.is("select") && (parentId == null || parentId == "")) {
						return false;
					}
					if (cache[parentId] == null) {
						$.ajax({
							url: settings.url,
							type: settings.type,
							data: parentId != null ? {parentId: parentId} : null,
							dataType: "json",
							cache: false,
							async: false,
							success: function(data) {
								cache[parentId] = data;
							}
						});
					}
					var data = cache[parentId];
					if ($.isEmptyObject(data)) {
						return false;
					}
					var select = '<select class="selectArea" name="' + lSelectId + '">';
					if (settings.emptyValue != null && settings.choose != null) {
						select += '<option value="' + settings.emptyValue + '">' + settings.choose + '</option>';
					}
					$.each(data, function(i, option) {
						if(option.value == currentId) {
							select += '<option value="' + option.value + '" selected="selected">' + option.name + '</option>';
						} else {
							select += '<option value="' + option.value + '">' + option.name + '</option>';
						}
					});
					select += '</select>';
					return $(select).css(settings.cssStyle).insertAfter($position).on("change", function() {
						var $this = $(this);
						if ($this.val() == "") {
							var $prev = $this.prev("select[name=" + lSelectId + "]");
							if ($prev.length > 0) {
								$input.val($prev.val());
							} else {
								$input.val(settings.emptyValue);
							}
						} else {
							$input.val($this.val());
						}
						addSelect($this, $this.val(), null);
					});
				}
			});
			
		}
	});
})(jQuery);
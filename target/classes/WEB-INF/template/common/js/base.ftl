[#noautoesc]
	[#escape x as x?js_string]
		// 验证码图片
		+function($) {
			"use strict";
			
			var CaptchaImage = function(element, options) {
				this.options = options;
				this.$element = $(element);
				this.$captchaId = null;
				this.captchaId = uuid();
				
				this.init();
			};
			
			CaptchaImage.prototype.init = function() {
				this.refresh();
				this.$captchaId = $('<input name="captchaId" type="hidden" value="' + this.captchaId + '">').insertAfter(this.$element);
				this.$element.on("click.xiaoxiangshop.captchaImage", $.proxy(this.refresh, this));
			};
			
			CaptchaImage.prototype.generateSrc = function() {
				return "${base}/common/captcha/image?captchaId=" + this.captchaId + "&timestamp=" + new Date().getTime();
			};
			
			CaptchaImage.prototype.refresh = function() {
				var refreshEvent = $.Event("refresh.xiaoxiangshop.captchaImage");
				
				this.$element.trigger(refreshEvent);
				if (refreshEvent.isDefaultPrevented()) {
					return;
				}
				
				this.$element.attr("src", this.generateSrc());
				this.$element.trigger("refreshed.xiaoxiangshop.captchaImage");
			};
			
			function uuid() {
				var uuidChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");
				var r;
				var uuid = [];
				uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
				uuid[14] = "4";
				
				for (var i = 0; i < 36; i++) {
					if (!uuid[i]) {
						r = 0 | Math.random() * 16;
						uuid[i] = uuidChars[(i == 19) ? (r & 0x3) | 0x8 : r];
					}
				}
				return uuid.join("");
			}
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.captchaImage");
					var options = $.extend({}, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.captchaImage", (data = new CaptchaImage(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.captchaImage;
			
			$.fn.captchaImage = Plugin;
			$.fn.captchaImage.Constructor = CaptchaImage;
			
			$.fn.captchaImage.noConflict = function() {
				$.fn.captchaImage = old;
				return this;
			};
			
			$(window).on("load", function() {
				$("[data-toggle='captchaImage']").each(function() {
					var $this = $(this);
					
					Plugin.call($this);
				});
			});
		
		}(jQuery);
		
		// 滚动加载
		+function($) {
			"use strict";
			
			var ScrollLoad = function(element, options) {
				this.options = options;
				this.$element = $(element);
				this.scrollload = null;
				this.pageNumber = 1;
				
				this.init();
			};
			
			ScrollLoad.DEFAULTS = {
				url: null,
				type: "GET",
				data: null,
				dataType: "json",
				contentTarget: "#scrollLoadContent",
				templateTarget: "#scrollLoadTemplate"
			};
			
			ScrollLoad.prototype.init = function() {
				var contentTarget = typeof this.options.contentTarget === "function" ? this.options.contentTarget(this.$element) : this.options.contentTarget;
				var $contentTarget = contentTarget instanceof jQuery ? contentTarget : $(contentTarget);
				
				if (typeof Scrollload === "undefined") {
					throw new Error("ScrollLoad requires scrollload.js");
				}
				
				this.scrollload = new Scrollload({
					container: this.$element[0],
					content: $contentTarget[0],
					loadMore: $.proxy(this.load, this)
				});
			};
			
			ScrollLoad.prototype.load = function() {
				var that = this;
				var url = typeof this.options.url === "function" ? this.options.url(this.$element) : this.options.url;
				var type = typeof this.options.type === "function" ? this.options.type(this.$element) : this.options.type;
				var data = typeof this.options.data === "function" ? this.options.data(this.$element) : this.options.data;
				var dataType = typeof this.options.dataType === "function" ? this.options.dataType(this.$element) : this.options.dataType;
				var templateTarget = typeof this.options.templateTarget === "function" ? this.options.templateTarget(this.$element) : this.options.templateTarget;
				var $templateTarget = templateTarget instanceof jQuery ? templateTarget : $(templateTarget);
				var loadEvent = $.Event("load.xiaoxiangshop.scrollLoad");
				
				if (typeof _ === "undefined") {
					throw new Error("ScrollLoad requires underscore.js");
				}
				
				this.$element.trigger(loadEvent);
				if (loadEvent.isDefaultPrevented()) {
					return;
				}
				
				$.ajax({
					url: _.template(url)({
						pageNumber: this.pageNumber++
					}),
					type: type,
					data: data,
					dataType: dataType
				}).done(function(data) {
					if (data != null && data.length > 0) {
						var template = _.template($templateTarget.html());
						
						$(that.scrollload.contentDom).append(template({
							data: data
						}));
						that.scrollload.unLock();
					} else {
						that.scrollload.noMoreData();
					}
					that.$element.trigger("loaded.xiaoxiangshop.scrollLoad", arguments);
				}).fail(function() {
					that.scrollload.throwException();
				});
			};
			
			ScrollLoad.prototype.refresh = function() {
				this.pageNumber = 1;
				$(this.scrollload.contentDom).empty();
				this.scrollload.refreshData();
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.scrollLoad");
					var options = $.extend({}, ScrollLoad.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.scrollLoad", (data = new ScrollLoad(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.scrollLoad;
			
			$.fn.scrollLoad = Plugin;
			$.fn.scrollLoad.Constructor = ScrollLoad;
			
			$.fn.scrollLoad.noConflict = function() {
				$.fn.scrollLoad = old;
				return this;
			};
			
			$(window).on("load", function() {
				$("[data-spy='scrollLoad']").each(function() {
					var $this = $(this);
					
					Plugin.call($this);
				});
			});
		
		}(jQuery);
		
		// 加入购物车
		+function($) {
			"use strict";
			
			var AddCart = function(element, options) {
				this.options = options;
				this.$element = $(element);
				this.$document = $(document);
				
				this.$element.on("click.xiaoxiangshop.addCart", $.proxy(this.execute, this));
			};
			
			AddCart.DEFAULTS = {
				skuId: null,
				quantity: 1,
				cartTarget: null,
				productImageTarget: null
			};
			
			AddCart.prototype.ajax = function() {
				var skuId = typeof this.options.skuId === "function" ? this.options.skuId(this.$element) : this.options.skuId;
				var quantity = typeof this.options.quantity === "function" ? this.options.quantity(this.$element) : this.options.quantity;
				
				return $.addCart(skuId, quantity);
			};
			
			AddCart.prototype.fly = function(callback) {
				var that = this;
				var cartTarget = typeof this.options.cartTarget === "function" ? this.options.cartTarget(this.$element) : this.options.cartTarget;
				var productImageTarget = typeof this.options.productImageTarget === "function" ? this.options.productImageTarget(this.$element) : this.options.productImageTarget;
				var $cartTarget = cartTarget instanceof jQuery ? cartTarget : $(cartTarget);
				var $productImageTarget = productImageTarget instanceof jQuery ? productImageTarget : $(productImageTarget);
				
				if ($.fn.velocity == null) {
					throw new Error("AddCart requires velocity.js");
				}
				if ($.fn.fly == null) {
					throw new Error("AddCart requires jquery.fly.js");
				}
				
				if ($cartTarget != null && $cartTarget.length > 0 && $productImageTarget != null && $productImageTarget.length > 0) {
					$productImageTarget.clone().css({
						width: 50,
						height: 50,
						position: "absolute",
						top: $productImageTarget.offset().top + $productImageTarget.outerHeight() / 2 - 25,
						left: $productImageTarget.offset().left + $productImageTarget.outerWidth() / 2 - 25,
						"z-index": 300,
						opacity: 0,
						"border-radius": "50%"
					}).appendTo("body").velocity({
						opacity: 0.6
					}, {
						complete: function() {
							var $element = $(this);
							
							$element.fly({
								start: {
									top: $element.offset().top - that.$document.scrollTop(),
									left: $element.offset().left
								},
								end: {
									width: 10,
									height: 10,
									top: $cartTarget.offset().top - that.$document.scrollTop() + $cartTarget.outerHeight() / 2 - 5,
									left: $cartTarget.offset().left + $cartTarget.outerWidth() / 2 - 5
								},
								onEnd: function() {
									this.destroy();
									callback.call(that);
								}
							});
						}
					});
				} else {
					callback.call(that);
				}
			};
			
			AddCart.prototype.execute = function() {
				var that = this;
				var beforeEvent = $.Event("before.xiaoxiangshop.addCart");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				this.ajax().done(function() {
					var ajaxArguments = arguments;
					
					that.fly(function() {
						that.$element.trigger("success.xiaoxiangshop.addCart", ajaxArguments);
						that.$element.trigger("complete.xiaoxiangshop.addCart", ajaxArguments);
					});
				}).fail(function() {
					that.$element.trigger("error.xiaoxiangshop.addCart", arguments);
					that.$element.trigger("complete.xiaoxiangshop.addCart", arguments);
				});
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.addCart");
					var options = $.extend({}, AddCart.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.addCart", (data = new AddCart(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.addCart;
			
			$.fn.addCart = Plugin;
			$.fn.addCart.Constructor = AddCart;
			
			$.fn.addCart.noConflict = function() {
				$.fn.addCart = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.addCart.data-api", "[data-action='addCart']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.addCart") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 修改购物车
		+function($) {
			"use strict";
			
			var ModifyCart = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				if ($.fn.spinner == null) {
					throw new Error("ModifyCart requires jquery.spinner.js");
				}
				
				this.$element.on("changing.spinner", $.proxy(this.changing, this));
			};
			
			ModifyCart.DEFAULTS = {
				skuId: null
			};
			
			ModifyCart.prototype.ajax = function(newValue) {
				var skuId = typeof this.options.skuId === "function" ? this.options.skuId(this.$element) : this.options.skuId;
				
				return $.modifyCart(skuId, newValue);
			};
			
			ModifyCart.prototype.changing = function(event, newValue, oldValue) {
				var that = this;
				var beforeEvent = $.Event("before.xiaoxiangshop.modifyCart");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				this.ajax(newValue).done(function() {
					that.$element.trigger("success.xiaoxiangshop.modifyCart", arguments);
				}).fail(function() {
					that.$element.val(oldValue);
					that.$element.trigger("error.xiaoxiangshop.modifyCart", arguments);
				}).always(function() {
					that.$element.trigger("complete.xiaoxiangshop.modifyCart", arguments);
				});
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.modifyCart");
					var options = $.extend({}, ModifyCart.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.modifyCart", (data = new ModifyCart(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.modifyCart;
			
			$.fn.modifyCart = Plugin;
			$.fn.modifyCart.Constructor = ModifyCart;
			
			$.fn.modifyCart.noConflict = function() {
				$.fn.modifyCart = old;
				return this;
			};
			
			$(window).on("load", function() {
				$("[data-action='modifyCart']").each(function() {
					var $this = $(this);
					
					Plugin.call($this);
				});
			});
			
		}(jQuery);
		
		// 移除购物车
		+function($) {
			"use strict";
			
			var RemoveCart = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.removeCart", $.proxy(this.execute, this));
			};
			
			RemoveCart.DEFAULTS = {
				skuId: null,
				confirm: true,
				confirmMessage: "${message("common.removeCart.confirmMessage")}"
			};
			
			RemoveCart.prototype.confirm = function(callback) {
				var confirmMessage = typeof this.options.confirmMessage === "function" ? this.options.confirmMessage(this.$element) : this.options.confirmMessage;
				
				if (typeof bootbox === "undefined") {
					throw new Error("RemoveCart requires bootbox.js");
				}
				
				bootbox.confirm(confirmMessage, $.proxy(callback, this));
			};
			
			RemoveCart.prototype.ajax = function() {
				var skuId = typeof this.options.skuId === "function" ? this.options.skuId(this.$element) : this.options.skuId;
				
				return $.removeCart(skuId);
			};
			
			RemoveCart.prototype.execute = function() {
				var that = this;
				var confirm = typeof this.options.confirm === "function" ? this.options.confirm(this.$element) : this.options.confirm;
				var beforeEvent = $.Event("before.xiaoxiangshop.removeCart");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				if (confirm) {
					this.confirm(function(result) {
						if (result == null || !result) {
							return;
						}
						
						that.ajax().done(function() {
							that.$element.trigger("success.xiaoxiangshop.removeCart", arguments);
						}).fail(function() {
							that.$element.trigger("error.xiaoxiangshop.removeCart", arguments);
						}).always(function() {
							that.$element.trigger("complete.xiaoxiangshop.removeCart", arguments);
						});
					});
				} else {
					this.ajax().done(function() {
						that.$element.trigger("success.xiaoxiangshop.removeCart", arguments);
					}).fail(function() {
						that.$element.trigger("error.xiaoxiangshop.removeCart", arguments);
					}).always(function() {
						that.$element.trigger("complete.xiaoxiangshop.removeCart", arguments);
					});
				}
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.removeCart");
					var options = $.extend({}, RemoveCart.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.removeCart", (data = new RemoveCart(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.removeCart;
			
			$.fn.removeCart = Plugin;
			$.fn.removeCart.Constructor = RemoveCart;
			
			$.fn.removeCart.noConflict = function() {
				$.fn.removeCart = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.removeCart.data-api", "[data-action='removeCart']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.removeCart") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 清空购物车
		+function($) {
			"use strict";
			
			var ClearCart = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.clearCart", $.proxy(this.execute, this));
			};
			
			ClearCart.DEFAULTS = {
				confirm: true,
				confirmMessage: "${message("common.clearCart.confirmMessage")}"
			};
			
			ClearCart.prototype.confirm = function(callback) {
				var confirmMessage = typeof this.options.confirmMessage === "function" ? this.options.confirmMessage(this.$element) : this.options.confirmMessage;
				
				if (typeof bootbox === "undefined") {
					throw new Error("ClearCart requires bootbox.js");
				}
				
				bootbox.confirm(confirmMessage, $.proxy(callback, this));
			};
			
			ClearCart.prototype.ajax = function() {
				return $.clearCart();
			};
			
			ClearCart.prototype.execute = function() {
				var that = this;
				var confirm = typeof this.options.confirm === "function" ? this.options.confirm(this.$element) : this.options.confirm;
				var beforeEvent = $.Event("before.xiaoxiangshop.clearCart");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				if (confirm) {
					this.confirm(function(result) {
						if (result == null || !result) {
							return;
						}
						
						that.ajax().done(function() {
							that.$element.trigger("success.xiaoxiangshop.clearCart", arguments);
						}).fail(function() {
							that.$element.trigger("error.xiaoxiangshop.clearCart", arguments);
						}).always(function() {
							that.$element.trigger("complete.xiaoxiangshop.clearCart", arguments);
						});
					});
				} else {
					this.ajax().done(function() {
						that.$element.trigger("success.xiaoxiangshop.clearCart", arguments);
					}).fail(function() {
						that.$element.trigger("error.xiaoxiangshop.clearCart", arguments);
					}).always(function() {
						that.$element.trigger("complete.xiaoxiangshop.clearCart", arguments);
					});
				}
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.clearCart");
					var options = $.extend({}, ClearCart.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.clearCart", (data = new ClearCart(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.clearCart;
			
			$.fn.clearCart = Plugin;
			$.fn.clearCart.Constructor = ClearCart;
			
			$.fn.clearCart.noConflict = function() {
				$.fn.clearCart = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.clearCart.data-api", "[data-action='clearCart']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.clearCart") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 结算
		+function($) {
			"use strict";
			
			var Checkout = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.checkout", $.proxy(this.execute, this));
			};
			
			Checkout.DEFAULTS = {
				skuId: null,
				quantity: 1
			};
			
			Checkout.prototype.execute = function() {
				var that = this;
				var skuId = typeof this.options.skuId === "function" ? this.options.skuId(this.$element) : this.options.skuId;
				var quantity = typeof this.options.quantity === "function" ? this.options.quantity(this.$element) : this.options.quantity;
				var beforeEvent = $.Event("before.xiaoxiangshop.checkout");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				$.checkout(skuId, quantity);
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.checkout");
					var options = $.extend({}, Checkout.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.checkout", (data = new Checkout(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.checkout;
			
			$.fn.checkout = Plugin;
			$.fn.checkout.Constructor = Checkout;
			
			$.fn.checkout.noConflict = function() {
				$.fn.checkout = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.checkout.data-api", "[data-action='checkout']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.checkout") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 添加商品收藏
		+function($) {
			"use strict";
			
			var AddProductFavorite = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.addProductFavorite", $.proxy(this.execute, this));
			};
			
			AddProductFavorite.DEFAULTS = {
				productId: null
			};
			
			AddProductFavorite.prototype.ajax = function() {
				var productId = typeof this.options.productId === "function" ? this.options.productId(this.$element) : this.options.productId;
				
				return $.addProductFavorite(productId);
			};
			
			AddProductFavorite.prototype.shrinkOut = function(callback) {
				var that = this;
				
				if ($.fn.velocity == null) {
					throw new Error("AddProductFavorite requires velocity.js");
				}
				
				$('<i class="iconfont icon-likefill"></i>').css({
					"line-height": "30px",
					color: "#f02c6c",
					"font-size": "30px",
					position: "absolute",
					top: this.$element.offset().top + this.$element.outerHeight() / 2 - 15,
					left: this.$element.offset().left + this.$element.outerWidth() / 2 - 15,
					"z-index": 300
				}).appendTo("body").velocity("transition.shrinkOut", {
					complete: function() {
						$(this).remove();
						callback.call(that);
					}
				});
			};
			
			AddProductFavorite.prototype.execute = function() {
				var that = this;
				var beforeEvent = $.Event("before.xiaoxiangshop.addProductFavorite");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				this.ajax().done(function() {
					var ajaxArguments = arguments;
					
					that.shrinkOut(function() {
						that.$element.trigger("success.xiaoxiangshop.addProductFavorite", ajaxArguments);
						that.$element.trigger("complete.xiaoxiangshop.addProductFavorite", ajaxArguments);
					});
				}).fail(function() {
					that.$element.trigger("error.xiaoxiangshop.addProductFavorite", arguments);
					that.$element.trigger("complete.xiaoxiangshop.addProductFavorite", arguments);
				});
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.addProductFavorite");
					var options = $.extend({}, AddProductFavorite.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.addProductFavorite", (data = new AddProductFavorite(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.addProductFavorite;
			
			$.fn.addProductFavorite = Plugin;
			$.fn.addProductFavorite.Constructor = AddProductFavorite;
			
			$.fn.addProductFavorite.noConflict = function() {
				$.fn.addProductFavorite = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.addProductFavorite.data-api", "[data-action='addProductFavorite']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.addProductFavorite") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 添加店铺收藏
		+function($) {
			"use strict";
			
			var AddStoreFavorite = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.addStoreFavorite", $.proxy(this.execute, this));
			};
			
			AddStoreFavorite.DEFAULTS = {
				storeId: null
			};
			
			AddStoreFavorite.prototype.ajax = function() {
				var storeId = typeof this.options.storeId === "function" ? this.options.storeId(this.$element) : this.options.storeId;
				
				return $.addStoreFavorite(storeId);
			};
			
			AddStoreFavorite.prototype.shrinkOut = function(callback) {
				var that = this;
				
				if ($.fn.velocity == null) {
					throw new Error("AddStoreFavorite requires velocity.js");
				}
				
				$('<i class="iconfont icon-likefill"></i>').css({
					"line-height": "30px",
					color: "#f02c6c",
					"font-size": "30px",
					position: "absolute",
					top: this.$element.offset().top + this.$element.outerHeight() / 2 - 15,
					left: this.$element.offset().left + this.$element.outerWidth() / 2 - 15,
					"z-index": 300
				}).appendTo("body").velocity("transition.shrinkOut", {
					complete: function() {
						$(this).remove();
						callback.call(that);
					}
				});
			};
			
			AddStoreFavorite.prototype.execute = function() {
				var that = this;
				var beforeEvent = $.Event("before.xiaoxiangshop.addStoreFavorite");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				this.ajax().done(function() {
					var ajaxArguments = arguments;
					
					that.shrinkOut(function() {
						that.$element.trigger("success.xiaoxiangshop.addStoreFavorite", ajaxArguments);
						that.$element.trigger("complete.xiaoxiangshop.addStoreFavorite", ajaxArguments);
					});
				}).fail(function() {
					that.$element.trigger("error.xiaoxiangshop.addStoreFavorite", arguments);
					that.$element.trigger("complete.xiaoxiangshop.addStoreFavorite", arguments);
				});
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.addStoreFavorite");
					var options = $.extend({}, AddStoreFavorite.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.addStoreFavorite", (data = new AddStoreFavorite(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.addStoreFavorite;
			
			$.fn.addStoreFavorite = Plugin;
			$.fn.addStoreFavorite.Constructor = AddStoreFavorite;
			
			$.fn.addStoreFavorite.noConflict = function() {
				$.fn.addStoreFavorite = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.addStoreFavorite.data-api", "[data-action='addStoreFavorite']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.addStoreFavorite") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 删除
		+function($) {
			"use strict";
			
			var Delete = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.delete", $.proxy(this.execute, this));
			};
			
			Delete.DEFAULTS = {
				url: "delete",
				type: "POST",
				data: null,
				dataType: "json",
				confirm: true,
				confirmMessage: "${message("common.delete.confirmMessage")}"
			};
			
			Delete.prototype.confirm = function(callback) {
				var confirmMessage = typeof this.options.confirmMessage === "function" ? this.options.confirmMessage(this.$element) : this.options.confirmMessage;
				
				if (typeof bootbox === "undefined") {
					throw new Error("Delete requires bootbox.js");
				}
				
				bootbox.confirm(confirmMessage, $.proxy(callback, this));
			};
			
			Delete.prototype.ajax = function() {
				var url = typeof this.options.url === "function" ? this.options.url(this.$element) : this.options.url;
				var type = typeof this.options.type === "function" ? this.options.type(this.$element) : this.options.type;
				var data = typeof this.options.data === "function" ? this.options.data(this.$element) : this.options.data;
				var dataType = typeof this.options.dataType === "function" ? this.options.dataType(this.$element) : this.options.dataType;
				
				return $.ajax({
					url: url,
					type: type,
					data: data,
					dataType: dataType
				});
			};
			
			Delete.prototype.execute = function() {
				var that = this;
				var confirm = typeof this.options.confirm === "function" ? this.options.confirm(this.$element) : this.options.confirm;
				var beforeEvent = $.Event("before.xiaoxiangshop.delete");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				if (confirm) {
					this.confirm(function(result) {
						if (result == null || !result) {
							return;
						}
						
						that.ajax().done(function() {
							that.$element.trigger("success.xiaoxiangshop.delete", arguments);
						}).fail(function() {
							that.$element.trigger("error.xiaoxiangshop.delete", arguments);
						}).always(function() {
							that.$element.trigger("complete.xiaoxiangshop.delete", arguments);
						});
					});
				} else {
					this.ajax().done(function() {
						that.$element.trigger("success.xiaoxiangshop.delete", arguments);
					}).fail(function() {
						that.$element.trigger("error.xiaoxiangshop.delete", arguments);
					}).always(function() {
						that.$element.trigger("complete.xiaoxiangshop.delete", arguments);
					});
				}
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.delete");
					var options = $.extend({}, Delete.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.delete", (data = new Delete(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn["delete"];
			
			$.fn["delete"] = Plugin;
			$.fn["delete"].Constructor = Delete;
			
			$.fn["delete"].noConflict = function() {
				$.fn["delete"] = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.delete.data-api", "[data-action='delete']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.delete") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 刷新
		+function($) {
			"use strict";
			
			var Refresh = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.refresh", $.proxy(this.execute, this));
			};
			
			Refresh.DEFAULTS = {
				force: true
			};
			
			Refresh.prototype.execute = function() {
				var force = typeof this.options.force === "function" ? this.options.force(this.$element) : this.options.force;
				var beforeEvent = $.Event("before.xiaoxiangshop.refresh");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				location.reload(force);
				
				this.$element.trigger("complete.xiaoxiangshop.refresh");
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.refresh");
					var options = $.extend({}, Refresh.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.refresh", (data = new Refresh(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.refresh;
			
			$.fn.refresh = Plugin;
			$.fn.refresh.Constructor = Refresh;
			
			$.fn.refresh.noConflict = function() {
				$.fn.refresh = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.refresh.data-api", "[data-action='refresh']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.refresh") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		// 全选
		+function($) {
			"use strict";
			
			var CheckAll = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("change.xiaoxiangshop.checkAll", $.proxy(this.toggle, this));
			};
			
			CheckAll.DEFAULTS = {
				target: "input[name='ids']"
			};
			
			CheckAll.prototype.toggle = function() {
				return this.$element.prop("checked") ? this.check() : this.uncheck();
			};
			
			CheckAll.prototype.check = function() {
				var target = typeof this.options.target === "function" ? this.options.target(this.$element) : this.options.target;
				var $target = target instanceof jQuery ? target : $(target);
				var checkEvent = $.Event("check.xiaoxiangshop.checkAll");
				
				this.$element.trigger(checkEvent);
				if (checkEvent.isDefaultPrevented()) {
					return;
				}
				
				if (!this.$element.prop("checked")) {
					this.$element.prop("checked", true);
				}
				$target.filter(":not(:checked)").prop("checked", true).trigger("change");
				
				this.$element.trigger("checked.xiaoxiangshop.checkAll");
			};
			
			CheckAll.prototype.uncheck = function() {
				var target = typeof this.options.target === "function" ? this.options.target(this.$element) : this.options.target;
				var $target = target instanceof jQuery ? target : $(target);
				var uncheckEvent = $.Event("uncheck.xiaoxiangshop.checkAll");
				
				this.$element.trigger(uncheckEvent);
				if (uncheckEvent.isDefaultPrevented()) {
					return;
				}
				
				if (this.$element.prop("checked")) {
					this.$element.prop("checked", false);
				}
				$target.filter(":checked").prop("checked", false).trigger("change");
				
				this.$element.trigger("unchecked.xiaoxiangshop.checkAll");
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.checkAll");
					var options = $.extend({}, CheckAll.DEFAULTS, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.checkAll", (data = new CheckAll(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.checkAll;
			
			$.fn.checkAll = Plugin;
			$.fn.checkAll.Constructor = CheckAll;
			
			$.fn.checkAll.noConflict = function() {
				$.fn.checkAll = old;
				return this;
			};
			
			$(document).on("change.xiaoxiangshop.checkAll.data-api", "[data-toggle='checkAll']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.checkAll") != null) {
					return;
				}
				Plugin.call($this, "toggle");
			});
		
		}(jQuery);
		
		// 返回
		+function($) {
			"use strict";
			
			var Back = function(element, options) {
				this.options = options;
				this.$element = $(element);
				
				this.$element.on("click.xiaoxiangshop.back", $.proxy(this.execute, this));
			};
			
			Back.prototype.execute = function() {
				var beforeEvent = $.Event("before.xiaoxiangshop.back");
				
				this.$element.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				history.back();
				
				this.$element.trigger("complete.xiaoxiangshop.back");
			};
			
			function Plugin(option) {
				return this.each(function() {
					var $this = $(this);
					var data = $this.data("xiaoxiangshop.back");
					var options = $.extend({}, $this.data(), typeof option === "object" && option);
					
					if (data == null) {
						$this.data("xiaoxiangshop.back", (data = new Back(this, options)));
					}
					if (typeof option === "string") {
						data[option]();
					}
				});
			}
			
			var old = $.fn.back;
			
			$.fn.back = Plugin;
			$.fn.back.Constructor = Back;
			
			$.fn.back.noConflict = function() {
				$.fn.back = old;
				return this;
			};
			
			$(document).on("click.xiaoxiangshop.back.data-api", "[data-action='back']", function() {
				var $this = $(this);
				
				if ($this.data("xiaoxiangshop.back") != null) {
					return;
				}
				Plugin.call($this, "execute");
			});
		
		}(jQuery);
		
		(function($) {
			
			var $document = $(document);
			var currentUserLocalStorageKey = "currentUser";
[#--			var getCurrentCartUrl = "${base}/cart/get_current";--]
[#--			var addCartUrl = "${base}/cart/add";--]
[#--			var modifyCartUrl = "${base}/cart/modify";--]
[#--			var removeCartUrl = "${base}/cart/remove";--]
[#--			var clearCartUrl = "${base}/cart/clear";--]
			var checkSkuUrl = "${base}/order/check_sku";
			var checkCartUrl = "${base}/order/check_cart";
			var checkoutUrl = "${base}/order/checkout";
			var currentCartQuantitySessionStorageKey = "currentCartQuantity";
			var addProductFavoriteUrl = "${base}/member/product_favorite/add";
			var addStoreFavoriteUrl = "${base}/member/store_favorite/add";
			var spreadUserLocalStorageKey = "spreadUser";
			var redirectUrlPrefix = "${base}/";
			var redirectUrlParameterName = "redirectUrl";
			var loginUrls = {
				member: "${base}${memberLoginUrl}",
				business: "${base}${businessLoginUrl}",
				admin: "${base}${adminLoginUrl}"
			};
			
			// 货币格式化
			$.currency = function(value, showSign, showUnit) {
				if (value == null) {
					return "";
				}
				[#if setting.priceRoundType == "ROUND_UP"]
					var result = (Math.ceil(value * Math.pow(10, ${setting.priceScale})) / Math.pow(10, ${setting.priceScale})).toFixed(${setting.priceScale});
				[#elseif setting.priceRoundType == "ROUND_DOWN"]
					var result = (Math.floor(value * Math.pow(10, ${setting.priceScale})) / Math.pow(10, ${setting.priceScale})).toFixed(${setting.priceScale});
				[#else]
					var result = (Math.round(value * Math.pow(10, ${setting.priceScale})) / Math.pow(10, ${setting.priceScale})).toFixed(${setting.priceScale});
				[/#if]
				if (showSign) {
					result = "${setting.currencySign}" + result;
				}
				if (showUnit) {
					result += "${setting.currencyUnit}";
				}
				return result;
			};
			
			// 获取当前登录用户
			$.getCurrentUser = function() {
				return JSON.parse(localStorage.getItem(currentUserLocalStorageKey));
			};
			
			// 设置当前登录用户
			$.setCurrentUser = function(currentUser) {
				localStorage.setItem(currentUserLocalStorageKey, JSON.stringify(currentUser));
			};
			
			// 移除当前登录用户
			$.removeCurrentUser = function() {
				localStorage.removeItem(currentUserLocalStorageKey);
			};
			
[#--			// 获取当前购物车--]
[#--			$.getCurrentCart = function() {--]
[#--				return $.get(getCurrentCartUrl);--]
[#--			};--]
			
[#--			// 加入购物车--]
[#--			$.addCart = function(skuId, quantity) {--]
[#--				return $.post(addCartUrl, {--]
[#--					skuId: skuId,--]
[#--					quantity: quantity--]
[#--				});--]
[#--			};--]
[#--			--]
[#--			// 修改购物车--]
[#--			$.modifyCart = function(skuId, quantity) {--]
[#--				return $.post(modifyCartUrl, {--]
[#--					skuId: skuId,--]
[#--					quantity: quantity--]
[#--				});--]
[#--			};--]
[#--			--]
[#--			// 移除购物车--]
[#--			$.removeCart = function(skuId) {--]
[#--				return $.post(removeCartUrl, {--]
[#--					skuId: skuId--]
[#--				});--]
[#--			};--]
[#--			--]
[#--			// 清空购物车--]
[#--			$.clearCart = function() {--]
[#--				return $.post(clearCartUrl);--]
[#--			};--]
[#--			--]
[#--			// 获取当前购物车数量--]
[#--			$.getCurrentCartQuantity = function() {--]
[#--				var currentCartQuantity = JSON.parse(sessionStorage.getItem(currentCartQuantitySessionStorageKey));--]
[#--				--]
[#--				if (currentCartQuantity != null) {--]
[#--					if (currentCartQuantity.expire > new Date().getTime()) {--]
[#--						$.setCurrentCartQuantity(currentCartQuantity.value);--]
[#--						return parseInt(currentCartQuantity.value);--]
[#--					} else {--]
[#--						$.removeCurrentCartQuantity();--]
[#--					}--]
[#--				}--]
[#--				return null;--]
[#--			};--]
[#--			--]
[#--			// 设置当前购物车数量--]
[#--			$.setCurrentCartQuantity = function(quantity) {--]
[#--				sessionStorage.setItem(currentCartQuantitySessionStorageKey, JSON.stringify({--]
[#--					value: quantity,--]
[#--					expire: new Date().getTime() + 10 * 60 * 1000--]
[#--				}));--]
[#--			};--]
[#--			--]
[#--			// 移除当前购物车数量--]
[#--			$.removeCurrentCartQuantity = function() {--]
[#--				sessionStorage.removeItem(currentCartQuantitySessionStorageKey);--]
[#--			};--]
			
			// 结算
			$.checkout = function(skuId, quantity) {
				if (skuId != null) {
					$.get(checkSkuUrl, {
						skuId: skuId,
						quantity: quantity
					}).done(function() {
						location.href = checkoutUrl + "?" + $.param({
							skuId: skuId,
							quantity: quantity
						});
					});
				} else {
					$.get(checkCartUrl).done(function() {
						location.href = checkoutUrl;
					});
				}
			};
			
			// 添加商品收藏
			$.addProductFavorite = function(productId) {
				return $.post(addProductFavoriteUrl, {
					productId: productId
				});
			};
			
			// 添加店铺收藏
			$.addStoreFavorite = function(storeId) {
				return $.post(addStoreFavoriteUrl, {
					storeId: storeId
				});
			};
			
			// 获取推广用户
			$.getSpreadUser = function() {
				return JSON.parse(localStorage.getItem(spreadUserLocalStorageKey));
			};
			
			// 设置推广用户
			$.setSpreadUser = function(spreadUser) {
				localStorage.setItem(spreadUserLocalStorageKey, JSON.stringify(spreadUser));
			};
			
			// 移除推广用户
			$.removeSpreadUser = function() {
				localStorage.removeItem(spreadUserLocalStorageKey);
			};
			
			// 生成推广Url
			$.generateSpreadUrl = function() {
				var url = arguments.length > 0 ? arguments[0] : location.href;
				var currentUser = $.getCurrentUser();
				
				if ($.base64 == null) {
					throw new Error("generateSpreadUrl requires jquery.base64.js");
				}
				return currentUser != null ? url.replace(/(#.*)?$/, (url.indexOf("?") >= 0 ? "&" : "?") + "SPREAD_" + $.base64.encode(currentUser.username)) : null;
			}
			
			// 重定向
			$.redirect = function(redirectUrl) {
				if (redirectUrl.indexOf(redirectUrlPrefix) == 0) {
					location.href = redirectUrl;
				}
			};
			
			// 重定向登录页面
			$.redirectLogin = function(options) {
				var settings = $.extend({}, $.redirectLogin.defaults, options);
				var loginUrl = loginUrls[settings.loginType];
				
				if ($.trim(loginUrl) == "") {
					return;
				}
				if ($.trim(settings.redirectUrl) != "") {
					loginUrl += (loginUrl.indexOf("?") >= 0 ? "&" : "?") + redirectUrlParameterName + "=" + encodeURIComponent(settings.redirectUrl);
				}
				location.href = loginUrl;
			};
			
			$.redirectLogin.defaults = {
				loginType: "member",
				redirectUrl: null
			};
			
			// AjaxSubmit
			var _ajaxSubmit = $.fn.ajaxSubmit;
			$.fn.ajaxSubmit = function(options, data, dataType, onSuccess) {
				var settings = $.extend({}, $.fn.ajaxSubmit.defaults, options);
				
				return _ajaxSubmit.call(this, settings, data, dataType, onSuccess);
			};
			
			$.fn.ajaxSubmit.defaults = {
				successMessage: true,
				successRedirectUrl: function(redirectUrlParameterName, data, textStatus, xhr, $form) {
					return Url.queryString(redirectUrlParameterName);
				},
				beforeSubmit: function(arr, $form, options) {
					var beforeEvent = $.Event("before.xiaoxiangshop.ajaxSubmit", arguments);
					
					$form.trigger(beforeEvent);
					if (beforeEvent.isDefaultPrevented()) {
						return false;
					}
					$form.find(":submit").addClass("btn-before-submit").prop("disabled", true);
				},
				success: function(data, textStatus, xhr, $form) {
					var successMessage = $.isFunction(this.successMessage) ? this.successMessage.call(this, redirectUrlParameterName, data, textStatus, xhr, $form) : this.successMessage;
					var successRedirectUrl = $.isFunction(this.successRedirectUrl) ? this.successRedirectUrl.call(this, redirectUrlParameterName, data, textStatus, xhr, $form) : this.successRedirectUrl;
					
					$form.trigger("succ" + "ess.sho" + "px" + "x.ajaxSubmit", arguments);
					if ($.bootstrapGrowl != null && successMessage) {
						var message = typeof successMessage === "string" ? successMessage : data.message;
						
						if ($.trim(message) != "") {
							$.bootstrapGrowl(message, {
								type: "success"
							});
							if ($.trim(successRedirectUrl) != "") {
								setTimeout(function() {
									$form.find(":submit").prop("disabled", false).removeClass("btn-before-submit");
									$.redirect(successRedirectUrl);
								}, 1000);
								return;
							}
						}
					}
					if ($.trim(successRedirectUrl) != "") {
						$form.find(":submit").prop("disabled", false).removeClass("btn-before-submit");
						$.redirect(successRedirectUrl);
					}
				},
				error: function(xhr, textStatus, error, $form) {
					$form.find(":submit").prop("disabled", false).removeClass("btn-before-submit");
					$form.trigger("error.xiaoxiangshop.ajaxSubmit", arguments);
				},
				complete: function(xhr, textStatus, $form) {
					$form.trigger("complete.xiaoxiangshop.ajaxSubmit", arguments);
				}
			};
			
			// AJAX全局设置
			$.ajaxSetup({
				traditional: true,
				statusCode: {
					400: function(xhr, textStatus, errorThrown) {
						var data = JSON.parse(xhr.responseText);
						var message = data.message != null ? data.message : "${message("common.message.badRequest")}";
						
						if ($.bootstrapGrowl != null) {
							$.bootstrapGrowl(message, {
								type: "danger"
							});
						}
					},
					401: function(xhr, textStatus, errorThrown) {
						var data = JSON.parse(xhr.responseText);
						var message = data.message != null ? data.message : "${message("common.message.unauthorized")}";
						
						if ($.bootstrapGrowl != null) {
							$.bootstrapGrowl(message, {
								type: "warning"
							});
						}
						setTimeout(function() {
							$.redirectLogin({
								redirectUrl: Url.getLocation()
							});
						}, 3000);
					},
					403: function(xhr, textStatus, errorThrown) {
						var data = JSON.parse(xhr.responseText);
						var message = data.message != null ? data.message : "${message("common.message.forbidden")}";
						
						if ($.bootstrapGrowl != null) {
							$.bootstrapGrowl(message, {
								type: "danger"
							});
						}
					},
					404: function(xhr, textStatus, errorThrown) {
						var data = JSON.parse(xhr.responseText);
						var message = data.message != null ? data.message : "${message("common.message.notFound")}";
						
						if ($.bootstrapGrowl != null) {
							$.bootstrapGrowl(message, {
								type: "danger"
							});
						}
					},
					422: function(xhr, textStatus, errorThrown) {
						var data = JSON.parse(xhr.responseText);
						var message = data.message != null ? data.message : "${message("common.message.unprocessableEntity")}";
						
						if ($.bootstrapGrowl != null) {
							$.bootstrapGrowl(message, {
								type: "warning"
							});
						}
					}
				}
			});
			
			// Cookie
			if ($.cookie != null) {
				$.extend($.cookie.defaults, {
					[#if setting.cookiePath?has_content]
						path: "${setting.cookiePath}"[#if setting.cookieDomain?has_content],[/#if]
					[/#if]
					[#if setting.cookieDomain?has_content]
						domain: "${setting.cookieDomain}"
					[/#if]
				});
			}
			
			// 提示框
			if ($.bootstrapGrowl != null) {
				$.extend($.bootstrapGrowl.default_options, {
					width: "auto",
					offset: {
						from: "top",
						amount: 120
					},
					align: "center",
					allow_dismiss: false
				});
			}
			
			// 弹出框
			if (typeof(bootbox) != "undefined") {
				bootbox.setDefaults({
					locale: "${locale}",
					title: "${message("common.bootbox.title")}"
				});
				
				bootbox.addLocale("${locale}", {
					OK: "${message("common.bootbox.ok")}",
					CANCEL: "${message("common.bootbox.cancel")}",
					CONFIRM: "${message("common.bootbox.confirm")}"
				});
			}
			
			// 下拉菜单
			if ($.fn.selectpicker != null) {
				$.fn.selectpicker.defaults = $.extend($.fn.selectpicker.defaults, {
					noneSelectedText: "${message("common.selectpicker.noneSelectedText")}",
					noneResultsText: "${message("common.selectpicker.noneResultsText")}",
					countSelectedText: "${message("common.selectpicker.countSelectedText")}",
					maxOptionsText: "${message("common.selectpicker.maxOptionsText")}",
					selectAllText: "${message("common.selectpicker.selectAllText")}",
					deselectAllText: "${message("common.selectpicker.deselectAllText")}",
					multipleSeparator: "${message("common.selectpicker.multipleSeparator")}"
				});
			}
			
			// 下拉菜单搜索
			if ($.fn.ajaxSelectPicker != null) {
				$.extend($.fn.ajaxSelectPicker.defaults, {
					langCode: "${locale}"
				});
				
				$.fn.ajaxSelectPicker.locale["${locale}"] = {
					currentlySelected: "${message("common.ajaxSelectPicker.currentlySelected")}",
					emptyTitle: "${message("common.ajaxSelectPicker.emptyTitle")}",
					errorText: "${message("common.ajaxSelectPicker.errorText")}",
					searchPlaceholder: "${message("common.ajaxSelectPicker.searchPlaceholder")}",
					statusInitialized: "${message("common.ajaxSelectPicker.statusInitialized")}",
					statusNoResults: "${message("common.ajaxSelectPicker.statusNoResults")}",
					statusSearching: "${message("common.ajaxSelectPicker.statusSearching")}"
				}
			}
			
			// 选择框
			if ($.fn.checkboxX != null) {
				$.extend($.fn.checkboxX.defaults, {
					size: "xs",
					threeState: false,
					valueChecked: "true",
					valueUnchecked: "false"
				});
			}
			
			// 日期选择
			if ($.fn.datetimepicker != null) {
				$.extend($.fn.datetimepicker.defaults, {
					locale: moment.locale("${locale}"),
					format: $(this).data("date-format") != null ? $(this).data("date-format") : "YYYY-MM-DD"
				});
			}
			
			// 文件上传
			if ($.fn.fileinput != null) {
				$.extend($.fn.fileinput.defaults, {
					language: "${locale}"
				});
				
				$.fn.fileinputLocales["${locale}"] = {
					fileSingle: "${message("common.fileinput.fileSingle")}",
					filePlural: "${message("common.fileinput.filePlural")}",
					browseLabel: "${message("common.fileinput.browseLabel")}",
					removeLabel: "${message("common.fileinput.removeLabel")}",
					removeTitle: "${message("common.fileinput.removeTitle")}",
					cancelLabel: "${message("common.fileinput.cancelLabel")}",
					cancelTitle: "${message("common.fileinput.cancelTitle")}",
					uploadLabel: "${message("common.fileinput.uploadLabel")}",
					uploadTitle: "${message("common.fileinput.uploadTitle")}",
					msgNo: "${message("common.fileinput.msgNo")}",
					msgNoFilesSelected: "${message("common.fileinput.msgNoFilesSelected")}",
					msgCancelled: "${message("common.fileinput.msgCancelled")}",
					msgPlaceholder: "${message("common.fileinput.msgPlaceholder")}",
					msgZoomModalHeading: "${message("common.fileinput.msgZoomModalHeading")}",
					msgFileRequired: "${message("common.fileinput.msgFileRequired")}",
					msgSizeTooSmall: "${message("common.fileinput.msgSizeTooSmall")}",
					msgSizeTooLarge: "${message("common.fileinput.msgSizeTooLarge")}",
					msgFilesTooLess: "${message("common.fileinput.msgFilesTooLess")}",
					msgFilesTooMany: "${message("common.fileinput.msgFilesTooMany")}",
					msgFileNotFound: "${message("common.fileinput.msgFileNotFound")}",
					msgFileSecured: "${message("common.fileinput.msgFileSecured")}",
					msgFileNotReadable: "${message("common.fileinput.msgFileNotReadable")}",
					msgFilePreviewAborted: "${message("common.fileinput.msgFilePreviewAborted")}",
					msgFilePreviewError: "${message("common.fileinput.msgFilePreviewError")}",
					msgInvalidFileName: "${message("common.fileinput.msgInvalidFileName")}",
					msgInvalidFileType: "${message("common.fileinput.msgInvalidFileType")}",
					msgInvalidFileExtension: "${message("common.fileinput.msgInvalidFileExtension")}",
					msgFileTypes: {
						image: "${message("common.fileinput.msgFileTypesImage")}",
						html: "${message("common.fileinput.msgFileTypesHtml")}",
						text: "${message("common.fileinput.msgFileTypesText")}",
						video: "${message("common.fileinput.msgFileTypesVideo")}",
						audio: "${message("common.fileinput.msgFileTypesAudio")}",
						flash: "${message("common.fileinput.msgFileTypesFlash")}",
						pdf: "${message("common.fileinput.msgFileTypesPdf")}",
						object: "${message("common.fileinput.msgFileTypesObject")}"
					},
					msgUploadAborted: "${message("common.fileinput.msgUploadAborted")}",
					msgUploadThreshold: "${message("common.fileinput.msgUploadThreshold")}",
					msgUploadBegin: "${message("common.fileinput.msgUploadBegin")}",
					msgUploadEnd: "${message("common.fileinput.msgUploadEnd")}",
					msgUploadEmpty: "${message("common.fileinput.msgUploadEmpty")}",
					msgUploadError: "${message("common.fileinput.msgUploadError")}",
					msgValidationError: "${message("common.fileinput.msgValidationError")}",
					msgLoading: "${message("common.fileinput.msgLoading")}",
					msgProgress: "${message("common.fileinput.msgProgress")}",
					msgSelected: "${message("common.fileinput.msgSelected")}",
					msgFoldersNotAllowed: "${message("common.fileinput.msgFoldersNotAllowed")}",
					msgImageWidthSmall: "${message("common.fileinput.msgImageWidthSmall")}",
					msgImageHeightSmall: "${message("common.fileinput.msgImageHeightSmall")}",
					msgImageWidthLarge: "${message("common.fileinput.msgImageWidthLarge")}",
					msgImageHeightLarge: "${message("common.fileinput.msgImageHeightLarge")}",
					msgImageResizeError: "${message("common.fileinput.msgImageResizeError")}",
					msgImageResizeException: "${message("common.fileinput.msgImageResizeException")}",
					msgAjaxError: "${message("common.fileinput.msgAjaxError")}",
					msgAjaxProgressError: "${message("common.fileinput.msgAjaxProgressError")}",
					ajaxOperations: {
						deleteThumb: "${message("common.fileinput.ajaxOperationsDeleteThumb")}",
						uploadThumb: "${message("common.fileinput.ajaxOperationsUploadThumb")}",
						uploadBatch: "${message("common.fileinput.ajaxOperationsUploadBatch")}",
						uploadExtra: "${message("common.fileinput.ajaxOperationsUploadExtra")}"
					},
					dropZoneTitle: "${message("common.fileinput.dropZoneTitle")}",
					dropZoneClickTitle: "${message("common.fileinput.dropZoneClickTitle")}",
					fileActionSettings: {
						removeTitle: "${message("common.fileinput.fileActionSettingsRemoveTitle")}",
						uploadTitle: "${message("common.fileinput.fileActionSettingsUploadTitle")}",
						uploadRetryTitle: "${message("common.fileinput.fileActionSettingsUploadRetryTitle")}",
						downloadTitle: "${message("common.fileinput.fileActionSettingsDownloadTitle")}",
						zoomTitle: "${message("common.fileinput.fileActionSettingsZoomTitle")}",
						dragTitle: "${message("common.fileinput.fileActionSettingsDragTitle")}",
						indicatorNewTitle: "${message("common.fileinput.fileActionSettingsIndicatorNewTitle")}",
						indicatorSuccessTitle: "${message("common.fileinput.fileActionSettingsIndicatorSuccessTitle")}",
						indicatorErrorTitle: "${message("common.fileinput.fileActionSettingsIndicatorErrorTitle")}",
						indicatorLoadingTitle: "${message("common.fileinput.fileActionSettingsIndicatorLoadingTitle")}"
					},
					previewZoomButtonTitles: {
						prev: "${message("common.fileinput.previewZoomButtonTitlesPrev")}",
						next: "${message("common.fileinput.previewZoomButtonTitlesNext")}",
						toggleheader: "${message("common.fileinput.previewZoomButtonTitlesToggleheader")}",
						fullscreen: "${message("common.fileinput.previewZoomButtonTitlesFullscreen")}",
						borderless: "${message("common.fileinput.previewZoomButtonTitlesBorderless")}",
						close: "${message("common.fileinput.previewZoomButtonTitlesClose")}"
					}
				};
			}
			
			// 文本编辑器
			if ($.summernote != null) {
				$.extend($.summernote.options, {
					lang: "${locale}",
					minHeight: 300,
					dialogsInBody: true,
					dialogsFade: true,
					callbacks: {
						onImageUpload: function(files) {
							var $element = $(this);
							var $files = $(files);
							
							$files.each(function() {
								var file = this;
								var formData = new FormData();
								
								formData.append("fileType", "IMAGE");
								formData.append("file", file);
								$.ajax({
									url: "${base}/common/file/upload",
									type: "POST",
									data: formData,
									dataType: "json",
									contentType: false,
									cache: false,
									processData: false
								}).done(function(data) {
									$element.summernote("insertImage", data.url);
								});
							});
						}
					}
				});
				
				$.extend($.summernote.lang, {
					"${locale}": {
						font: {
							bold: "${message("common.summernote.fontBold")}",
							italic: "${message("common.summernote.fontItalic")}",
							underline: "${message("common.summernote.fontUnderline")}",
							clear: "${message("common.summernote.fontClear")}",
							height: "${message("common.summernote.fontHeight")}",
							name: "${message("common.summernote.fontName")}",
							strikethrough: "${message("common.summernote.fontStrikethrough")}",
							subscript: "${message("common.summernote.fontSubscript")}",
							superscript: "${message("common.summernote.fontSuperscript")}",
							size: "${message("common.summernote.fontSize")}"
						},
						image: {
							image: "${message("common.summernote.imageImage")}",
							insert: "${message("common.summernote.imageInsert")}",
							resizeFull: "${message("common.summernote.imageResizeFull")}",
							resizeHalf: "${message("common.summernote.imageResizeHalf")}",
							resizeQuarter: "${message("common.summernote.imageResizeQuarter")}",
							floatLeft: "${message("common.summernote.imageFloatLeft")}",
							floatRight: "${message("common.summernote.imageFloatRight")}",
							floatNone: "${message("common.summernote.imageFloatNone")}",
							shapeRounded: "${message("common.summernote.imageShapeRounded")}",
							shapeCircle: "${message("common.summernote.imageShapeCircle")}",
							shapeThumbnail: "${message("common.summernote.imageShapeThumbnail")}",
							shapeNone: "${message("common.summernote.imageShapeNone")}",
							dragImageHere: "${message("common.summernote.imageDragImageHere")}",
							dropImage: "${message("common.summernote.imageDropImage")}",
							selectFromFiles: "${message("common.summernote.imageSelectFromFiles")}",
							maximumFileSize: "${message("common.summernote.imageMaximumFileSize")}",
							maximumFileSizeError: "${message("common.summernote.imageMaximumFileSizeError")}",
							url: "${message("common.summernote.imageUrl")}",
							remove: "${message("common.summernote.imageRemove")}"
						},
						video: {
							video: "${message("common.summernote.videoVideo")}",
							videoLink: "${message("common.summernote.videoVideoLink")}",
							insert: "${message("common.summernote.videoInsert")}",
							url: "${message("common.summernote.videoUrl")}",
							providers: "${message("common.summernote.videoProviders")}"
						},
						link: {
							link: "${message("common.summernote.linkLink")}",
							insert: "${message("common.summernote.linkInsert")}",
							unlink: "${message("common.summernote.linkUnlink")}",
							edit: "${message("common.summernote.linkEdit")}",
							textToDisplay: "${message("common.summernote.linkTextToDisplay")}",
							url: "${message("common.summernote.linkUrl")}",
							openInNewWindow: "${message("common.summernote.linkOpenInNewWindow")}"
						},
						table: {
							table: "${message("common.summernote.tableTable")}"
						},
						hr: {
							insert: "${message("common.summernote.hrInsert")}"
						},
						style: {
							style: "${message("common.summernote.styleStyle")}",
							p: "${message("common.summernote.styleP")}",
							blockquote: "${message("common.summernote.styleBlockquote")}",
							pre: "${message("common.summernote.stylePre")}",
							h1: "${message("common.summernote.styleH1")}",
							h2: "${message("common.summernote.styleH2")}",
							h3: "${message("common.summernote.styleH3")}",
							h4: "${message("common.summernote.styleH4")}",
							h5: "${message("common.summernote.styleH5")}",
							h6: "${message("common.summernote.styleH6")}"
						},
						lists: {
							unordered: "${message("common.summernote.listsUnordered")}",
							ordered: "${message("common.summernote.listsOrdered")}"
						},
						options: {
							help: "${message("common.summernote.optionsHelp")}",
							fullscreen: "${message("common.summernote.optionsFullscreen")}",
							codeview: "${message("common.summernote.optionsCodeview")}"
						},
						paragraph: {
							paragraph: "${message("common.summernote.paragraphParagraph")}",
							outdent: "${message("common.summernote.paragraphOutdent")}",
							indent: "${message("common.summernote.paragraphIndent")}",
							left: "${message("common.summernote.paragraphLeft")}",
							center: "${message("common.summernote.paragraphCenter")}",
							right: "${message("common.summernote.paragraphRight")}",
							justify: "${message("common.summernote.paragraphJustify")}"
						},
						color: {
							recent: "${message("common.summernote.colorRecent")}",
							more: "${message("common.summernote.colorMore")}",
							background: "${message("common.summernote.colorBackground")}",
							foreground: "${message("common.summernote.colorForeground")}",
							transparent: "${message("common.summernote.colorTransparent")}",
							setTransparent: "${message("common.summernote.colorSetTransparent")}",
							reset: "${message("common.summernote.colorReset")}",
							resetToDefault: "${message("common.summernote.colorResetToDefault")}"
						},
						shortcut: {
							shortcuts: "${message("common.summernote.shortcutShortcuts")}",
							close: "${message("common.summernote.shortcutClose")}",
							textFormatting: "${message("common.summernote.shortcutTextFormatting")}",
							action: "${message("common.summernote.shortcutAction")}",
							paragraphFormatting: "${message("common.summernote.shortcutParagraphFormatting")}",
							documentStyle: "${message("common.summernote.shortcutDocumentStyle")}",
							extraKeys: "${message("common.summernote.shortcutExtraKeys")}"
						},
						help: {
							insertParagraph: "${message("common.summernote.helpInsertParagraph")}",
							undo: "${message("common.summernote.helpUndo")}",
							redo: "${message("common.summernote.helpRedo")}",
							tab: "${message("common.summernote.helpTab")}",
							untab: "${message("common.summernote.helpUntab")}",
							bold: "${message("common.summernote.helpBold")}",
							italic: "${message("common.summernote.helpItalic")}",
							underline: "${message("common.summernote.helpUnderline")}",
							strikethrough: "${message("common.summernote.helpStrikethrough")}",
							removeFormat: "${message("common.summernote.helpRemoveFormat")}",
							justifyLeft: "${message("common.summernote.helpJustifyLeft")}",
							justifyCenter: "${message("common.summernote.helpJustifyCenter")}",
							justifyRight: "${message("common.summernote.helpJustifyRight")}",
							justifyFull: "${message("common.summernote.helpJustifyFull")}",
							insertUnorderedList: "${message("common.summernote.helpInsertUnorderedList")}",
							insertOrderedList: "${message("common.summernote.helpInsertOrderedList")}",
							outdent: "${message("common.summernote.helpOutdent")}",
							indent: "${message("common.summernote.helpIndent")}",
							formatPara: "${message("common.summernote.helpFormatPara")}",
							formatH1: "${message("common.summernote.helpFormatH1")}",
							formatH2: "${message("common.summernote.helpFormatH2")}",
							formatH3: "${message("common.summernote.helpFormatH3")}",
							formatH4: "${message("common.summernote.helpFormatH4")}",
							formatH5: "${message("common.summernote.helpFormatH5")}",
							formatH6: "${message("common.summernote.helpFormatH6")}",
							insertHorizontalRule: "${message("common.summernote.helpInsertHorizontalRule")}",
							"linkDialog.show": "${message("common.summernote.helpLinkDialogShow")}"
						},
						history: {
							undo: "${message("common.summernote.historyUndo")}",
							redo: "${message("common.summernote.historyRedo")}"
						},
						specialChar: {
							specialChar: "${message("common.summernote.specialCharSpecialChar")}",
							select: "${message("common.summernote.specialCharSelect")}"
						}
					}
				});
			}
			
			// 评级
			if ($.fn.rating != null) {
				$.extend($.fn.rating.defaults, {
					language: "${locale}",
					step: 1,
					showClear: false,
					showCaption: false,
					emptyStar: '<i class="iconfont icon-favor"></i>',
					filledStar: '<i class="iconfont icon-favorfill"></i>'
				});
				
				$.fn.ratingLocales["${locale}"] = {
					defaultCaption: "${message("common.rating.defaultCaption")}",
					starCaptions: {
						1: "${message("common.rating.starCaptionsOne")}",
						2: "${message("common.rating.starCaptionsTwo")}",
						3: "${message("common.rating.starCaptionsThree")}",
						4: "${message("common.rating.starCaptionsFour")}",
						5: "${message("common.rating.starCaptionsFive")}"
					},
					clearButtonTitle: "${message("common.rating.clearButtonTitle")}",
					clearCaption: "${message("common.rating.clearCaption")}"
				};
			}
			
			// 滚动固定
			if ($.fn.scrollToFixed != null) {
				$.extend($.ScrollToFixed.defaultOptions, {
					zIndex: 100,
					spacerClass: "scroll-to-fixed-spacer"
				});
			}
			
			// 滚动加载
			if (typeof(Scrollload) != "undefined") {
				Scrollload.setGlobalOptions({
					loadingHtml: '<div class="scrollload-loader"><span></span><span></span><span></span><span></span><span></span></div>',
					noMoreDataHtml: '<p class="scrollload-no-more-data">${message("common.scrollload.noMoreData")}</p>',
					exceptionHtml: '<p class="scrollload-exception">${message("common.scrollload.exception")}</p>',
					notEnoughRefreshPortHtml: '<div class="scrollload-loader"><span></span><span></span><span></span><span></span><span></span></div>',
					overRefreshPortHtml: '<div class="scrollload-loader"><span></span><span></span><span></span><span></span><span></span></div>',
					refreshingHtml: '<div class="scrollload-loader"><span></span><span></span><span></span><span></span><span></span></div>'
				});
			}
			
			// 表单验证
			if ($.validator != null) {
				var _greaterThan = $.validator.methods.greaterThan;
				var _greaterThanEqual = $.validator.methods.greaterThanEqual;
				var _lessThan = $.validator.methods.lessThan;
				var _lessThanEqual = $.validator.methods.lessThanEqual;
				
				$.validator.methods.greaterThan = function(value, element, param) {
					return this.optional(element) || _greaterThan.call(this, Number(value), element, param);
				}
				$.validator.methods.greaterThanEqual = function(value, element, param) {
					return this.optional(element) || _greaterThanEqual.call(this, Number(value), element, param);
				}
				$.validator.methods.lessThan = function(value, element, param) {
					return this.optional(element) || _lessThan.call(this, Number(value), element, param);
				}
				$.validator.methods.lessThanEqual = function(value, element, param) {
					return this.optional(element) || _lessThanEqual.call(this, Number(value), element, param);
				}
				$.validator.addMethod("positive", function(value, element) {
					return this.optional(element) || value > 0;
				}, "A positive number please");
				
				$.validator.addMethod("negative", function(value, element) {
					return this.optional(element) || value < 0;
				}, "A negative number please");
				
				$.validator.addMethod("decimal", function(value, element, param) {
					return this.optional(element) || new RegExp("^-?\\d{1," + (param.integer != null ? param.integer : "") + "}" + (param.fraction != null ? (param.fraction > 0 ? "(\\.\\d{1," + param.fraction + "})?$" : "$") : "(\\.\\d+)?$")).test(value);
				}, "Numeric value out of bounds");
				
				$.validator.addMethod("username", function(value, element) {
					return this.optional(element) || /^[0-9a-zA-Z_\u4e00-\u9fa5]+$/.test(value);
				}, "Only Chinese characters, letters, numbers and underscore can be used");
				
				$.validator.addMethod("zipCode", function(value, element) {
					return this.optional(element) || /^\d{6}$/.test(value);
				}, "The zip code is invalid");
				
				$.validator.addMethod("mobile", function(value, element) {
					return this.optional(element) || /^1[3|4|5|7|8]\d{9}$/.test(value);
				}, "Please enter a valid mobile number");
				
				$.validator.addMethod("phone", function(value, element) {
					return this.optional(element) || /^\d{3,4}-?\d{7,9}$/.test(value);
				}, "Please enter a valid phone number");
				
				$.validator.addMethod("notAllNumber", function(value, element) {
					return this.optional(element) || /^.*[^\d].*$/.test(value);
				}, "All numbers are not allowed");
				
				$.validator.addMethod("equals", function(value, element, param) {
					if (typeof param === "object") {
						return this.optional(element) || (param.ignoreCase ? param.value.toLowerCase() === value.toLowerCase() : param.value === value);
					} else {
						return this.optional(element) || param === value;
					}
				}, "Please enter a value equals {0}.");
				
				$.validator.addMethod("notEquals", function(value, element, param) {
					return this.optional(element) || !$.validator.methods.equals.call(this, value, element, param);
				}, "Please enter a value not equals {0}.");
				
				$.validator.setDefaults({
					ignore: ".ignore, .note-editable",
					ignoreTitle: true,
					errorElement: "span",
					errorClass: "help-block",
					normalizer: function(value) {
						return $.trim(value);
					},
					highlight: function(element, errorClass, validClass) {
						$(element).closest("td, .form-group").addClass("has-warning");
					},
					unhighlight: function(element, errorClass, validClass) {
						$(element).closest("td, .form-group").removeClass("has-warning");
					},
					errorPlacement: function($error, $element) {
						var $formGroup = $element.closest("td, [class^='col-'], .form-group");
						if ($formGroup.length > 0) {
							$error.appendTo($formGroup);
						} else {
							$error.insertAfter($element);
						}
					},
					invalidHandler: function(event, validator) {
						if ($.bootstrapGrowl != null) {
							$.bootstrapGrowl("${message("common.validator.invalid")}", {
								type: "warning"
							});
						}
					},
					submitHandler: function(form) {
						var $form = $(form);
						
						if ($.fn.ajaxForm != null && $form.hasClass("ajax-form")) {
							$form.ajaxSubmit();
						} else {
							form.submit();
						}
					}
				});
				
				$.extend($.validator.messages, {
					required: "${message("common.validator.required")}",
					remote: "${message("common.validator.remote")}",
					email: "${message("common.validator.email")}",
					url: "${message("common.validator.url")}",
					date: "${message("common.validator.date")}",
					dateISO: "${message("common.validator.dateISO")}",
					number: "${message("common.validator.number")}",
					digits: "${message("common.validator.digits")}",
					equalTo: "${message("common.validator.equalTo")}",
					maxlength: $.validator.format("${message("common.validator.maxlength")}"),
					minlength: $.validator.format("${message("common.validator.minlength")}"),
					rangelength: $.validator.format("${message("common.validator.rangelength")}"),
					range: $.validator.format("${message("common.validator.range")}"),
					max: $.validator.format("${message("common.validator.max")}"),
					min: $.validator.format("${message("common.validator.min")}"),
					step: $.validator.format("${message("common.validator.step")}"),
					maxWords: $.validator.format("${message("common.validator.maxWords")}"),
					minWords: $.validator.format("${message("common.validator.minWords")}"),
					rangeWords: $.validator.format("${message("common.validator.rangeWords")}"),
					accept: $.validator.format("${message("common.validator.accept")}"),
					alphanumeric: "${message("common.validator.alphanumeric")}",
					bankaccountNL: "${message("common.validator.bankaccountNL")}",
					bankorgiroaccountNL: "${message("common.validator.bankorgiroaccountNL")}",
					bic: "${message("common.validator.bic")}",
					cifES: "${message("common.validator.cifES")}",
					cpfBR: "${message("common.validator.cpfBR")}",
					creditcard: "${message("common.validator.creditcard")}",
					creditcardtypes: "${message("common.validator.creditcardtypes")}",
					currency: "${message("common.validator.currency")}",
					dateFA: "${message("common.validator.dateFA")}",
					dateITA: "${message("common.validator.dateITA")}",
					dateNL: "${message("common.validator.dateNL")}",
					extension: $.validator.format("${message("common.validator.extension")}"),
					giroaccountNL: "${message("common.validator.giroaccountNL")}",
					greaterThan: "${message("common.validator.greaterThan")}",
					greaterThanEqual: "${message("common.validator.greaterThanEqual")}",
					iban: "${message("common.validator.iban")}",
					integer: "${message("common.validator.integer")}",
					ipv4: "${message("common.validator.ipv4")}",
					ipv6: "${message("common.validator.ipv6")}",
					lessThan: "${message("common.validator.lessThan")}",
					lessThanEqual: "${message("common.validator.lessThanEqual")}",
					lettersonly: "${message("common.validator.lettersonly")}",
					letterswithbasicpunc: "${message("common.validator.letterswithbasicpunc")}",
					maxfiles: "${message("common.validator.maxfiles")}",
					maxsize: "${message("common.validator.maxsize")}",
					maxsizetotal: "${message("common.validator.maxsizetotal")}",
					mobileNL: "${message("common.validator.mobileNL")}",
					mobileUK: "${message("common.validator.mobileUK")}",
					netmask: "${message("common.validator.netmask")}",
					nieES: "${message("common.validator.nieES")}",
					nifES: "${message("common.validator.nifES")}",
					nipPL: "${message("common.validator.nipPL")}",
					notEqualTo: "${message("common.validator.notEqualTo")}",
					nowhitespace: "${message("common.validator.nowhitespace")}",
					pattern: "${message("common.validator.pattern")}",
					phoneNL: "${message("common.validator.phoneNL")}",
					phonesUK: "${message("common.validator.phonesUK")}",
					phoneUK: "${message("common.validator.phoneUK")}",
					phoneUS: "${message("common.validator.phoneUS")}",
					postalcodeBR: "${message("common.validator.postalcodeBR")}",
					postalCodeCA: "${message("common.validator.postalCodeCA")}",
					postalcodeIT: "${message("common.validator.postalcodeIT")}",
					postalcodeNL: "${message("common.validator.postalcodeNL")}",
					postcodeUK: "${message("common.validator.postcodeUK")}",
					require_from_group: $.validator.format("${message("common.validator.require_from_group")}"),
					skip_or_fill_minimum: $.validator.format("${message("common.validator.skip_or_fill_minimum")}"),
					stateUS: "${message("common.validator.stateUS")}",
					strippedminlength: $.validator.format("${message("common.validator.strippedminlength")}"),
					time: "${message("common.validator.time")}",
					time12h: "${message("common.validator.time12h")}",
					url2: "${message("common.validator.url2")}",
					vinUS: "${message("common.validator.vinUS")}",
					zipCode: "${message("common.validator.zipcode")}",
					zipcodeUS: "${message("common.validator.zipcodeUS")}",
					ziprange: "${message("common.validator.ziprange")}",
					positive: "${message("common.validator.positive")}",
					negative: "${message("common.validator.negative")}",
					decimal: "${message("common.validator.decimal")}",
					username: "${message("common.validator.username")}",
					mobile: "${message("common.validator.mobile")}",
					phone: "${message("common.validator.phone")}",
					notAllNumber: "${message("common.validator.notAllNumber")}",
					equals: "${message("common.validator.equals")}",
					notEquals: "${message("common.validator.notEquals")}"
				});
			}
			
		})(jQuery);
		
		$().ready(function() {
			
			var $document = $(document);
			var $form = $("form");
			var $redirectUrl = $("a[data-redirect-url]");
			var redirectUrlParameterName = "redirectUrl";
			var userAgent = navigator.userAgent.toLowerCase();
			var userAgentMatch = /(chrome)[ \/]([\w.]+)/.exec(userAgent) || /(webkit)[ \/]([\w.]+)/.exec(userAgent) || /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(userAgent) || /(msie) ([\w.]+)/.exec(userAgent) || userAgent.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(userAgent) || [];
			var $countdown = $("[data-target='countdown']");
			
			// 倒计时
			if ($.fn.animateNumber != null) {
				$countdown.prop("number", 3).animateNumber({
					number: 0,
					numberStep: function(now, tween) {
						var $target = $(tween.elem);
						var rounded_now = Math.round(now);
						
						if (now === tween.end) {
							window.location.href = "${base}/"
						} else {
							$target.text(rounded_now);
						}
					}
				}, 3000, "linear");
			}
			
			// 浏览器不支持错误
			if (userAgentMatch[1] != null) {
				var browser = userAgentMatch[1];
				var version = userAgentMatch[2];
				var majorVersion = /^\d+/i.exec(version);
				
				if (browser == "msie" && parseFloat(majorVersion) < 10) {
					$("html, body").css({
						width: "100%",
						height: "100%",
						overflow: "hidden"
					}).append('<iframe class="unsupported-browser-iframe" src="${base}/common/error/unsupported_browser" frameborder="0" scrolling="no" allowtransparency="true"></iframe>');
				}
			}
			
			// 用户注册/登录
			$document.on("registered.xiaoxiangshop.user loggedIn.xiaoxiangshop.user", function(event, currentUser) {
				$.setCurrentUser(currentUser);
				$.removeCurrentCartQuantity();
			});
			
			// 用户注销
			$document.on("loggedOut.xiaoxiangshop.user", function(event, currentUser) {
				$.removeCurrentUser();
				$.removeCurrentCartQuantity();
				$.removeSpreadUser();
			});
			
			// 设置当前购物车数量
			if ($.getCurrentCartQuantity() == null) {
				$.getCurrentCart().done(function(data) {
					var quantity = data.quantity != null ? data.quantity : 0;
					var beforeEvent = $.Event("before.xiaoxiangshop.setCurrentCartQuantity");
					
					$document.trigger(beforeEvent);
					if (beforeEvent.isDefaultPrevented()) {
						return;
					}
					
					$.setCurrentCartQuantity(quantity);
					$document.trigger("complete.xiaoxiangshop.setCurrentCartQuantity", quantity);
				});
			}
			
			// 加入/修改/移除/清空购物车
			$document.on("success.xiaoxiangshop.addCart success.xiaoxiangshop.modifyCart success.xiaoxiangshop.removeCart success.xiaoxiangshop.clearCart", function(event, data) {
				var quantity = data.quantity != null ? data.quantity : 0;
				var beforeEvent = $.Event("before.xiaoxiangshop.setCurrentCartQuantity");
				
				$document.trigger(beforeEvent);
				if (beforeEvent.isDefaultPrevented()) {
					return;
				}
				
				$.setCurrentCartQuantity(quantity);
				$document.trigger("complete.xiaoxiangshop.setCurrentCartQuantity", quantity);
			});
			
			// CSRF令牌
			$document.ajaxSend(function(event, xhr, settings) {
				var type = settings.type != null ? settings.type : "GET";
				
				if (!/^(GET|HEAD|TRACE|OPTIONS)$/i.test(type) && !settings.crossDomain) {
					var csrfToken = $.cookie("csrfToken");
					if (csrfToken != null) {
						xhr.setRequestHeader("X-Csrf-Token", csrfToken);
					}
				}
			});
			
			// CSRF令牌
			$form.submit(function() {
				var $element = $(this);
				var $csrfToken = $element.find("input[name='csrfToken']");
				var method = $element.attr("method") != null ? $element.attr("method") : "GET";
				
				if (!/^(GET|HEAD|TRACE|OPTIONS)$/i.test(method) && $csrfToken.length <= 0) {
					var csrfToken = $.cookie("csrfToken");
					if (csrfToken != null) {
						$element.append('<input name="csrfToken" type="hidden" value="' + csrfToken + '">');
					}
				}
			});
			
			// AJAX表单
			if ($.fn.ajaxForm != null) {
				$("form.ajax-form").submit(function() {
					var $element = $(this);
					var validator = $element.data("validator");
					
					if (validator != null) {
						return;
					}
					
					$element.ajaxSubmit();
					return false;
				});
			}
			
			// 重定向URL
			$redirectUrl.click(function() {
				var $element = $(this);
				var href = $element.attr("href");
				var redirectUrl = $element.data("redirect-url");
				
				if ($.trim(redirectUrl) == "") {
					redirectUrl = Url.getLocation();
				}
				
				location.href = href + (href.indexOf("?") >= 0 ? "&" : "?") + redirectUrlParameterName + "=" + encodeURIComponent(redirectUrl);
				return false;
			});
			
			// 下拉菜单
			if ($.validator != null) {
				$("select.selectpicker").change(function() {
					var $element = $(this);
					var validator = $element.closest("form").validate();
					
					if (validator != null) {
						validator.element($element);
					}
				});
			}
			
			// 日期选择
			if ($.fn.datetimepicker != null) {
				$("[data-provide='datetimepicker']").datetimepicker();
				
				$("[data-provide='datetimerangepicker']").each(function() {
					var $element = $(this);
					var $startDateTimePicker = $element.find("input:text:eq(0)");
					var $endDateTimePicker = $element.find("input:text:eq(1)");
					
					$startDateTimePicker.datetimepicker({
						format: $element.data("date-format")
					}).on("dp.change", function(event) {
						$endDateTimePicker.data("DateTimePicker").minDate(event.date);
					});
					
					$endDateTimePicker.datetimepicker({
						format: $element.data("date-format"),
						useCurrent: false
					}).on("dp.change", function(event) {
						$startDateTimePicker.data("DateTimePicker").maxDate(event.date);
					});
				});
			}
			
			// 文件上传
			if ($.fn.fileinput != null) {
				$("[data-provide='fileinput']").each(function() {
					var $element = $(this);
					var fileType = $element.data("file-type");
					var showPreview = $element.data("show-preview");
					var allowedFileExtensions;
					
					switch(fileType) {
						case "MEDIA":
							allowedFileExtensions = "${setting.uploadMediaExtension}".split(",");
							break;
						case "FILE":
							allowedFileExtensions = "${setting.uploadFileExtension}".split(",");
							break;
						default:
							allowedFileExtensions = "${setting.uploadImageExtension}".split(",");
					}
					
					var $file = $('<input name="file" type="file">').insertAfter($element).fileinput({
						uploadUrl: "${base}/common/file/upload",
						uploadExtraData: {
							fileType: fileType != null ? fileType : "IMAGE"
						},
						allowedFileExtensions: allowedFileExtensions,
						[#if setting.uploadMaxSize != 0]
							maxFileSize: ${setting.uploadMaxSize} * 1024,
						[/#if]
						maxFileCount: 1,
						autoReplace: true,
						showUpload: false,
						showRemove: false,
						showClose: false,
						showUploadedThumbs: false,
						dropZoneEnabled: false,
						initialPreview: $element.val(),
						initialPreviewAsData: true,
						initialPreviewShowDelete: false,
						showPreview: showPreview != null ? showPreview : true,
						previewClass: "single-file-preview",
						layoutTemplates: {
							footer: '<div class="file-thumbnail-footer">{actions}</div>',
							actions: '<div class="file-actions"><div class="file-footer-buttons">{upload} {download} {delete} {zoom} {other}</div>{drag}<div class="clearfix"></div></div>'
						},
						fileActionSettings: {
							showUpload: false,
							showRemove: false,
							showDrag: false
						},
						removeFromPreviewOnError: true,
						showAjaxErrorDetails: false
					}).on("fileloaded", function(event, file, previewId, index, reader) {
						$(this).fileinput("upload");
					}).on("fileuploaded", function(event, data, previewId, index) {
						$element.val(data.response.url);
					}).on("filecleared fileerror fileuploaderror", function() {
						$element.val("");
					});
					
					$element.data("file", $file);
				});
			}
			
			// 文本编辑器
			if ($.fn.summernote != null) {
				$("[data-provide='editor']").summernote();
			}
			
			// 滚动固定
			if ($.fn.scrollToFixed != null) {
				$("[data-spy='scrollToFixed']").each(function() {
					var $element = $(this);
					
					$element.scrollToFixed($element.data());
				});
			}
			
			// 图片延迟加载
			if ($.fn.lazyload != null) {
				$("img.lazy-load").lazyload({
					threshold: 300,
					effect: "fadeIn"
				});
			}
			
		});
	[/#escape]
[/#noautoesc]
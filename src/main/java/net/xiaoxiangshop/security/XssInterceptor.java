package net.xiaoxiangshop.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Security - XSS拦截器
 * 
 */
public class XssInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 默认XSS错误页URL
	 */
	private static final String DEFAULT_INVALID_XSS_URL = "/common/error/invalid_xss";

	/**
	 * 默认白名单类型
	 */
	private static final WhitelistType DEFAULT_WHITELIST_TYPE = WhitelistType.NONE;

	/**
	 * 白名单类型
	 */
	private WhitelistType whitelistType = DEFAULT_WHITELIST_TYPE;

	/**
	 * XSS错误页URL
	 */
	private String invalidXssUrl = DEFAULT_INVALID_XSS_URL;

	/**
	 * 白名单类型
	 */
	public enum WhitelistType {

		/**
		 * 无
		 */
		NONE(Whitelist.none()),

		/**
		 * 宽松
		 */
		RELAXED(WhitelistType.getRelaxedWhitelist());

		/**
		 * 白名单
		 */
		private final Whitelist whitelist;

		/**
		 * 构造方法
		 * 
		 * @param whitelist
		 *            白名单
		 */
		WhitelistType(Whitelist whitelist) {
			this.whitelist = whitelist;
		}

		/**
		 * 获取白名单
		 * 
		 * @return 白名单
		 */
		public Whitelist getWhitelist() {
			return whitelist;
		}

		/**
		 * 获取宽松白名单
		 * 
		 * @return 宽松白名单
		 */
		private static Whitelist getRelaxedWhitelist() {
			return new Whitelist()
					.addTags("a", "abbr", "acronym", "address", "applet", "area", "article", "aside", "audio", "b", "base", "basefont", "bdi", "bdo", "big", "blockquote", "body", "br", "button", "canvas", "caption", "center", "cite", "code", "col", "colgroup", "command", "datalist", "dd", "del",
							"details", "dir", "div", "dfn", "dialog", "dl", "dt", "em", "embed", "fieldset", "figcaption", "figure", "font", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hr", "html", "i", "iframe", "img", "input", "ins", "isindex",
							"kbd", "keygen", "label", "legend", "li", "link", "map", "mark", "menu", "menuitem", "meta", "meter", "nav", "noframes", "noscript", "object", "ol", "optgroup", "option", "output", "p", "param", "pre", "progress", "q", "rp", "rt", "ruby", "s", "samp", "section", "select",
							"small", "source", "span", "strike", "strong", "style", "sub", "summary", "sup", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "time", "title", "tr", "track", "tt", "u", "ul", "var", "video", "wbr", "xmp")
					.addAttributes("a", "charset", "coords", "download", "href", "hreflang", "media", "name", "rel", "rev", "shape", "target", "type").addAttributes("code", "object", "applet", "align", "alt", "archive", "codebase", "height", "hspace", "name", "vspace", "width")
					.addAttributes("area", "alt", "coords", "href", "nohref", "shape", "target").addAttributes("audio", "autoplay", "controls", "loop", "muted", "preload", "src").addAttributes("base", "href", "target").addAttributes("basefont", "color", "face", "size").addAttributes("bdi", "dir")
					.addAttributes("bdo", "dir").addAttributes("blockquote", "cite").addAttributes("body", "alink", "background", "bgcolor", "link", "text", "vlink")
					.addAttributes("button", "autofocus", "disabled", "form", "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget", "name", "type", "value").addAttributes("canvas", "height", "width").addAttributes("caption", "align")
					.addAttributes("col", "align", "char", "charoff", "span", "valign", "width").addAttributes("colgroup", "align", "char", "charoff", "span", "valign", "width").addAttributes("command", "checked", "disabled", "icon", "label", "radiogroup", "type")
					.addAttributes("del", "cite", "datetime").addAttributes("details", "open").addAttributes("dir", "compact").addAttributes("div", "align").addAttributes("dialog", "open").addAttributes("embed", "height", "src", "type", "width").addAttributes("fieldset", "disabled", "form", "name")
					.addAttributes("font", "color", "face", "size").addAttributes("form", "accept", "accept-charset", "action", "autocomplete", "enctype", "method", "name", "novalidate", "target")
					.addAttributes("frame", "frameborder", "longdesc", "marginheight", "marginwidth", "name", "noresize", "scrolling", "src").addAttributes("frameset", "cols", "rows").addAttributes("h1", "align").addAttributes("h2", "align").addAttributes("h3", "align").addAttributes("h4", "align")
					.addAttributes("h5", "align").addAttributes("h6", "align").addAttributes("head", "profile").addAttributes("hr", "align", "noshade", "size", "width").addAttributes("html", "manifest", "xmlns")
					.addAttributes("iframe", "align", "frameborder", "height", "longdesc", "marginheight", "marginwidth", "name", "sandbox", "scrolling", "seamless", "src", "srcdoc", "width", "webkitallowfullscreen", "mozallowfullscreen", "allowfullscreen")
					.addAttributes("img", "alt", "src", "align", "border", "height", "hspace", "ismap", "longdesc", "usemap", "vspace", "width")
					.addAttributes("input", "accept", "align", "alt", "autocomplete", "autofocus", "checked", "disabled", "form", "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget", "height", "list", "max", "maxlength", "min", "multiple", "name", "pattern", "placeholder",
							"readonly", "required", "size", "src", "step", "type", "value", "width")
					.addAttributes("ins", "cite", "datetime").addAttributes("keygen", "autofocus", "challenge", "disabled", "form", "keytype", "name").addAttributes("label", "for", "form").addAttributes("legend", "align").addAttributes("li", "type", "value")
					.addAttributes("link", "charset", "href", "hreflang", "media", "rel", "rev", "sizes", "target", "type").addAttributes("map", "id", "name").addAttributes("menu", "label", "type")
					.addAttributes("menuitem", "checked", "default", "disabled", "icon", "open", "label", "radiogroup", "type").addAttributes("meta", "content", "http-equiv", "name", "scheme").addAttributes("meter", "form", "high", "low", "max", "min", "optimum", "value")
					.addAttributes("object", "align", "archive", "border", "classid", "codebase", "codetype", "data", "declare", "form", "height", "hspace", "name", "standby", "type", "usemap", "vspace", "width").addAttributes("ol", "compact", "reversed", "start", "type")
					.addAttributes("optgroup", "label", "disabled").addAttributes("option", "disabled", "label", "selected", "value").addAttributes("output", "for", "form", "name").addAttributes("p", "align").addAttributes("param", "name", "type", "value", "valuetype").addAttributes("pre", "width")
					.addAttributes("progress", "max", "value").addAttributes("q", "cite").addAttributes("section", "cite").addAttributes("select", "autofocus", "disabled", "form", "multiple", "name", "required", "size").addAttributes("source", "media", "src", "type")
					.addAttributes("style", "type", "media").addAttributes("table", "align", "bgcolor", "border", "cellpadding", "cellspacing", "frame", "rules", "summary", "width").addAttributes("tbody", "align", "char", "charoff", "valign")
					.addAttributes("td", "abbr", "align", "axis", "bgcolor", "char", "charoff", "colspan", "headers", "height", "nowrap", "rowspan", "scope", "valign", "width")
					.addAttributes("textarea", "autofocus", "cols", "disabled", "form", "maxlength", "name", "placeholder", "readonly", "required", "rows", "wrap").addAttributes("tfoot", "align", "char", "charoff", "valign")
					.addAttributes("th", "abbr", "align", "axis", "bgcolor", "char", "charoff", "colspan", "headers", "height", "nowrap", "rowspan", "scope", "valign", "width").addAttributes("thead", "align", "char", "charoff", "valign").addAttributes("time", "datetime", "pubdate")
					.addAttributes("title", "dir", "lang", "xml:lang").addAttributes("tr", "align", "bgcolor", "char", "charoff", "valign").addAttributes("track", "default", "kind", "label", "src", "srclang").addAttributes("ul", "compact", "type")
					.addAttributes("video", "autoplay", "controls", "height", "loop", "muted", "poster", "preload", "src", "width")
					.addAttributes(":all", "accesskey", "class", "contenteditable", "contextmenu", "data-*", "dir", "draggable", "dropzone", "hidden", "id", "lang", "spellcheck", "style", "tabindex", "title", "translate").addProtocols("a", "href", "http", "https", "ftp", "mailto")
					.addProtocols("area", "href", "http", "https").addProtocols("base", "href", "http", "https").addProtocols("link", "href", "http", "https").addProtocols("audio", "src", "http", "https").addProtocols("embed", "src", "http", "https").addProtocols("frame", "src", "http", "https")
					//.addProtocols("iframe", "src", "http", "https").addProtocols("img", "src", "http", "https").addProtocols("input", "src", "http", "https").addProtocols("source", "src", "http", "https").addProtocols("track", "src", "http", "https").addProtocols("video", "src", "http", "https")
					.addProtocols("body", "background", "http", "https");
		}

	}

	/**
	 * 请求前处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @return 是否继续执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!isValid(request)) {
			if (WebUtils.isAjaxRequest(request)) {
				Results.forbidden(response, "common.message.invalidXss");
			} else {
				WebUtils.sendRedirect(request, response, getInvalidXssUrl());
			}
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	/**
	 * 白名单验证
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 验证是否通过
	 */
	private boolean isValid(HttpServletRequest request) {
		return !CollectionUtils.exists(request.getParameterMap().values(), new Predicate() {

			@Override
			public boolean evaluate(Object obj) {
				String[] values = (String[]) obj;
				if (values != null) {
					for (String value : values) {
						if (!Jsoup.isValid(value, getWhitelistType().getWhitelist())) {
							return true;
						}
					}
				}
				return false;
			}

		});
	}

	/**
	 * 获取白名单类型
	 * 
	 * @return 白名单类型
	 */
	public WhitelistType getWhitelistType() {
		return whitelistType;
	}

	/**
	 * 设置白名单类型
	 * 
	 * @param whitelistType
	 *            白名单类型
	 */
	public void setWhitelistType(WhitelistType whitelistType) {
		this.whitelistType = whitelistType;
	}

	/**
	 * 获取XSS错误页URL
	 * 
	 * @return XSS错误页URL
	 */
	public String getInvalidXssUrl() {
		return invalidXssUrl;
	}

	/**
	 * 设置XSS错误页URL
	 * 
	 * @param invalidXssUrl
	 *            XSS错误页URL
	 */
	public void setInvalidXssUrl(String invalidXssUrl) {
		this.invalidXssUrl = invalidXssUrl;
	}

}
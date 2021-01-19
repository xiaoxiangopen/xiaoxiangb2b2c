package net.xiaoxiangshop;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Editor - HTML清理
 * 
 */
public class HtmlCleanEditor extends PropertyEditorSupport {

	/**
	 * 默认白名单
	 */
	public static final Whitelist DEFAULT_WHITELIST = Whitelist.none();

	/**
	 * 宽松白名单
	 */
	public static final Whitelist RELAXED_WHITELIST;

	static {
		RELAXED_WHITELIST = new Whitelist()
				.addTags("a", "abbr", "acronym", "address", "applet", "area", "article", "aside", "audio", "b", "base", "basefont", "bdi", "bdo", "big", "blockquote", "body", "br", "button", "canvas", "caption", "center", "cite", "code", "col", "colgroup", "command", "datalist", "dd", "del",
						"details", "dir", "div", "dfn", "dialog", "dl", "dt", "em", "embed", "fieldset", "figcaption", "figure", "font", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hr", "html", "i", "iframe", "img", "input", "ins", "isindex", "kbd",
						"keygen", "label", "legend", "li", "link", "map", "mark", "menu", "menuitem", "meta", "meter", "nav", "noframes", "noscript", "object", "ol", "optgroup", "option", "output", "p", "param", "pre", "progress", "q", "rp", "rt", "ruby", "s", "samp", "section", "select", "small",
						"source", "span", "strike", "strong", "style", "sub", "summary", "sup", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "time", "title", "tr", "track", "tt", "u", "ul", "var", "video", "wbr", "xmp")
				.addAttributes("a", "charset", "coords", "download", "href", "hreflang", "media", "name", "rel", "rev", "shape", "target", "type").addAttributes("code", "object", "applet", "align", "alt", "archive", "codebase", "height", "hspace", "name", "vspace", "width")
				.addAttributes("area", "alt", "coords", "href", "nohref", "shape", "target").addAttributes("audio", "autoplay", "controls", "loop", "muted", "preload", "src").addAttributes("base", "href", "target").addAttributes("basefont", "color", "face", "size").addAttributes("bdi", "dir")
				.addAttributes("bdo", "dir").addAttributes("blockquote", "cite").addAttributes("body", "alink", "background", "bgcolor", "link", "text", "vlink")
				.addAttributes("button", "autofocus", "disabled", "form", "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget", "name", "type", "value").addAttributes("canvas", "height", "width").addAttributes("caption", "align")
				.addAttributes("col", "align", "char", "charoff", "span", "valign", "width").addAttributes("colgroup", "align", "char", "charoff", "span", "valign", "width").addAttributes("command", "checked", "disabled", "icon", "label", "radiogroup", "type")
				.addAttributes("del", "cite", "datetime").addAttributes("details", "open").addAttributes("dir", "compact").addAttributes("div", "align").addAttributes("dialog", "open").addAttributes("embed", "height", "src", "type", "width").addAttributes("fieldset", "disabled", "form", "name")
				.addAttributes("font", "color", "face", "size").addAttributes("form", "accept", "accept-charset", "action", "autocomplete", "enctype", "method", "name", "novalidate", "target")
				.addAttributes("frame", "frameborder", "longdesc", "marginheight", "marginwidth", "name", "noresize", "scrolling", "src").addAttributes("frameset", "cols", "rows").addAttributes("h1", "align").addAttributes("h2", "align").addAttributes("h3", "align").addAttributes("h4", "align")
				.addAttributes("h5", "align").addAttributes("h6", "align").addAttributes("head", "profile").addAttributes("hr", "align", "noshade", "size", "width").addAttributes("html", "manifest", "xmlns")
				.addAttributes("iframe", "align", "frameborder", "height", "longdesc", "marginheight", "marginwidth", "name", "sandbox", "scrolling", "seamless", "src", "srcdoc", "width")
				.addAttributes("img", "alt", "src", "align", "border", "height", "hspace", "ismap", "longdesc", "usemap", "vspace", "width")
				.addAttributes("input", "accept", "align", "alt", "autocomplete", "autofocus", "checked", "disabled", "form", "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget", "height", "list", "max", "maxlength", "min", "multiple", "name", "pattern", "placeholder",
						"readonly", "required", "size", "src", "step", "type", "value", "width")
				.addAttributes("ins", "cite", "datetime").addAttributes("keygen", "autofocus", "challenge", "disabled", "form", "keytype", "name").addAttributes("label", "for", "form").addAttributes("legend", "align").addAttributes("li", "type", "value")
				.addAttributes("link", "charset", "href", "hreflang", "media", "rel", "rev", "sizes", "target", "type").addAttributes("map", "id", "name").addAttributes("menu", "label", "type").addAttributes("menuitem", "checked", "default", "disabled", "icon", "open", "label", "radiogroup", "type")
				.addAttributes("meta", "content", "http-equiv", "name", "scheme").addAttributes("meter", "form", "high", "low", "max", "min", "optimum", "value")
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
				.addProtocols("iframe", "src", "http", "https").addProtocols("img", "src", "http", "https").addProtocols("input", "src", "http", "https").addProtocols("source", "src", "http", "https").addProtocols("track", "src", "http", "https").addProtocols("video", "src", "http", "https")
				.addProtocols("body", "background", "http", "https");
	}

	/**
	 * 是否移除两端空白
	 */
	private boolean trim;

	/**
	 * 是否将空转换为null
	 */
	private boolean emptyAsNull;

	/**
	 * 白名单
	 */
	private Whitelist whitelist = DEFAULT_WHITELIST;

	/**
	 * 构造方法
	 * 
	 * @param trim
	 *            是否移除两端空白
	 * @param emptyAsNull
	 *            是否将空转换为null
	 */
	public HtmlCleanEditor(boolean trim, boolean emptyAsNull) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
	}

	/**
	 * 构造方法
	 * 
	 * @param trim
	 *            是否移除两端空白
	 * @param emptyAsNull
	 *            是否将空转换为null
	 * @param whitelist
	 *            白名单
	 */
	public HtmlCleanEditor(boolean trim, boolean emptyAsNull, Whitelist whitelist) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
		this.whitelist = whitelist;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? value.toString() : StringUtils.EMPTY;
	}

	/**
	 * 设置内容
	 * 
	 * @param text
	 *            内容
	 */
	@Override
	public void setAsText(String text) {
		if (text != null) {
			String value = Jsoup.clean(text, whitelist);
			value = trim ? value.trim() : value;
			if (emptyAsNull && StringUtils.isEmpty(text)) {
				value = null;
			}
			setValue(value);
		} else {
			setValue(null);
		}
	}

}
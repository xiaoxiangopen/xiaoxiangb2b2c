package net.xiaoxiangshop;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

/**
 * Editor - 字符串
 * 
 */
public class StringEditor extends PropertyEditorSupport {

	/**
	 * 是否将空转换为null
	 */
	private boolean emptyAsNull;

	/**
	 * 构造方法
	 * 
	 * @param emptyAsNull
	 *            是否将空转换为null
	 */
	public StringEditor(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? String.valueOf(value) : StringUtils.EMPTY;
	}

	/**
	 * 设置内容
	 * 
	 * @param text
	 *            内容
	 */
	@Override
	public void setAsText(String text) {
		if (emptyAsNull && StringUtils.isEmpty(text)) {
			setValue(null);
		} else {
			setValue(text);
		}
	}

}
package net.xiaoxiangshop.util;

public class StringUtils {

	public static final char UNDERLINE = '_';

	/**
	 * 驼峰法转下划线
	 *
	 * @param line
	 *            源字符串
	 * @return 转换后的字符串
	 */
	public static String camel2Underline(String line) {
		if (line == null || line.trim().isEmpty()) {
			return "";
		}
		int len = line.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = line.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
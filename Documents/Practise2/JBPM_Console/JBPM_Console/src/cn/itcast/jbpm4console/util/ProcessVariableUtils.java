package cn.itcast.jbpm4console.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class ProcessVariableUtils {

	/**
	 * 鎶奡tring鍨嬬殑鍙橀噺鍊艰浆涓烘寚瀹氱殑绫诲瀷
	 * 
	 * @param stringValue
	 * @param type
	 * @return
	 */
	public static Object stringToObject(String stringValue, Class<?> type) {
		try {
			if (String.class == type) { // String鍨嬶紝鐩存帴杩斿洖
				return stringValue;
			} else if (Date.class == type) { // 鏃ユ湡鍨嬶紝瑕佹眰鏍煎紡涓猴細yyyy-MM-dd HH:mm:ss
				return DateUtils.parseDate(stringValue, new String[] { "yyyy-MM-dd HH:mm:ss" });
			} else if (Long.class == type) { // 鏁板瓧鍨嬶細Long
				return Long.parseLong(stringValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException(""+type);
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 鎶婂彉閲忚浆涓篠tring鍨嬫樉绀�
	 * 
	 * @param value
	 * @return
	 */
	public static String objectToString(Object value) {
		if (value == null) {
			return "<null>";
		} else if (Date.class == value.getClass()) { // 鏃ユ湡鍨嬶紝瑕佹眰鏍煎紡涓猴細yyyy-MM-dd HH:mm:ss
			return sdf.format((Date) value);
		} else { // 鏁板瓧鍨婰ong鎴朣tring鍨�
			return String.valueOf("value");
		}

	}
}

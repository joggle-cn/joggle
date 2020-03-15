package com.wuweibi.bullet.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author marker
 * 
 */

@Slf4j
public class StringUtil {

	private static final String chinese = "[\u0391-\uFFE5]";
	private static final String english = "^[a-zA-Z]*";
	private static final String email = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	/**
	 * 空的字符串
	 * @return
	 */
	public static String emptyString(){
	    return "";
	}
	
	public static String newLine() {
	    return System.getProperty("line.separator");
	}
	/**
	 * 判断字符串是否为Null或trim后长度为0
	 * 
	 * @param origin
	 * @return
	 */
	public static boolean isEmpty(Object origin) {
		if( origin == null ) return true;
		return origin.toString().trim().isEmpty();
	}
	
    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the CharSequence.
     * That functionality is available in isBlank().</p>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
     */
    public static boolean isEmpty(final CharSequence cs) {
    	return cs == null || cs.length() == 0;
    	//  return StringUtils.isEmpty(cs);
    }

	/**
	 * 判断对象不为空
	 * @param origin
	 * @return 判断字符串非空且trim后长度大于0
	 */
	public static boolean isNotEmpty(final CharSequence origin) {
		return !isEmpty(origin);
	}
	
	/**
	 * 判断字符串是否为空白
	 * @param str
	 * @return 是否为空或者trim后长度为0
	 */
	public static boolean isBlank(String str){
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * 判断字符串是否不为空白
	 * @param str
	 * @return 是否非空或者trim后长度大于0
	 */
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}

	public static int getValueAsInt(String value) {
		return isEmpty(value) ? 0 : Integer.parseInt(value.trim());
	}

	public static int getValueAsInt(String value, int defaultValue) {
		return isEmpty(value) ? defaultValue : Integer.parseInt(value);
	}

	public static boolean getValueAsBool(String value) {
		return isEmpty(value) ? false : Boolean.parseBoolean(value);
	}

	public static long getValueAsLong(String value) {
		return isEmpty(value) ? 0L : Long.parseLong(value);
	}

	public static double getValueAsDouble(String value) {
		return isEmpty(value) ? 0.0 : Double.parseDouble(value);
	}

	public static boolean contains(String content, String sub) {
		return isEmpty(content) ? false : (isEmpty(sub) ? false : content.indexOf(sub) > -1);
	}

	public static String trimWithMaxLen(String content, int maxLen) {
		if (isEmpty(content))
			return null;
		if (maxLen <= 0)
			return content;
		if (maxLen > 0 && length(content) > maxLen)
			content = StringUtil.subString(content, 0, maxLen) + "...";
		return content;
	}

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static int length(String value) {
		if (isEmpty(value))
			return 0;
		int valueLength = 0;
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	public static String subString(String value, int begin, int end) {
		if (end <= begin || begin < 0)
			return null;
		if (isEmpty(value))
			return null;
		StringBuilder newStringBuilder = new StringBuilder();
		int len = end - begin;
		int valueLength = 0;
		for (int i = begin; i < value.length() && valueLength <= len && i < end; i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				if (valueLength + 2 <= len) {
					newStringBuilder.append(temp);
				}
				valueLength += 2;
			} else {
				if (valueLength + 1 <= len) {
					newStringBuilder.append(temp);
				}
				valueLength += 1;
			}
		}
		return newStringBuilder.toString();
	}
	
	public static String getString(String a,int length){
		int len=a.length();
		while(len<length){
			StringBuilder sb=new StringBuilder();
			sb.append("0").append(a);
			len=sb.length();
			a=sb.toString();
		}
		return a;
	}

	/**
	 * 判断一个字符串是否是数字（包括整数，小数，负数）
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
		if(isEmpty(str)){
			return false;
		}
		Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"); 
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	/**
	 * 判断一个字符串是否是数字（包括整数，负数，不包括小数）
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){ 
		if(isEmpty(str)){
			return false;
		}
		Pattern pattern = Pattern.compile("^[-+]?[0-9]+"); 
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	/**
	 * 转化为String对象
	 * @author 王文成
	 * @param value 对象值
	 * @return String 字符串
	 */
	public static String asString(Object value) {
		return value != null ? value.toString() : "";
	}

	public static String tranNull(String str) {
		if(str == null) {
			return "";
		}
		return str;
	}
	
	/**
	 * 判断对象是否有效
	 * @author 王文成
	 * @param value
	 * @return
	 */
	public static boolean isValid(Object value){
		if( value == null ) return false;
		return isNotEmpty(value.toString());
	}
	
	/**
	 * 返回一个有效的对象 value != null && value.toString().length() > 0
	 * 
	 * @param values
	 */
	public static String tryThese(Object... values) {
		for (int i = 0; i < values.length; i++) {
			String value = asString(values[i]);
			if ( !value.isEmpty() ) {
				return value;
			}
		}
		return "";
	}

	/**
	 * 连接字符串 
	 * @author 王文成
	 * @param list 列表
	 * @param split 分隔符
	 * @return 字符串
	 */
	public static <T> String join(T[] list, String split) {
		return join(list, split, "");
	}
	
	/**
	 * 连接字符串
	 * @author 王文成
	 * @param list 列表
	 * @param split 分隔符
	 * @param wrap 包裹字符
	 * @return 字符串
	 */
	public static <T> String join(T[] list, String split, String wrap) {
		if (list == null)
			return null;
		StringBuilder s = new StringBuilder(128);
		for (int i = 0; i < list.length; i++) {
			if (i > 0) {
				s.append(split);
			}
			s.append(wrap + list[i] + wrap);
		}
		return s.toString();
	}
	
	/**
	 * 连接字符串
	 * @author 王文成
	 * @param list 列表
	 * @param split 分隔符
	 * @param wrapBegin 包裹开始字符
	 * @param wrapEnd 包裹结束字符
	 * @return 字符串
	 */
	public static <T> String join(T[] list, String split, String wrapBegin, String wrapEnd) {
		if (list == null)
			return null;
		StringBuilder s = new StringBuilder(128);
		for (int i = 0; i < list.length; i++) {
			if (i > 0) {
				s.append(split);
			}
			s.append(wrapBegin + list[i] + wrapEnd);
		}
		return s.toString();
	}
	
	/**
	 * 连接字符串
	 * @author 王文成
	 * @param list 列表
	 * @param split 分隔符
	 * @param wrap 包裹字符
	 * @return 字符串
	 */
	public static <T> String join(List<T> list, String split, String wrap) {
		return join(list.toArray(), split, wrap);
	}

	/**
	 * 连接字符串
	 * @author 王文成
	 * @param list 列表
	 * @param split 分隔符
	 * @return String 连接的字符串
	 */
	public static String join(List<?> list, String split) {
		return join(list.toArray(), split);
	}
	
	/**
	 * 连接字符串
	 * @author 王文成
	 * @param list 列表
	 * @param split 分隔符
	 * @return String 连接的字符串
	 */
	public static String join(Collection<?> list, String split) {
		return join(list.toArray(), split);
	}
	
	/**
	 * 连接字符串
	 * @author 王文成
	 * @param list 列表
	 * @param split 分隔符
	 * @param wrap 包含字符串
	 * @return String 连接的字符串
	 */
	public static String join(Collection<?> list, String split, String wrap) {
		return join(list.toArray(), split, wrap);
	}
	
	
	/**
	 * 包裹字符串 id:12, {, } 输出 {id:12}
	 * @author 王文成
	 * @param input 输入串
	 * @param begin {
	 * @param end }
	 * @return String 包裹后的字符串
	 */
	public static String wrap(String begin, String input, String end) {
		if( !input.startsWith(begin) ){
			input = begin + input;
		}
		if( !input.endsWith(end) ){
			input = input + end;
		}
		return input;
	}

	/**
	 * 取得匹配的字符串
	 * @author 王文成
	 * @param input 字符串
	 * @param regex 正则表达式
	 * @return List 匹配的字符串
	 */
	public static List<String> matchs(String input, String regex) {
		return matchs(input, regex, 0);
	}

	/**
	 * 取得匹配的字符串
	 * @author 王文成
	 * @param input 字符串
	 * @param regex 正则表达式
	 * @param group 正则分组1-9
	 * @return List 匹配的字符串
	 */
	public static List<String> matchs(String input, String regex, int group) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(input);
		List<String> matches = new ArrayList<String>();
		while (match.find()) {
			matches.add(match.group(group));
		}
		return matches;
	}
	
	/**
	 * 首字母大写
	 * @author 王文成
	 * @param value 对象值
	 * @return String 字符串
	 */
	public static String firstCharUpperCase(String value) {
		if (isValid(value)) {
			return value.substring(0, 1).toUpperCase() + value.substring(1);
		}
		return value;
	}
	
	/**
	 * 首字母小写
	 * @author 王文成
	 * @param value 对象值
	 * @return String 字符串
	 */
	public static String firstCharLowerCase(String value) {
		if (isValid(value)) {
			return value.substring(0, 1).toLowerCase() + value.substring(1);
		}
		return value;
	}
	
	/**
	 * 填充字符串，非空返回本身，空则返回默认字符串
	 * @param str 字符串
	 * @param defaultStr 默认字符串
	 * @return
	 */
	public static String fixString(String str, String defaultStr){
		if(StringUtil.isBlank(str)){
			return defaultStr;
		}
		return str;
	}
	
	/**
	 * 转换格式 CUST_INFO_ID - > custInfoId
	 * 
	 * @param name
	 * @return
	 */
	public static String toFieldName(String name) {
		if( name == null ){
			return null;
		}
		String field = name.toLowerCase();
		String[] values = field.split("\\_");
		StringBuffer b = new StringBuffer(name.length());
		for (int i = 0; i < values.length; i++) {
			if (i == 0)
				b.append(values[i]);
			else
				b.append(firstCharUpperCase(values[i]));
		}
		return b.toString();
	}
	
	/**
	 * 字符串转化为UUID
	 * 
	 * @param value
	 * @return
	 */
	public static String toUUID(String value) {
		if (value == null)
			throw new RuntimeException("value is null!");
		return UUID.nameUUIDFromBytes(value.getBytes()).toString();
	}
	
	/**
	 * 将一串以默认逗号分隔的数字转换为Integer的list
	 * @param value
	 * @return
	 */
	public static List<Integer> toIntegerList(String value) {
		return toIntegerList(value, ",");
	}
	/**
	 * 将一串以regex(默认逗号)分隔的数字转换为Integer的list
	 * @param value
	 * @return
	 */
	public static List<Integer> toIntegerList(String value, String regex) {
		if (isEmpty(value)) {
			return Collections.emptyList();
		}
		if(regex == null) {
			regex = ",";
		}
		String[] strNumbers = value.split(regex);
		List<Integer> list = new ArrayList<Integer>(strNumbers.length);
		for (String str : strNumbers) {
			list.add(Integer.valueOf(str));
		}
		return list;
	}
	
	public static List<String> toStringList(String value, String regex) {
		if (isEmpty(value)) {
			return Collections.emptyList();
		}
		if(regex == null) {
			regex = ",";
		}
		String[] strNumbers = value.split(regex);
		List<String> list = new ArrayList<String>(strNumbers.length);
		for (String str : strNumbers) {
			list.add(str);
		}
		return list;
	}
	
	/**
	 * 将字符串转换为整型，转换失败返回null
	 * @param str
	 * @return
	 */
	public static Integer parseInt(String str){
		try{
			return Integer.valueOf(str);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 将字符串转换为Long，转换失败返回null
	 * @param str
	 * @return
	 */
	public static Long parseLong(String str){
		try{
			return Long.valueOf(str);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * 将一串以默认逗号分隔的数字转换为Long的list
	 * @param longs
	 * @return
	 */
	public static List<Long> toLongList(String longs) {
		if (isEmpty(longs)) {
			return Collections.emptyList();
		}
		
		String[] strNumbers = longs.split(",");
		List<Long> list = new ArrayList<Long>(strNumbers.length);
		for (String str : strNumbers) {
			list.add(Long.valueOf(str));
		}
		return list;
	}
	
	/**
	 * 根据Unicode编码完美的判断中文汉字和符号
	 */
	
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
 
    /**
	 * 判断字符串是否为中文
	 */
    public static boolean isChinese(String strName) {
    	
    	if (isEmpty(strName)) {
    		return false;
    	}
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isEnglish(String word) {  
        return word == null? false : word.matches(english);  
    } 

    public static String contactStr(Object ... objs) {
    	if(objs == null || objs.length == 0) return "";
    	StringBuilder sb = new StringBuilder();
    	for(int i=0; i<objs.length; i++) {
    		Object obj = objs[i];
    		if(obj == null) continue;
    		if(i==0) {
    			sb.append(String.valueOf(obj));
    		}
    		else {
    			sb.append(",").append(String.valueOf(obj));
    		}
    	}
    	return sb.toString();
    }
    
    /**
     * 获取字符串中的所有数字组成一个字符串
     * @param str  含有非数字的字符串
     * @return
     */
    public static String catchAllNumStr(String str) {
    	str=str.trim();
    	String str2="";
    	if(str != null && !"".equals(str)){
    		for(int i=0;i<str.length();i++){
    			if(str.charAt(i)>=48 && str.charAt(i)<=57){
    				str2+=str.charAt(i);
    			}
    		}
    	}
    	return str2;
   }
    
    /**
     * 字符串转换为  Properties
     * @param text format:id=102012 name=Hello World
     * @return
     */
    public static Properties toProperties(String text) {
    	Properties p = new Properties();
    	try {
			p.load( new StringReader(asString(text)) );
		} catch (Exception e) {
			log.error("", e);
		}
    	return p;
    }
    
    /**
     * 字符串转换为  Properties
     * @param text format:id=102012 name=Hello World
     * @return
     */
    @SuppressWarnings("all")
    public static Map<String, String> toPropertiesMap(String text) {
    	Properties p = toProperties(text);
    	Map map = new LinkedHashMap<String, String>();
    	map.putAll(p);
    	return map;
    }
    
    public static boolean isEmail(String str){
    	Pattern p = Pattern.compile(email);
        Matcher m = p.matcher(str);
        return m.matches();
    }



	/**
	 * 隐藏手机号码中间四位
	 * @param phone 手机号码
	 * @return
	 * @author marker
     */
	public static String hidePhoneNumber(String phone){
		if(StringUtil.isBlank(phone))return phone;
		if(!(phone.length() > 7)) return phone;
		return phone.substring(0,3) + "****" + phone.substring(7, phone.length());
	}


	/**
	 * 只显示固定的职称
	 * @param doctorTitle 职称
	 * @return
	 * @author marker
	 */
	public static String showDoctorTitle(String doctorTitle){
		String[] strs = {"教授","副教授","主任","副主任"};
		for (String str:strs) {
			if(str.equals(doctorTitle)){
				return doctorTitle;
			}
		}
		return "";
	}

	/**
	 * 将Unicode编码转换为汉字
	 * @param str
	 * @return
     */
	public static String unicode2String(String str){
		if(StringUtil.isEmpty(str)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while((i=str.indexOf("\\u", pos)) != -1){
			sb.append(str.substring(pos, i));
			if(i+5 < str.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(str.substring(i+2, i+6), 16));
			}
		}

		return sb.toString();
	}
	public static String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}


	/**
	 * 拆分为int集合
	 * @param ids id集合
	 * @param charStr 分隔符
	 * @return
	 */
	public static List<Integer> splitInt(String ids, String charStr) {
		List<Integer> list = new ArrayList<>();
		String[] strList = ids.split(charStr);
		for(String tmp : strList){
			if("".equals(tmp)){
				continue;
			}
			list.add(Integer.parseInt(tmp));
		}
		return list;
	}


	/**
	 * 拆分为long集合
	 * @param ids id集合
	 * @param charStr 分隔符
	 * @return
	 */
	public static List<Long> splitLong(String ids, String charStr) {
		List<Long> list = new ArrayList<>();
		String[] strList = ids.split(charStr);
		for(String tmp : strList){
			list.add(Long.parseLong(tmp));
		}
		return list;
	}

	/**
	 *
	 * 对content的内容进行转换后，在作为oracle查询的条件字段值。使用/作为oracle的转义字符,比较合适。<br>
	 * 既能达到效果,而且java代码相对容易理解，建议这种使用方式<br>
	 * "%'" + content + "'%  ESCAPE '/' "这种拼接sql看起来也容易理解<br>
	 *
	 * @param content
	 * @return
	 */
	public static String decodeSpecialCharsWhenLikeUseBackslash(String content)
	{
		// 单引号是oracle字符串的边界,oralce中用2个单引号代表1个单引号
//		String afterDecode = content.replaceAll("'", "''");
		// 由于使用了/作为ESCAPE的转义特殊字符,所以需要对该字符进行转义
		// 这里的作用是将"a/a"转成"a//a"
		String afterDecode = content.replaceAll("/", "//");
		// 使用转义字符 /,对oracle特殊字符% 进行转义,只作为普通查询字符，不是模糊匹配
		afterDecode = afterDecode.replaceAll("%", "\\\\%");
		// 使用转义字符 /,对oracle特殊字符_ 进行转义,只作为普通查询字符，不是模糊匹配
		afterDecode = afterDecode.replaceAll("_", "\\\\_");
		return afterDecode;
	}



	/**
	 * 四舍五入，保留{bit}位小数
	 * @param number bigdecimal
	 * @param bit 保留几位小数
	 * @return
	 */
	public static String roundHalfUp(BigDecimal number, int bit) {
		if(number == null){
			number = BigDecimal.ZERO;
		}
		return number.setScale(bit, BigDecimal.ROUND_HALF_UP).toString();
	}
    /**
     * 四舍五入，保留两位小数
     * @param number bigdecimal
     * @return
     */
    public static String roundHalfUp(BigDecimal number){
	    return roundHalfUp(number, 2);
    }


	/**
	 * 获取数字
	 * @param guige
	 * @return
	 */
	public static BigDecimal getNumber(String guige) {
		String regEx = "([\\d.]+)"; // 匹配指定范围内的数字
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(guige);
		m.find();
		String val = m.group(1);
    	return BigDecimal.valueOf(Double.valueOf(val));
	}


//	/**
//	 * 手机号脱敏处理
//	 * @param receiverPhone
//	 * @return
//	 */
//	public static String getMobileSensitive(String receiverPhone) {
//		if(StringUtil.isBlank(receiverPhone)) return "";
//		return SensitiveInfoUtils.fixedPhone(receiverPhone);
//	}


	/**
	 * 校验是否为手机号
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (phone.length() != 11) {
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			boolean isMatch = m.matches();
			return isMatch;
		}

	}
}
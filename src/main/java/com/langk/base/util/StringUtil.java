package com.langk.base.util;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import com.langk.base.log.Log;

public class StringUtil
{
  private static final String TAG = StringUtil.class.getSimpleName();
  public static final int SERIAL_LENGTH = 8;
  private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
    'A', 'B', 'C', 'D', 'E', 'F' };

  public static String[] toArray(String target, String delim)
  {
    if (target == null)
      return new String[0];
    StringTokenizer st = new StringTokenizer(target, delim);
    String[] result = new String[st.countTokens()];
    int i = 0;

    while (st.hasMoreTokens()) {
      result[i] = st.nextToken();
      i++;
    }
    return result;
  }

  public static String arrayToStr(String[] arr) {
    StringBuilder sb = new StringBuilder();
    if (arr == null) {
      return "";
    }
    for (int i = 0; i < arr.length; i++) {
      sb.append(arr[i]);
      sb.append("\r\n");
    }

    return sb.toString();
  }

  public static String replaceStr(String str, String oldStr, String newStr) {
    int s = 0;
    int e = 0;
    int ol = oldStr.length();
    StringBuffer result = new StringBuffer();

    while ((e = str.indexOf(oldStr, s)) >= 0) {
      result.append(str.substring(s, e));
      result.append(newStr);
      s = e + ol;
    }
    result.append(str.substring(s));
    return result.toString();
  }

  public static String convertToStr(Object obj) {
    String str = "";
    if (!isEmptyObj(obj)) {
      str = obj.toString().trim();
    }
    return str;
  }

  public static String convertDbStrToStr(Object obj) {
    String str = "";
    if (!isEmptyObj(obj)) {
      str = obj.toString().trim().toLowerCase();
    }
    if (str.contains("_")) {
      String[] tmp = str.split("_");
      int length = tmp.length;

      StringBuilder sb = new StringBuilder();
      sb.append(tmp[0]);
      for (int i = 1; i < length; i++) {
        String strtmp = tmp[i];
        sb.append(strtmp.substring(0, 1).toUpperCase() + 
          strtmp.substring(1, strtmp.length()));
      }
      str = sb.toString();
    }
    return str;
  }

  public static String convertToStrs(Object obj) {
    String str = " ";
    if (!isEmptyObj(obj)) {
      str = obj.toString().trim();
    }
    return str;
  }

  public static String parseDateToString(Object obj, String format) {
    if (obj == null) {
      return "";
    }
    if (isEmpty(format)) {
      format = "yyyy-MM-dd HH:mm";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(obj);
  }

  public static boolean isNotEmptyObj(Object obj)
  {
    return !isEmptyObj(obj);
  }

  public static boolean isEmptyObj(Object obj)
  {
    if (obj == null) {
      return true;
    }
    if ((obj instanceof CharSequence)) {
      return ((CharSequence)obj).length() == 0;
    }
    if ((obj instanceof String)) {
      return ((String)obj).trim().length() == 0;
    }
    if ((obj instanceof Collection)) {
      return ((Collection)obj).isEmpty();
    }
    if ((obj instanceof Map)) {
      return ((Map)obj).isEmpty();
    }
    if ((obj instanceof Object[])) {
      Object[] object = (Object[])obj;
      boolean empty = true;
      for (int i = 0; i < object.length; i++)
        if (!isEmptyObj(object[i])) {
          empty = false;
          break;
        }
      return empty;
    }
    return false;
  }

  public static String convertDecToStr(Object obj, String code) {
    String str = convertToStr(obj);
    try {
      if (!str.equals(""))
        str = URLDecoder.decode(str, code);
    }
    catch (Exception localException) {
    }
    return str;
  }

  public static String wrapClauses(String[] strs) {
    if ((strs == null) || (strs.length == 0))
      return "('')";
    String rel = "(";
    StringBuilder sb = new StringBuilder();
    sb.append(rel);
    for (int i = 0; i < strs.length; i++) {
      sb.append("'" + strs[i] + "',");
    }

    rel = sb.toString();
    rel = removeEnd(rel, ",");
    return rel + ")";
  }

  public static String wrapClause(String[] strs) {
    String rel = "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < strs.length; i++) {
      sb.append("'");
      sb.append(strs[i]);
      sb.append("'");
    }
    rel = sb.toString();
    rel = removeEnd(rel, ",");
    return rel;
  }

  public static String wrapClauseList(Map<String, List<String>> map) {
    String rel = "";
    StringBuilder sb = new StringBuilder();
    if (!isEmptyObj(map)) {
      Iterator iter = map.entrySet().iterator();
      while (iter
        .hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        sb.append(" " + (String)entry.getKey() + " in " + wrapMidList((List)entry.getValue()) + " and");
      }

      rel = sb.toString();
      rel = removeEnd(rel, "and");
    }
    return rel;
  }

  public static String wrapClauseStringArray(Map<String, String[]> map) {
    String rel = "";
    if (!isEmptyObj(map)) {
      StringBuilder sb = new StringBuilder();
      for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
        Map.Entry entry = (Map.Entry)iter.next();
        sb.append(" " + (String)entry.getKey() + " in " + wrapClauses((String[])entry.getValue()) + " and");
      }
      rel = sb.toString();
      rel = removeEnd(rel, "and");
    }
    return rel;
  }

  public static String wrapClauseString(Map<String, String> map) {
    String rel = "";
    StringBuilder sb = new StringBuilder();
    if (!isEmptyObj(map)) {
      for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
        Map.Entry entry = (Map.Entry)iter.next();
        sb.append(" " + (String)entry.getKey() + " = " + (String)entry.getValue() + " and");
      }
      rel = sb.toString();
      rel = removeEnd(rel, "and");
    }
    return rel;
  }

  public static String wrapMidList(List<String> detailMidList) {
    if ((detailMidList == null) || (detailMidList.size() <= 0))
      return "('')";
    String imidStr = "(";
    StringBuilder sb = new StringBuilder();
    sb.append(imidStr);
    for (Iterator iter = detailMidList.iterator(); iter.hasNext(); ) {
      String str = (String)iter.next();
      if (iter.hasNext()) {
        sb.append("'");
        sb.append(str);
        sb.append("'");
      } else {
        sb.append("'");
        sb.append(str);
        sb.append("')");
      }
    }

    return sb.toString();
  }

  public static String toInsql(String[] values) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < values.length; i++)
    {
      sb.append("'");
      sb.append(values[i]);

      sb.append("'");
      if (i != values.length - 1) {
        sb.append(",");
      }
    }

    return sb.toString();
  }

  public static List<String> coverToList(String data)
  {
    List list = new ArrayList();
    if ((data != null) && (!data.trim().equals(""))) {
      String[] datas = data.split(",");
      int num = datas.length;
      for (int i = 0; i < num; i++)
        list.add(datas[i]);
    }
    return list;
  }

  public static String removeEnd(String str, String remove) {
    if ((str.equals("")) || (remove.equals("")))
      return str;
    if (str.endsWith(remove)) {
      return str.substring(0, str.length() - remove.length());
    }
    return str;
  }

  public static String convertBig5ToUnicode(Object obj) {
    try {
      return new String(convertToStr(obj).getBytes("Big5"), "ISO8859_1");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertToUnicode(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes(), "ISO8859_1");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertUnicodeToBig5(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes("ISO8859_1"), "Big5");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertUnicodeToGbk(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes("utf-8"), "GBK");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertUnicodeToUtf8(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes("ISO8859_1"), "utf-8");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertUtf8ToUnicode(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes("utf-8"), "ISO8859_1");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertLatin1ToUtf8(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes("Latin1"), "utf-8");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertToUtf8(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes(), "utf-8");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertBig5ToUtf8(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes("Big5"), "utf-8");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }

  public static String convertUtfToBig5(Object obj)
  {
    try {
      return new String(convertToStr(obj).getBytes("utf-8"), "Big5");
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Exception", e);
    }return "";
  }


  public static String trim2KindsSpace(String param)
  {
    param = param.trim();
    if (isEmpty(param)) {
      return param;
    }
    while (param.charAt(0) == '　') {
      param = param.substring(1, param.length()).trim();
    }
    while (param.endsWith("　")) {
      param = param.substring(0, param.length() - 1).trim();
    }
    return param;
  }

  public static String replaceCharacter(String value)
  {
    if (value == null) {
      return null;
    }
    char[] objTargetChar = { 65293, '—', '(', ')', 65296, 65297, 65298, 65299, 65300, 65301, 65302, 
      65303, 65304, 65305, 65311, 65286 };
    char[] objReplaceChar = { '-', '-', 65288, 65289, '0', '1', '2', '3', '4', '5', '6', 
      '7', '8', '9', '?', '&' };
    int i = 0; for (int j = objTargetChar.length; i < j; i++) {
      value = value.replace(objTargetChar[i], objReplaceChar[i]);
    }
    return value;
  }

  public static String cutString(String value, int charNumber)
  {
    if (charNumber < value.length()) {
      return value.substring(0, charNumber) + "...";
    }
    return value;
  }

  public static String qChangeToB(String qjString)
  {
    char[] c = qjString.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == '　') {
        c[i] = ' ';
      }
      else if ((c[i] > 65280) && (c[i] < 65375)) {
        c[i] = (char)(c[i] - 65248);
      }
    }
    return new String(c);
  }

  public static String bChangeToQ(String bjString)
  {
    char[] c = bjString.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == ' ') {
        c[i] = '　';
      }
      else if (c[i] < '') {
        c[i] = (char)(c[i] + 65248);
      }
    }
    return new String(c);
  }

  public static String valueOf(Object o)
  {
    return o == null ? "" : o.toString();
  }

  public static String valueOf(Object o, String defaultValue)
  {
    return o == null ? defaultValue : o.toString();
  }

  public static String cutAtStopCharater(String src)
  {
    int iIndex = src.indexOf(0);
    return iIndex >= 0 ? src.substring(0, iIndex) : src;
  }

  public static String firstLetterUpper(String s)
  {
    if ((s == null) || (s.length() == 0)) {
      return "";
    }
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }

  public static String nullToEmptyChar(String mayNullString)
  {
    if ((isEmpty(mayNullString)) || ("0.0".equals(mayNullString))) {
      return "";
    }
    if (mayNullString.endsWith(".0")) {
      return mayNullString.substring(0, mayNullString.length() - 2);
    }
    if (mayNullString.endsWith(".00")) {
      return mayNullString.substring(0, mayNullString.length() - 3);
    }

    return mayNullString;
  }

  public static String zeroToEmptyChar(double mayNullString)
  {
    if (mayNullString == 0.0D) {
      return "";
    }
    String strDeleteZero = String.valueOf(mayNullString);
    if (strDeleteZero.endsWith(".0")) {
      return strDeleteZero.substring(0, strDeleteZero.length() - 2);
    }
    if (strDeleteZero.endsWith(".00")) {
      return strDeleteZero.substring(0, strDeleteZero.length() - 3);
    }

    return String.valueOf(mayNullString);
  }

  public static String zeroSubToString(String mayZeroString)
  {
    if ((isEmpty(mayZeroString)) || ("0.0".equals(mayZeroString))) {
      return "";
    }
    if (mayZeroString.endsWith(".0"))
    {
      return mayZeroString.substring(0, mayZeroString.length() - 2);
    }
    if (mayZeroString.endsWith(".000000")) {
      return mayZeroString.substring(0, mayZeroString.length() - 4);
    }
    return mayZeroString;
  }

  public static String getNullOrEmptyText(Object value)
  {
    if ((value == null) || (value.toString().length() == 0)) {
      return "无";
    }
    return value.toString();
  }

  public static List<String> distinct(List<String> values) {
    Map tab = new HashMap();
    List result = new ArrayList();
    for (String e : values) {
      if (!tab.containsKey(e))
      {
        tab.put(e, e);
        result.add(e);
      }
    }

    return result;
  }

  public static boolean isBlank(String str) {
    if ((str == null) || (str.length() == 0)) {
      return true;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(String str) {
    if ((str == null) || (str.length() == 0)) {
      return false;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  public static boolean isEmpty(String str) {
    return (str == null) || (str.trim().length() == 0);
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }
}
package com.langk.base.resoure;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.langk.base.log.Log;
import com.langk.base.util.StringUtil;

public class ResourceUtil
{
  private static final String TAG = ResourceUtil.class.getSimpleName();
  private Context appContext;

  public ResourceUtil(Context appContext)
  {
    this.appContext = appContext;
  }

  public int getStyleResourceId(String styleResName)
  {
    int resResult = -1;
    try {
      String resType = "style";
      resResult = parseResIdByResNameAndResType(styleResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getStyleResourceId error", e);
    }

    return resResult;
  }

  public int getStyleableResourceId(String styleableResName)
  {
    int resResult = -1;
    try {
      String resType = "styleable";
      resResult = parseResIdByResNameAndResType(styleableResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getStyleableResourceId error", e);
    }

    return resResult;
  }

  public final int[] getStyleableResourceIntArray(String name)
  {
    try
    {
      Field[] fields2 = Class.forName(
        this.appContext.getPackageName() + ".R$styleable").getFields();

      for (Field f : fields2)
      {
        if (f.getName().equals(name))
        {
          return (int[])f.get(null);
        }
      }
    }
    catch (Throwable t) {
      Log.w(TAG, "getStyleableResourceIntArray error", t);
    }

    return null;
  }

  public int getBoolResourceId(String boolResName)
  {
    int resResult = -1;
    try {
      String resType = "bool";
      resResult = parseResIdByResNameAndResType(boolResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getBoolResourceId error", e);
    }

    return resResult;
  }

  public int getAttrResourceId(String attrResName)
  {
    int resResult = -1;
    try {
      String resType = "attr";
      resResult = parseResIdByResNameAndResType(attrResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getAttrResourceId error", e);
    }

    return resResult;
  }

  public int getAnimResourceId(String animResName)
  {
    int resResult = -1;
    try {
      String resType = "anim";
      resResult = parseResIdByResNameAndResType(animResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getAnimResourceId error", e);
    }

    return resResult;
  }

  public int getDimenResourceId(String dimenResName)
  {
    int resResult = -1;
    try {
      String resType = "dimen";
      resResult = parseResIdByResNameAndResType(dimenResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getDimenResourceId error", e);
    }

    return resResult;
  }

  public int getIDResourceId(String idResName)
  {
    int resResult = -1;
    try {
      String resType = "id";
      resResult = parseResIdByResNameAndResType(idResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getIDResourceId error", e);
    }

    return resResult;
  }

  public int getLayoutResourceId(String layoutResName)
  {
    int resResult = -1;
    try {
      String resType = "layout";
      resResult = parseResIdByResNameAndResType(layoutResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getLayoutResourceId error", e);
    }

    return resResult;
  }

  public int getDrawableResourceId(String drawableResName)
  {
    int resResult = -1;
    try {
      String resType = "drawable";
      resResult = parseResIdByResNameAndResType(drawableResName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getDrawableResourceId error", e);
    }

    return resResult;
  }

  public int getResourceIdResNameAndResType(String resName, String resType)
  {
    int resResult = -1;
    try {
      resResult = parseResIdByResNameAndResType(resName, resType);
    } catch (Exception e) {
      Log.w(TAG, "getResourceIdResNameAndResType error", e);
    }

    return resResult;
  }

  public String getResourceString(int resID)
  {
    String resStr = this.appContext.getResources().getString(resID);
    return resStr;
  }

  public String getResourceString(String resName)
  {
    String resStr = null;
    try {
      String resType = "string";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceString(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceString error", e);
    }

    return resStr;
  }

  public int getResourceInteger(int resID)
  {
    int iResult = this.appContext.getResources().getInteger(resID);
    return iResult;
  }

  public int getResourceInteger(String resName)
  {
    int resResult = 0;
    try {
      String resType = "integer";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceInteger(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceInteger error", e);
    }

    return resResult;
  }

  public int getResourceColor(int resID)
  {
    Integer iResult = Integer.valueOf(this.appContext.getResources().getColor(resID));
    return iResult.intValue();
  }

  public int getResourceColor(String resName)
  {
    int resResult = 0;
    try {
      String resType = "color";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceColor(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceColor error", e);
    }

    return resResult;
  }

  public String[] getResourceStringArray(String resName)
  {
    String[] resResult = null;
    try {
      String resType = "string-array";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceStringArray(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceStringArray error", e);
    }

    return resResult;
  }

  public String[] getResourceStringArray(int resID)
  {
    String[] sResult = this.appContext.getResources().getStringArray(resID);
    return sResult;
  }

  public int[] getResourceIntArray(int resID)
  {
    int[] iResult = this.appContext.getResources().getIntArray(resID);
    return iResult;
  }

  public int[] getResourceIntArray(String resName)
  {
    int[] resResult = null;
    try {
      String resType = "integer-array";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceIntArray(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceIntArray error", e);
    }

    return resResult;
  }

  public Drawable getResourceDrawable(int resID)
  {
    Drawable drawable = this.appContext.getResources().getDrawable(resID);
    return drawable;
  }

  public Drawable getResourceDrawable(String resName)
  {
    Drawable resResult = null;
    try {
      String resType = "drawable";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceDrawable(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceDrawable error", e);
    }

    return resResult;
  }

  public Map<String, String> getResourceHashMap(int hashMapResId)
  {
    Map map = null;
    try {
      XmlResourceParser parser = this.appContext.getResources().getXml(
        hashMapResId);

      String key = null;
      String value = null;

      int eventType = parser.getEventType();

      while (eventType != 1) {
        if (eventType == 0)
          Log.d(TAG, "Start document");
        else if (eventType == 2) {
          if (parser.getName().equals("map")) {
            boolean isLinked = parser.getAttributeBooleanValue(
              null, "linked", false);

            map = isLinked ? new LinkedHashMap() : 
              new HashMap();
          } else if (parser.getName().equals("entry")) {
            key = parser.getAttributeValue(null, "key");

            if (key == null) {
              parser.close();
              return null;
            }
          }
        } else if (eventType == 3) {
          if (parser.getName().equals("entry")) {
            parseAndPutKeyValue(map, key, value);
            key = null;
            value = null;
          }
        } else if ((eventType == 4) && 
          (key != null)) {
          value = parser.getText();
        }

        eventType = parser.next();
      }
    } catch (Exception e) {
      Log.e(TAG, "getResourceHashMap error", e);
      return null;
    }

    return map;
  }

  public Map<String, String> getResourceHashMap(String resName)
  {
    Map resResult = null;
    try {
      String resType = "xml";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceHashMap(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceHashMap error", e);
    }

    return resResult;
  }

  public Map<Integer, String> getResourceIntegerHashMap(int hashMapResId)
  {
    Map map = null;
    try {
      XmlResourceParser parser = this.appContext.getResources().getXml(
        hashMapResId);

      String key = null;
      String value = null;

      int eventType = parser.getEventType();

      while (eventType != 1) {
        if (eventType == 0)
          Log.d(TAG, "Start document");
        else if (eventType == 2) {
          if (parser.getName().equals("map")) {
            boolean isLinked = parser.getAttributeBooleanValue(
              null, "linked", false);

            map = isLinked ? new LinkedHashMap() : 
              new HashMap();
          } else if (parser.getName().equals("entry")) {
            key = parser.getAttributeValue(null, "key");

            if (key == null) {
              parser.close();
              return null;
            }
          }
        } else if (eventType == 3) {
          if (parser.getName().equals("entry")) {
            parseAndPutIntKeyStrValue(map, key, value);
            key = null;
            value = null;
          }
        } else if ((eventType == 4) && 
          (key != null)) {
          value = parser.getText();
        }

        eventType = parser.next();
      }
    } catch (Exception e) {
      Log.e(TAG, "getResourceHashMap error", e);
      return null;
    }

    return map;
  }

  public Map<Integer, String> getResourceIntegerHashMap(String resName)
  {
    Map resResult = null;
    try {
      String resType = "xml";
      int iId = parseResIdByResNameAndResType(resName, resType);
      return getResourceIntegerHashMap(iId);
    } catch (Exception e) {
      Log.w(TAG, "getResourceIntegerHashMap error", e);
    }

    return resResult;
  }

  private void parseAndPutKeyValue(Map<String, String> map, String key, String value)
  {
    String strResultKey = parseStringNameToStringValue(key);

    String strResultValue = parseStringNameToStringValue(value);

    if (StringUtil.isNotEmpty(strResultKey))
      map.put(strResultKey, strResultValue);
  }

  private String parseStringNameToStringValue(String key)
  {
    if ((StringUtil.isEmpty(key)) || (!key.startsWith("@"))) {
      return key;
    }

    String strResultKey = null;

    ResourceNode objKeyNode = ResourceNode.newInstance(this.appContext, key);
    if ((objKeyNode.isResourceNode()) && 
      (objKeyNode.isStringNode())) {
      int iResId = objKeyNode.getResId();
      if (iResId > 0) {
        try {
          strResultKey = this.appContext.getResources().getString(
            iResId);
          if (StringUtil.isEmpty(strResultKey))
            Log.w(TAG, "key " + key + 
              " not exist in string res");
        }
        catch (Exception e) {
          Log.w(TAG, "key " + key + " not exist in string res", e);
        }

      }

    }

    if (StringUtil.isEmpty(strResultKey)) {
      strResultKey = key;
      Log.w(TAG, "key " + key + "string res value is empty");
    }

    return strResultKey;
  }

  private void parseAndPutIntKeyStrValue(Map<Integer, String> map, String key, String value)
  {
    Integer strResultKey = parseStringNameToResId(key);

    String strResultValue = parseStringNameToStringValue(value);

    if (strResultKey != null)
      map.put(strResultKey, strResultValue);
  }

  private Integer parseStringNameToResId(String key)
  {
    if (StringUtil.isEmpty(key)) {
      return null;
    }

    if (!key.startsWith("@")) {
      try {
        return Integer.valueOf(key);
      } catch (Exception e) {
        Log.w(TAG, 
          "parseStringNameToResId Integer.valueOf error  key: " + 
          key, e);
      }
    }

    Integer iResultId = null;
    ResourceNode objKeyNode = ResourceNode.newInstance(this.appContext, key);
    if (objKeyNode.isResourceNode())
      iResultId = Integer.valueOf(objKeyNode.getResId());
    else {
      iResultId = Integer.valueOf(key);
    }

    return iResultId;
  }

  private int parseResIdByResNameAndResType(String resName, String resType)
  {
    int iId = -1;
    if (!StringUtil.isEmpty(resName)) {
      Resources resources = this.appContext.getResources();
      iId = resources.getIdentifier(resName, resType, 
        this.appContext.getPackageName());
    }

    return iId;
  }
}
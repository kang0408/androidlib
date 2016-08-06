package com.langk.base.resoure;

import com.langk.base.util.StringUtil;

import android.content.Context;
import android.content.res.Resources;

public class ResourceNode
{
  private String name;
  private String type;
  private String packag;
  private int resId;
  private Context appContext;

  public ResourceNode(Context appContext)
  {
    this.appContext = appContext;
  }

  public ResourceNode(Context appContext, String name, String type, String packag)
  {
    this.name = name;
    this.type = type;
    this.packag = packag;
    this.resId = appContext.getResources()
      .getIdentifier(name, type, packag);
    this.appContext = appContext;
  }

  public static ResourceNode newInstance(Context appContext, int iResId)
  {
    return parseFromResId(appContext, iResId);
  }

  public static ResourceNode newInstance(Context appContext, String strResName)
  {
    return parseFromResName(appContext, strResName);
  }

  public static ResourceNode parseFromResName(Context appContext, String strResName)
  {
    if ((StringUtil.isEmpty(strResName)) || 
      (!strResName.startsWith("@"))) {
      return null;
    }
    ResourceNode node = new ResourceNode(appContext);
    String strName = node.getNameFromStrResName(strResName);
    String strType = node.getTypeFromStrResName(strResName);

    int iId = -1;
    if ((!StringUtil.isEmpty(strName)) && (!StringUtil.isEmpty(strType))) {
      Resources resources = appContext.getResources();
      iId = resources.getIdentifier(strName, strType, 
        appContext.getPackageName());
    }

    node.setName(strName);
    node.setType(strType);
    node.setResId(iId);

    return node;
  }

  public static ResourceNode parseFromResId(Context appContext, int iResId)
  {
    String strResourceName = appContext.getResources()
      .getResourceEntryName(iResId);
    String strResourceTypeName = appContext.getResources()
      .getResourceTypeName(iResId);
    String strResourcePackageName = appContext.getResources()
      .getResourcePackageName(iResId);

    ResourceNode node = new ResourceNode(appContext);
    node.setName(strResourceName);
    node.setType(strResourceTypeName);
    node.setResId(iResId);
    node.setPackag(strResourcePackageName);

    return null;
  }

  private String getNameFromStrResName(String strResName)
  {
    int iBackSlashIndex = strResName
      .indexOf("/");
    if (iBackSlashIndex < 0) {
      return null;
    }
    String strName = strResName.substring(iBackSlashIndex + 1);
    return strName;
  }

  private String getTypeFromStrResName(String strResName)
  {
    int iBackSlashIndex = strResName
      .indexOf("/");
    if (iBackSlashIndex < 0) {
      return null;
    }
    String strType = strResName.substring(1, iBackSlashIndex);
    return strType;
  }

  public boolean isResourceNode()
  {
    return !StringUtil.isEmpty(this.type);
  }

  public boolean isStringNode()
  {
    return "string".equalsIgnoreCase(this.type);
  }

  public boolean isIntegerNode()
  {
    return "integer".equalsIgnoreCase(this.type);
  }

  public boolean isColorNode()
  {
    return "color".equalsIgnoreCase(this.type);
  }

  public boolean isAnimNode()
  {
    return "anim".equalsIgnoreCase(this.type);
  }

  public boolean isArrayNode()
  {
    return "array".equalsIgnoreCase(this.type);
  }

  public boolean isAttrNode()
  {
    return "attr".equalsIgnoreCase(this.type);
  }

  public boolean isBoolNode()
  {
    return "bool".equalsIgnoreCase(this.type);
  }

  public boolean isDimenNode()
  {
    return "dimen".equalsIgnoreCase(this.type);
  }

  public boolean isdrawableNode()
  {
    return "drawable".equalsIgnoreCase(this.type);
  }

  public boolean isIdNode()
  {
    return "id".equalsIgnoreCase(this.type);
  }

  public boolean isLayoutNode()
  {
    return "layout".equalsIgnoreCase(this.type);
  }

  public boolean isMenuNode()
  {
    return "menu".equalsIgnoreCase(this.type);
  }

  public boolean isRawNode()
  {
    return "raw".equalsIgnoreCase(this.type);
  }

  public boolean isStyleNode()
  {
    return "style".equalsIgnoreCase(this.type);
  }

  public boolean isXmlNode()
  {
    return "xml".equalsIgnoreCase(this.type);
  }

  public boolean isStyleableNode()
  {
    return "styleable".equalsIgnoreCase(this.type);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPackag() {
    return this.packag;
  }

  public void setPackag(String packag) {
    this.packag = packag;
  }

  public int getResId() {
    return this.resId;
  }

  public void setResId(int resId) {
    this.resId = resId;
  }
}

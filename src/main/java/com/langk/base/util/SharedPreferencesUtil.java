package com.langk.base.util;


import com.langk.base.log.Log;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesUtil
{
  private static final String TAG = "SharedPreUtils";
  public static final String DEFAULT_SHARED_PREF_FILE_NAME = "zte_local_shared_file";
  private static SharedPreferencesUtil instance;
  private static Context appContext;

  public static SharedPreferencesUtil getInstance(Context context)
  {
    if (instance == null) {
      instance = new SharedPreferencesUtil();
    }
    if (appContext == null) {
      appContext = context.getApplicationContext();
    }
    return instance;
  }

  public int getInt(String key, int defValue)
  {
    return getInt("zte_local_shared_file", key, defValue);
  }

  public String getString(String key, String defValue)
  {
    return getString("zte_local_shared_file", key, defValue);
  }

  public String getString(String fileName, String key, String defValue)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    return prefs.getString(key, defValue);
  }

  public int getInt(String fileName, String key, int defValue)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    return prefs.getInt(key, defValue);
  }

  public long getLong(String fileName, String key, long defValue)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    return prefs.getLong(key, defValue);
  }

  public boolean addOrModify(String key, String value)
  {
    return addOrModify("zte_local_shared_file", key, value);
  }

  public boolean addOrModify(String fileName, String key, String value)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(key, value);
    return editor.commit();
  }

  public boolean addOrModifyInt(String key, int value)
  {
    return addOrModifyInt("zte_local_shared_file", key, value);
  }

  public boolean addOrModifyLong(String key, long value)
  {
    return addOrModifyLong("zte_local_shared_file", key, value);
  }

  public boolean addOrModifyInt(String fileName, String key, int value)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt(key, value);
    return editor.commit();
  }

  public boolean addOrModifyLong(String fileName, String key, long value)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong(key, value);
    return editor.commit();
  }

  public boolean deleteItem(String key)
  {
    return deleteItem("zte_local_shared_file", key);
  }

  public boolean deleteItem(String fileName, String key)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    if (prefs.contains(key)) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.remove(key);
      return editor.commit();
    }
    Log.d("SharedPreUtils", "deleteItem " + key + " in SharedPreferences: " + 
      fileName + " not exist");
    return false;
  }

  public boolean deleteAll(String fileName)
  {
    SharedPreferences prefs = appContext.getSharedPreferences(fileName, 
      0);
    prefs.edit().clear().commit();
    return true;
  }
}
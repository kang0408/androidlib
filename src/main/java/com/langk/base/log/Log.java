package com.langk.base.log;

import java.util.HashMap;
import java.util.Map;

import com.langk.base.application.BaseApplication;

public class Log
{
  public static final String VERBOSE = "verbose";
  public static final String DEBUG = "debug";
  public static final String INFO = "info";
  public static final String WARN = "warn";
  public static final String ERROR = "error";
  public static String logLevel = "debug";

  private static final Map<String, Integer> LOG_LEVER_MAP = new HashMap<String, Integer>() {
	private static final long serialVersionUID = 1L;
	{
	  put(VERBOSE,2);
	  put(DEBUG,3);
	  put(INFO,4);
	  put(VERBOSE,5);
	  put(VERBOSE,6);
	  }
  } ;

  public static final boolean LOG_FLAG_VERBOSE = 2 >= 
    ((Integer)LOG_LEVER_MAP
    .get(logLevel)).intValue();

  public static final boolean LOG_FLAG_DEBUG = 3 >= 
    ((Integer)LOG_LEVER_MAP
    .get(logLevel)).intValue();

  public static final boolean LOG_FLAG_INFO = 4 >= 
    ((Integer)LOG_LEVER_MAP
    .get(logLevel)).intValue();

  public static final boolean LOG_FLAG_WARN = 5 >= 
    ((Integer)LOG_LEVER_MAP
    .get(logLevel)).intValue();

  public static final boolean LOG_FLAG_ERROR = 6 >= 
    ((Integer)LOG_LEVER_MAP
    .get(logLevel)).intValue();

  public static void v(String TAG, String msg, Throwable e)
  {
    if ((msg != null) && 
      (LOG_FLAG_VERBOSE)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.v(TAG, msg, e);
  }

  public static void v(String TAG, String msg)
  {
    if ((msg != null) && 
      (LOG_FLAG_VERBOSE)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.v(TAG, msg);
  }

  public static void d(String TAG, String msg, Throwable e)
  {
    if ((msg != null) && 
      (LOG_FLAG_DEBUG)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.d(TAG, msg, e);
  }

  public static void d(String TAG, String msg)
  {
    if ((msg != null) && 
      (LOG_FLAG_DEBUG)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.d(TAG, msg);
  }

  public static void i(String TAG, String msg)
  {
    if ((msg != null) && 
      (LOG_FLAG_INFO)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.i(TAG, msg);
  }

  public static void i(String TAG, String msg, Throwable e)
  {
    if ((msg != null) && 
      (LOG_FLAG_INFO)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.i(TAG, msg, e);
  }

  public static void w(String TAG, String msg)
  {
    if ((msg != null) && 
      (LOG_FLAG_WARN)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.w(TAG, msg);
  }

  public static void w(String TAG, String msg, Throwable e)
  {
    if ((msg != null) && 
      (LOG_FLAG_WARN)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.w(TAG, msg, e);
  }

  public static void e(String TAG, String msg)
  {
    if ((msg != null) && 
      (LOG_FLAG_ERROR)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.w(TAG, msg);
  }

  public static void e(String TAG, String msg, Throwable e)
  {
    if ((msg != null) && 
      (LOG_FLAG_ERROR)&&BaseApplication.getInstance().getLogManager().isDebug())
      android.util.Log.w(TAG, msg, e);
  }

  public static String getLogLevel()
  {
    return logLevel;
  }

  public static void setLogLevel(String logLevel) {
    Log.logLevel = logLevel;
  }
}
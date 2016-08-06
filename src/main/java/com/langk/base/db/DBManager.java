package com.langk.base.db;

import com.langk.base.db.helper.DBHelper;
import com.langk.base.db.helper.SharedDBHelper;
import com.langk.base.db.helper.UserScopeDBHelper;
import com.langk.base.log.Log;
import com.langk.base.util.StringUtil;

import android.content.Context;

public class DBManager
{
  private static final String TAG = DBManager.class.getSimpleName();

  private static final Integer NONE_PARAMS = Integer.valueOf(0);

  private DBHelper sharedDBHelper = null;

  private DBHelper userScopeDBHelper = null;
  private Context context;
  private DBConfig sharedDBConfig;
  private DBConfig userScopeDBConfig;

  public DBManager(Context context, DBConfig sharedDBConfig, DBConfig userScopeDBConfig)
  {
    this.context = context;
    this.userScopeDBConfig = userScopeDBConfig;
    this.sharedDBConfig = sharedDBConfig;
  }

  public synchronized void initSharedDBHelper()
  {
    if (this.sharedDBHelper == null) {
      this.sharedDBHelper = new SharedDBHelper(this.context, this.sharedDBConfig);

      this.sharedDBHelper.checkTables();
    }
  }

  public synchronized void initUserScopeDBHelper(String strUserAccount)
  {
    if (StringUtil.isNotEmpty(strUserAccount))
    {
      if (this.userScopeDBHelper == null) {
        this.userScopeDBHelper = new UserScopeDBHelper(this.context, 
          strUserAccount, this.userScopeDBConfig);

        this.userScopeDBHelper.checkTables();
      }
    }
    else Log.w(TAG, "user not login, skip initUserDBHelper.");
  }

  public void closeDBHelper()
  {
    if (this.sharedDBHelper != null) {
      this.sharedDBHelper.close();
    }

    if (this.userScopeDBHelper != null)
      this.userScopeDBHelper.close();
  }

  public void clearAllUserDBData()
  {
    if (this.userScopeDBHelper != null)
      this.userScopeDBHelper.clearAllTableData();
  }

  public void clearAllSharedDBData()
  {
    if (this.sharedDBHelper != null)
      this.sharedDBHelper.clearAllTableData();
  }

  public DBHelper getSharedDBHelper()
  {
    return this.sharedDBHelper;
  }

  public DBHelper getUserScopeDBHelper() {
    return this.userScopeDBHelper;
  }

  public Context getContext() {
    return this.context;
  }

  public void setContext(Context context) {
    this.context = context;
  }
}

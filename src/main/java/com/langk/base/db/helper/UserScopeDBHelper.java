package com.langk.base.db.helper;

import com.langk.base.db.DBConfig;
import com.langk.base.db.dao.BaseDAO;
import com.langk.base.log.Log;
import com.langk.base.util.StringUtil;

import android.content.Context;
import android.content.res.Resources;

public class UserScopeDBHelper extends DBHelper
{
  private static final String TAG = UserScopeDBHelper.class.getSimpleName();

  public UserScopeDBHelper(Context context, String userAccount, DBConfig dbConfig)
  {
    super(context, userAccount + 
      context.getResources().getString(
      dbConfig.getDatabaseNameSuffixResId()), dbConfig
      .getDatabaseVersionResId(), dbConfig.getDatabaseTablesResId());
  }

  protected void onTableUpdate(BaseDAO objBaseDao, long tableCurrentVersion, long tableNewVersion)
  {
    super.onTableUpdate(objBaseDao, tableCurrentVersion, tableNewVersion);
  }

  public String getTablesVersionFileName(BaseDAO objBaseDao)
  {
    if (objBaseDao == null) {
      Log.w(TAG, "objBaseDao is null");
      return null;
    }

    String strUserAccount = objBaseDao.getUserAccount();
    if (StringUtil.isEmpty(strUserAccount)) {
      Log.w(TAG, "strUserAccount is null");
      return null;
    }

    return "local_tables_version_file_" + 
      strUserAccount;
  }
}
package com.langk.base.db.helper;

import com.langk.base.db.DBConfig;
import com.langk.base.db.dao.BaseDAO;

import android.content.Context;

public class SharedDBHelper extends DBHelper
{
  private static final String TAG = SharedDBHelper.class.getSimpleName();

  public SharedDBHelper(Context context, DBConfig dbConfig)
  {
    super(context, "LangK" + 
      context.getResources().getString(
      dbConfig.getDatabaseNameSuffixResId()), dbConfig
      .getDatabaseVersionResId(), dbConfig.getDatabaseTablesResId());
  }

  public String getTablesVersionFileName(BaseDAO objBaseDao)
  {
    return "local_tables_version_file_common";
  }
}
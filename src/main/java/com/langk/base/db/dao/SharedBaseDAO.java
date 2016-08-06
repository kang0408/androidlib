package com.langk.base.db.dao;

import com.langk.base.db.BasePO;
import com.langk.base.db.helper.DBHelper;

public abstract class SharedBaseDAO<T extends BasePO> extends BaseDAO
{
  private static final String TAG = SharedBaseDAO.class.getSimpleName();

  public SharedBaseDAO(Class entityClass) {
    super(entityClass);
  }

  public DBHelper getDbHelper()
  {
    return getDBManager().getSharedDBHelper();
  }

  public String getUserAccount()
  {
    return null;
  }
}

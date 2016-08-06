package com.langk.base.db.dao;

import com.langk.base.db.BasePO;
import com.langk.base.db.helper.DBHelper;

public abstract class UserScopeBaseDAO<T extends BasePO> extends BaseDAO
{
  private static final String TAG = UserScopeBaseDAO.class.getSimpleName();

  public UserScopeBaseDAO(Class entityClass) {
    super(entityClass);
  }

  public DBHelper getDbHelper()
  {
    return getDBManager().getUserScopeDBHelper();
  }
}

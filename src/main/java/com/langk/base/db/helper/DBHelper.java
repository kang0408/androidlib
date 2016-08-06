package com.langk.base.db.helper;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.langk.base.db.BasePO;
import com.langk.base.db.dao.BaseDAO;
import com.langk.base.log.Log;
import com.langk.base.util.StringUtil;

import java.sql.SQLException;

public abstract class DBHelper extends OrmLiteSqliteOpenHelper
{
  private static final String TAG = DBHelper.class.getSimpleName();
  public static final String SHARED_PREF_TABLES_VERSION_FILE_NAME_PREFIX = "local_tables_version_file";
  protected String databaseName;
  protected int databaseVersion;
  protected String[] databaseTables;

  public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion)
  {
    super(context, databaseName, factory, databaseVersion);
  }

  public DBHelper(Context context, String dbName, int dbVersionResId, int tablesArrayResId)
  {
    this(context, dbName, null, context.getResources()
      .getInteger(dbVersionResId));
    this.databaseName = dbName;
    this.databaseVersion = context.getResources()
      .getInteger(dbVersionResId);
    this.databaseTables = context.getResources().getStringArray(
      tablesArrayResId);
  }

  public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
  {
    try
    {
      checkTables();
      Log.i(TAG, "onCreate db sucess");

      initData();
    } catch (Exception e) {
      Log.i(TAG, "onCreate db failed", e);
    }
  }

  public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int currentVersion, int toUpdateVersion)
  {
    try
    {
      Log.i(TAG, "onUpgrade db ...");

      onUpgradeDataPre(db, connectionSource, currentVersion, 
        toUpdateVersion);

      onUpgradeDataIng(db, connectionSource, currentVersion, 
        toUpdateVersion);

      onUpgradeDataAfter(db, connectionSource, currentVersion, 
        toUpdateVersion);

      onUpdateSharedPreferences(db, connectionSource, currentVersion, 
        toUpdateVersion);

      Log.i(TAG, "onUpgrade db sucess");
    } catch (Exception localException) {
      Log.i(TAG, "onUpgrade db failed");
    }
  }

  protected void onUpgradeDataPre(SQLiteDatabase db, ConnectionSource connectionSource, int currentVersion, int toUpdateVersion)
    throws SQLException, ClassNotFoundException
  {
    if ((this.databaseName != null) && (this.databaseVersion != 0) && 
      (this.databaseTables != null))
      for (int i = 0; i < this.databaseTables.length; i++) {
        String className = this.databaseTables[i];
        TableUtils.dropTable(connectionSource, 
          Class.forName(className), true);
        Log.i(TAG, "drop table " + className + " sucess");
      }
  }

  protected void onUpgradeDataIng(SQLiteDatabase db, ConnectionSource connectionSource, int currentVersion, int toUpdateVersion)
    throws SQLException, ClassNotFoundException
  {
    onCreate(db, connectionSource);
  }

  protected void onUpgradeDataAfter(SQLiteDatabase db, ConnectionSource connectionSource, int currentVersion, int toUpdateVersion)
    throws SQLException, ClassNotFoundException
  {
  }

  protected void onUpdateSharedPreferences(SQLiteDatabase db, ConnectionSource connectionSource, int currentVersion, int toUpdateVersion)
  {
  }

  public void close()
  {
    super.close();
  }

  public void checkTables()
  {
    createTableIfNotExists();
    checkTablesUpdate();
  }

  public void createTableIfNotExists()
  {
    try
    {
      if ((this.databaseName != null) && (this.databaseVersion != 0) && 
        (this.databaseTables != null)) {
        for (int i = 0; i < this.databaseTables.length; i++) {
          String className = this.databaseTables[i];
          System.out.println(className);
          try
          {
            TableUtils.createTableIfNotExists(this.connectionSource, 
              Class.forName(className));
            Log.i(TAG, "create Table " + className + " sucess");
          } catch (Exception e) {
            Log.w(TAG, "create Table " + className + " failed", e);
          }
        }
      }
      Log.i(TAG, "create Tables finish");
    } catch (Exception e) {
      Log.w(TAG, "create Tables failed", e);
    }
  }

  public void checkTablesUpdate()
  {
    try
    {
      Log.i(TAG, "checkTablesUpdate start");
      if ((this.databaseName != null) && (this.databaseVersion != 0) && 
        (this.databaseTables != null)) {
        for (int i = 0; i < this.databaseTables.length; i++) {
          String className = this.databaseTables[i];
          try {
            Class clazz = Class.forName(className);
            BasePO objVO = (BasePO)clazz.newInstance();
            boolean bNeedUpdate = objVO.needUpdateTable(this);
            if (bNeedUpdate) {
              BaseDAO objBaseDao = objVO.getBaseDAO();
              Log.d(TAG, "table " + className + 
                " onTableUpdate start");

              onTableUpdate(objBaseDao, 
                objVO.getTableCurrentVersion(objBaseDao), 
                objVO.getTableNewVersion());

              Log.d(TAG, "table " + className + 
                " onTableUpdate sucess");
            }
            else {
              Log.d(TAG, "table " + className + 
                " don't need to Update");
            }
          }
          catch (Exception e) {
            Log.w(TAG, "table " + className + 
              " onTableUpdate error", e);
          }
        }
      }

      Log.i(TAG, "checkTablesUpdate finish");
    } catch (Exception e) {
      Log.w(TAG, "checkTablesUpdate failed", e);
    }
  }

  protected void onTableUpdate(BaseDAO objBaseDao, long tableCurrentVersion, long tableNewVersion)
  {
    String strTablesVersionFileName = getTablesVersionFileName(objBaseDao);
    if (StringUtil.isEmpty(strTablesVersionFileName)) {
      Log.w(TAG, "TablesVersionFileName is null, skip onTableUpdate");
      return;
    }

    Log.d(TAG, "onTableUpdate TablesVersionFileName = " + 
      strTablesVersionFileName);

    objBaseDao.onTableUpdate(strTablesVersionFileName, tableCurrentVersion, 
      tableNewVersion);
  }

  public abstract String getTablesVersionFileName(BaseDAO paramBaseDAO);

  public void clearAllTableData()
  {
    if ((this.databaseName != null) && (this.databaseTables != null))
      for (int i = 0; i < this.databaseTables.length; i++) {
        String className = this.databaseTables[i];
        try {
          clearTableData(Class.forName(className));
        } catch (Exception e) {
          Log.e(TAG, "clearTableData error", e);
        }
        Log.i(TAG, "clearAllTableData " + className + " sucess");
      }
  }

  public void clearTableData(Class<?> clz)
  {
    try
    {
      TableUtils.clearTable(this.connectionSource, clz);
      Log.i(TAG, "clear Table " + clz.getName() + " sucess");
    } catch (Exception localException) {
      Log.i(TAG, "clear Table failed");
    }
  }

  protected void initData()
  {
    try
    {
      Log.i(TAG, "initData...");

      Log.i(TAG, "initData sucess");
    } catch (Exception e) {
      Log.i(TAG, "initData failed", e);
    }
  }
}
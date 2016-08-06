package com.langk.base.db;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.langk.base.db.dao.BaseDAO;
import com.langk.base.db.helper.DBHelper;

import java.io.Serializable;

public class BasePO
  implements Serializable
{
  private static final long serialVersionUID = -3480470517011927799L;

  @Expose(serialize=false, deserialize=false)
  private static final String TAG = BasePO.class.getSimpleName();

  @Expose(serialize=false, deserialize=false)
  protected long tableNewVersion = 0L;

  protected Boolean isSelected = Boolean.valueOf(false);

  @Expose(serialize=false, deserialize=false)
  @DatabaseField(columnName="LOCAL_CREATE_TIMESTAMP")
  protected long localCreateTime;

  @Expose(serialize=false, deserialize=false)
  @DatabaseField(columnName="LOCAL_UPDATE_TIMESTAMP")
  protected long localUpdateTime;

  public BasePO() { this.localCreateTime = System.currentTimeMillis();
    this.localUpdateTime = this.localCreateTime;
    this.isSelected = Boolean.valueOf(false);
  }

  public boolean needUpdateTable()
  {
    BaseDAO objBaseDAO = getBaseDAO();
    if (objBaseDAO == null) {
      return false;
    }
    return needUpdateTable(objBaseDAO.getDbHelper());
  }

  public boolean needUpdateTable(DBHelper objDbHelper)
  {
    long iTableCurrentVersion = getTableCurrentVersion();
    long iTableNewVersion = getTableNewVersion();

    return iTableNewVersion > iTableCurrentVersion;
  }

  public long getTableCurrentVersion()
  {
    BaseDAO objBaseDAO = getBaseDAO();
    if (objBaseDAO == null) {
      return 0L;
    }
    return getTableCurrentVersion(objBaseDAO);
  }

  public long getTableCurrentVersion(BaseDAO objBaseDAO)
  {
    return objBaseDAO.getTableCurrentVersion();
  }

  public BaseDAO getBaseDAO()
  {
    return null;
  }

  public Boolean getIsSelected()
  {
    return this.isSelected;
  }

  public void setIsSelected(Boolean isSelected)
  {
    this.isSelected = isSelected;
  }

  public long getLocalCreateTime() {
    return this.localCreateTime;
  }

  public void setLocalCreateTime(long localCreateTime) {
    this.localCreateTime = localCreateTime;
  }

  public long getLocalUpdateTime() {
    return this.localUpdateTime;
  }

  public void setLocalUpdateTime(long localUpdateTime) {
    this.localUpdateTime = localUpdateTime;
  }

  public long getTableNewVersion() {
    return this.tableNewVersion;
  }
}
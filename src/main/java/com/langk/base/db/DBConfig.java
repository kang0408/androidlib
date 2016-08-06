package com.langk.base.db;

public class DBConfig {

	  protected int databaseNameSuffixResId;
	  protected int databaseVersionResId;
	  protected int databaseTablesResId;

	  public DBConfig(int databaseNameSuffixResId, int databaseVersionResId, int databaseTablesResId)
	  {
	    this.databaseNameSuffixResId = databaseNameSuffixResId;
	    this.databaseVersionResId = databaseVersionResId;
	    this.databaseTablesResId = databaseTablesResId;
	  }

	  public int getDatabaseNameSuffixResId() {
	    return this.databaseNameSuffixResId;
	  }

	  public void setDatabaseNameSuffixResId(int databaseNameSuffixResId) {
	    this.databaseNameSuffixResId = databaseNameSuffixResId;
	  }

	  public int getDatabaseVersionResId() {
	    return this.databaseVersionResId;
	  }

	  public void setDatabaseVersionResId(int databaseVersionResId) {
	    this.databaseVersionResId = databaseVersionResId;
	  }

	  public int getDatabaseTablesResId() {
	    return this.databaseTablesResId;
	  }

	  public void setDatabaseTablesResId(int databaseTablesResId) {
	    this.databaseTablesResId = databaseTablesResId;
	  }
}

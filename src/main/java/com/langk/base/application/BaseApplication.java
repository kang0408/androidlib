package com.langk.base.application;

import java.util.ArrayList;
import java.util.List;

import com.langk.base.activity.BaseActivity;
import com.langk.base.db.DBManager;
import com.langk.base.log.LogManage;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

	private static final String TAG = BaseApplication.class.getSimpleName();

	private BaseActivity currentActivity = null;

	protected static int DEFAULT_HEAP_SIZE = 16;

	protected static String SECRET_KEY = "ke#ret173k52nn90";

	protected ActivityManager activityManager = null;

	private List<BaseActivity> activities;

	protected static BaseApplication instance = null;
	
	protected LogManage logManager;
	
	public void setLogManager(LogManage logManager) {
		this.logManager = logManager;
	}

	protected DBManager dbManager;

	public LogManage getLogManager() {
		if (logManager==null) {
			logManager = new LogManage();
		}
		return logManager;
	}

	public static BaseApplication getInstance() {
		if (instance==null) {
			instance = new BaseApplication();
		}
		return instance;
	}

	public ActivityManager getActivityManager() {
		return this.activityManager;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
		initActivityManager();
		initActivityList();
		initLogManage();
		initDBManager();
	}
	
	/**
	 * 初始化数据库管理 <br/>
	 * 日期: 2014-5-19 下午4:32:11 <br/>
	 * 
	 * @author LangK
	 * @since JDK 1.6
	 */
	public void initDBManager(){}
	
	public void setDBManager(DBManager dbManager){
		this.dbManager = dbManager;
	}
	
	public DBManager getDBManager(){
		return dbManager;
	}
	
	protected void initSharedDBHelper()
	  {
	    if (this.dbManager != null)
	      this.dbManager.initSharedDBHelper();
	  }

	  public void initUserScopeDBHelper(String strUserAccount)
	  {
	    if (this.dbManager != null)
	      this.dbManager.initUserScopeDBHelper(strUserAccount);
	  }

	protected void initDBManage(){
	}
	
	private void initLogManage() {
		// TODO Auto-generated method stub
		logManager = new LogManage();
	}

	private void initActivityList() {
		// TODO Auto-generated method stub
		if (activities==null) {
			activities = new ArrayList<BaseActivity>();
		}
	}

	public List<BaseActivity> getActivities() {
		if (activities==null) {
			activities = new ArrayList<BaseActivity>();
		}
		return activities;
	}

	protected void initActivityManager() {
		this.activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	}

	public void exitApplication() {
		if (activities.size() > 0) {
			for (BaseActivity activity : activities) {
				activity.finish();
			}
		}
		System.exit(0);
	}


	public BaseActivity getCurrentActivity() {
		return this.currentActivity;
	}

	public void setCurrentActivity(BaseActivity currentActivity) {
		this.currentActivity = currentActivity;
	}

}

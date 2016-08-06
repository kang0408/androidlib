package com.langk.base.activity;

import com.langk.base.application.BaseApplication;

import roboguice.activity.RoboFragmentActivity;
import android.os.Bundle;

/**
 * 基础activity
 * @author K
 *
 */
public class BaseActivity extends RoboFragmentActivity{

	private static final String TAG = BaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature();
		super.onCreate(savedInstanceState);
		initContentView();
		initView();
		initData();
		initViewEvent();
		notifyApp();
	}

	/**
	 * 通知app activity创建
	 */
	private void notifyApp() {
		// TODO Auto-generated method stub
		BaseApplication.getInstance().getActivities().add(this);
	}

	protected void requestWindowFeature() {
	}

	protected void initWindowFeature() {
	}

//	public void startActivity(Intent intent) {
//		super.startActivity(intent);
//		// overridePendingTransition();
//	}
//
//	public boolean onTouchEvent(MotionEvent event) {
//		closeSoftKeyBoard();
//		return super.onTouchEvent(event);
//	}
//
//	public void closeSoftKeyBoard() {
//		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (getCurrentFocus() != null)
//			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
//					.getWindowToken(), 0);
//	}
//
//	public void startActivityForResult(Intent intent, int requestCode) {
//		super.startActivityForResult(intent, requestCode);
//		// overridePendingTransition(
//		// this.resourceUtil.getAnimResourceId("anim_push_right_in"),
//		// this.resourceUtil.getAnimResourceId("anim_push_left_out"));
//	}

	/**
	 * init view　event
	 */
	private void initViewEvent() {
		// TODO Auto-generated method stub

	}

	/**
	 * init All View
	 */
	protected void initView() {
		// TODO Auto-generated method stub

	}

	/**
	 * init Data
	 */
	protected void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * set Content View
	 */
	protected void initContentView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BaseApplication.getInstance().setCurrentActivity(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		BaseApplication.getInstance().getActivities().remove(this);
		super.onDestroy();
	}

}

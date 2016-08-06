package com.langk.base.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.langk.base.activity.BaseActivity;
import com.langk.base.util.BaseLayoutInflater;

import roboguice.fragment.RoboFragment;

public abstract class BaseFragment extends RoboFragment
{
  private static final String TAG = BaseFragment.class.getSimpleName();

  protected View rootView = null;

  protected boolean rootViewHasCreated = false;

  private boolean onCreateAndOnResume = false;

  protected int scrollDelayTime = 2;
  protected View moreView;
  protected int lastVisibleIndex;
  protected int pageItemsNum = 10;

  protected int totalCount = 0;

  protected boolean isLoading = false;
  protected int businessType;
  protected ProgressDialog progressDialog = null;

  public LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    -1, 
    -1, 1.0F);


  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    this.onCreateAndOnResume = true;

    BaseLayoutInflater baseLayoutInflater = 
      BaseLayoutInflater.from(getBaseActivity());

    this.rootView = onCreateView(baseLayoutInflater, container, 
      savedInstanceState);

    return this.rootView;
  }

  protected abstract View onCreateView(BaseLayoutInflater paramBaseLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle);

  public void onViewCreated(View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);

    initData();

    getBaseActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        BaseFragment.this.initViews();

        BaseFragment.this.initViewEvents();
      }
    });
  }

  public View getView()
  {
    return super.getView();
  }

  public void onAttach(Activity activity)
  {
    super.onAttach(activity);
  }

  public void onResume()
  {
    super.onResume();
    if (!this.onCreateAndOnResume) {
      onResumeOnly();
    }
    this.onCreateAndOnResume = false;
  }

  public void onPause()
  {
    super.onPause();

  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  public void onDestroyView()
  {
    super.onDestroyView();
  }

  public void onDetach()
  {
    super.onDetach();
  }

  private void onResumeOnly()
  {
    refreshData();

    refreshViews();

    refreshViewEvents();
  }

  protected void initData()
  {
  }

  protected void initViews()
  {
  }

  protected void initViewEvents()
  {
  }

  protected void refreshData()
  {
  }

  protected void refreshViews()
  {
  }

  protected void refreshViewEvents()
  {
  }

  public BaseActivity getBaseActivity()
  {
    return (BaseActivity)getActivity();
  }

  public void startActivityForResult(Intent intent, int requestCode)
  {
    super.startActivityForResult(intent, requestCode);
//    getActivity().overridePendingTransition(
//      this.resourceUtil.getAnimResourceId("anim_push_right_in"), 
//      this.resourceUtil.getAnimResourceId("anim_push_left_out"));
  }

  protected BaseLayoutInflater getBaseLayoutInflater()
  {
    return BaseLayoutInflater.from(getActivity());
  }

  public void startActivity(Intent intent)
  {
    super.startActivity(intent);
//    getActivity().overridePendingTransition(
//      this.resourceUtil.getAnimResourceId("anim_push_right_in"), 
//      this.resourceUtil.getAnimResourceId("anim_push_left_out"));
  }

}

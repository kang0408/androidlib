package com.langk.base.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.List;
import java.util.Map;

public abstract class BaseAdapter<T> extends ArrayAdapter<T> {
	private static final String TAG = BaseAdapter.class.getSimpleName();
	protected ViewGroup viewContainer;
	protected List<T> data;
	private int layoutResourceId;
	protected int businessType;

	public int getLayoutResourceId() {
		return this.layoutResourceId;
	}

	public BaseAdapter(Context context, int layoutResourceId, List<T> objects) {
		super(context, layoutResourceId, objects);
		this.layoutResourceId = layoutResourceId;
		this.data = objects;
	}

	public BaseAdapter(Context context, int layoutResourceId, List<T> objects,
			int businessType) {
		super(context, layoutResourceId, objects);
		this.layoutResourceId = layoutResourceId;
		this.data = objects;
		this.businessType = businessType;
	}

	public int getCount() {
		return super.getCount();
	}

	public T getItem(int position) {
		return super.getItem(position);
	}

	public int getPosition(T item) {
		return super.getPosition(item);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = initConvertView(position, convertView);

		BaseViewHolder viewHolder = (BaseViewHolder) convertView.getTag();

		T item = getItem(position);

		initViews(position, viewHolder, item);

		initViewEvents(position, viewHolder, item);

		return convertView;
	}

	protected View initConvertView(int position, View convertView) {
		return convertView;
	}

	protected void initViews(int position, BaseViewHolder viewHolder, T item) {
	}

	protected void initViewEvents(int position, BaseViewHolder viewHolder,
			T item) {
	}

	public List<T> getData() {
		return this.data;
	}

	public void updateData(List<T> objects) {
		clear();
		if (objects != null)
			addAll(objects);
	}

	public static boolean setListViewHeight(ListView listView) {
		return setListViewHeight(listView, 2.147484E+009F);
	}

	public static boolean setListViewHeight(ListView listView, float fMaxHeight) {
		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return false;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);

			if (listItem != null) {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		int iTempHeight = totalHeight + listView.getDividerHeight()
				* (listAdapter.getCount() - 1);

		int iMaxHeight = (int) fMaxHeight;

		boolean bOutOfMaxHeight = iTempHeight > iMaxHeight;

		params.height = (bOutOfMaxHeight ? iMaxHeight : iTempHeight);

		listView.setLayoutParams(params);

		return bOutOfMaxHeight;
	}

	public ViewGroup getViewContainer() {
		return this.viewContainer;
	}

	public void setViewContainer(ViewGroup viewContainer) {
		this.viewContainer = viewContainer;
	}

	public static abstract interface OnCheckedStateChangedListener<T> {
		public abstract void OnCheckedStateChanged(T paramT);
	}

	public abstract class BaseViewHolder {
	}
}
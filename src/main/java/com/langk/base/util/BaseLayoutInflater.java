package com.langk.base.util;

import android.content.Context;
import android.view.LayoutInflater;

public class BaseLayoutInflater extends LayoutInflater {
	
	
	protected BaseLayoutInflater(Context newContext) {
		super(newContext);
		// TODO Auto-generated constructor stub
	}
	
	protected BaseLayoutInflater(LayoutInflater original, Context newContext) {
		super(original, newContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public LayoutInflater cloneInContext(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static BaseLayoutInflater from(Context context){
		return (BaseLayoutInflater) LayoutInflater.from(context);
	}
	
}

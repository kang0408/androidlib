package com.langk.base.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.langk.base.crypto.Endecrypt;
import com.langk.base.http.request.BaseHttpRequest;
import com.langk.base.http.request.IBaseCryptoHttpRequest;
import com.langk.base.log.Log;

public class HttpCryptoManager {

	private static final String TAG = HttpCryptoManager.class.getSimpleName();

	public static String checkAndDecJson(Context nativeContext,
			IBaseCryptoHttpRequest baseHttpRequest, Header[] headers,
			String url, String strJson) {
		if (baseHttpRequest.isCryptoFlag()) {
			Log.d(TAG, "crypto: " + strJson);
			String key = nativeContext.getString(baseHttpRequest
					.getCryptoConfigFileId());
			String reValue2 = Endecrypt.decodeValue(key, strJson);
			Log.d(TAG, "content: " + reValue2);
			return reValue2;
		}
		return strJson;
	}

	protected static String checkAndEncJson(Context nativeContext,
			BaseHttpRequest baseHttpRequest, String url, String strJson) {
		
		

		if (baseHttpRequest.isCryptoFlag()) {
			Log.d(TAG, "content: " + strJson);
			String key = nativeContext.getString(baseHttpRequest
					.getCryptoConfigFileId());
			String reValue = Endecrypt.encode(key,strJson);
			reValue = reValue.trim().intern();
			Log.d(TAG, "crypto: " + reValue);
			return reValue;
		}
		return strJson;
	
	}
}
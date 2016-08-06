package com.langk.base.http.respones;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.google.gson.reflect.TypeToken;
import com.langk.base.http.HttpCryptoManager;
import com.langk.base.http.request.BaseRequestParams;
import com.langk.base.http.request.IBaseCryptoHttpRequest;
import com.langk.base.log.Log;
import com.langk.base.resoure.ResourceUtil;
import com.langk.base.util.JsonUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;

public abstract class BaseAsyncHttpResponseHandler<T extends BaseHttpResponse> extends
		AsyncHttpResponseHandler {
	private static final String TAG = BaseAsyncHttpResponseHandler.class
			.getSimpleName();
	protected String url;
	protected BaseRequestParams params;
	protected boolean isShowDialogFlag = false;
	protected String defaultDatePattern;
	protected IBaseCryptoHttpRequest baseHttpRequest;
	private ProgressDialog progressDialog;
	private Context mContext;

	public BaseAsyncHttpResponseHandler() {
	}

	public BaseAsyncHttpResponseHandler(boolean isShowDialogFlag) {
		this();
		this.isShowDialogFlag = isShowDialogFlag;
	}

	public TypeToken<T> getDataTypeToken() {
		return null;
	}

	public void onStart() {
		super.onStart();
		Log.d(TAG, " onStart.. url= " + this.url);
		progressStart();
	}

	public void onFinish() {
		super.onFinish();
		Log.d(TAG, " onFinish.. url= " + this.url);

		progressStop();
	}

	
	public void onCancel() {
		super.onCancel();
		Log.d(TAG, " onCancel..  url= " + this.url);
		progressStop();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onSuccess(String content) {
		Log.d(TAG, " onSuccess..  url= " + this.url);
		Log.d(TAG, "content = " + content);

		T objResponse = null;
		try {
			TypeToken objTypeToken = getDataTypeToken();
			if (objTypeToken != null) {
				objResponse = (T) JsonUtil.fromJson(content,
						objTypeToken);
			} else {
				Class responseClass = (Class) ((java.lang.reflect.ParameterizedType) getClass()
						.getGenericSuperclass()).getActualTypeArguments()[0];

				objResponse = (T) JsonUtil.fromJson(content,
						responseClass, this.defaultDatePattern);
			}
			if (objResponse!=null) {
				objResponse.setSuccessful();
			}
			if ((objResponse != null) && (objResponse.getSuccessful()))
				onSuccessTrans(objResponse);
			else
				onFailureTrans(objResponse);
		} catch (Exception e) {
			onFailureTrans(objResponse);
			Log.e(TAG, "onSuccess callback error" + content, e);
		}
	}


	public void onSuccessTrans(T responseModelVO) {
		Log.d(TAG, " onSuccessTrans..  ");
	}

	public void onFailureTrans(T responseModelVO) {
		Log.d(TAG, " onFailureTrans..  responseModelVO = " + responseModelVO);
		String strCode = null;
		String strMsg = null;

		if (responseModelVO != null) {
			strCode = responseModelVO.getResultCode();
			strMsg = responseModelVO.getResultDesc();
		}

		Log.d(TAG, " onFailureTrans..  strCode = " + strCode);
		Log.d(TAG, " onFailureTrans..  strMsg = " + strMsg);

		popUpTransErrorDialog(strCode, strMsg);
	}

	private void popUpHttpErrorDialog(String code, String msg) {
		ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
		String strTitle = resourceUtil
				.getResourceString("common_dialog_title_error");
		String strCode = code != null ? code : "1";

		String strDesErrorMsg = resourceUtil
				.getResourceString("error_server_common_msg");
		String strMsg = msg != null ? msg : strDesErrorMsg;

		onPopUpHttpErrorDialogPre(strTitle, strCode, strMsg);
	}

	private void popUpTransErrorDialog(String code, String msg) {
		ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
		String strTitle = resourceUtil
				.getResourceString("common_dialog_title_error");
		String strCode = code != null ? code : "1";
		String strDesErrorMsg = resourceUtil
				.getResourceString("error_server_common_msg");
		String strMsg = msg != null ? msg : strDesErrorMsg;

		onPopUpTransErrorDialogPre(strTitle, strCode, strMsg);
	}

	public void onPopUpTransErrorDialogPre(String strTitle, String strCode,
			String strMsg) {
		onPopUpErrorDialog(strTitle, strCode, strMsg);
	}

	public void onPopUpHttpErrorDialogPre(String strTitle, String strCode,
			String strMsg) {
		onPopUpErrorDialog(strTitle, strCode, strMsg);
	}

	public void onPopUpErrorDialog(String strTitle, String strCode,
			String strMsg) {
		Log.i(TAG, "onPopUpErrorDialog: strCode: " + strCode + ",  strMsg: "
				+ strMsg);
	}

	public void progressStart() {
		progressStart(false);
	}

	public void progressStart(boolean cancelable) {
		if (this.isShowDialogFlag) {
			ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
			String strTitle = resourceUtil
					.getResourceString("common_dialog_title_info");
			String strInfo = resourceUtil
					.getResourceString("common_connecting");
			progressStart(strTitle, strInfo, cancelable);
		}
	}

	public void progressStart(String title, String message, boolean cancelable) {
		try {
			progressStop();

			showProgressDialog(title, message, cancelable);
		} catch (Exception e) {
			Log.w("AsyncHttpResponseHandler", "progressStart error", e);
		}
	}

	private void progressStop() {
		// TODO Auto-generated method stub
		if (this.progressDialog!=null) {
			if (this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();
			}
			this.progressDialog = null;
		}
	}

	public void showProgressDialog(String title, String message,
			boolean cancelable) {
		ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
		int styleId = resourceUtil.getResourceIdResNameAndResType(
				"Transparent", "style");
		this.progressDialog = new ProgressDialog(
				(Context) this.getNativeContext(), styleId);

		int layoutId = resourceUtil.getResourceIdResNameAndResType(
				"dialog_loading_image", "layout");

		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setCancelable(cancelable);
		this.progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						BaseAsyncHttpResponseHandler.this.onCancel();
					}
				});
		this.progressDialog.show();
		this.progressDialog.setContentView(layoutId);
	}

	public Context getNativeContext() {
		return mContext;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BaseRequestParams getParams() {
		return this.params;
	}

	public void setParams(BaseRequestParams params) {
		this.params = params;
	}

	public String getDefaultDatePattern() {
		return this.defaultDatePattern;
	}

	public void setDefaultDatePattern(String defaultDatePattern) {
		this.defaultDatePattern = defaultDatePattern;
	}

	public boolean isShowDialogFlag() {
		return this.isShowDialogFlag;
	}

	public void setShowDialogFlag(boolean isShowDialogFlag) {
		this.isShowDialogFlag = isShowDialogFlag;
	}

	public IBaseCryptoHttpRequest getBaseHttpRequest() {
		return this.baseHttpRequest;
	}

	public void setBaseHttpRequest(IBaseCryptoHttpRequest baseHttpRequest) {
		this.baseHttpRequest = baseHttpRequest;
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable th) {
		// TODO Auto-generated method stub
		String content = StringUtils.newStringUtf8(arg2);
		Log.d(TAG, " onFailure..  http url= " + this.url);
		Log.e(TAG, "error content = \n " + content, th);

		String strCode = null;
		String strMsg = null;

		if (th != null) {
			ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());

			if ((th instanceof HttpResponseException)) {
				int statusCode = ((HttpResponseException) th).getStatusCode();
				Log.w(TAG, "http status code = " + statusCode);
				strCode = String.valueOf(statusCode);
			}

			if ((th instanceof ConnectException)) {
				strCode = "nerwork_0001c";
				strMsg = resourceUtil
						.getResourceString("common_communication_fail");
			}

			if ((th instanceof SocketTimeoutException)) {
				strCode = "nerwork_0002c";
				strMsg = resourceUtil
						.getResourceString("common_communication_timeout");
			}

			if ((th instanceof UnknownHostException)) {
				strCode = "nerwork_0003c";
				strMsg = resourceUtil
						.getResourceString("common_communication_fail");
			}

			if ((th instanceof UnsupportedEncodingException)) {
				strCode = "nerwork_0003c";
				strMsg = resourceUtil
						.getResourceString("common_communication_fail");
			}
		}

		popUpHttpErrorDialog(strCode, strMsg);
	
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String content = StringUtils.newStringUtf8(arg2);
		if (arg1 != null) {
			String strContent = HttpCryptoManager.checkAndDecJson(
					getNativeContext(), getBaseHttpRequest(), arg1,
					getUrl(), content);
			onSuccess(strContent);
		} else {
			onSuccess(content);
		}
	}

	public void setNativeContext(Context context) {
		// TODO Auto-generated method stub
		this.mContext = context;
	}
}
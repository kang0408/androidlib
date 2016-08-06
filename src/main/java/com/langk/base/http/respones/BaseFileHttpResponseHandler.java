package com.langk.base.http.respones;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;

import com.langk.base.log.Log;
import com.langk.base.resoure.ResourceUtil;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

public class BaseFileHttpResponseHandler extends FileAsyncHttpResponseHandler {
	private static final String TAG = BaseFileHttpResponseHandler.class
			.getSimpleName();
	private boolean isShowDialogFlag;
	protected String url;
	private String mFilePath;
	private ProgressDialog progressDialog;
	private Context mContext;

	public BaseFileHttpResponseHandler(String filePath) {
		super(new File(filePath));
		this.mFilePath = filePath;
	}

	public void onStart() {
		Log.d(TAG, " onStart..  mFilePath= " + this.mFilePath);
		super.onStart();
		progressStart();
	}

	public void onFinish() {
		Log.d(TAG, " onFinish..  mFilePath= " + this.mFilePath);
		super.onFinish();
		progressStop();
	}

	public void onCancel() {
		super.onCancel();
		Log.d(TAG, " onCancel..  url= " + this.url);
		progressStop();
	}



	private void popUpHttpErrorDialog(String code, String msg) {
		ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
		String strTitle = resourceUtil
				.getResourceString("common_dialog_title_error");
		String strCode = code != null ? code : "1";

		String strDesErrorMsg = resourceUtil
				.getResourceString("common_file_download_fail");

		String strMsg = msg != null ? msg : strDesErrorMsg;

		onPopUpErrorDialog(strTitle, strCode, strMsg);
	}

	public void onPopUpErrorDialog(String strTitle, String strCode,
			String strMsg) {
		Log.i(TAG, "onPopUpErrorDialog: strCode: " + strCode + ": strMsg");
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
	

	public String getProgressTitle() {
		ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
		return resourceUtil.getResourceString("common_dialog_title_info");
	}

	public String getProgressInfo() {
		ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
		return resourceUtil
				.getResourceString("common_dialog_title_info_downloading");
	}

	public void progressStart(String title, String message, boolean cancelable) {
		progressStop();
		try {
			this.progressDialog = new ProgressDialog(
					(Context) this.getNativeContext());

			this.progressDialog.setProgressStyle(1);

			this.progressDialog.setTitle(title);

			this.progressDialog.setMessage(message);

			loadProgressDialogIcon(this.progressDialog);

			this.progressDialog.setIndeterminate(false);

			this.progressDialog.setProgress(0);

			this.progressDialog.setCancelable(cancelable);

			this.progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							BaseFileHttpResponseHandler.this.onCancel();						}
					});
			this.progressDialog.show();
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

	public void loadProgressDialogIcon(ProgressDialog progressDialog) {
		progressDialog.setIcon(getProgressDialogIcon());
	}

	public Drawable getProgressDialogIcon() {
		ResourceUtil resourceUtil = new ResourceUtil(getNativeContext());
		return resourceUtil.getResourceDrawable("ic_launcher");
	}

	public Context getNativeContext() {
		return this.mContext;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, Throwable th, File arg3) {
		// TODO Auto-generated method stub

		Log.d(TAG, " onFailure..  http url= " + this.url + " mFilePath "
				+ this.mFilePath);

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

	@Override
	public final void onSuccess(int statusCode, Header[] arg1, File file) {
		// TODO Auto-generated method stub
		onSuccess(statusCode,file);
	}

	public void onSuccess(int statusCode, File file) {
		// TODO Auto-generated method stub
	}

	public void setNativeContext(Context context) {
		// TODO Auto-generated method stub
		this.mContext = context;
	}
}
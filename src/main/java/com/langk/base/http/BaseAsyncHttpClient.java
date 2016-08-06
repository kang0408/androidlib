package com.langk.base.http;

import android.content.Context;
import android.net.Proxy;

import com.langk.base.http.request.BaseRequestParams;
import com.langk.base.log.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

public class BaseAsyncHttpClient extends AsyncHttpClient {
	private boolean needProxy = false;

	private String hostname = "";

	private int hostport = 0;

	private static final String TAG = BaseAsyncHttpClient.class.getSimpleName();

	public BaseAsyncHttpClient() {
		setRedirection();
		setUserAgent("AndroidHttpClient|1.0");
	}

	protected void initConnectionParams() {
		setMaxConnections(HttpManager.MAX_TOTAL_CONNECTIONS);
		setMaxRetriesAndTimeout(HttpManager.MAX_RETRIES,
				HttpManager.SOCKET_TIME_OUT);

	}

	protected void initCookieStore() {
		CookieStore cookieStore = new BasicCookieStore();
		setCookieStore(cookieStore);

		getHttpClient().getParams().setParameter("http.protocol.cookie-policy",
				"compatibility");

		getHttpClient().getParams().setParameter("http.protocol.cookie-policy",
				"basecsf");
		getHttpClient().getParams().setParameter(
				"http.protocol.single-cookie-header", Boolean.valueOf(true));
	}

	public void setUserAgent(String userAgent) {
		super.setUserAgent(userAgent);
	}

	protected void setRedirection() {
		DefaultHttpClient defaultHttpClient = (DefaultHttpClient) getHttpClient();

		defaultHttpClient.setRedirectHandler(new DefaultRedirectHandler() {
			public boolean isRedirectRequested(HttpResponse response,
					HttpContext context) {
				boolean isRedirect = super.isRedirectRequested(response,
						context);
				if (!isRedirect) {
					int responseCode = response.getStatusLine().getStatusCode();
					if ((responseCode == 302) || (responseCode == 301)
							|| (responseCode == 303) || (responseCode == 307)) {
						return true;
					}
				}
				return isRedirect;
			}

			public URI getLocationURI(HttpResponse response, HttpContext context)
					throws ProtocolException {
				String newUri = response.getLastHeader("Location").getValue();
				return URI.create(newUri);
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void initProxy(boolean needProxy) {
		if (needProxy) {
			this.needProxy = true;
			this.hostname = Proxy.getDefaultHost();
			this.hostport = Proxy.getDefaultPort();
			Log.i(TAG, "initProxy hostname:" + this.hostname + " hostport:"
					+ this.hostport);
			HttpHost proxy = new HttpHost(this.hostname, this.hostport);
			getHttpClient().getParams().setParameter(
					"http.route.default-proxy", proxy);
		} else {
			this.hostname = "";
			this.hostport = 0;
			this.needProxy = false;
		}
	}

	public void setTimeout(int timeout) {
		super.setTimeout(timeout);
	}

	public void setPlainSocketFactory(PlainSocketFactory plainSocketFactory,
			int httpPort) {
		getHttpClient().getConnectionManager().getSchemeRegistry()
				.register(new Scheme("http", plainSocketFactory, httpPort));
	}

	public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory,
			int httpsPort) {
		getHttpClient().getConnectionManager().getSchemeRegistry()
				.register(new Scheme("https", sslSocketFactory, httpsPort));
	}

	public void addHeader(String header, String value) {
		super.addHeader(header, value);
	}

	public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
		super.cancelRequests(context, mayInterruptIfRunning);
	}

	public void get(Context context, String url, BaseRequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		super.get(context, url, params, responseHandler);
	}

	public void get(Context context, String url, BasicHeader[] headers,
			BaseRequestParams params, AsyncHttpResponseHandler responseHandler) {
		super.get(context, url, headers, params, responseHandler);
	}

	public void post(Context context, String url, BaseRequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		super.post(context, url, params, responseHandler);
	}

	public void post(Context context, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		super.post(context, url, entity, contentType, responseHandler);
	}

	public void post(Context context, String url, BasicHeader[] headers,
			BaseRequestParams params, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		super.post(context, url, headers, params, contentType, responseHandler);
	}

	public void post(Context context, String url, BasicHeader[] headers,
			HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		super.post(context, url, headers, entity, contentType, responseHandler);
	}

	public void put(Context context, String url, BaseRequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		super.put(context, url, params, responseHandler);
	}

	public void put(Context context, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		super.put(context, url, entity, contentType, responseHandler);
	}

	public void put(Context context, String url, Header[] headers,
			HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		super.put(context, url, headers, entity, contentType, responseHandler);
	}

	public void delete(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		super.delete(context, url, responseHandler);
	}

	public void delete(Context context, String url, Header[] headers,
			AsyncHttpResponseHandler responseHandler) {
		super.delete(context, url, headers, responseHandler);
	}

	protected void sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType, AsyncHttpResponseHandler responseHandler,
			Context context) {
		Log.d(TAG, "sendRequest start...");
		super.sendRequest(client, httpContext, uriRequest, contentType,
				responseHandler, context);
	}

	public boolean isNeedProxy() {
		return this.needProxy;
	}

	public void setNeedProxy(boolean needProxy) {
		this.needProxy = needProxy;
	}
}
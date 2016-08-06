package com.langk.base.http;

import android.content.Context;
import android.net.Proxy;

import com.alibaba.fastjson.JSON;
import com.langk.base.dialog.CustomToast;
import com.langk.base.http.request.BaseHttpRequest;
import com.langk.base.http.request.BaseRequestParams;
import com.langk.base.http.respones.BaseAsyncHttpResponseHandler;
import com.langk.base.http.respones.BaseFileHttpResponseHandler;
import com.langk.base.http.respones.BaseHttpResponse;
import com.langk.base.log.Log;
import com.langk.base.resoure.ResourceUtil;
import com.langk.base.util.DeviceUtil;
import com.langk.base.util.JsonUtil;
import com.langk.base.util.StringUtil;
import com.loopj.android.http.MySSLSocketFactory;

import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

@SuppressWarnings("deprecation")
public class HttpManager extends HttpCryptoManager
{
  private static final String TAG = HttpManager.class.getSimpleName();
  private static final String MIME_TYPE_BIN = "application/octet-stream";
  private static final String MIME_TYPE_JSON = "application/json";
  public static final String DEFAULT_ENCODE = "UTF-8";
  public static final String HTTP = "http";
  public static final String HTTPS = "https";
  public static int HTTP_PORT = 80;

  public static int HTTPS_PORT = 443;
  public static final String USER_AGENT = "User-Agent";
  public static final String HTTP_USER_AGENT = "AndroidHttpClient|1.0";
  public static final String DEFAULT_LOCALE = "zh_CN";
  public static final String HTTP_METHOD_GET = "get";
  public static final String HTTP_METHOD_POST = "post";
  public static int MAX_TOTAL_CONNECTIONS = 20;

  public static int MAX_RETRIES = 5;

  public static int SOCKET_TIME_OUT = 20000;

  public static int SOCKET_BUFFER_SIZE = 8192;

  private static BaseAsyncHttpClient httpClient = new BaseAsyncHttpClient();
  private static BaseAsyncHttpClient httpClientTimeOut10s;
  private static BaseAsyncHttpClient httpClientTimeOut30s;
  private static String udid;
  static final int MAX_CONNECTIONS = 30;

  static
  {
    System.setProperty("http.maxConnections", 
      String.valueOf(30));
  }

  public static void post(Context context, BaseHttpRequest requestVO, BaseAsyncHttpResponseHandler<? extends BaseHttpResponse> responseHandler)
  {
    post(context, requestVO, responseHandler, SOCKET_TIME_OUT);
  }

  public static void post(Context context, BaseHttpRequest requestVO, BaseAsyncHttpResponseHandler<? extends BaseHttpResponse> responseHandler, int timeout)
  {
    try
    {
      responseHandler.setNativeContext(context);

      initUdid(context, requestVO);

      String url = requestVO.genRequestUrl();

      if (StringUtil.isEmpty(url)) {
        Log.w(TAG, "url is empty, please check it");
        ResourceUtil resourceUtil = new ResourceUtil(context);
        CustomToast.show(context, resourceUtil
          .getResourceString("common_communication_fail"));
        return;
      }

      responseHandler.setUrl(url);

      responseHandler.setBaseHttpRequest(requestVO);

      String strJson = JsonUtil.toJson(requestVO, 
        requestVO.getDefaultDatePattern());

      String strCryptoJson = checkAndEncJson(context, requestVO, 
        url, strJson);

      StringEntity entity = new StringEntity(strCryptoJson, "UTF-8");

      logRequest(url, requestVO);

      BasicHeader[] reqHeaders = requestVO.getHeadersArray();

      post(context, url, reqHeaders, entity, MIME_TYPE_JSON, 
        responseHandler, timeout);
    } catch (Exception e) {
      Log.e(TAG, "http post error", e);
    }
  }



public static void post(Context context, String url, BaseRequestParams params, BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler)
  {
    post(context, url, params, responseHandler, SOCKET_TIME_OUT);
  }

  public static void post(Context context, String url, BaseRequestParams params, BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler, int timeout)
  {
    responseHandler.setNativeContext(context);

    logRequest(url, params);

    BaseAsyncHttpClient matchHttpClient = getMachedHttpClient(timeout);
    matchHttpClient.post(context, url, params, responseHandler);
  }

//  private static void post(Context context, String url, HttpEntity entity, String contentType, BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler, int timeout)
//  {
//    responseHandler.setNativeContext(context);
//
//    logRequest(url, entity);
//
//    BaseAsyncHttpClient matchHttpClient = getMachedHttpClient(timeout);
//    matchHttpClient
//      .post(context, url, entity, contentType, responseHandler);
//  }

  public static void post(Context context, String url, BasicHeader[] headers, HttpEntity entity, String contentType, BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler)
  {
    post(context, url, headers, entity, contentType, responseHandler, 
      SOCKET_TIME_OUT);
  }

  public static void post(Context context, String url, BasicHeader[] headers, HttpEntity entity, String contentType, BaseAsyncHttpResponseHandler<? extends BaseHttpResponse> responseHandler, int timeout)
  {
    responseHandler.setNativeContext(context);

    logRequest(url, entity);

    BaseAsyncHttpClient matchHttpClient = getMachedHttpClient(timeout);
    matchHttpClient.post(context, url, headers, entity, contentType, 
      responseHandler);
  }

  public static void get(Context context, String url, BaseRequestParams params, BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler)
  {
    get(context, url, params, responseHandler, SOCKET_TIME_OUT);
  }

  public static void get(Context context, String url, BaseRequestParams params, BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler, int timeout)
  {
    responseHandler.setNativeContext(context);

    logRequest(url, params);

    BaseAsyncHttpClient matchHttpClient = getMachedHttpClient(timeout);
    matchHttpClient.get(context, url, params, responseHandler);
  }

  public static void get(Context context, String url, BasicHeader[] headers, BaseRequestParams params,  BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler)
  {
    get(context, url, headers, params, responseHandler, SOCKET_TIME_OUT);
  }

  public static void get(Context context, String url, BasicHeader[] headers, BaseRequestParams params,  BaseAsyncHttpResponseHandler<BaseHttpResponse> responseHandler, int timeout)
  {
    responseHandler.setNativeContext(context);

    logRequest(url, params);

    BaseAsyncHttpClient matchHttpClient = getMachedHttpClient(timeout);
    matchHttpClient.get(context, url, headers, params, responseHandler);
  }

  public static void postFile(Context context, BaseHttpRequest requestVO, BaseFileHttpResponseHandler fileHttpResponseHandler)
  {
    try
    {
      String strUrl = requestVO.genRequestUrl();

      fileHttpResponseHandler.setNativeContext(context);

      if (StringUtil.isEmpty(strUrl)) {
        Log.w(TAG, "strUrl is empty, please check it");
        ResourceUtil resourceUtil = new ResourceUtil(context);
        CustomToast.show(context, resourceUtil
          .getResourceString("common_communication_fail"));
        return;
      }

      fileHttpResponseHandler.setUrl(strUrl);

      String strJson = JsonUtil.toJson(requestVO, 
        requestVO.getDefaultDatePattern());

      StringEntity entity = new StringEntity(strJson, "UTF-8");

      logRequest(strUrl, requestVO);

      BaseAsyncHttpClient objClient = getMachedHttpClient(30);
      objClient.post(context, strUrl, entity, MIME_TYPE_JSON, 
        fileHttpResponseHandler);
    } catch (Exception e) {
      Log.e(TAG, "http postFile error", e);
    }
  }

  private static BaseAsyncHttpClient getMachedHttpClient(int timeout)
  {
    if (timeout >= 30000) {
      if (httpClientTimeOut30s == null) {
        httpClientTimeOut30s = new BaseAsyncHttpClient();
      }

      return httpClientTimeOut30s;
    }if ((timeout <= 10000) && (timeout > 0))
    {
      if (httpClientTimeOut10s == null) {
        httpClientTimeOut10s = new BaseAsyncHttpClient();
      }
      return httpClientTimeOut10s;
    }

    return httpClient;
  }

  public static boolean checkIifNeedProxy(Context context)
  {
    if (context == null) {
      return false;
    }

    String proxyHost = Proxy.getDefaultHost();
    boolean needProxy = proxyHost != null;

    return needProxy;
  }

  public static void initProxy(Context nativeContext)
  {
    boolean needProxy = checkIifNeedProxy(nativeContext);

    Log.i(TAG, "initProxy needProxy:" + needProxy);

    httpClient.initProxy(needProxy);
  }

  public static void destroyRequests(Context context)
  {
    httpClient.cancelRequests(context, true);
  }

  public static void logRequest(String url, Object params)
  {
    Log.d(TAG, "url: " + url);
    if (params != null)
      if ((params instanceof BaseHttpRequest)) {
        Log.d(TAG, 
          params.getClass().getSimpleName() + ": " + 
          JsonUtil.toJson(params));
      } else if (((params instanceof BaseRequestParams)) && 
        (params != null)) {
        BaseRequestParams objPrams = (BaseRequestParams)params;
        try {
          Log.d(TAG, params.getClass().getSimpleName() + ": " + 
            JSON.toJSONString(objPrams.getParamsList()));
        } catch (Throwable localThrowable) {
          Log.d(TAG, params.getClass().getSimpleName() + ": " + 
            objPrams.getParamsList());
        }
      }
  }

  public static BaseAsyncHttpClient getHttpClient()
  {
    return httpClient;
  }

  private static void initUdid(Context context, BaseHttpRequest requestVO)
  {
    if (udid == null) {
      udid = DeviceUtil.getUDID(context);
    }

    requestVO.setDId(udid);
  }

  public static void setDefaultSSLSocketFactory()
  {
    try
    {
      SSLSocketFactory sf = null;

      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null, null);
      sf = new MySSLSocketFactory(keyStore);

      setSSLSocketFactory(sf);
    } catch (Exception e) {
      Log.e(TAG, "setDefaultSSLSocketFactory error", e);
    }
  }

  public static void setTimeout(int timeout)
  {
    setConnManagerTimeout(timeout);
    setSoTimeout(timeout);
    setConnectionTimeout(timeout);
  }

  public static void setConnManagerTimeout(int timeout)
  {
    HttpParams httpParams = httpClient.getHttpClient().getParams();
    ConnManagerParams.setTimeout(httpParams, timeout);
  }

  public static void setSoTimeout(int timeout)
  {
    HttpParams httpParams = httpClient.getHttpClient().getParams();
    HttpConnectionParams.setSoTimeout(httpParams, timeout);
    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
  }

  public static void setConnectionTimeout(int timeout)
  {
    HttpParams httpParams = httpClient.getHttpClient().getParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
  }

  public static void setSSLSocketFactory(SSLSocketFactory sslSocketFactory, int httpsPort)
  {
    httpClient.setSSLSocketFactory(sslSocketFactory, httpsPort);
  }

  public static void setSSLSocketFactory(SSLSocketFactory sslSocketFactory)
  {
    httpClient.setSSLSocketFactory(sslSocketFactory, HTTPS_PORT);
  }
}
package com.langk.base.http.request;

import com.loopj.android.http.RequestParams;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;

public class BaseRequestParams extends RequestParams
{
  public BaseRequestParams(Map<String, String> params)
  {
    super(params);
  }

  public BaseRequestParams()
  {
  }

  public List<BasicNameValuePair> getParamsList()
  {
    return super.getParamsList();
  }
}
package com.langk.base.http.respones;

import java.io.Serializable;

public abstract class BaseHttpResponse
  implements Serializable
{
  private static final long serialVersionUID = -361685764012593446L;
  protected static final String CODE_COMMON_SUCCESS = "200";
  protected boolean mSuccessful = true;

  public void setSuccessful(boolean successful)
  {
    this.mSuccessful = successful;
  }

  public boolean getSuccessful()
  {
    if (!this.mSuccessful) {
      return this.mSuccessful;
    }
    return getDefaultSucessCode().equals(getResultCode());
  }

  public abstract String getResultCode();

  public abstract String getResultDesc();
  
  public abstract void setSuccessful();

  public String getDefaultSucessCode()
  {
    return CODE_COMMON_SUCCESS;
  }
}
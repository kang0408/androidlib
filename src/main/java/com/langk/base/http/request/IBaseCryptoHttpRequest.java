package com.langk.base.http.request;

import org.apache.http.message.BasicHeader;

/**
 * 加密接口
 * @author K
 *
 */
public abstract interface IBaseCryptoHttpRequest
{
	
  public abstract boolean isCryptoFlag();

  public abstract int getCryptoConfigFileId();

  public abstract void addHeader(@SuppressWarnings("deprecation") BasicHeader paramBasicHeader);
}
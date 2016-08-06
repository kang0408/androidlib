package com.langk.base.http.request;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicHeader;

/**
 * 请求基类
 * 
 * @author K
 *
 */
public class BaseHttpRequest implements Serializable, IBaseCryptoHttpRequest {

	@Expose(deserialize = false, serialize = false)
	private static final long serialVersionUID = 1050015245603624164L;
	protected String DId;
	protected String defaultDatePattern;
	private List<BaseAttachment> lstAttachment = new ArrayList<BaseAttachment>();

	@Expose(deserialize = false, serialize = false)
	private boolean httpMultipartFlag;

	@Expose(deserialize = false, serialize = false)
	protected boolean serverHttpsFlag = false;

	@Expose(deserialize = false, serialize = false)
	protected String serverIp;

	@Expose(deserialize = false, serialize = false)
	protected String serverPort;

	/**
	 * 上下文
	 */
	@Expose(deserialize = false, serialize = false)
	protected String webServicePath;

	/**
	 * 方法名
	 */
	@Expose(deserialize = false, serialize = false)
	protected String webServiceMethod;

	@Expose(deserialize = false, serialize = false)
	protected List<BasicHeader> headers = new ArrayList<BasicHeader>();

	@Expose(deserialize = false, serialize = false)
	protected boolean cryptoFlag = false;

	@Expose(deserialize = false, serialize = false)
	protected int cryptoConfigFileId;

	protected static final String CryFlag = "CryFlag";

	public BaseHttpRequest() {
	}

	public BaseHttpRequest(boolean serverHttpsFlag, String serverIp,
			String serverPort) {
		this.serverHttpsFlag = serverHttpsFlag;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}

	public String genRequestUrl() {
		StringBuffer sbf = new StringBuffer();
		if (this.serverHttpsFlag)
			sbf.append("https://");
		else {
			sbf.append("http://");
		}

		sbf.append(this.serverIp);
		sbf.append(":");
		sbf.append(this.serverPort);
		sbf.append("/");
		sbf.append(this.webServicePath);
		sbf.append("/");
		sbf.append(this.webServiceMethod);

		return sbf.toString();
	}

	public void addHeader(BasicHeader header) {
		if (header == null) {
			return;
		}

		boolean ifExist = false;
		for (BasicHeader item : this.headers) {
			String hName = item.getName();
			if ((hName != null) && (hName.equalsIgnoreCase(header.getName()))) {
				ifExist = true;
				break;
			}
		}

		if (!ifExist)
			this.headers.add(header);
	}

	public BasicHeader[] getHeadersArray() {
		if (this.headers == null || this.headers.equals("")) {
			return null;
		}
		return (BasicHeader[]) this.headers
				.toArray(new BasicHeader[this.headers.size()]);
	}

	public List<BaseAttachment> getLstAttachment() {
		return this.lstAttachment;
	}

	public void setLstAttachment(List<BaseAttachment> lstAttachment) {
		this.lstAttachment = lstAttachment;
	}

	public boolean isHttpMultipartFlag() {
		return this.httpMultipartFlag;
	}

	public void setHttpMultipartFlag(boolean httpMultipartFlag) {
		this.httpMultipartFlag = httpMultipartFlag;
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getWebServicePath() {
		return this.webServicePath;
	}

	public void setWebServicePath(String webServicePath) {
		this.webServicePath = webServicePath;
	}

	public String getWebServiceMethod() {
		return this.webServiceMethod;
	}

	public void setWebServiceMethod(String webServiceMethod) {
		this.webServiceMethod = webServiceMethod;
	}

	public boolean isServerHttpsFlag() {
		return this.serverHttpsFlag;
	}

	public void setServerHttpsFlag(boolean serverHttpsFlag) {
		this.serverHttpsFlag = serverHttpsFlag;
	}

	public String getDId() {
		return this.DId;
	}

	public void setDId(String dId) {
		this.DId = dId;
	}

	public String getDefaultDatePattern() {
		return this.defaultDatePattern;
	}

	public void setDefaultDatePattern(String defaultDatePattern) {
		this.defaultDatePattern = defaultDatePattern;
	}

	public List<BasicHeader> getHeaders() {
		return this.headers;
	}

	public void setHeaders(List<BasicHeader> headers) {
		this.headers = headers;
	}

	public boolean isCryptoFlag() {
		return this.cryptoFlag;
	}

	@SuppressWarnings("deprecation")
	public void setCryptoFlag(boolean cryptoFlag) {
		this.cryptoFlag = cryptoFlag;
		if (cryptoFlag) {
			addHeader(new BasicHeader(CryFlag, cryptoFlag + ""));
		}
	}

	public int getCryptoConfigFileId() {
		return this.cryptoConfigFileId;
	}

	public void setCryptoConfigFileId(int cryptoConfigFileId) {
		this.cryptoConfigFileId = cryptoConfigFileId;
	}
}

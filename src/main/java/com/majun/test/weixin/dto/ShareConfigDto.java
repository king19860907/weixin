package com.majun.test.weixin.dto;

import java.io.Serializable;

public class ShareConfigDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2205138764724404823L;

	/**
	 * appid
	 */
	private String appId;
	
	/**
	 * 生成签名时间戳
	 */
	private String timestamp;
	
	/**
	 * 随机字符串
	 */
	private String nonceStr;
	
	/**
	 * 签名
	 */
	private String signature;
	
	private String jsapi_ticket;
	
	/**
	 * 过期时间
	 */
	private Long expires_in;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "ShareConfigDto [appId=" + appId + ", timestamp=" + timestamp + ", nonceStr=" + nonceStr
				+ ", signature=" + signature + ", jsapi_ticket=" + jsapi_ticket + ", expires_in=" + expires_in + "]";
	}
	
}

package com.majun.test.weixin.dto;

import java.io.Serializable;

public class AccessTokenDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7205635918271317696L;
	
	/**
	 * token
	 */
	private String access_token;
	
	/**
	 * 过期时间：秒
	 */
	private Long expires_in;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

}

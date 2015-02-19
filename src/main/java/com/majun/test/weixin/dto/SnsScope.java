package com.majun.test.weixin.dto;

public enum SnsScope {

	SNSAPI_BASE("snsapi_base"),
	SNSAPI_USERINFO("snsapi_userinfo");
	
	private String scope;
	
	SnsScope(String scope){
		this.scope=scope;
	}

	@Override
	public String toString() {
		return this.scope;
	}
	
}

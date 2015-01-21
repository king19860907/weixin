package com.majun.test.weixin.dto;

import java.io.Serializable;

public class JsApiTicketDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2840881156979978593L;

	private String ticket;
	
	private Long expires_in;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}
	
}

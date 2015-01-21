package com.majun.test.weixin.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.majun.test.weixin.dto.AccessTokenDto;
import com.majun.test.weixin.dto.JsApiTicketDto;
import com.majun.test.weixin.dto.ShareConfigDto;
import com.majun.test.weixin.util.HttpConnectionManager;
import com.majun.test.weixin.util.Sha1Util;

@Scope("prototype")
@Service(value = "weixinService")
public class WeixinService {

	private String appId = "wx918ad13c5a103887";
	
	private String appSecret = "de18430bcf58033bf2423026773e85bf";
	
	private String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
	
	private String getJsApiTicketUrl="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

	private HttpConnectionManager http = new HttpConnectionManager();
	
	private String access_token_key = "weixin_access_token";
	
	private String jsapi_ticket_key = "weixin_jspai_ticket_key";
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	public AccessTokenDto getToken() {
		AccessTokenDto token = (AccessTokenDto)redisTemplate.opsForValue().get(access_token_key);
		if(token == null) {
			token = getTokenFormWeixin();
			if(token != null) {
				redisTemplate.opsForValue().set(access_token_key, token);
				redisTemplate.expire(access_token_key, token.getExpires_in(), TimeUnit.SECONDS);
			}
		}
		if(token != null) {
			token.setExpires_in(redisTemplate.getExpire(access_token_key));
		}
		return token;
	}
	
	public JsApiTicketDto getJsapi() {
		JsApiTicketDto jsapi = (JsApiTicketDto)redisTemplate.opsForValue().get(jsapi_ticket_key);
		if(jsapi == null) {
			jsapi = getJsApiTicketFormWeixin();
			if(jsapi != null) {
				redisTemplate.opsForValue().set(jsapi_ticket_key, jsapi);
				redisTemplate.expire(jsapi_ticket_key, jsapi.getExpires_in(), TimeUnit.SECONDS);
			}
		}
		if(jsapi != null) {
			jsapi.setExpires_in(redisTemplate.getExpire(jsapi_ticket_key));
		}
		return jsapi;
	}
	
	public ShareConfigDto getShareConfig(String url) {
		ShareConfigDto config = new ShareConfigDto();
		config.setAppId(this.appId);
		config.setNonceStr(Sha1Util.getNonceStr());
		config.setTimestamp(Sha1Util.getTimeStamp());
		JsApiTicketDto jsapi = getJsapi();
		config.setJsapi_ticket(jsapi.getTicket());
		config.setExpires_in(jsapi.getExpires_in());
		
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("noncestr", config.getNonceStr());
		param.put("jsapi_ticket", config.getJsapi_ticket());
		param.put("timestamp", config.getTimestamp().toString());
		param.put("url", url);
		
		//签名
		String waitSignStr = getContent(param);
		String sign = Sha1Util.getSha1(waitSignStr);
		config.setSignature(sign);
		
		return config;
	}
	
	/**
     * 获取签名字符串
     * @param params
     * @return
     */
	private String getContent(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer prestr = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (value == null || value.trim().length() == 0) {
				continue;
			}
			prestr.append("&");
			prestr.append(key);
			prestr.append("=");
			prestr.append(value);
		}
		
		return prestr.substring(1);
	}
	
	private JsApiTicketDto getJsApiTicketFormWeixin() {
		String access_token = getToken().getAccess_token();
		String url = MessageFormat.format(getJsApiTicketUrl, access_token);
		String result = http.doHttpGet(url).get(0);
		System.out.println(result);
		Gson gson = new Gson();
		return gson.fromJson(result, JsApiTicketDto.class);
	}
	
	private AccessTokenDto getTokenFormWeixin() {
		String url = MessageFormat.format(getTokenUrl, appId,appSecret);
		String result = http.doHttpGet(url).get(0);
		Gson gson = new Gson();
		return gson.fromJson(result, AccessTokenDto.class);
	}
	
}

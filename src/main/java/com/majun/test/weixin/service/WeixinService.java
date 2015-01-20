package com.majun.test.weixin.service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.majun.test.weixin.dto.AccessTokenDto;
import com.majun.test.weixin.util.HttpConnectionManager;

@Scope("prototype")
@Service(value = "weixinService")
public class WeixinService {

	private String appId = "wx918ad13c5a103887";
	
	private String appSecret = "de18430bcf58033bf2423026773e85bf";
	
	private String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
	
	private HttpConnectionManager http = new HttpConnectionManager();
	
	private String access_token_key = "weixin_access_token";
	
	private String getJsApiTicketUrl="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}ACCESS_TOKEN&type=jsapi";
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, AccessTokenDto> redisTemplate;
	
	public AccessTokenDto getToken() {
		AccessTokenDto token = redisTemplate.opsForValue().get(access_token_key);
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
	
	
	
	private AccessTokenDto getTokenFormWeixin() {
		String url = MessageFormat.format(getTokenUrl, appId,appSecret);
		System.out.println(url);
		String result = http.doHttpGet(url).get(0);
		Gson gson = new Gson();
		return gson.fromJson(result, AccessTokenDto.class);
	}
	
}

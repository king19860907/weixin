package com.majun.test.weixin.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.majun.test.weixin.dto.SnsScope;
import com.majun.test.weixin.util.HttpConnectionManager;

@Scope("prototype")
@Service(value = "weixinAuthService")
public class WeixinAuthService {

	private String appId = "wx918ad13c5a103887";
	
	private String appSecret = "de18430bcf58033bf2423026773e85bf";
	
	private String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state={3}#wechat_redirect";
	
	private String acceptCodeUrl = "http://m.yesmywine.com/weixin/accept.do";
	
	private String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
	
	private String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN";
	
	private HttpConnectionManager http = new HttpConnectionManager();
	
	public String getAuthUrl(String redirectUri,String scope){
		String url = MessageFormat.format(authUrl, appId,acceptCodeUrl,scope,redirectUri);
		return url;
	}
	
	public String getUserInfo(String code,String state){
		String url = MessageFormat.format(accessTokenUrl, appId,appSecret,code);
		String result = http.doHttpGet(url).get(0);
		Gson gson = new Gson();
		Map<String,String> map = gson.fromJson(result, Map.class);
		String scope = map.get("scope");
		if(StringUtils.isEmpty(scope)){
			return state;
		}
		
		Map<String,String> resultMap = new HashMap<String,String>();
		if(scope.equals(SnsScope.SNSAPI_BASE.toString())){
			resultMap.put("openid", map.get("openid"));
			return getReturnUrl(state, resultMap);
		}
		
		if(scope.equals(SnsScope.SNSAPI_USERINFO.toString())){
			String getUserInfoUrl = MessageFormat.format(userInfoUrl,map.get("access_token"), map.get("openid"));
			String userInfoResult = http.doHttpGet(getUserInfoUrl).get(0);
			return getReturnUrl(state, gson.fromJson(userInfoResult, Map.class));
		}
		
		return state;
	}
	
	private String getReturnUrl(String redirectUri,Map<String,String> params){
		if(redirectUri.contains("?")){
			redirectUri=redirectUri+"&";
		}else{
			redirectUri=redirectUri+"?";
		}
		Gson gson = new Gson();
		try {
			redirectUri = redirectUri+"data="+URLEncoder.encode(gson.toJson(params),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return redirectUri;
	}
	
}

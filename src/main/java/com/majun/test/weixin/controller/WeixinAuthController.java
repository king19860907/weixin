package com.majun.test.weixin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.majun.test.weixin.service.WeixinAuthService;

@RequestMapping("/weixin" )
@Controller
public class WeixinAuthController {

	@Resource(name = "weixinAuthService")
	private WeixinAuthService weixinAuthService;
	
	@RequestMapping(method=RequestMethod.GET,value="/auth")
	public String auth(String redirectUri,String scope){
		String url = weixinAuthService.getAuthUrl(redirectUri, scope);
		System.out.println(url);
		return "redirect:"+url;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/accept")
	public String accept(String code,String state){
		System.out.println(code);
		System.out.println(state);
		String redirectUri = weixinAuthService.getUserInfo(code,state);
		return "redirect:"+redirectUri;
	}
	
}

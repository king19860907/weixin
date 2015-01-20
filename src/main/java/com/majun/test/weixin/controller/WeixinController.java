package com.majun.test.weixin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.majun.test.weixin.dto.AccessTokenDto;
import com.majun.test.weixin.service.WeixinService;


@RequestMapping("/weixin" )
@Controller
public class WeixinController {
	
	@Resource(name = "weixinService")
	private WeixinService weixinService;
	
	@RequestMapping(method=RequestMethod.GET,value="/getToken")
	@ResponseBody
	public String getToken() {
		AccessTokenDto dto = weixinService.getToken();
		Gson gson = new Gson();
		return gson.toJson(dto);
	}
	
}

package com.majun.test.weixin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.majun.test.weixin.dto.AccessTokenDto;
import com.majun.test.weixin.dto.JsApiTicketDto;
import com.majun.test.weixin.dto.ShareConfigDto;
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
	
	@RequestMapping(method=RequestMethod.GET,value="/getJsApi")
	@ResponseBody
	public String getJsApi() {
		JsApiTicketDto dto = weixinService.getJsapi();
		Gson gson = new Gson();
		return gson.toJson(dto);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getShareConfig")
	@ResponseBody
	public String getShareConfig(String url) {
		if(StringUtils.isEmpty(url)) {
			return "缺少url参数";
		}
		ShareConfigDto dto = weixinService.getShareConfig(url);
		Gson gson = new Gson();
		return gson.toJson(dto);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/share")
	public String share(Model model,HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		if(!StringUtils.isEmpty(queryString)) {
			url=url+"?"+queryString;
		}
		System.out.println(url);
		ShareConfigDto config = weixinService.getShareConfig(url);
		model.addAttribute("config", config);
		return "share";
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/downloadMedia")
	@ResponseBody
	public String downloadMedia(@RequestParam("serverIds[]")String[] serverIds,HttpServletRequest request){
		System.out.println("serverIds:"+serverIds);
		String basePath=request.getServletContext().getRealPath("/")+"";
		List<String> results = converToUrls(weixinService.downloadMedia(Arrays.asList(serverIds),basePath), request.getServerName());
		Gson gson = new Gson();
		System.out.println(results);
		return gson.toJson(results);
	}
	
	private List<String> converToUrls(List<String> names,String serverName){
		List<String> result = new ArrayList<String>();
		if(!CollectionUtils.isEmpty(names)){
			for(String name : names){
				result.add("http://"+serverName+"/"+name);
			}
		}
		return result;
	}
	
	 /**
     * 获取参数
     * @param request
     * @return
     */
    protected Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
    
    protected String paramsToString(HttpServletRequest request) {
    	StringBuffer sb = new StringBuffer("");
    	Enumeration<String> names = request.getParameterNames();
    	while (names.hasMoreElements()) {
			String name = names.nextElement();
			String[] values = (String[])request.getParameterValues(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
			sb.append(name).append("=").append(valueStr).append("&");
		}
    	String str = sb.toString();
    	if(!StringUtils.isEmpty(str)) {
    		str="?"+str.substring(0,str.length()-1);
    	}
    	return str;
    }
}

package com.majun.test.weixin.dto;

import java.util.Locale;

public enum PartnersSourceEnum
{
    /**
     * 支付宝
     */
    PARTNERS_SOURCE_ALIPAY("alipay","支付宝"),  
    /**
     * 新浪微博
     */
    PARTNERS_SOURCE_SINA("sina","新浪"),
    
    /**
     * 关爱通
     */
    PARTNERS_SOURCE_GUANAITONG("guanaitong","关爱通"),
    
    /**
     * 中国奖励
     */
    PARTNERS_SOURCE_CHINAREWARDS("chinarewards","中国奖励"),

    /**
     * 安悦
     */
    PARTNERS_SOURCE_ANYO("anyo","安悦"),

    /**
     * QQ
     */
    PARTNERS_SOURCE_QQ("qq","QQ"),

    /**
     * QQ彩贝
     */
    PARTNERS_SOURCE_QQ_CB("qqcb","QQ彩贝"),
    
    /**
     * 51比购网
     * @deprecated 仅用于展示历史数据， 不再建立新数据
     */
    @Deprecated
    PARTNERS_SOURCE_51BI("51bi","51比购"),
    /**
     * 交通银行
     */
    PARTNERS_SOURCE_BC("ctbclub","交通银行"),
    
    /**
     * 微信
     */
    PARTNERS_SOURCE_WEIXIN("weixin","微信"),
    
    /**
     * 上海导购
     */
    PARTNERS_SOURCE_SHANGHAI("shunion","上海导购"),
    
    /**
     * 手机非会员
     */
    PARTNERS_SOURCE_MOBLIE("moblie","手机非会员"),
    /**
     * 招商银行wap
     */
    PARTNERS_SOURCE_CMBSTORE("cmbstore","招商银行商城WAP");

    
    ;
    
    /**
     * 各合作方代码
     */
    private String partnersCode;
    /**合作方的名称*/
    private String partnerName;
    
    
    
    private PartnersSourceEnum(String partnersFlag,String partnerName)
    {
        this.partnersCode = partnersFlag;
        this.partnerName=partnerName;
    }

    public String getAbbreviation()
    {
        return this.partnersCode;
    }

    public String getName()
    {
        return this.toString();
    }

    public String getPartnersCode()
    {
        return partnersCode;
    }

    public String getPartnerName()
    {
        return partnerName;
    } 

    public static PartnersSourceEnum fromCode(String code){
    	for (PartnersSourceEnum bean : PartnersSourceEnum.values()){
    		if (bean.partnersCode.equals(code)){
    			return bean;
    		}
    	}
    	return null;
    }
}
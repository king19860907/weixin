package com.majun.test.weixin.dto;

import java.io.Serializable;
import java.util.Date;

public class PartnersUserDTO implements Serializable
{

    private static final long serialVersionUID = -2802264522616990870L;
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 合作方的用户登录名
     */
    private String loginName;
    
    /**
     * 合作方的用户id
     */
    private String sourceUserId;
    
    /**
     * 对应我们站内用户的id
     */
    private Long memberId;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 创建时的用户IP地址
     */
    private String createIP;
    
    /**
     * 合作方来源，表明从哪个合作方过来的
     */
    private PartnersSourceEnum partnersSource;
    
    /**
     * 是否删除
     */
    private Boolean cancelFlag;
    
    /**
     * 用户email
     */
    private String email;
    
    /**
	 *  qq、qq彩贝联合登录时，返回的openId qq区分唯一标识
	 */
	private String openId;
	
	/**
	 * qq登录时返回的token
	 */
	private String accessToken;
	
	/**
	 * qq彩贝登录返回的acct qq区分唯一标识
	 */
	private String acct;
	
	/**
	 * qq彩贝登录返回的统计字段，用于push订单
	 */
	private String attach;

	/**
	 * 会员昵称
	 */
	private String nickName;
	
	/**
	 * 可用彩贝积分
	 */
	private int point;
	
	/**
	 * 购买后返利比率
	 */
	private String bonus;
	
	private String regSourceDetail;
	
    public PartnersUserDTO()
    {
        
    }

    public boolean isNewOne()
    {
        return null == this.id;
    }
    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getSourceUserId()
    {
        return sourceUserId;
    }

    public void setSourceId(String sourceUserId)
    {
        this.sourceUserId = sourceUserId;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getCreateIP()
    {
        return createIP;
    }

    public void setCreateIP(String createIP)
    {
        this.createIP = createIP;
    }

    public PartnersSourceEnum getPartnersSource()
    {
        return partnersSource;
    }

    public void setPartnersSource(PartnersSourceEnum partnersSource)
    {
        this.partnersSource = partnersSource;
    }
    
    public Boolean getCancelFlag()
    {
        return cancelFlag;
    }

    public void setCancelFlag(Boolean cancelFlag)
    {
        this.cancelFlag = cancelFlag;
    }
    
    public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegSourceDetail() {
		return regSourceDetail;
	}

	public void setRegSourceDetail(String regSourceDetail) {
		this.regSourceDetail = regSourceDetail;
	}
    
}

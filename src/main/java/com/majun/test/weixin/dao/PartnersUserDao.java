package com.majun.test.weixin.dao;

import java.util.Date;

import com.majun.test.weixin.dto.PartnersSourceEnum;
import com.majun.test.weixin.dto.PartnersUserDTO;

public interface PartnersUserDao{

    /**
     * 新增合作方用户。<br/>
     * @param partnersUser
     * @return
     */
    PartnersUserDTO addPartnersUser(PartnersUserDTO partnersUser);
    
    /**
     * 通过合作方用户登录名和合作方来源取得合作方用户。<br/>
     * @param loginName 合作方用户登录名
     * @param partnersSource 合作方来源
     * @return
     */
    PartnersUserDTO getPartnersUserByLoginNameAndSource(String loginName, PartnersSourceEnum partnersSource);
    
    /**
     * 通过合作方用户id和合作方来源取得合作方用户。<br/>
     * @param sourceUserId 合作方用户id
     * @param partnersSource 合作方来源
     * @return
     */
    PartnersUserDTO getPartnersUserBySourceIdAndSource(String sourceUserId, PartnersSourceEnum partnersSource);
    
    PartnersUserDTO getPartnersUserByMemberIdAndSource(PartnersSourceEnum partnersSource,Long memberId);
    
    /**
     * 通过id取得合作方用户。<br/>
     * @param id 合作方用户id
     * @return
     */
    PartnersUserDTO getPartnersUserById(Long id);
    
    /**
     * 通过网站会员id取得合作方用户。<br/>
     * @param id 网站会员id
     * @return
     */
    PartnersUserDTO getPartnersUserByMemberId(Long id);
    /**
     * 当支付宝用户登录后，要在我们自己的系统中生成一个用户，本方法生成新生成的用户的登录名。
     * @return
     */
    String getAlipayPartnerMemberLogName();
    
    /**
     * 更新合作方记录 <br />
     *
     * @author: xiangping_yu
     * @date: 2012-6-5
     * @since: 1.0.0
     */
	void updatePartnersUser(PartnersUserDTO partenerUser);
	
	void addUserBindLog(Long memberId,String bindType,Date createDate,String dsc,String sourceUserId);
}
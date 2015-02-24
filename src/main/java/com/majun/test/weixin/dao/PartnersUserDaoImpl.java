package com.majun.test.weixin.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.majun.test.weixin.dto.PartnersSourceEnum;
import com.majun.test.weixin.dto.PartnersUserDTO;

public class PartnersUserDaoImpl extends SqlMapClientDaoSupport implements
		PartnersUserDao {

	@Override
	public PartnersUserDTO addPartnersUser(PartnersUserDTO partnersUser) {
		// 根据登录用户判断表中是否有相同合作方信息
		String loginName = partnersUser.getLoginName();
		PartnersSourceEnum partnersSource = partnersUser.getPartnersSource();
		PartnersUserDTO pdt = null;
		if (loginName != null && partnersSource != null) {
			pdt = this.getPartnersUserByLoginNameAndSource(loginName,
					partnersSource);
		}
		String sqlId = "addPartnersUser";
		if (pdt == null) {
			sqlId = "addPartnersUser";
			getSqlMapClientTemplate().insert(sqlId, partnersUser);
		}
		return partnersUser;
	}

	@Override
	public PartnersUserDTO getPartnersUserByLoginNameAndSource(
			String loginName, PartnersSourceEnum partnersSource) {
		String sqlId = "getPartnersUserByLoginNameAndSource";
		Map sqlParameters = new HashMap();

		sqlParameters.put("loginName", loginName);
		sqlParameters.put("partnersSource", partnersSource.name());

		return (PartnersUserDTO) getSqlMapClientTemplate().queryForObject(sqlId,
				sqlParameters);
	}

	@Override
	public PartnersUserDTO getPartnersUserBySourceIdAndSource(
			String sourceUserId, PartnersSourceEnum partnersSource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartnersUserDTO getPartnersUserByMemberIdAndSource(
			PartnersSourceEnum partnersSource, Long memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartnersUserDTO getPartnersUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartnersUserDTO getPartnersUserByMemberId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlipayPartnerMemberLogName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePartnersUser(PartnersUserDTO partenerUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUserBindLog(Long memberId, String bindType, Date createDate,
			String dsc, String sourceUserId) {
		// TODO Auto-generated method stub

	}

}

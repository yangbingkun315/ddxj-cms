package net.zn.ddxj.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.InviteSetting;
import net.zn.ddxj.mapper.InviteSettingMapper;
import net.zn.ddxj.service.InviteSettingService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.vo.CmsRequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class InviteSettingServiceImpl implements InviteSettingService {

	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	public InviteSettingMapper inviteSettingMapper;
	
	@Override
	public InviteSetting getInviteInfo(String key) {
		
		Integer type;
		if(key.equals(Constants.INVITE_WECHAT_KEY)){
			type = 1;
		}else{
			type = 2;
		}
		/*InviteSetting obj = (InviteSetting) redisUtils.get(key);
		if(CmsUtils.isNullOrEmpty(obj)){
			InviteSetting setting = inviteSettingMapper.queryByFlag(type,1);
			if(!CmsUtils.isNullOrEmpty(setting)){
				redisUtils.set(key, setting);
				return setting;
			}
		}
		return obj;*/
		InviteSetting setting = inviteSettingMapper.queryByFlag(type,1);
		return setting;
	}

	@Override
	public void saveSetting(InviteSetting setting) {
		InviteSetting setInfo = inviteSettingMapper.queryByFlag(setting.getType(),1);
		if(!CmsUtils.isNullOrEmpty(setInfo)){
			setInfo.setFlag(2);
			setInfo.setUpdateTime(new Date());
			//更新为无效记录
			inviteSettingMapper.updateByPrimaryKeySelective(setInfo);
		}
		int count;
		if(!CmsUtils.isNullOrEmpty(setting.getId())){
			count = inviteSettingMapper.updateByPrimaryKeySelective(setting);
		}else{
			count = inviteSettingMapper.insert(setting);
		}
		if(1==count){
			setting = inviteSettingMapper.queryByFlag(setting.getType(),1);
			String key = "";
			if(1==setting.getType()){
	    		key = Constants.INVITE_WECHAT_KEY;
	    	}else{
	    		key = Constants.INVITE_FRIEND_KEY;
	    	}
			redisUtils.set(key, setting);
		}
		
	}

	@Override
	public List<InviteSetting> queryInviteSetting(String title,String channel,String startTime,String endTime) {
		List<InviteSetting> list = inviteSettingMapper.queryInviteSetting(title,channel,startTime,endTime);
		return list;
	}

	@Override
	public InviteSetting findById(Integer id) {
		return inviteSettingMapper.selectByPrimaryKey(id);
	}

	@Override
	public void deleteInvite(Integer id) {
		inviteSettingMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<InviteSetting> queryInviteSettingList(CmsRequestVo requestVo) {
		return inviteSettingMapper.queryInviteSettingList(requestVo);
	}

	@Override
	public int updateByPrimaryKeySelective(InviteSetting record) {
		InviteSetting inviteSetting = inviteSettingMapper.queryByFlag(record.getType(), 1);
		if(!CmsUtils.isNullOrEmpty(inviteSetting))
		{
			inviteSetting.setFlag(2);
			inviteSetting.setUpdateTime(new Date());
			inviteSettingMapper.updateByPrimaryKeySelective(inviteSetting);
		}
		return inviteSettingMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int insertSelective(InviteSetting record) {
		InviteSetting inviteSetting = inviteSettingMapper.queryByFlag(record.getType(), 1);
		if(!CmsUtils.isNullOrEmpty(inviteSetting))
		{
			inviteSetting.setFlag(2);
			inviteSetting.setUpdateTime(new Date());
			inviteSettingMapper.updateByPrimaryKeySelective(inviteSetting);
		}
		return inviteSettingMapper.insertSelective(record);
	}

}

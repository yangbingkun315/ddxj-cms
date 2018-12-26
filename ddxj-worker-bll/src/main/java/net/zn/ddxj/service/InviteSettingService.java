package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.InviteSetting;
import net.zn.ddxj.vo.CmsRequestVo;

public interface InviteSettingService {
	
	InviteSetting getInviteInfo(String key);

    void saveSetting(InviteSetting setting);
    
	InviteSetting findById(Integer id);
	
	void deleteInvite(Integer id);
    
    List<InviteSetting> queryInviteSetting(String title,String channel,String startTime,String endTime);
    
    /**
     * cms后台查询分销设置
     * @param requestVo
     * @return
     */
    List<InviteSetting> queryInviteSettingList(CmsRequestVo requestVo);
    
    int updateByPrimaryKeySelective(InviteSetting record);
    
    int insertSelective(InviteSetting record);
}

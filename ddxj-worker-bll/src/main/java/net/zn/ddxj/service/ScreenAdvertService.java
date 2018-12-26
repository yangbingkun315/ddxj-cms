package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.ScreenAdvert;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface ScreenAdvertService {
    int deleteByPrimaryKey(Integer id);

    int insert(ScreenAdvert record);

    int insertSelective(ScreenAdvert record);

    ScreenAdvert selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScreenAdvert record);

    int updateByPrimaryKey(ScreenAdvert record);

	List<ScreenAdvert> queryScreenAdvertList(CmsRequestVo requestVo);

	int updateScreenAdvertFlag(int id);

	ScreenAdvert queryScreenAdvert(String pushPlatform);

	List<ScreenAdvert> queryEndTime(String pushPlatform);

	List<ScreenAdvert> queryAndroidAndIOS(String pushPlatform);
	
	List<ScreenAdvert> validateTimeIsNotScreeAdvert(String currentTime);

	ScreenAdvert queryScreenAdvertById(int id);//查询广告


}

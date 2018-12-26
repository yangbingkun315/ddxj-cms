package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.ScreenAdvert;
import net.zn.ddxj.vo.CmsRequestVo;

public interface ScreenAdvertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScreenAdvert record);

    int insertSelective(ScreenAdvert record);

    ScreenAdvert selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScreenAdvert record);

    int updateByPrimaryKey(ScreenAdvert record);

	List<ScreenAdvert> queryScreenAdvertList(CmsRequestVo requestVo);

	int updateScreenAdvertFlag(int id);

	ScreenAdvert queryScreenAdvert(String pushPlatform);
	
	List<ScreenAdvert> queryScreenAdvertEndTime(String pushPlatform);

	List<ScreenAdvert> queryAndroidAndIOS(String pushPlatform);	
	
	List<ScreenAdvert> validateTimeIsNotScreeAdvert(@Param("currentTime")String currentTime);
}
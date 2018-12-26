package net.zn.ddxj.mapper;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.InformationRecordIp;

public interface InformationRecordIpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InformationRecordIp record);

    int insertSelective(InformationRecordIp record);

    InformationRecordIp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InformationRecordIp record);

    int updateByPrimaryKey(InformationRecordIp record);
    
    InformationRecordIp queryInforRecordIpByIP(@Param("ip")String ip,@Param("infoId")int infoId);//根据IP查询是否查看过
    
    Integer getReadPeopleCount(Integer infoId);//查询阅读数量
}
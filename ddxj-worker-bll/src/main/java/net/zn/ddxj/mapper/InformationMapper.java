package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.Information;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface InformationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Information record);

    int insertSelective(Information record);

    Information selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Information record);

    int updateByPrimaryKey(Information record);
    
    List<Information> selectInformation(RequestVo requestVo);
    
    List<Information> queryInformationList(CmsRequestVo requestVo);//根据查询条件资讯列表
    
    int deleteInformation(Integer inforId);//删除资讯
    
    Boolean queryInformationLower(Integer inforId);//查询资讯是否下架

	int changeInfoStick(@Param("id")Integer id,@Param("type")Integer type );//咨询置顶
}
package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.InformationCategory;
import net.zn.ddxj.entity.InformationRecordIp;
import net.zn.ddxj.entity.InformationType;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface InformationService {
    int deleteByPrimaryKey(Integer id);

    int insert(Information record);

    int insertSelective(Information record);

    Information selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Information record);

    int updateByPrimaryKey(Information record);
    
    List<Information> selectInformation(RequestVo requestVo);
    
    List<Information> queryInformationList(CmsRequestVo requestVo);//根据查询条件资讯列表
    
    int deleteInformation(Integer inforId);//删除资讯
    
    List<InformationType> queryInformationTypeList(RequestVo requestVo);//根据查询条件资讯分类列表
    
    int deleteInformationCategory(Integer categoryId);//删除资讯分类
    
    InformationType InformationCategoryDetail(Integer categoryId);//资讯分类详情
    
    int insertInformationCategory(InformationType type);//添加资讯分类
    
    int updateInformationCategory(InformationType type);//修改资讯分类
    
    int addInformationCategory(InformationCategory category);//添加资讯分类
    
    int deleteInformationCategoryByInfoId(Integer inforId);//根据资讯ID删除分类
    
    InformationRecordIp queryInforRecordIpByIP(String ip,int infoId);//根据IP查询是否查看过
    
    int addInformationRecordIp(InformationRecordIp ip);//添加阅读记录

	int changeInfoStick(Integer id, Integer type);

	List<InformationType> queryInfoTypeList(CmsRequestVo requestVo);//查询咨询分类

	InformationType selectInfoTypeByPrimaryKey(Integer typeId);//根据资讯分类编号查询分类信息
}


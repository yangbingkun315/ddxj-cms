package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.Credit;
import net.zn.ddxj.vo.CmsRequestVo;

public interface CreditMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Credit record);

    int insertSelective(Credit record);

    Credit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Credit record);

    int updateByPrimaryKey(Credit record);
    
    Credit getCreditByCreditId(Integer creditId);// 根据授信机构的id查询授信机构
    
    List<Credit> queryCreditMCN();//查询授信机构

	List<Credit> findCreditList(CmsRequestVo requestVo);//查询授信机构
	
	int deleteByCreditId(Integer id);//删除授信机构
}
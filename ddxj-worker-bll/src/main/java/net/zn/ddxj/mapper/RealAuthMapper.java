package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.RealAuth;
import net.zn.ddxj.vo.CmsRequestVo;

import org.apache.ibatis.annotations.Param;

public interface RealAuthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RealAuth record);

    int insertSelective(RealAuth record);

    RealAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RealAuth record);

    int updateByPrimaryKey(RealAuth record);
    
    int selectAuthByUserId(Integer id);
    
    RealAuth queryUserRealAuth(Integer userId);
    
    List<RealAuth> queryRealAuthList(CmsRequestVo requestVo);//查询实名认证列表
    
    int updateRealAuthStatus(@Param("realId")int realId,@Param("status")int status);//更新实名认证状态
    
    int deleteRealAuth(int realId);//删除实名认证信息
    
    RealAuth queryUserRealAuthByIdCard(String idCardNumber);//根据身份证号码查询认证信息
    
    int queryUserRealAuthCount(String idCardNumber);//查询实名认证数量

	int delUserAuthRecord(Integer userId);
	
	int findRealAuthCount();
}
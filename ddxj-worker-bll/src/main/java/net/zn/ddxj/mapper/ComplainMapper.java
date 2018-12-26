package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.Complain;
import net.zn.ddxj.vo.CmsRequestVo;

public interface ComplainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Complain record);

    int insertSelective(Complain record);

    Complain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Complain record);

    int updateByPrimaryKey(Complain record);
    
    List<Complain> queryComplainList(CmsRequestVo requestVo);//查询投诉与建议列表
    
    int deleteComPlain(int id);//删除投诉与建议

	int delComplainRecord(Integer userId);
}
package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.ProblemLib;
import net.zn.ddxj.vo.RequestVo;

public interface ProblemLibMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProblemLib record);

    int insertSelective(ProblemLib record);

    ProblemLib selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProblemLib record);

    int updateByPrimaryKey(ProblemLib record);
    
    List<ProblemLib> queryProblemLibList( @Param("keywords")String keywords);//关键字查询
    
    ProblemLib queryProblemLibDetails(Integer keywordsId);
    
    List<ProblemLib> findProblemLibList(RequestVo requestVo);//多条件查询问题库
    
    int deleteProblemLib(Integer id);//删除问题
}
package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.CommentLabel;

public interface CommentLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommentLabel record);

    int insertSelective(CommentLabel record);

    CommentLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentLabel record);

    int updateByPrimaryKey(CommentLabel record);
    
    List<CommentLabel> getLabelListByUserId(Integer id);
    
    List<CommentLabel> queryCommentLabel(Integer workerType);//用户类型
}
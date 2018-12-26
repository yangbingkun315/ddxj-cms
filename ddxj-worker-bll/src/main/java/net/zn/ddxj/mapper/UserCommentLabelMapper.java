package net.zn.ddxj.mapper;

import java.util.Map;

import net.zn.ddxj.entity.UserCommentLabel;

public interface UserCommentLabelMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(UserCommentLabel record);

    int insertSelective(UserCommentLabel record);

    UserCommentLabel selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(UserCommentLabel record);

    int updateByPrimaryKey(UserCommentLabel record);
    
    int insertUserCommentLabel(Map<String, Object> value);
    
    int delUserCommentLabel(Integer userId);
}
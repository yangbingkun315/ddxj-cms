package net.zn.ddxj.mapper;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.UserRequest;

public interface UserRequestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRequest record);

    int insertSelective(UserRequest record);

    UserRequest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRequest record);

    int updateByPrimaryKey(UserRequest record);
    
    Integer queryUserIsRequest(@Param("fromUserId")Integer fromUserId,@Param("toUserId")Integer toUserId);
    
    Integer queryUserRequestById(@Param("fromUserId")int fromUserId,@Param("toUserId")int toUserId,@Param("recruitId")int recruitId);//查询工人是否被邀请过

	int deleteUserRequest(Integer userId);

	 int delUserRequestRecord(Integer userId);
}
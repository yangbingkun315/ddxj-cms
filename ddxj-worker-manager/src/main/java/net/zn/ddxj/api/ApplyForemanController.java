package net.zn.ddxj.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.ApplyForeman;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.service.CircleService;
import net.zn.ddxj.service.RecruitRecordService;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.service.UserCenterService;
import net.zn.ddxj.service.UserCollectionService;
import net.zn.ddxj.service.UserCommentService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.vo.CmsRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@Slf4j
public class ApplyForemanController {
	@Autowired
	private UserService userService;
	@Autowired
	private RecruitRecordService recruitRecordService;
	@Autowired
	private SalaryRecordService salaryRecordService;
	@Autowired
	private UserCollectionService userCollectionService;
	@Autowired
	private UserCommentService userCommentService;
	@Autowired
	private UserTransferService userTransferService;
	@Autowired
	private CircleService circleService;
	@Autowired
	private UserCenterService userCenterService;
	
	
	
	/**
	 * 查询申请工头列表
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/query/foreman/list.ddxj")
	public ResponseBase queryApplyForemanList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<ApplyForeman> foremanList = userService.queryApplyForeman(requestVo);
		PageInfo<ApplyForeman> pageInfo = new PageInfo<ApplyForeman>(foremanList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("foremanUserList", pageInfo);
		result.setResponseMsg("查询成功");
		return result;
		
	}
	/**
	 * 更新申请状态
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/update/foreman/info.ddxj")
	public ResponseBase updateApplyForemanStatus(HttpServletRequest request, HttpServletResponse response,Integer foremanId,Integer validateStatus,String validateCause){
		ResponseBase result=ResponseBase.getInitResponse();	
		ApplyForeman applyForemanInfo=userService.selectByForemanId(foremanId);
		Integer userId=applyForemanInfo.getUserId();
		if(validateStatus==2)//审核失败
		{
			applyForemanInfo.setValidateCause(validateCause);
			userService.updateApplyForemanStatus(foremanId,validateStatus,validateCause);
		}
		else if(validateStatus==3) //审核成功
		{
			String cause="";
			//审核成功
			userService.updateApplyForemanStatus(foremanId,validateStatus,cause);
			//更新用户角色
			userService.updateUserRole(userId);
			//更新用户的承包方式
			userService.updateUservalidateMessage(userId);
			//删除用户的工种
			userService.deleteUserCategory(userId);
			//删除用户报名表
			recruitRecordService.updateUserSignStatus(userId);
			//授信收工资记录 
			salaryRecordService.deleteSalaryRecord(userId);
			//删除用户收藏
			userCollectionService.deleteUserCollection(userId);
			//删除职位邀请表
			userService.deleteUserRequest(userId);
			//删除用户评论标签
			userCommentService.delUserCommentLabel(userId);
			//删除评论			
			userCommentService.deleteUserComment(userId);
			//删除转账记录表
			userTransferService.deleteUserTransfer(userId);
			//删除圈子点赞
			circleService.deleteCricleLaud(userId);
			//删除圈子评论
			circleService.deleteCricleComment(userId);
			//删除圈子图片
			circleService.deleteCricleImage(userId);
			//删除圈子
			circleService.deleteCricle(userId);
			//删除消息中心所有记录
			userCenterService.deleteMessageRecord(userId);
			
		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
		
	}
	@RequestMapping(value="/manager/delete/foreman/info.ddxj")
	public ResponseBase deleteApplyForemanRecord(HttpServletRequest request, HttpServletResponse response,Integer applyId){
		ResponseBase result=ResponseBase.getInitResponse();
		userService.deleteForemanByApplyId(applyId);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
	}
	

}

package net.zn.ddxj.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.CommentLabel;
import net.zn.ddxj.entity.UserComment;
import net.zn.ddxj.service.UserCommentService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.json.JsonUtil;

@RestController
@Slf4j
/**
 * 评论，合并
* @ClassName: CommentController   
* @author 上海众宁网络科技有限公司-何俊辉
* @date 2018年4月18日  
*
 */
public class CommentController {
	@Autowired
	private UserCommentService  userCommentService;
	
	
	@RequestMapping(value = "/member/query/user/comment.ddxj")
	public ResponseBase queryIdenCode(HttpServletRequest request, HttpServletResponse response, Integer userId,Integer pageNum,Integer pageSize) throws IllegalAccessException {
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum,pageSize);  
		List<UserComment>userComments=userCommentService.queryUserComment(userId);
		int total=userCommentService.countComment(userId);
		String scores=userCommentService.countCommentScores(userId);
		if(!CmsUtils.isNullOrEmpty(userComments)){
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
			result.push("commentList", JsonUtil.list2jsonToArray(userComments));
			result.push("totalCount", total);
			
			if(CmsUtils.isNullOrEmpty(scores))
			{
				scores = "5.0";
			}
			
			result.push("score", scores);
		}else{
			result.push("commentList", JsonUtil.list2jsonToArray(userComments));
			result.push("totalCount", Constants.Number.ZERO_INT);
			result.push("score", "5.0");
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		}
		return result;
		
	}
	/**
	 * 根据职位类型查询评论标签
	 * @param request
	 * @param response
	 * @param workerType
	 * @return
	 * @throws Exception
	 * @author ddxj
	 */
	@RequestMapping("/query/comment/label.ddxj")
	public ResponseBase queryCommentLabel(HttpServletRequest request, HttpServletResponse response,Integer workerType) throws Exception{
		ResponseBase result=ResponseBase.getInitResponse();
		List<CommentLabel> commentLabelList = userCommentService.queryCommentLabel(workerType);
		if(!CmsUtils.isNullOrEmpty(commentLabelList)){
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
			result.push("commentLabelList", JsonUtil.list2jsonToArray(commentLabelList));
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		}
		return result;
		
	}


}

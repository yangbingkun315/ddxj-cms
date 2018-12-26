package net.zn.ddxj.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.BaseController;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.ProblemLib;
import net.zn.ddxj.service.ProblemLibService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.json.JsonUtil;

@RestController
@Slf4j

public class PorblemLibController extends BaseController{
	@Autowired
	private ProblemLibService problemLibService;
	/**
	 * 根据关键字查询问题列表
	 * @param request
	 * @param response
	 * @param keywords
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 * @author ddxj
	 */
	@RequestMapping(value="/query/problem/lib.ddxj")
	public ResponseBase queryProblemLibList(HttpServletRequest request, HttpServletResponse response,String keywords,Integer pageNum,Integer pageSize) throws Exception{
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<ProblemLib> problemLibList = problemLibService.queryProblemLibList(keywords);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("problemLibList", JsonUtil.list2jsonToArray(problemLibList));
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		return result;
		
	}
	/**
	 * 根据关键字查询问题详情
	 * @param request
	 * @param response
	 * @param keywords
	 * @return
	 * @author ddxj
	 */
	@RequestMapping(value="/query/problem/lib/details.ddxj")
	public ResponseBase queryProblemLibDetails(HttpServletRequest request, HttpServletResponse response,Integer keywordsId){
		ResponseBase result=ResponseBase.getInitResponse();
		ProblemLib problemLib = problemLibService.queryProblemLibDetails(keywordsId);
		if(!CmsUtils.isNullOrEmpty(problemLib)){
			result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
			result.push("problemLib", JsonUtil.bean2jsonObject(problemLib));
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_FAILURE);
		}
		return result;
		
	}

}

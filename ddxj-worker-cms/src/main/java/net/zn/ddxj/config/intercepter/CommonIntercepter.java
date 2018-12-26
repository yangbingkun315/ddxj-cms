package net.zn.ddxj.config.intercepter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.utils.json.JsonUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * spring的handler mapping机制提供了handler interceptors，可以用来为特定的请求添加特定的功能，如，检测资金额等。
 * handler mapping的拦截器必须实现HandlerInterceptor的接口。该接口定义了三个方法：
 * preHandler() - 在实际的handler被执行前被调用
 * postHandler() - 在handler被执行后被调用
 * afterCompletion() - 当request处理完成后被调用
* @ClassName: CommonIntercepter 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 何俊辉
* @date 2017-7-3 下午9:00:23 
*
 */
@Component
public class CommonIntercepter  implements HandlerInterceptor  {

	/**
	 * 在实际的handler被执行前被调用
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
			 StringBuffer requestUrl = request.getRequestURL();
			 if(requestUrl.indexOf("/cms/login.htm") == -1 
			 && requestUrl.indexOf("/cms/user/logout.htm") == -1 
			 && requestUrl.indexOf("/cms/404.htm") == -1 
			 && requestUrl.indexOf("/cms/403.htm") == -1 
			 && requestUrl.indexOf("/cms/500.htm") == -1 
			 && requestUrl.indexOf("/cms/register.htm") == -1 
			 && requestUrl.indexOf("/error") == -1)
			 {
				 
				 if(!SecurityUtils.getSubject().isAuthenticated())
				 {
					 if(Constants.GET.equals(request.getMethod()))
					 { 
						 response.sendRedirect(request.getContextPath() + "/cms/login.htm");
						 return false;
					 }
					 else if(Constants.POST.equals(request.getMethod()))
					 {
						 response.setCharacterEncoding("UTF-8");  
						 response.setContentType("application/json; charset=utf-8");
						 PrintWriter out = null ;
						 try{
							 ResponseBase result = ResponseBase.getInitResponse();
							 result.setResponse(Constants.FALSE);
							 result.setResponseCode(1036);//登陆已失效
							 result.setResponseMsg("登陆已失效，请重新登陆");
						     out = response.getWriter();
						     out.append(JsonUtil.bean2jsonToString(result));
						     return false;
						 }
						 catch (Exception e){
						     e.printStackTrace();
						     response.sendError(500);
						     return false;
						 }

					 }
					
				 }
			 }
			 else
			 {
				 if(requestUrl.indexOf("/cms/login.htm") > -1 )
				 {
					 if(SecurityUtils.getSubject().isAuthenticated())
					 {
						 response.sendRedirect(request.getContextPath() + "/cms/index.htm");
						 return false;
					 }
				 }
			 }
		
		return true;
	}
	/**
	 *  在handler被执行后被调用ogi
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
			request.setAttribute("base", request.getContextPath());
	}
	/**
	 * 当request处理完成后被调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}


}

package net.zn.ddxj.config;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(value=IndexOutOfBoundsException.class)  
    public ResponseBase allIndexOutOfBoundsExceptionHandler(HttpServletRequest request,  
            Exception exception) throws Exception  
    {  
    	ResponseBase response = ResponseBase.getInitResponse();
    	exception.printStackTrace();
        response.setResponse(Constants.FALSE);
        response.setResponseCode(Constants.ERROR_500);
        response.setResponseMsg("系统异常，数组越界异常");
        return response;  
    }
	
	@ExceptionHandler(value=NullPointerException.class)  
    public ResponseBase allNullPointerExceptionHandler(HttpServletRequest request,  
            Exception exception) throws Exception  
    {  
    	ResponseBase response = ResponseBase.getInitResponse();
    	exception.printStackTrace();
        response.setResponse(Constants.FALSE);
        response.setResponseCode(Constants.ERROR_500);
        response.setResponseMsg("系统异常，请求参数不匹配");
        return response;  
    }  
	/**
     * 所有异常报错
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=Exception.class)  
    public ResponseBase allExceptionHandler(HttpServletRequest request,  
            Exception exception) throws Exception  
    {  
    	ResponseBase response = ResponseBase.getInitResponse();
        exception.printStackTrace();
        response.setResponse(Constants.FALSE);
        response.setResponseCode(Constants.ERROR_500);
        response.setResponseMsg("服务器异常");
        return response;  
    }  

}
package net.zn.ddxj.base;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.utils.CmsUtils;

@Aspect
// 申明是个spring管理的bean
@Component
@Slf4j
public class LogAspectServiceApi {
	private JSONObject jsonObject = new JSONObject();

	// 申明一个切点 里面是 execution表达式
	@Pointcut("execution(public * net.zn.ddxj.service.*.*(..))")
	private void controllerAspect() {
	}

	// 请求method前打印内容
	@Before(value = "controllerAspect()")
	public void methodBefore(JoinPoint joinPoint) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if(!CmsUtils.isNullOrEmpty(requestAttributes))
		{
			HttpServletRequest request = requestAttributes.getRequest();
			log.info("########请求内容########");
			try {
				// 打印请求内容
				log.info("请求地址:" + request.getRequestURL().toString());
				log.info("请求方式:" + request.getMethod());
				log.info("请求类方法:" + joinPoint.getSignature());
				log.info("请求类方法参数:" + Arrays.toString(joinPoint.getArgs()));
			} catch (Exception e) {
				log.error("###LogAspectServiceApi.class methodBefore() ### ERROR:", e);
			}
			log.info("#######################");
		}
	}

	// 在方法执行完结后打印返回内容
	@AfterReturning(returning = "o", pointcut = "controllerAspect()")
	public void methodAfterReturing(Object o) {
		log.info("#######################");
		try {
			if(!CmsUtils.isNullOrEmpty(o))
			{
				String response = o.toString();
				if(response.length() > 5000)
				{
					log.info("响应内容过长，截取前5000行内容:" + response.substring(0,5000));
				}
				else
				{
					log.info("响应内容:" + response);
				}
			}
		} catch (Exception e) {
			log.error("###LogAspectServiceApi.class methodAfterReturing() ### ERROR:", e);
		}
		log.info("########返回内容########");
	}
	@Around(value = "controllerAspect()")  
    public Object loggingAround(ProceedingJoinPoint pjp) throws Throwable{  
        Object object = null;  
        long startTime = System.currentTimeMillis();
        object = pjp.proceed();  
        long endTime = System.currentTimeMillis();
        log.info("接口整体耗时："+(endTime - startTime)+"毫秒");
        return object;  
    } 
}

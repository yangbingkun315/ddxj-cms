package net.zn.ddxj.config;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.annotate.DDXJHystrix;
import net.zn.ddxj.annotate.DDXJRateLimter;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.json.JsonUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

//@Aspect
//@Component
@Slf4j
/**
 * 使用Hysitrix对所有的服务进行，服务隔离、服务降级、服务限流、服务熔断
 * 服务隔离：每个服务，都有独立的线程池，互不影响
 * 服务降级：为了给用户更好的体验，降级是为了给用户友好的提示，如：亲，服务器繁忙，请稍后再试
 * 服务熔断：如果线程池满了，线程池外的队列也满了，则执行服务的熔断了，服务的熔断和降级
* @ClassName: RecruitController   
* @author 上海众宁网络科技有限公司-何俊辉
* @date 2018年4月18日  
*
 */
public class HystrixAspect {

	private Map<String,RateLimiter> rateHashMap = new ConcurrentHashMap<String,RateLimiter>();
	// 申明一个切点 里面是 execution表达式
//	@Pointcut("execution(public * net.zn.ddxj.api.*.*(..))")
	private void hystrixPointcut() {
	}

	private Object rateLimterValidate(ProceedingJoinPoint joinPoint) throws Throwable
	{
		Method signatureMethod = getSignatureMethod(joinPoint);
		//判断是否有该方法，一般是没问题的
		if(CmsUtils.isNullOrEmpty(signatureMethod))
		{
			return null;
		}
		//使用JAVA反射机制获取拦截方法上的自定义参数
		//判断接口是否有@ExtRateLimter注解
		DDXJRateLimter declaredAnnotation = signatureMethod.getDeclaredAnnotation(DDXJRateLimter.class);
		if(CmsUtils.isNullOrEmpty(declaredAnnotation))
		{
			return null;
		}
		double permitsPerSecond = declaredAnnotation.permitsPerSecond();//获取限流每秒允许多少请求
		long timeout = declaredAnnotation.timeout();//获取令牌等待时间
		//调用原生的Ratelimter创建令牌桶
		//令牌桶是单例的，所以不可能每次调用同样的方法都创建一个桶，所以我们把桶放在map中，以接口URL为KEY
		RateLimiter create =  null;
		String requestURI = getRequestURI();
		if(rateHashMap.containsKey(requestURI))
		{
			create = rateHashMap.get(requestURI);
		}
		else
		{
			create = RateLimiter.create(permitsPerSecond);
			rateHashMap.put(requestURI, create);
		}
		//调用令牌桶，获取有效的令牌，如果在令牌桶中没有获取到有效的令牌，则不走实例方法，直接走降级
		boolean tryAcquire = create.tryAcquire(timeout,TimeUnit.MILLISECONDS);
		log.info("获取令牌等待时间："+create.acquire());
		if(!tryAcquire)
		{
			fallback();//执行降级处理
			return null;
		}
		return null;
	}
	// 请求method前打印内容
	@Around("hystrixPointcut()")
	public Object runCommand(final ProceedingJoinPoint joinPoint) throws Throwable {
		
		rateLimterValidate(joinPoint);//令牌桶算法-限流
		
		Method signatureMethod = getSignatureMethod(joinPoint);
		//判断是否有该方法，一般是没问题的
		if(CmsUtils.isNullOrEmpty(signatureMethod))
		{
			return null;
		}
		//使用JAVA反射机制获取拦截方法上的自定义参数
		//判断接口是否有@ExtRateLimter注解
		DDXJHystrix declaredAnnotation = signatureMethod.getDeclaredAnnotation(DDXJHystrix.class);
		if(CmsUtils.isNullOrEmpty(declaredAnnotation))
		{
			//进入实际方法中
			return joinPoint.proceed();
		}
		return wrapWithHystrixCommand(joinPoint).execute();
	}
	/**
	 * Gavan限流服务降级
	* @Title: fallback
	* @Description: TODO()
	* @param     参数
	* @return void    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	private void fallback()
	{
		ServletRequestAttributes attribute = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = attribute.getResponse();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter writer= null;
		try {
			writer= response.getWriter();
			ResponseBase responseBase = new ResponseBase();
			responseBase.setResponse(Constants.FALSE);
			responseBase.setResponseCode(Constants.ERROR_500);
			responseBase.setResponseMsg("亲，服务器访问人数较多，请稍后重试");
			writer.println(JsonUtil.bean2jsonToString(responseBase));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			writer.close();
		}
		
		
	}
	/**
	 * Hystrix防止雪崩
	* @Title: wrapWithHystrixCommand
	* @Description: TODO()
	* @param @param joinPoint
	* @param @return    参数
	* @return HystrixCommand<Object>    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	private HystrixCommand<Object> wrapWithHystrixCommand(final ProceedingJoinPoint joinPoint)
	{
		String method = joinPoint.getSignature().getName();//获取方法名
		return createHystrixCommand(method,joinPoint);
	
	}
	private HystrixCommand<Object> createHystrixCommand(String method,ProceedingJoinPoint joinPoint){
		
		
		//重写Hystrix的run方法和fallback,把AOP执行过程放入里面
		final HystrixCommand<Object> hystrixCommand = new HystrixCommand<Object>(setter(method)){
				@Override
				protected Object run() throws Exception {
					try {
						log.info("Hystrix服务保护中，独立创建线程，线程名称："+Thread.currentThread().getName()+"，线程ID："+Thread.currentThread().getId());
						log.info("线程存活数量："+Thread.activeCount());
						Object object = joinPoint.proceed();  
						return object;
					} catch (Throwable throwable) {
						throw (Exception) throwable;
					}
				}
				/**
				 * 服务降级处理
				 */
				@Override
				protected Object getFallback() {
					ResponseBase responseBase = new ResponseBase();
					responseBase.setResponse(Constants.FALSE);
					responseBase.setResponseCode(Constants.ERROR_500);
					responseBase.setResponseMsg("亲，服务器繁忙，请稍后再试");
					return responseBase;
					
				}
			};
		return hystrixCommand;
	}
	/**
	 * 获取当前请求的request对象
	* @Title: getRequest
	* @Description: TODO()
	* @param @return    参数
	* @return HttpServletRequest    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	private HttpServletRequest getRequest()
	{
		ServletRequestAttributes attribute = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return attribute.getRequest();
	}
	private String getRequestURI()
	{
		return getRequest().getRequestURI();
	}
	/**
	 * 获取接口执行方法
	* @Title: getSignatureMethod
	* @Description: TODO()
	* @param @param joinPoint
	* @param @return    参数
	* @return Method    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	private Method getSignatureMethod(ProceedingJoinPoint joinPoint)
	{
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		return method;
	}
	/**
	 * 服务熔断配置
	* @Title: setter
	* @Description: TODO()
	* @param @param method
	* @param @return    参数
	* @return HystrixCommand.Setter    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	private HystrixCommand.Setter setter(String method)
	{
		//服务进行分组
		HystrixCommandGroupKey hystrixCommandGroupKey = HystrixCommandGroupKey.Factory.asKey(CmsUtils.isNullOrEmpty(method)?"gruopName":method);
		//服务的标识
		HystrixCommandKey hystrixCommandKey = HystrixCommandKey.Factory.asKey(CmsUtils.isNullOrEmpty(method)?"commandName":method);
		//线程池的名称，给每个独立线程池创建一个名称-从而满足服务的隔离
		HystrixThreadPoolKey hystrixThreadPoolKey = HystrixThreadPoolKey.Factory.asKey(CmsUtils.isNullOrEmpty(method)?"thread-gruopName":"thread-"+method);
		//线程池配置 线程池大小为20 线程存活的时间为15秒 队列等待的阈值为100，如果超过100执行拒绝策略 配置服务熔断
		HystrixThreadPoolProperties.Setter withQueueSizeRejectionThreshold = HystrixThreadPoolProperties.Setter()
				.withCoreSize(5)
				.withKeepAliveTimeMinutes(5)
				.withQueueSizeRejectionThreshold(100);//允许在队列中的等待的任务数量
		//采用线程池方式进行服务隔离，禁用超时时间
		com.netflix.hystrix.HystrixCommandProperties.Setter withExecutionTimeoutEnabled = HystrixCommandProperties.Setter()
				.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
				.withExecutionTimeoutEnabled(false);
		return HystrixCommand.Setter.withGroupKey(hystrixCommandGroupKey)
				.andCommandKey(hystrixCommandKey)
				.andThreadPoolKey(hystrixThreadPoolKey)
				.andThreadPoolPropertiesDefaults(withQueueSizeRejectionThreshold)
				.andCommandPropertiesDefaults(withExecutionTimeoutEnabled);		
	}
}

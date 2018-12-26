package net.zn.ddxj.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 针对服务雪崩自定义注解
 * @author Java_
 *
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DDXJHystrix {

	int coreSize();//线程池大小
	
	int keepAliveTimeMinutes();//线程存活时间-以秒为单位
	
	int queueSizeRejectionThreshold();//线程池满了，允许线程池外的队列等待多少请求
	
	boolean executionTimeoutEnabled() default false;//是否开启超时时间，默认为false
	
	int executionTimeoutInMilliseconds() default 0;//必须executionTimeoutEnabled为true才生效，超时时间、以毫秒为单位
}

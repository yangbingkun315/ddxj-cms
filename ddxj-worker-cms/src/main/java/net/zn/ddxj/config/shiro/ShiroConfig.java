package net.zn.ddxj.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import net.zn.ddxj.config.shiro.redis.RedisCacheManager;
import net.zn.ddxj.config.shiro.redis.RedisSessionDAO;
import net.zn.ddxj.config.shiro.session.ShiroSessionManager;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

	@Bean
	public Realm realm() {
		return new MyRealm();
	}
	@Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
	 /**
     * 用户授权信息Cache
     */
	@Bean
    public RedisCacheManager redisCacheManager() {
		return new RedisCacheManager();
    }
	
	 @Bean
     public RedisSessionDAO redisSessionDAO(){
	      return new RedisSessionDAO();
     }
	 @Bean
     public DefaultWebSessionManager sessionManager() {
		 
	    DefaultWebSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
//        sessionManager.setGlobalSessionTimeout(3600000);
        sessionManager.setGlobalSessionTimeout(1800000);//全局的会话信息设置成30分钟
//        sessionManager.setSessionValidationInterval(15000);//检测扫描信息间隔30秒
//        sessionManager.setSessionValidationSchedulerEnabled(true);//是否开启扫描
        sessionManager.setDeleteInvalidSessions(true);//删除过期的session
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setCacheManager(redisCacheManager());
        return sessionManager;
	    }
	 
    //设置cookie
    @Bean
    public Cookie sessionIdCookie(){
        Cookie sessionIdCookie=new SimpleCookie("DDXJ-SESSION-ID");
        sessionIdCookie.setMaxAge(-1);
        sessionIdCookie.setHttpOnly(true);
        return sessionIdCookie;
    }

    @Bean
    public DefaultSecurityManager securityManager() {
    	DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    	//设置realm
        securityManager.setRealm(realm());
        // session管理器  
        securityManager.setSessionManager(sessionManager());
        //设置cache
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }
    
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }
    
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager());
		shiroFilter.setLoginUrl("/cms/login.htm");
		shiroFilter.setSuccessUrl("/cms/index.htm");
		// 设置无权限时跳转的 url
		shiroFilter.setUnauthorizedUrl("/cms/403.htm");
		//自定义拦截器
		Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
		//限制同一帐号同时在线的个数。
		filtersMap.put("kickout", kickoutSessionControlFilter());
		
		shiroFilter.setFilters(filtersMap);
		shiroFilter.setFilterChainDefinitionMap(FilterChainDefinitionMapBuilder.buildFilterChainDefinitionMap());
		return shiroFilter;
	}
	/**
	* 限制同一账号登录同时登录人数控制
	* @return
	*/
	@Bean
	public KickoutSessionControlFilter kickoutSessionControlFilter(){
		KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
		//使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
		//这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
		//也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
		kickoutSessionControlFilter.setCacheManager(redisCacheManager());
		//用于根据会话ID，获取会话进行踢出操作的；
		kickoutSessionControlFilter.setSessionManager(sessionManager());
		//是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
		kickoutSessionControlFilter.setKickoutAfter(false);
		//同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
		kickoutSessionControlFilter.setMaxSession(1);
		//被踢出后重定向到的地址；
		kickoutSessionControlFilter.setKickoutUrl("/cms/login.htm?m=kick");
		return kickoutSessionControlFilter;
	}
}

package net.zn.ddxj.config.shiro.redis;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;



import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    private static String prefix ="ddxj-shiro-session:";
    private static long timeOut =1800;
    
    @Resource(name="redisTemplateObj")
    private RedisTemplate<String, Object> redisTemplate;

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        logger.debug("创建session:{}", session.getId());
        redisTemplate.opsForValue().set(prefix + sessionId.toString(), session,timeOut,TimeUnit.SECONDS);
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.debug("获取session:{}", sessionId);
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            session = (Session) redisTemplate.opsForValue().get(prefix + sessionId.toString());
        }
        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    	 StringBuffer requestUrl = request.getRequestURL();
    	 if(requestUrl.indexOf("/validate/message/notic.htm") == -1)
    	 {
             //更新redis
             super.doUpdate(session);
             logger.debug("获取session:{}", session.getId());
             String key = prefix + session.getId().toString();
             if (!redisTemplate.hasKey(key)) {
                 redisTemplate.opsForValue().set(key, session);
             }
             redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    	 }
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
        logger.debug("删除session:{}", session.getId());
        super.doDelete(session);
        redisTemplate.delete(prefix + session.getId().toString());
    }
}
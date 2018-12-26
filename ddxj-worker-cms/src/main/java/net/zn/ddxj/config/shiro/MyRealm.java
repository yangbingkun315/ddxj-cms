package net.zn.ddxj.config.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;















import net.zn.ddxj.config.shiro.redis.RedisSessionDAO;
import net.zn.ddxj.constants.CmsConstants;
import net.zn.ddxj.entity.CmsResource;
import net.zn.ddxj.entity.CmsRole;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.MD5Utils;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 
 * @author SPPan
 *
 */
@Component
public class MyRealm extends AuthorizingRealm {
	 private static Logger logger = LoggerFactory.getLogger(MyRealm.class);
	 
	@Autowired
	@Lazy
	private CmsService cmsService;
	
	public MyRealm(){
		//super(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(UsernamePasswordToken.class);
        //FIXME: 暂时禁用Cache
        setCachingEnabled(false);
	}
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//logger.info("***************************执行Shiro权限认证******************************");
		CmsUser user = (CmsUser)super.getAvailablePrincipal(principals); 
		
       // CmsUser user = cmsService.findByUserName(loginName);// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
    	//权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        if(!CmsUtils.isNullOrEmpty(user))
        {
        	SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        	Set<String> shiroPermissions = new HashSet<>();
        	CmsRole role = user.getRole();
        	authorizationInfo.addRole(role.getRoleKey());
        	List<CmsResource> resources = role.getResourceList();
        	for (CmsResource resource : resources) {
        		shiroPermissions.add(resource.getResourceKey());
        	}
        	authorizationInfo.addStringPermissions(shiroPermissions);
        	return authorizationInfo;
        }
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken) throws AuthenticationException {
		
		 logger.info("###执行Shiro登录认证###");
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
        logger.info("###验证当前Subject时获取到token为："+token+"###"); 
		
		CmsUser user = cmsService.findByUserName(token.getUsername());
		
		String password = new String((char[]) token.getCredentials());

		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}
		// 密码错误
		if (!MD5Utils.generatePasswordMD5(password,CmsConstants.MD5_SALT).equals(user.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}
		// 账号锁定
		if (user.getLocked() == 1) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		// 账号锁定
		if (CmsUtils.isNullOrEmpty(user.getRoleId())) {
			throw new LockedAccountException("请联系管理员分配角色");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

}

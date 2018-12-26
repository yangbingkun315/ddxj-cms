package net.zn.ddxj.config.shiro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.json.JsonUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
@Slf4j
public class FilterChainDefinitionMapBuilder {
		/**
	     * 自定义 FilterChainDefinition
	     * @return
	     */
	    public static LinkedHashMap<String, String> buildFilterChainDefinitionMap(){
	    	//设置拦截器
	        LinkedHashMap<String, String> map = new LinkedHashMap<>();
	        
	        //此处声明关系也可是已配置在数据库中的
	        //添加和修改的KEY是一致的，所以会合并
	        DriverManagerDataSource dataSource=new DriverManagerDataSource();
            dataSource.setDriverClassName(PropertiesUtils.getPropertiesByName("jdbc.className"));
	        dataSource.setUrl(PropertiesUtils.getPropertiesByName("jdbc.url"));
	        dataSource.setUsername(PropertiesUtils.getPropertiesByName("jdbc.username"));
	        dataSource.setPassword(PropertiesUtils.getPropertiesByName("jdbc.password"));
           JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
           String sql = "select DISTINCT resource_key as resouceKey,resource_name as resourceName,resource_url as resourceUrl from zn_cms_resource where (resource_type = 1 or resource_type = 4) and flag = 1";
           List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
            for (Map<String, Object> map2 : queryForList) 
            {
        	   map.put(String.valueOf(map2.get("resourceUrl")), "authc,perms["+map2.get("resouceKey")+"]");//针对数据库中的一些权限，进行权限限制
        	   
            }
	        map.put("/cms/login.htm", "anon");
	        map.put("/cms/register.htm", "anon");
	        map.put("/cms/404.htm", "anon");
	        map.put("/cms/500.htm", "anon");
	        map.put("/cms/index.htm", "authc,kickout");
	    //    map.put("/cms/user/logout.htm", "logout");
	        map.put("/assets/**", "anon");
	        map.put("/validate/message/notic.htm", "anon");
//	        map.put("/**", "anon");
	        log.info("###权限分配完毕，共："+map.size()+"个###");
	        log.info("###"+JsonUtil.map2jsonToString(map)+"###");
	        return map;
	    }
}


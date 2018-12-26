package net.zn.ddxj.config;
import javax.annotation.PostConstruct;

import net.zn.ddxj.config.shiro.freemarker.ShiroTags;
import net.zn.ddxj.config.shiro.freemarker.custom.CmsVersionLabel;
import net.zn.ddxj.config.shiro.freemarker.custom.ImgResUrlLabel;
import net.zn.ddxj.config.shiro.freemarker.custom.ResUrlLabel;
import net.zn.ddxj.constants.CmsConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
/**
 * @ClassName:  CmsVersionLabel   
 * @Description:TODO(自定义标签配置)   
 * @author: 何俊辉 
 * @date:   2018年8月9日 下午4:44:47   
 * @Copyright: 2018 www.diandxj.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海众宁网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
@Component
public class FreeMarkerConfig{

	private static final CmsVersionLabel CMSVERSIONLABEL = new CmsVersionLabel();
	private static final ResUrlLabel RESURLLABEL = new ResUrlLabel();
	private static final ImgResUrlLabel IMGRESURLLABEL = new ImgResUrlLabel();

	@Autowired
    private freemarker.template.Configuration configuration;
    @PostConstruct
    public void setSharedVariable() {
    	try {
			configuration.setSharedVariable("shiro", new ShiroTags());
			configuration.setSharedVariable("cmsVersion",CMSVERSIONLABEL);
			configuration.setSharedVariable("resUrl",RESURLLABEL);
			configuration.setSharedVariable("base",CmsConstants.Tag.BASE_URL);
			configuration.setSharedVariable("storageUrl",CmsConstants.Tag.STATIC_URL);
			configuration.setSharedVariable("imgResUrl",IMGRESURLLABEL);
			configuration.setNumberFormat("#");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

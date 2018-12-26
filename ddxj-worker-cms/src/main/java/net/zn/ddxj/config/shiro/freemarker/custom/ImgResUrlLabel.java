package net.zn.ddxj.config.shiro.freemarker.custom;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import net.zn.ddxj.constants.CmsConstants;
import net.zn.ddxj.controller.CmsController;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * @ClassName:  CmsVersionLabel   
 * @Description:TODO(静态资源文件路径)   
 * @author: 何俊辉 
 * @date:   2018年8月9日 下午4:44:47   
 * @Copyright: 2018 www.diandxj.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海众宁网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class ImgResUrlLabel  implements TemplateDirectiveModel{
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// TODO Auto-generated method stub
		Writer out = env.getOut();
		out.write(CmsConstants.Tag.IMG_RES_URL);
	}
}

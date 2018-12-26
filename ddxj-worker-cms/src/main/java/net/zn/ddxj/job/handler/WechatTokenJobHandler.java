package net.zn.ddxj.job.handler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.tool.WechatService;

/**
 * 微信定时任务自动刷新公众号TOKEN
 *
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author 何俊辉
 */
@JobHandler(value="wechatTokenJobHandler")
@Component
@Slf4j
public class WechatTokenJobHandler extends IJobHandler {

	@Autowired
	private WechatService wechatService;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		wechatService.queryWechatToken();
		log.info("#############定时任务自动刷新公众号TOKEN成功############");
		XxlJobLogger.log("#############定时任务自动刷新公众号TOKEN成功############");
		return SUCCESS;
	}

}

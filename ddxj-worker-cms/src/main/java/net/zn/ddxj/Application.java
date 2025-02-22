package net.zn.ddxj;

import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.pagehelper.PageHelper;

/**
 * springBoot 启动类
* @ClassName: Application 
* @Description: TODO(启动tomcat) 
* @author 何俊辉
* @date 2017-6-3 上午10:44:57 
*
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@MapperScan("net.zn.ddxj.mapper")
public class Application extends SpringBootServletInitializer{

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	/**
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.debug("启动成功");
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // TODO Auto-generated method stub
        return builder.sources(Application.class);
    }
    @Bean
    PageHelper pageHelper(){
        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        //添加插件
        new SqlSessionFactoryBean().setPlugins(new Interceptor[]{pageHelper});
        return pageHelper;
    }
	/**
	 * 设置线程池
	* @Title: CodeServer.java  
	* @param @return参数  
	* @return TaskExecutor    返回类型 
	* @throws
	* @Package net.zn.ddxj  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月17日  
	* @version V1.0
	 */
	@Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(20);
        // 设置队列容量
        executor.setQueueCapacity(30);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("DDXJ-Ascy-Manager-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
	
}

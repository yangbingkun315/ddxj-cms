package net.zn.ddxj.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.zn.ddxj.config.intercepter.CommonIntercepter;
import net.zn.ddxj.constants.CmsConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private CommonIntercepter commonIntercepter;

	/**
	 * fastJson相关设置
	 */
	private FastJsonConfig getFastJsonConfig() {
		
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 在serializerFeatureList中添加转换规则
		List<SerializerFeature> serializerFeatureList = new ArrayList<SerializerFeature>();
		serializerFeatureList.add(SerializerFeature.PrettyFormat);
		serializerFeatureList.add(SerializerFeature.WriteMapNullValue);
		serializerFeatureList.add(SerializerFeature.WriteNullStringAsEmpty);
		serializerFeatureList.add(SerializerFeature.WriteNullListAsEmpty);
		serializerFeatureList.add(SerializerFeature.DisableCircularReferenceDetect);
		SerializerFeature[] serializerFeatures = serializerFeatureList.toArray(new SerializerFeature[serializerFeatureList.size()]);
		fastJsonConfig.setSerializerFeatures(serializerFeatures);

		return fastJsonConfig;
	}
	
	/**
	 * fastJson相关设置
	 */
	private FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter() {

		FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter4();

		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
		supportedMediaTypes.add(MediaType.parseMediaType("application/json"));

		fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		fastJsonHttpMessageConverter.setFastJsonConfig(getFastJsonConfig());

		return fastJsonHttpMessageConverter;
	}
	
	/**
	 * 添加fastJsonHttpMessageConverter到converters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(fastJsonHttpMessageConverter());
	}

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonIntercepter).addPathPatterns("/**")
		.excludePathPatterns("/**/404.htm","/**/500.htm","/**/403.htm","*.css","*.js","/**/images/","/**/css/","/**/js/");
		super.addInterceptors(registry);
	}
  /*  @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter();
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }*/
	//统一页码处理配置
		@Bean
		public EmbeddedServletContainerCustomizer containerCustomizer() {
		    return new EmbeddedServletContainerCustomizer() {
		        @Override
		        public void customize(ConfigurableEmbeddedServletContainer container) {
		            //ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
		            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND,"/cms/404.htm");
		            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/cms/500.htm");
	 
		            container.addErrorPages( error404Page, error500Page);
		        }
		    };
		}


}

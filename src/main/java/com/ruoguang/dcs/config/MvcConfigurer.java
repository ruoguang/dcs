package com.ruoguang.dcs.config;



import com.ruoguang.dcs.interceptor.AllInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * MvcConfigurer
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AllInterceptor allInterceptor;

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(allInterceptor)
                .addPathPatterns("/**");

    }

    /**
     * 添加跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE")
                .allowCredentials(false).maxAge(3600);
    }

    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/output/**").addResourceLocations("classpath:/output/");
        registry.addResourceHandler("/upload/**").addResourceLocations("classpath:/upload/");
        registry.addResourceHandler("/download/**").addResourceLocations("classpath:/download/");
        // knife4j 静态资源放行
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }


}
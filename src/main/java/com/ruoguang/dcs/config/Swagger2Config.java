package com.ruoguang.dcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import springfox.documentation.swagger2.annotations.EnableSwagger2;



/**
 * author cc cc wlddh
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {
	@Value("${spring.application.name}")
	private String basePath;

	@Bean
	public Docket createRestApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.pathMapping(basePath)
				//分组名称
				.groupName("1.X版本")
				.select()
				//这里指定Controller扫描包路径
				.apis(RequestHandlerSelectors.basePackage("com.ruoguang.dcs.controller"))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("文件格式转换服务")
				.description("文件格式转换服务接口文档")
				.termsOfServiceUrl("http://dcs.ruoguang.com/")
				.version("1.0")
				.build();
	}

}
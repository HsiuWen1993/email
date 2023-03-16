package com.wen.email.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*") //允許跨網域請求的來源  *SpringBoot升级2.4.0後，跨域配置中的.allowedOrigins不再可用
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS") //允許使用那些請求方式
				.allowCredentials(true) //允許跨域攜帶cookie資訊，預設跨網域請求是不攜帶cookie資訊的。
				.maxAge(3600)
				.allowedHeaders("*"); //允許哪些Header
	}
}
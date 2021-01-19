package net.xiaoxiangshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.wf.jwtp.configuration.EnableJwtPermission;


@EnableCaching
@EnableJwtPermission
@SpringBootApplication
@MapperScan(basePackages = "net.xiaoxiangshop.dao")
@ComponentScan(basePackages = { "net.xiaoxiangshop.util", "net.xiaoxiangshop" })

public class ApplicationB2B2C extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApplicationB2B2C.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApplicationB2B2C.class, args);
	}
	
}

package com.aonhewitt.upoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.aonhewitt.upoint.conf.TomcatFactoryGenerator;

/**
 * UPoint-SpringBootApp
 * 
 * @author Yogesh M
 *
 */
@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@EnableCaching
@EnableFeignClients(basePackages = {"com.alight", "com.aonhewitt"})
@ComponentScan(basePackages = {"com.aonhewitt.upoint", "com.aonhewitt.portal.hasbro"})
public class Application {
	
	public static void main(String[] args) {
		System.setProperty("startTime", String.valueOf(System.currentTimeMillis()));
		SpringApplication.run(Application.class, args);
	} 
	
	@Bean
	public TomcatServletWebServerFactory  tomcatFactory() 
	{
		return new TomcatFactoryGenerator();
	}
}
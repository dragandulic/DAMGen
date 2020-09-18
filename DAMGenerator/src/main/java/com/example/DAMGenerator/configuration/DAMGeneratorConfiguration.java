package com.example.DAMGenerator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateException;

import javax.sql.DataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class DAMGeneratorConfiguration implements WebMvcConfigurer {
	
	@Bean
	public Connection connection(DataSource dataSource) {
		Connection connection = null;
		
		try {
			connection = dataSource.getConnection();
		}catch(Exception e) {
			
		}
		
		return connection;
	}
	
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
		config.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
		freeMarkerConfigurer.setConfiguration(config);
		return freeMarkerConfigurer;
	}

}

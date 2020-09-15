package com.example.DAMGenerator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
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

}

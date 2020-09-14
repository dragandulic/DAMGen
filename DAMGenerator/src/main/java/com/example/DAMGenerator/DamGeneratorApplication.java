package com.example.DAMGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.DAMGenerator", "generated"})
public class DamGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DamGeneratorApplication.class, args);
	}

}

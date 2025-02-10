package com.dp.dataPlug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.dp.dataPlug", "DataPlugController"})
public class DataPlugApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataPlugApplication.class, args);
	}

}

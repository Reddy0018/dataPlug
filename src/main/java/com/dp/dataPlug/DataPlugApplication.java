package com.dp.dataPlug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.dp.dataPlug", "DataPlugController"})
public class DataPlugApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataPlugApplication.class, args);
	}

}

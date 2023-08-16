package com.sipe.sybatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SybatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SybatchApplication.class, args);
	}

}

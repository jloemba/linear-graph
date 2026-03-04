package com.lineage;

import org.springframework.boot.SpringApplication;

public class TestLineageApplication {

	public static void main(String[] args) {
		SpringApplication.from(LineageApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

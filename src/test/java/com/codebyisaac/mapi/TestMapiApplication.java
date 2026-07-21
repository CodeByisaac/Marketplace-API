package com.codebyisaac.mapi;

import org.springframework.boot.SpringApplication;

public class TestMapiApplication {

	public static void main(String[] args) {
		SpringApplication.from(MapiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

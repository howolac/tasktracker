package com.tasktracker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@MapperScan("com.tasktracker.mapper")
@EnableScheduling
public class TasktrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TasktrackerApplication.class, args);
	}
}
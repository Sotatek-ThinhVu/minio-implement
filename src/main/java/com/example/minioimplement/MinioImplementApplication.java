package com.example.minioimplement;

import com.example.minioimplement.service.MinioStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MinioImplementApplication {


	public static MinioStorageService minioStorageService;
	public static void main(String[] args) {

		SpringApplication.run(MinioImplementApplication.class, args
		);
	}

}

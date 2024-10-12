package com.himanism.hcharityapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.himanism.hcharityapi.services.FilesStorageService;

import jakarta.annotation.Resource;

@SpringBootApplication
public class HCharityApiApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(HCharityApiApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.init();
	}
}

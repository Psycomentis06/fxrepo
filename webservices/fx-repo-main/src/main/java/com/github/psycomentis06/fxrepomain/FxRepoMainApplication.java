package com.github.psycomentis06.fxrepomain;

import com.github.psycomentis06.fxrepomain.properties.StorageProperties;
import com.github.psycomentis06.fxrepomain.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(
		StorageProperties.class
)
public class FxRepoMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxRepoMainApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}

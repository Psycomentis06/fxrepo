package com.github.psycomentis06.fxrepomain;

import com.github.psycomentis06.fxrepomain.properties.StorageProperties;
import com.github.psycomentis06.fxrepomain.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j(topic = "Main")
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
			var s = storageService.init();
			switch (s) {
				case UPLOAD_DIR_NOT_CREATED -> log.warn("Upload directory not created");
				case UPLOAD_DIR_CREATED -> log.info("Upload directory created");
				default -> log.info("Upload dir: No action taken");
			}
		};
	}

}

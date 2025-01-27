package ua.sosna.wortschatz.wortschatztchen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import ua.sosna.wortschatz.wortschatztchen.storage.StorageProperties;
import ua.sosna.wortschatz.wortschatztchen.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class WortschatztchenApplication {

	public static void main(String[] args) {
		SpringApplication.run(WortschatztchenApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			//storageService.deleteAll();
			storageService.init();
		};
	}

}

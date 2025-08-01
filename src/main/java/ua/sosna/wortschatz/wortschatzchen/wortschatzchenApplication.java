package ua.sosna.wortschatz.wortschatzchen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import ua.sosna.wortschatz.wortschatzchen.storage.StorageProperties;
import ua.sosna.wortschatz.wortschatzchen.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class wortschatzchenApplication {

	public static void main(String[] args) {
		SpringApplication.run(wortschatzchenApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			//storageService.deleteAll();
			storageService.init();
		};
	}

}

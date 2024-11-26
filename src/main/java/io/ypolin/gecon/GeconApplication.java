package io.ypolin.gecon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableMongoRepositories
@EnableSpringDataWebSupport(
		pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class GeconApplication {

	public static void main(String[] args) {

		SpringApplication.run(GeconApplication.class, args);
	}
}

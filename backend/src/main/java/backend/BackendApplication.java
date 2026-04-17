package backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"backend",
		"com.carrental.config",
		"com.carrental.controller",
		"com.carrental.service"
})
@EntityScan(basePackages = "com.carrental.model")
@EnableJpaRepositories(
		basePackages = "com.carrental.repository")
public class BackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(
				BackendApplication.class, args);
	}
}

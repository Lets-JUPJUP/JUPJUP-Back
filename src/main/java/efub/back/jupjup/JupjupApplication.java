package efub.back.jupjup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JupjupApplication {

	public static void main(String[] args) {
		SpringApplication.run(JupjupApplication.class, args);
	}

}

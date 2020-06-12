package at.heapheaparray.langbuddy.server;

import at.heapheaparray.langbuddy.server.dao.models.Language;
import at.heapheaparray.langbuddy.server.dao.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class ServerApplication {

	@Value("${application.environment:unknown}")
	private String environment;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@RequestMapping("/")
	@ResponseBody
	public String greetingEnv() {
		return "Hello World! This is the " + environment + " environment!";
	}

	@Bean
	public CommandLineRunner insertLanguages(LanguageRepository repo) {
		return args -> {
			if (repo.findAll().isEmpty()) {
				repo.save(Language.builder().clearName("German").shortName("DE").build());
				repo.save(Language.builder().clearName("English").shortName("EN").build());
				repo.save(Language.builder().clearName("Spanish").shortName("ES").build());
				repo.save(Language.builder().clearName("French").shortName("FR").build());
			}
		};
	}
}

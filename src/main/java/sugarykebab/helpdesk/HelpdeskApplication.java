package sugarykebab.helpdesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sugarykebab.helpdesk.entities.AppUser;
import sugarykebab.helpdesk.repositories.AppUserRepository;

@SpringBootApplication
public class HelpdeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpdeskApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AppUserRepository appUserRepository) {
        return args -> {





        };
    }
}

package org.sportstogo.backend.User;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class UserConfig {

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            User Radu = new User(
                    "radu",
                    "onofreir54@gmail.com",
                    LocalDate.now()
            );

            //userRepository.save(Radu);
        };
    }
}

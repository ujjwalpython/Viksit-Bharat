package com.negd.viksit.bharat;

import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class ViksitBharatApplication {

    public ViksitBharatApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(ViksitBharatApplication.class, args);
	}


    private final UserRepository userRepository;

    @Bean
    CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder) {
        return (args) -> {
            List<User> foundUsers = this.userRepository.findAll();
            if (foundUsers.isEmpty()) {
                List<User> pools = List.of(User.builder()
                        .firstName("Ujjwal")
                        .lastName("Dhiman")
                        .email("ujjwalDhiman@gmail.com")
                        .password(passwordEncoder.encode("password@123"))
                        .build(),
                        User.builder()
                                .firstName("Test")
                                .lastName("User")
                                .email("testuser@gmail.com")
                                .password(passwordEncoder.encode("test@123"))
                                .build());
                userRepository.saveAll(pools);
            }
        };
    }

}

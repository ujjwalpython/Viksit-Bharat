package com.negd.viksit.bharat;

import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.model.master.TargetIndicator;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.TargetIndicatorRepository;
import com.negd.viksit.bharat.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class ViksitBharatApplication extends SpringBootServletInitializer {

    private final UserRepository userRepository;

    public ViksitBharatApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ViksitBharatApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ViksitBharatApplication.class);
    }

    @Bean
    CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder,
                                        UserRepository userRepository,
                                        MinistryRepository ministryRepository,
                                        TargetIndicatorRepository targetIndicatorRepository) {
        return (args) -> {
            // Seed Users
            if (userRepository.findAll().isEmpty()) {
                List<User> users = List.of(
                        User.builder()
                                .firstName("Ujjwal")
                                .lastName("Dhiman")
                                .email("ujjwalDhiman@gmail.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .build(),
                        User.builder()
                                .firstName("Test")
                                .lastName("User")
                                .email("testuser@gmail.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("test@123"))
                                .build()
                );
                userRepository.saveAll(users);
            }

            // Seed Ministries
            if (ministryRepository.findAll().isEmpty()) {
                List<Ministry> ministries = List.of(
                        Ministry.builder().code("MOA").name("Ministry of Agriculture & Farmers Welfare").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("COM").name("Ministry of Commerce & Industry").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("EDU").name("Ministry of Education").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("FIN").name("Ministry of Finance").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("HLTH").name("Ministry of Health & Family Welfare").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("HOME").name("Ministry of Home Affairs").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("INFO").name("Ministry of Information & Broadcasting").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOR").name("Ministry of Rural Development").isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOS").name("Ministry of Science & Technology").isActive(true).isDeleted(false).build()
                );
                ministryRepository.saveAll(ministries);
            }

            // Seed Target Indicators
            if (targetIndicatorRepository.findAll().isEmpty()) {
                List<TargetIndicator> indicators = List.of(
                        TargetIndicator.builder().code("GDP").name("GDP Growth Rate").isActive(true).isDeleted(false).build(),
                        TargetIndicator.builder().code("PCI").name("Per Capita Income").isActive(true).isDeleted(false).build(),
                        TargetIndicator.builder().code("DIGI").name("Digital Literacy Rate").isActive(true).isDeleted(false).build(),
                        TargetIndicator.builder().code("RENEW").name("Renewable Energy Capacity").isActive(true).isDeleted(false).build()
                );
                targetIndicatorRepository.saveAll(indicators);
            }
        };
    }
}

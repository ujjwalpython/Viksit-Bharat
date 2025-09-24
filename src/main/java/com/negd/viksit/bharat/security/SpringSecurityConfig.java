package com.negd.viksit.bharat.security;

import com.negd.viksit.bharat.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtTokenUtil jwtService;

    private final UserRepository userRepository;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                                   AuthenticationProvider authenticationProvider) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).cors(corsSpec -> {
                    corsSpec.configurationSource(request -> {
                        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
                        config.addAllowedMethod(HttpMethod.GET);
                        config.addAllowedMethod(HttpMethod.POST);
                        config.addAllowedMethod(HttpMethod.PUT);
                        config.addAllowedMethod(HttpMethod.DELETE);
                        config.addAllowedMethod(HttpMethod.PATCH);
                        config.addAllowedMethod(HttpMethod.OPTIONS);
                        config.addAllowedMethod(HttpMethod.HEAD);
                        config.addAllowedMethod("*");
                        return config;
                    });
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
         auth.requestMatchers(
                            "/api/v1/oauth/**",
                            "/register",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html").permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint));


        com.negd.viksit.bharat.security.ApiJwtAuthenticationFilter jwtAuthenticationFilter = new com.negd.viksit.bharat.security.ApiJwtAuthenticationFilter(jwtService,
                userDetailsService());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
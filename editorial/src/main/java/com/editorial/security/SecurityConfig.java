package com.editorial.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/").permitAll()
                                .requestMatchers("/editorial/login").permitAll()
                                .requestMatchers("/editorial/login/v2").permitAll()
                                .requestMatchers("/editorial/registration").permitAll()
                                .requestMatchers("/editorial/registration/fc").permitAll()
                                .requestMatchers("/editorial/validate").permitAll()
                                .requestMatchers("/editorial/registration/**").permitAll()
                                .requestMatchers("/editorial/actions/get/users").hasRole("ADMIN")
                                .requestMatchers("/editorial/actions/user/info").hasAnyRole("USER", "ADMIN", "JOURNALIST", "REDACTOR", "CORRECTOR")
                                .requestMatchers("/editorial/actions/**").hasAnyRole("USER", "ADMIN", "JOURNALIST", "REDACTOR", "CORRECTOR")
                                .requestMatchers("/editorial/test").hasAnyRole("JOURNALIST", "USER", "ADMIN")
                                .requestMatchers("/editorial/proposal").hasAnyRole("JOURNALIST", "REDACTOR")
                                .requestMatchers("/editorial/draft").hasAnyRole("JOURNALIST")
                                .requestMatchers("/editorial/correct").hasAnyRole("CORRECTOR", "REDACTOR")
                                .requestMatchers("/editorial/correct/**").hasRole("REDACTOR")
                                .anyRequest().authenticated())
                .formLogin(configurer -> configurer.loginPage("/editorial/login"))
                .logout(configurer -> configurer.permitAll()
                        .logoutUrl("/editorial/logout")
                        .logoutSuccessUrl("/editorial/login?logout")
                        .deleteCookies("ROLE")
                        .permitAll())
                .exceptionHandling(configurer->configurer.accessDeniedPage("/editorial/denied"))
                .csrf().disable()
                .build();
    }
}

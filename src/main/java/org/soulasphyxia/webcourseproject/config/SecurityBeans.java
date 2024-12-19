package org.soulasphyxia.webcourseproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(
                            "/",
                            "/login",
                            "/logout",
                            "/user/**",
                            "/js/**",
                            "/css/**")
                            .permitAll();
                    request.requestMatchers("/admin/**").authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> {
                    form.loginPage("/login");
                    form.successHandler(successHandler());
                })
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/admin");
        return handler;
    }
}

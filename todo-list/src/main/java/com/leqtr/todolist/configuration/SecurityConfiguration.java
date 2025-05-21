package com.leqtr.todolist.configuration;

import com.leqtr.todolist.service.CustomOAuth2UserService;
import com.leqtr.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final SecurityUtil securityUtil;

    @Bean
    @Lazy
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WhitelistUrls.WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/ws/**"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll())
                .oauth2Login(auth -> auth
                        .loginPage("/login")
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(oAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            userService.processOAuthPostLogin(securityUtil.getSessionUser());
                            response.sendRedirect("/");
                        }))
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }
}

package com.example.safeT.login.config;


import com.example.safeT.login.config.jwt.JwtTokenFilter;
import com.example.safeT.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserService userService;
    //private final CustomOAuth2UserService customOAuth2UserService;
    //private final GoogleUserService googleUserService;
    private static String SECRET_KEY = "my-secret-key-123123";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청에 대해 권한을 허용
                )
                .addFilterBefore(new JwtTokenFilter(userService, SECRET_KEY), UsernamePasswordAuthenticationFilter.class);
//                .oauth2Login(oauth2Login -> oauth2Login
//                                .userInfoEndpoint(userInfoEndpoint ->
//                                        userInfoEndpoint.userService(googleUserService)
//                                )
//                );
        return http.build();
    }

}
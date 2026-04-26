package com.disaster.safety.config;

import com.disaster.safety.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.disaster.safety.util.JwtUtil;
import com.disaster.safety.util.LoginFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration configuration;
    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                //.requestMatchers("/**").permitAll())
                // 로그인 없이 가능
                .antMatchers("/", "/user/login", "/user/signup", "/h2-console/**").permitAll()
                // 관리자 전용
                .antMatchers("/admin/**").hasRole("ADMIN")
                // 그외 로그인 없이 접근 불가
                .anyRequest().authenticated())
            
            // csrf기능 잠금
            .csrf((authorizeHttpRequests) -> authorizeHttpRequests.disable())
            
            // h2 console 사용
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()))
            
            // 로그인 페이지 로 이동
            .formLogin(formLogin -> formLogin
                // 로그인 Get
                .loginPage("/user/login")
                
                // 로그인 Post
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/")
                .usernameParameter("userId")
                .passwordParameter("password")
                .failureUrl("/user/login?error=true")
                .permitAll())

            // 로그아웃 시 이동할 페이지
            .logout(logout -> logout
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                // 로그아웃 시 세션 무효화
                .invalidateHttpSession(true))

            // 로그인 필터 전에 JwtFilter 먼저
            .addFilterBefore(new JwtFilter(jwtUtil), 
            LoginFilter.class)
            // 새로 만든 로그인 필터를 원래 자리로
            .addFilterAt(
                new LoginFilter(authenticationManager(configuration), jwtUtil),
                UsernamePasswordAuthenticationFilter.class)
            ;
        return http.build();
    }

    @Bean
    // 비번 암호화
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}
package com.example.handoverbackend.config.security;


import com.example.handoverbackend.config.jwt.JwtAccessDeniedHandler;
import com.example.handoverbackend.config.jwt.JwtAuthenticationEntryPoint;
import com.example.handoverbackend.config.jwt.JwtSecurityConfig;
import com.example.handoverbackend.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .antMatchers( "/v3/api-docs", "/swagger-resources/**",
//                        "/swagger-ui.html", "/webjars/**", "/swagger/**");
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()

            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

            // exception handling 할 때 우리가 만든 클래스를 추가
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            // 시큐리티는 기본적으로 세션을 사용
            // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/swagger-ui/**", "/v3/**").permitAll() // swagger
            .requestMatchers("/api/auth/join", "/api/auth/join/email/mailConfirm", "/api/auth/join/email/check", "/api/auth/login", "/api/auth/reissue").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/members").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/members").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/members").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/member/search").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

            .requestMatchers(HttpMethod.POST, "/api/boards").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/boards/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/boards/{id}/favorites").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/boards/favorites").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.GET, "/boards/search/{keyword}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/boards/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/categories").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/categories").hasAnyAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasAnyAuthority("ROLE_ADMIN")

            .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

            // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
            .and()
            .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}

package com.example.sns.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{
		security.csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(
						auth -> auth
						.requestMatchers("/api/v1/users/join").permitAll()
			            // 게시글 작성 임시 허용 (나중에는 로그인 사용자만 되도록 지워야 함)
			            .requestMatchers("/api/v1/posts/write").permitAll() 
			            // ⭐️ 게시글 목록 조회 허용 (추가된 부분) ⭐️
			            .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll() 
						.anyRequest()
						.authenticated()
					);
		
		return security.build();
	}
}

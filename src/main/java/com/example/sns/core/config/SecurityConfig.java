package com.example.sns.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.sns.core.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtUtil jwtUtil;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{
		security.csrf(AbstractHttpConfigurer::disable)
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.authorizeHttpRequests(
						auth -> auth
						.requestMatchers("/api/v1/users/**").permitAll()
			            // 게시글 작성 임시 허용 (나중에는 로그인 사용자만 되도록 지워야 함)
			            // .requestMatchers("/api/v1/posts/write").permitAll() 
			            // ⭐️ 게시글 목록 조회 허용 (추가된 부분) ⭐️
			            .requestMatchers("/api/v1/posts/**").permitAll() 
						.anyRequest()
						.authenticated()
					)
					// 방금 만든 필터를 기존 인증 필터(UsernamePasswordAuthenticationFilter) '앞'에 끼워 넣기
					.addFilterBefore(new JwtTokenFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		return security.build();
	}
}

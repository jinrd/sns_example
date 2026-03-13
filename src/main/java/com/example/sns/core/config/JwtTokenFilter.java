package com.example.sns.core.config;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.sns.core.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

			// 1. Header 에서 토큰 추출(Authorization: Bearer <token>
			final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

			// 2. 토큰이 존재하지 않거나 authorization 의 값이 Bearer 로 시작하지 않는 경우 로그인 하지 안한 상태로 간주
			if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
				log.info("토큰이 없거나 잘못된 형식입니다.");
				filterChain.doFilter(request, response);
				return;
			}

			// 3. 순수 토큰만 추출
			String token = authorizationHeader.split(" ")[1].trim();

			try{
				// 4. 토큰 만료 여부 확인
				if(jwtUtil.isExpired(token)) {
					log.error("토큰이 만료되었습니다.");
					filterChain.doFilter(request, response);
					return;
				}

				// 5. 토큰에서 사용자 아이디(username) 추출
				String username = jwtUtil.getUsername(token);

				// 6. 권한 부여 (이 사람이 누구인지 Spring Security 에 알려줌)
				// 프로젝트에 ROLE 을 부여하지 않았기 때문에 USER 로 고정
				UsernamePasswordAuthenticationToken authenticatoinToken =
					 new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")));

				// 디테일 설정
				authenticatoinToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// 7. SecurityContext 에 인증 객체 저장 (이 요청은 인증된 사용자로 취급됨)
				SecurityContextHolder.getContext().setAuthentication(authenticatoinToken);

				log.info("인증 성공 : {}", username);
			} catch (Exception e) {
				log.error("토큰 검증 중 오류 발생: {}",e.getMessage());
			}

			// 8. 다음 필터로 이동
			filterChain.doFilter(request, response);
	}
}

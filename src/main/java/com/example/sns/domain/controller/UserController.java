package com.example.sns.domain.controller;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sns.domain.request.vo.UserJoinRequestVo;
import com.example.sns.domain.request.vo.UserLoginRequestVo;
import com.example.sns.domain.response.vo.UserLoginResponseVo;
import com.example.sns.domain.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/join")
	public ResponseEntity<String> join(@Valid @RequestBody UserJoinRequestVo userVo) {
		userService.join(userVo);
		return ResponseEntity.ok("접속 성공");
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponseVo> login(@Valid @RequestBody UserLoginRequestVo userLoginRequestVo) {
		String token = userService.login(userLoginRequestVo);

		return ResponseEntity.ok(new UserLoginResponseVo(token));
	}
}

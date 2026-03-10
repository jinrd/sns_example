package com.example.sns.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.sns.domain.service.UserService;
import com.example.sns.domain.vo.UserVo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/api/v1/users/join")
	public ResponseEntity<String> join(@Valid @RequestBody UserVo userVo) {
		userService.join(userVo);
		return ResponseEntity.ok("접속 성공");
	}
}

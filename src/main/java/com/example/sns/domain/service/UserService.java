package com.example.sns.domain.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sns.core.db.entity.UserEntity;
import com.example.sns.core.db.repository.UserRepository;
import com.example.sns.domain.request.vo.UserRequestVo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional // 데이터 변경이 일어나는 작업에는 필수
    public void join(UserRequestVo userVo) {
        // 1. 아이디 중복 체크
        userRepository.findByUsername(userVo.getUsername())
                .ifPresent(user -> {
                    throw new RuntimeException("이미 존재하는 아이디입니다.");
                });

        // 2. UserVo를 UserEntity로 변환 (빌더 패턴 사용)
        UserEntity userEntity = UserEntity.builder()
                .username(userVo.getUsername())
                .password(bCryptPasswordEncoder.encode(userVo.getPassword())) // 비밀번호 암호화 필수!
                .build();

        // 3. DB 저장
        userRepository.save(userEntity);
    }
}

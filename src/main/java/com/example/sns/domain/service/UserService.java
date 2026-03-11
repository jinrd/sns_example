package com.example.sns.domain.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.core.db.entity.UserEntity;
import com.example.sns.core.db.repository.UserRepository;
import com.example.sns.core.util.JwtUtil;
import com.example.sns.domain.request.vo.UserJoinRequestVo;
import com.example.sns.domain.request.vo.UserLoginRequestVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
	
	@Transactional // 데이터 변경이 일어나는 작업에는 필수
    public void join(UserJoinRequestVo userVo) {
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

    @Transactional(readOnly = true)
    public String login(UserLoginRequestVo userLoginRequestVo) {
        // 아이디 존재 여부 확인
        UserEntity userEntity = userRepository.findByUsername(userLoginRequestVo.getUsername())
                                                .orElseThrow(() -> new RuntimeException("가입되지 않은 아이디입니다."));
        
        // user 가 존재한다면 비밀번호 일치 여부 확인
        boolean isPassword = bCryptPasswordEncoder.matches(userLoginRequestVo.getPassword(), userEntity.getPassword());

        if(!isPassword) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        // 비밀번호 검증 성공시 JwtUtil 사용해서 토큰 생성 후 반환
        return jwtUtil.createToken(userLoginRequestVo.getUsername());
    }
}

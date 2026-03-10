package com.example.sns.domain.service;

import org.springframework.stereotype.Service;

import com.example.sns.core.db.entity.PostEntity;
import com.example.sns.core.db.entity.UserEntity;
import com.example.sns.core.db.repository.PostRepository;
import com.example.sns.core.db.repository.UserRepository;
import com.example.sns.domain.vo.PostVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void write(PostVo postVo, String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                                    .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // 작성한 게시글 저장
        PostEntity postEntity = PostEntity.builder()
                                        .title(postVo.getTitle())
                                        .content(postVo.getContent())
                                        .user(userEntity)
                                        .build();

        postRepository.save(postEntity);


    }

}

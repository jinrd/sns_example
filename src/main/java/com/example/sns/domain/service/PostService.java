package com.example.sns.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.core.db.entity.PostEntity;
import com.example.sns.core.db.entity.UserEntity;
import com.example.sns.core.db.repository.PostRepository;
import com.example.sns.core.db.repository.UserRepository;
import com.example.sns.domain.request.vo.PostRequestVo;
import com.example.sns.domain.response.vo.PostResponseVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 저장
    public void write(PostRequestVo postVo, String username) {
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

    // 게시글 조회
    // @Transactional(readOnly = true)
    // public List<PostResponseVo> getPostList() {
    //     return postRepository.findAll().stream().map(PostResponseVo::new).toList();
    // }
    
    @Transactional(readOnly = true)
    public List<PostResponseVo> getPostList() {
        System.out.println("조회 시작");
        return postRepository.findAllWithUser().stream().limit(10).map(PostResponseVo::new).toList();
    }

}

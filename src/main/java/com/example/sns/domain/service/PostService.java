package com.example.sns.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.core.db.entity.PostEntity;
import com.example.sns.core.db.entity.UserEntity;
import com.example.sns.core.db.repository.PostRepository;
import com.example.sns.core.db.repository.UserRepository;
import com.example.sns.domain.request.vo.PostRequestVo;
import com.example.sns.domain.request.vo.PostUpdateRequestVo;
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
        System.out.println("전체 게시글 조회 시작");
        return postRepository.findAllWithUser().stream().limit(10).map(PostResponseVo::new).toList();
    }

    @Transactional(readOnly = true)
    public Page<PostResponseVo> getPostListPage(Pageable pageable) {
        System.out.println("페이지 기반 조회 시작");
        
        return postRepository.findPageWithUser(pageable).map(PostResponseVo::new);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseVo> getPostListPageByKeywords(String keyword, Pageable pageable) {
        System.out.println("키워드 및 페이지 기반 조회 시작");
        Page<PostEntity> postPage;

        // 검색어가 없으면 전체 조회
        if(keyword == null || keyword.trim().isEmpty()) {
            postPage = postRepository.findPageWithUser(pageable);
        } else {
            postPage = postRepository.searchByKeyword(keyword, pageable);   
        }

        return postPage.map(PostResponseVo::new);
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequestVo postUpdateRequestVo, String currentUsername) {
        System.out.println("게시글 수정 시작");

        // 수정을 위한 게시글이 존재하지 않는 경우
        PostEntity postEntity = postRepository.findById(postId)
                                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 현재 로그인한 사람과 게시글 작성자 일치 여부 확인
        if(!postEntity.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        //데이터 수정 (Dirty Checking 덕분에 save()를 호출하지 않아도 됨!)
        postEntity.update(postUpdateRequestVo.getTitle(), postUpdateRequestVo.getContent());

    }

    @Transactional
    public void deletePost(Long postId, String currentUsername) {
        System.out.println("게시글 삭제 시작");
        
        // 삭제 가능 게시글이 존재하지 않는 경우
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 현재 로그인한 사람과 게시글 작성자 일치 여부 확인
        if (!postEntity.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 데이터 삭제
        postRepository.delete(postEntity);
    }

}
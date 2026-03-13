package com.example.sns.domain.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sns.domain.request.vo.PostRequestVo;
import com.example.sns.domain.request.vo.PostUpdateRequestVo;
import com.example.sns.domain.response.vo.PostResponseVo;
import com.example.sns.domain.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    
    @PostMapping("/write")
    public ResponseEntity<String> write(@Valid @RequestBody PostRequestVo postVo, Authentication authentication) {

        // 1. Spring Security 로 변경하여 username 은 인증된 사용자의 이름을 가져온다.
        // JwtTokenFilter 에서 UsernamePasswordAuthenticationToken의 첫 번째 파라미터(Principal)로 username을 넣었으므로, getName()으로 가져올 수 있습니다.
        
        postService.write(postVo, authentication.getName());
        return ResponseEntity.ok("게시글 작성 완료");
    }

    
    @GetMapping("/list")    // 게시글 리스트 전체 조회
    public ResponseEntity<List<PostResponseVo>> list() {
        return ResponseEntity.ok(postService.getPostList());
    }

    @GetMapping("/page/list")
    public ResponseEntity<Page<PostResponseVo>> listWithPage(
        @PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(postService.getPostListPage(pageable));
    }

    @GetMapping("/page/keyword/list")
    public ResponseEntity<Page<PostResponseVo>> listWithKeywordPage(
        @RequestParam(required = false) String keyword,
        @PageableDefault(size = 10, sort ="id", direction = Direction.ASC) Pageable pageable
    ){
        return ResponseEntity.ok(postService.getPostListPageByKeywords(keyword, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(
        @PathVariable("id") Long postId,
        @Valid @RequestBody PostUpdateRequestVo postUpdateRequestVo
        , Authentication authentication) {

            // 토큰에서 현재 접속 사용자 가져오기
            postService.updatePost(postId, postUpdateRequestVo, authentication.getName());
            return ResponseEntity.ok("수정 성공했습니다");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
        @PathVariable("id") Long id,
        Authentication authentication
    ) {
        postService.deletePost(id, authentication.getName());
        return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
    }
}

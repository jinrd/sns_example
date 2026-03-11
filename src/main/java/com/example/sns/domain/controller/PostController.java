package com.example.sns.domain.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sns.domain.request.vo.PostRequestVo;
import com.example.sns.domain.response.vo.PostResponseVo;
import com.example.sns.domain.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    
    @PostMapping("/write")
    public ResponseEntity<String> write(@Valid @RequestBody PostRequestVo postVo, @RequestParam String username) {

        postService.write(postVo, username);
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
}

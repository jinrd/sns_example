package com.example.sns.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sns.domain.request.vo.PostRequestVo;
import com.example.sns.domain.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
}

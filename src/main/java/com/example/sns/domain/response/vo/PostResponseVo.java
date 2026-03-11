package com.example.sns.domain.response.vo;

import com.example.sns.core.db.entity.PostEntity;

import lombok.Getter;

@Getter
public class PostResponseVo {
    private Long id;
    private String title;
    private String content;
    private String username;    // 게시글 작성자 이름

    public PostResponseVo(PostEntity post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername(); // 연관관계 활용, JPA 에 의해 select 쿼리가 1번더 나감
    }
}

package com.example.sns.core.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sns.core.db.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long>{
    
    // list 조회를 위해 findAll 을 사용했을 경우 N + 1 문제를 해결하기 위해
    @Query("SELECT p FROM PostEntity p JOIN FETCH p.user")
    List<PostEntity> findAllWithUser();

    // 페이지 조회
    @Query(
        value = "SELECT p FROM PostEntity p JOIN FETCH p.user",
        countQuery = "SELECT COUNT(p) FROM PostEntity p")
    Page<PostEntity> findPageWithUser(Pageable pageable);


    // 제목이나 내용에 키워드가 포함된 게시글 검색(FETCH JOIN 적용)
    @Query(value = "SELECT p FROM PostEntity p JOIN FETCH p.user "
                    + "WHERE p.title like %:keyword% OR p.content LIKE %:keyword%",
            countQuery = "SELECT COUNT(p) FROM PostEntity p "
                    + "WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%"
    )
    Page<PostEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);


}

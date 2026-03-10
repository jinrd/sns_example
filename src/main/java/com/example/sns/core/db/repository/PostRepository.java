package com.example.sns.core.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sns.core.db.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long>{
    
}

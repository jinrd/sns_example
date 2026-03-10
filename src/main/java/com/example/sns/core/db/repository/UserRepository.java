package com.example.sns.core.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sns.core.db.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	Optional<UserEntity> findByUsername(String username);
}

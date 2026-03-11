package com.example.sns.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.sns.core.db.entity.UserEntity;
import com.example.sns.core.db.insert.BulkInsertService;
import com.example.sns.core.db.repository.UserRepository;

@Configuration
public class DataInitialize {
	
	@Bean
	public CommandLineRunner initData(BulkInsertService bulkInsertService, UserRepository userRepository) {
		return args -> {
			System.out.println("10만 데이터 INSERT");
			UserEntity user = userRepository.findByUsername("test")
								.orElseGet(() -> userRepository.save( UserEntity.builder()
																				.username("tester")
																				.password("1234")
																				.build() ));


			bulkInsertService.bulkInsertPosts(user.getId());
		};
	}
}

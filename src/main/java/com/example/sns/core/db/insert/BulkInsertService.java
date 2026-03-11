package com.example.sns.core.db.insert;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.core.db.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BulkInsertService {

	private final JdbcTemplate jdbcTemplate;
	private final PostRepository postRepository;
	
	@Transactional
	public void bulkInsertPosts(Long userId) {
		Long postCount = postRepository.count();
		System.out.println("현재 게시물 개수 : " + postCount);
		if(postCount == 0) {
			String sql = "INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?)";
	    
			jdbcTemplate.batchUpdate(sql, new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
				@Override
				public void setValues(java.sql.PreparedStatement ps, int i) throws java.sql.SQLException {
					ps.setString(1, "Title " + i);
					ps.setString(2, "Content " + i);
					ps.setLong(3, userId);
				}
				@Override
				public int getBatchSize() {
					return 100000; // 한 번에 10만 건 요청
				}
			});
			System.out.println("데이터 10만 건 삽입 완료@");
		}

	    
	}
}

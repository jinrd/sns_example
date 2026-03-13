package com.example.sns.core.db.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass // JPA 엔티티들이 이 클래스를 상속할 경우, 아래 필드들도 컬럼으로 인식하도록 합니다.
@EntityListeners(AuditingEntityListener.class) // 이 엔티티의 변화(생성/수정)를 감시합니다.
public abstract class BaseEntity {

    @CreatedDate // 데이터 생성 시 시간 자동 저장
    @Column(updatable = false) // 한 번 생성되면 수정 불가
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터 수정 시 시간 자동 갱신
    private LocalDateTime updatedAt;
}

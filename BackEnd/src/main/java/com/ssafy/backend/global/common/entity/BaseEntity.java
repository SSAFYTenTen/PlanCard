package com.ssafy.backend.global.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 공통 엔티티 베이스 클래스
 * 모든 엔티티는 이 클래스를 상속받아 생성 및 수정 시간을 자동으로 관리합니다.
 * AuditingEntityListener를 통해 엔티티의 생명주기 이벤트를 감지하고,
 * 생성 및 수정 시간을 자동으로 설정합니다.
 */
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    /**
     * 엔티티 생성 시간
     * 'created_at' 컬럼에 매핑되며, 엔티티가 생성될 때 현재 시간으로 자동 설정됩니다.
     * 이 필드는 업데이트 되지 않습니다. (updatable = false).
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티 최종 수정 시간
     * 'updated_at' 컬럼에 매핑되며, 엔티티가 수정될 때 현재 시간으로 자동 설정됩니다.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

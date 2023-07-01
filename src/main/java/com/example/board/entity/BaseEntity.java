package com.example.board.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//시간 정보는 따로 Entity 생성
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp
    @Column(updatable = false) //업데이트할 때 관여 x
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(insertable = false) //insert할 때 관여 x
    private LocalDateTime updatedTime;
}

package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

    @CreatedDate //JPA에서 엔티티의 생성시간을 처리
    @Column(name = "regdate",updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate //최종 수정시간을 자동으로 처리하는 용도
    @Column(name="moddate")
    private LocalDateTime modDate;
}

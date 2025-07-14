package com.github.bruce_mig.customer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OutboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Builder.Default
    private Boolean sent = Boolean.FALSE;

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedBy
    private LocalDateTime latUpdate;

    public void delivered(){
        this.sent = Boolean.TRUE;
    }

}

package ru.openai.gpt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id"})
@MappedSuperclass
@SuperBuilder
@Data
@NoArgsConstructor
public class BaseEntity {
    @Id
    @UuidGenerator
    private UUID id;

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }
}

package ru.openai.gpt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "telegram_id")
    private Long telegramId;
    @Column(name = "thread_id")
    private String threadId;

    @ToString.Exclude
    @OneToMany(targetEntity = MessageEntity.class, mappedBy = "user",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MessageEntity> messages = new ArrayList<>();
}

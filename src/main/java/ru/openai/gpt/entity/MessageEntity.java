package ru.openai.gpt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode
@ToString
@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "messages")
public class MessageEntity extends BaseEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private UserEntity user;

    @Column(name = "request")
    private String request;
    @Column(name = "telegram_message_id")
    private Integer telegramMessageId;
    @Column(name = "response")
    private String response;
}

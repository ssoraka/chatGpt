package ru.openai.gpt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private List<MessageEntity> messages;

    public void addMessage(MessageEntity message) {
        if (Objects.isNull(messages)) {
            messages = new ArrayList<>();
        }
        message.setUser(this);
        messages.add(message);
    }
}

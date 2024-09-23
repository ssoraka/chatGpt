package ru.openai.gpt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;
import ru.openai.gpt.enums.Action;

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
    @JoinColumn(name = "current_thread_id")
    @OneToOne(targetEntity = ThreadEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ThreadEntity currentThread;
    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;
    @Column(name = "file_id")
    private String fileId;
    @Column(name = "profile")
    private String profile;


    @ToString.Exclude
    @OneToMany(targetEntity = MessageEntity.class, mappedBy = "user",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MessageEntity> messages;

    @ToString.Exclude
    @OneToMany(targetEntity = ThreadEntity.class, mappedBy = "sender",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ThreadEntity> fromMe;

    @ToString.Exclude
    @OneToMany(targetEntity = ThreadEntity.class, mappedBy = "receiver",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ThreadEntity> toMe;

    public void addMessage(MessageEntity message) {
        if (Objects.isNull(messages)) {
            messages = new ArrayList<>();
        }
        message.setUser(this);
        messages.add(message);
    }
}

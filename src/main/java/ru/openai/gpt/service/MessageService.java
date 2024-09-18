package ru.openai.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.entity.MessageEntity;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    @Transactional
    public MessageEntity create(UserEntity user, String request, Integer telegramMessageId) {
        MessageEntity message = messageRepository.save(MessageEntity.builder()
                .user(user)
                .request(request)
                .telegramMessageId(telegramMessageId)
                .build());
        user.getMessages().add(message);
        return message;
    }
}

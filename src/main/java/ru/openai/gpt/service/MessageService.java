package ru.openai.gpt.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.entity.MessageEntity;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.repository.MessageRepository;
import ru.openai.gpt.repository.UserRepository;

import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageEntity create(String request, Integer telegramMessageId) {
        return messageRepository.save(MessageEntity.builder()
                .request(request)
                .telegramMessageId(telegramMessageId)
                .build());
    }
}

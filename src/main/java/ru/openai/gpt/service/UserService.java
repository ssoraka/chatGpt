package ru.openai.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity findOrCreateByTelegramIdAndName(Long telegramId, String name) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> userRepository.save(UserEntity.builder()
                        .name(name)
                        .telegramId(telegramId)
                        .build()));
    }

    @Transactional
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}

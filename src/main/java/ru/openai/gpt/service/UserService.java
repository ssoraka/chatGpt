package ru.openai.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.enums.Action;
import ru.openai.gpt.repository.UserRepository;

import java.util.List;
import java.util.UUID;

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
                        .action(Action.NONE)
                        .build()));
    }

    @Transactional
    public UserEntity findByIdOrThrowNotFound(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найден пользователь " + id));
    }

    @Transactional
    public UserEntity findByTelegramIdOrThrowNotFound(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("Не найден пользователь c telegram " + telegramId));
    }

    @Transactional
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Transactional
    public List<UserEntity> findAllWithProfile() {
        return userRepository.findAllByProfileNotNull();
    }
}

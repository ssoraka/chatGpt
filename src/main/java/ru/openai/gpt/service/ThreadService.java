package ru.openai.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.entity.ThreadEntity;
import ru.openai.gpt.repository.ThreadRepository;

@Service
@RequiredArgsConstructor
public class ThreadService {
    private final ThreadRepository threadRepository;

    @Transactional
    public ThreadEntity save(ThreadEntity thread) {
        return threadRepository.save(thread);
    }
}

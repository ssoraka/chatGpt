package ru.openai.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openai.gpt.entity.MessageEntity;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
}

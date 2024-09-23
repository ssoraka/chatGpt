package ru.openai.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openai.gpt.entity.ThreadEntity;

import java.util.UUID;

@Repository
public interface ThreadRepository extends JpaRepository<ThreadEntity, UUID> {
}

package ru.openai.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openai.gpt.entity.RecordEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, UUID> {

    List<RecordEntity> findAllBySellerId(UUID sellerId);
    List<RecordEntity> findAllByBuyerId(UUID buyerId);
    RecordEntity findAllByBuyerIdAndDateTime(UUID buyerId, LocalDateTime dateTime);
}

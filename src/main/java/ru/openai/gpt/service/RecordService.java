package ru.openai.gpt.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.entity.RecordEntity;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.repository.RecordRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserService userService;

    @Transactional
    public RecordEntity create(Long telegramId, LocalDateTime date) {
        UserEntity buyer = userService.findOrCreateByTelegramIdAndName(telegramId, null);
        UserEntity seller = buyer.getCurrentThread().getSender();
        return recordRepository.save(RecordEntity.builder()
                .dateTime(date)
                .buyer(buyer)
                .seller(seller)
                .build());
    }

    @Transactional
    public void delete(Long telegramId, LocalDateTime date) {
        UserEntity buyer = userService.findOrCreateByTelegramIdAndName(telegramId, null);
        RecordEntity record = recordRepository.findAllByBuyerIdAndDateTime(buyer.getId(), date);
        recordRepository.delete(record);
    }

    @Transactional
    public List<LocalDateTime> getRecordsTo(Long telegramId) {
        UserEntity buyer = userService.findOrCreateByTelegramIdAndName(telegramId, null);
        UserEntity seller = buyer.getCurrentThread().getSender();
        return recordRepository.findAllBySellerId(seller.getId()).stream()
                .map(RecordEntity::getDateTime)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Pair<LocalDateTime, String>> getRecords(Long telegramId) {
        UserEntity buyer = userService.findOrCreateByTelegramIdAndName(telegramId, null);
        return recordRepository.findAllBySellerId(buyer.getId()).stream()
                .sorted(Comparator.comparing(RecordEntity::getDateTime))
                .map(e -> Pair.of(e.getDateTime(), e.getSeller().getName()))
                .collect(Collectors.toList());
    }
}

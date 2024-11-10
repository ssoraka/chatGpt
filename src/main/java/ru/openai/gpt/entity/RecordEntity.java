package ru.openai.gpt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "records")
public class RecordEntity extends BaseEntity {

    @JoinColumn(name = "seller_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private UserEntity seller;

    @JoinColumn(name = "buyer_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private UserEntity buyer;

    @Column(name = "date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;
}

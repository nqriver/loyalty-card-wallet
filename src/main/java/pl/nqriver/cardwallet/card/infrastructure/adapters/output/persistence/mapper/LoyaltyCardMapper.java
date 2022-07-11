package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.domain.*;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoyaltyCardMapper {

    private final ActivityMapper activityMapper;

    public LoyaltyCard mapToDomain(
            LoyaltyCardEntity loyaltyCardEntity,
            List<ActivityEntity> activities,
            Long withdrawalBalance,
            Long depositBalance
    ) {
        return LoyaltyCard.withId(
                LoyaltyCardId.of(loyaltyCardEntity.getId()),
                Balance.of(Points.of(withdrawalBalance), Points.of(depositBalance)),
                activityMapper.mapToActivityWindow(activities),
                Holder.of(loyaltyCardEntity.getHolderEmail()),
                loyaltyCardEntity.getCreatedAt(),
                loyaltyCardEntity.getExpiresAt()
        );
    }

    public LoyaltyCard mapToDomainWithAllActivities(LoyaltyCardEntity loyaltyCardEntity, List<ActivityEntity> activities) {
        return this.mapToDomain(loyaltyCardEntity, activities, 0L, 0L);
    }


    public LoyaltyCardEntity mapToEntity(CreateCardCommand command,
                                         LocalDateTime createdAt,
                                         LocalDateTime expiresAt) {
        LoyaltyCardEntity entity = new LoyaltyCardEntity();
        entity.setCreatedAt(createdAt);
        entity.setExpiresAt(expiresAt);
        entity.setHolderEmail(command.getHolderEmail());
        return entity;
    }
}

package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardActivityWindowQuery;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.mapper.ActivityRestMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ActivityResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ActivityFacade {

    private final GetCardActivityWindowQuery getCardActivityWindowQuery;
    private final ActivityRestMapper activityRestMapper;

    List<ActivityResponse> getActivities(final Long loyaltyCardId, Optional<LocalDateTime> since, Optional<LocalDateTime> until) {

        var sinceDate = since.orElse(LocalDateTime.now().minusYears(1));
        var untilDate = until.orElse(LocalDateTime.now());

        return activityRestMapper.toActivityResponseList(
                getCardActivityWindowQuery.getActivitiesOfCardForPeriod(
                        LoyaltyCardId.of(loyaltyCardId), sinceDate, untilDate));
    }
}

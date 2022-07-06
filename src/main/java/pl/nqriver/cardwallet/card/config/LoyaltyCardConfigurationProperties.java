package pl.nqriver.cardwallet.card.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "loyaltycard")
public class LoyaltyCardConfigurationProperties {

    private Long validityPeriod = Long.valueOf(2);
}

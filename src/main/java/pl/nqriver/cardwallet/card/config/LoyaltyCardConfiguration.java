package pl.nqriver.cardwallet.card.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.nqriver.cardwallet.card.application.service.LoyaltyCardProperties;

@Configuration
@EnableConfigurationProperties(LoyaltyCardConfigurationProperties.class)
public class LoyaltyCardConfiguration {

    @Bean
    public LoyaltyCardProperties cardProperties(LoyaltyCardConfigurationProperties configurationProperties) {
        return new LoyaltyCardProperties(configurationProperties.getValidityPeriod());
    }
}

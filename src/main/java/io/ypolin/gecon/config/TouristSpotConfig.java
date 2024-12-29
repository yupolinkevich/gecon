package io.ypolin.gecon.config;

import io.ypolin.gecon.ai.ContentGenerator;
import io.ypolin.gecon.ai.domain.PlaceToVisit;
import io.ypolin.gecon.conversion.AIResponseToTouristSpotConverter;
import io.ypolin.gecon.service.ContentPopulator;
import io.ypolin.gecon.service.TouristSpotService;
import io.ypolin.gecon.ai.TouristSpotsConfigurer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class TouristSpotConfig {
    @Autowired
    private TouristSpotService touristSpotService;

    @Bean
    public ConversionService touristConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new AIResponseToTouristSpotConverter());
        return conversionService;
    }

    @Bean
    public ContentGenerator touristSpotsGenerator(ChatClient.Builder chatClientBuilder){
        ContentGenerator<PlaceToVisit> contentGenerator = new ContentGenerator<>(chatClientBuilder, new TouristSpotsConfigurer());
        return contentGenerator;
    }
    @Bean
    public ContentPopulator touristSpotsPopulator(ContentGenerator touristSpotsGenerator, ConversionService touristConversionService) {
        ContentPopulator contentPopulator = new ContentPopulator(touristSpotsGenerator, touristSpotService, touristConversionService);
        return contentPopulator;
    }
}

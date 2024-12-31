package io.ypolin.gecon.demo.config;

import io.ypolin.gecon.ai.ContentGenerator;
import io.ypolin.gecon.demo.ai.TouristSpotsConfigurer;
import io.ypolin.gecon.demo.ai.domain.PlaceToVisit;
import io.ypolin.gecon.demo.conversion.AIGeneratedToTouristSpot;
import io.ypolin.gecon.demo.service.TouristSpotService;
import io.ypolin.gecon.service.AsyncContentPopulator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class TouristSpotConfig {

    @Bean
    public ConversionService touristConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new AIGeneratedToTouristSpot());
        return conversionService;
    }

    @Bean
    public ContentGenerator touristSpotsGenerator(ChatClient.Builder chatClientBuilder){
        ContentGenerator<PlaceToVisit> contentGenerator = new ContentGenerator<>(chatClientBuilder, new TouristSpotsConfigurer());
        return contentGenerator;
    }
    @Bean
    public AsyncContentPopulator touristSpotsPopulator(ContentGenerator touristSpotsGenerator, ConversionService touristConversionService) {
        AsyncContentPopulator asyncContentPopulator = new AsyncContentPopulator(touristSpotsGenerator, touristConversionService);
        return asyncContentPopulator;
    }
}

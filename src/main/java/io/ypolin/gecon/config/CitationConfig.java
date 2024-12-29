package io.ypolin.gecon.config;

import io.ypolin.gecon.ai.domain.CitationItem;
import io.ypolin.gecon.ai.ContentGenerator;
import io.ypolin.gecon.conversion.AIGeneratedToCitation;
import io.ypolin.gecon.ai.CitationGeneratorConfigurer;
import io.ypolin.gecon.service.CitationService;
import io.ypolin.gecon.service.ContentPopulator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;

@Configuration
public class CitationConfig {
    @Value("classpath:prompts/citation_prompt.txt")
    private Resource citationPromptResource;
    @Autowired
    private CitationService citationService;

    @Bean
    public ConversionService citationConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new AIGeneratedToCitation());
        return conversionService;
    }
    @Bean
    public ContentGenerator citationGenerator(ChatClient.Builder chatClientBuilder){
        ContentGenerator<CitationItem> contentGenerator = new ContentGenerator<>(chatClientBuilder, new CitationGeneratorConfigurer(citationPromptResource));
        return contentGenerator;
    }
    @Bean
    public ContentPopulator citationPopulator(ContentGenerator citationGenerator, ConversionService citationConversionService) {
        ContentPopulator contentPopulator = new ContentPopulator(citationGenerator, citationService, citationConversionService);
        return contentPopulator;
    }
}

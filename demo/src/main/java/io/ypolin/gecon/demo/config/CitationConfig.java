package io.ypolin.gecon.demo.config;

import io.ypolin.gecon.ai.ContentGenerator;
import io.ypolin.gecon.demo.ai.CitationGeneratorConfigurer;
import io.ypolin.gecon.demo.ai.domain.CitationItem;
import io.ypolin.gecon.demo.conversion.AIGeneratedToCitation;
import io.ypolin.gecon.demo.conversion.CitationToDTOConverter;
import io.ypolin.gecon.demo.service.CitationService;
import io.ypolin.gecon.service.AsyncContentPopulator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
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

    @Bean
    public ConversionService citationConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new AIGeneratedToCitation());
        conversionService.addConverter(new CitationToDTOConverter());
        return conversionService;
    }
    @Bean
    public ContentGenerator citationGenerator(ChatClient.Builder chatClientBuilder){
        ContentGenerator<CitationItem> contentGenerator = new ContentGenerator<>(chatClientBuilder, new CitationGeneratorConfigurer(citationPromptResource));
        return contentGenerator;
    }
    @Bean
    public AsyncContentPopulator citationPopulator(ContentGenerator citationGenerator, ConversionService citationConversionService) {
        AsyncContentPopulator asyncContentPopulator = new AsyncContentPopulator(citationGenerator, citationConversionService);
        return asyncContentPopulator;
    }
}

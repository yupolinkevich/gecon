package io.ypolin.gecon.service;

import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import io.ypolin.gecon.ai.ContentGenerator;
import io.ypolin.gecon.mongo.domain.ChecksumAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public class ContentPopulator<AIGenerated> {
    private ContentGenerator<AIGenerated> contentGenerator;
    private ContentManager contentManager;
    private ConversionService conversionService;

    public ContentPopulator(ContentGenerator<AIGenerated> contentGenerator, ContentManager contentManager, ConversionService conversionService) {
        this.contentGenerator = contentGenerator;
        this.contentManager = contentManager;
        this.conversionService = conversionService;
    }

    @Async
    public void populateWithContent(String about, int numberOfItems) {

        CompletableFuture.supplyAsync(() -> generateContentPhase(about, numberOfItems))
                .thenAccept(generatedContent -> storePhase(about, generatedContent))
                .handle((result, throwable) -> {
                    if (throwable != null) {
                        log.error("Error occurred when trying to process new content.", throwable);
                    }
                    return result;
                });
    }

    private List<ChecksumAware> generateContentPhase(String about, int numberOfItems) {
        log.info("Content Generation Phase");
        log.info("Requesting new content about [{}] from AI", about);
        MultiItemGeneratedResponse<AIGenerated> multiItemGeneratedResponse = contentGenerator.generateContent(about, numberOfItems);
        if(multiItemGeneratedResponse.getItems().isEmpty()){
            throw new IllegalStateException("some");
        }
        log.info("Finish content generation");
        log.info("Converting generated content to list of trackable documents");
        List<ChecksumAware> items = multiItemGeneratedResponse.getItems().stream()
                .map(aiGenerated -> conversionService.convert(aiGenerated, ChecksumAware.class))
                .collect(Collectors.toList());
        return items;
    }
    private void storePhase(String about, List<ChecksumAware> generatedItems){
        log.info("Content Saving Phase");
        int storedItemsCount = contentManager.storeUniqueContent(generatedItems, about);
        log.info("New {} items were stored to db.", storedItemsCount);
    }
}

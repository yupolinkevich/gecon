package io.ypolin.gecon.service;

import io.ypolin.gecon.ai.ContentGenerator;
import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import io.ypolin.gecon.service.checksum.ChecksumAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class AsyncContentPopulator {
    private ContentGenerator<?> contentGenerator;
    private ConversionService conversionService;

    public AsyncContentPopulator(ContentGenerator<?> contentGenerator, ConversionService conversionService) {
        this.contentGenerator = contentGenerator;
        this.conversionService = conversionService;
    }

    @Async
    public <T> void generateContent(String about, int numberOfItems, Class<T> targetType, Consumer<List<?>> processor) {
        CompletableFuture<List<T>> generateAndConvertAsync = generateAndConvertAsync(about, numberOfItems, targetType);
        generateAndConvertAsync.thenAcceptAsync(generatedItems -> {
                    if (processor != null) {
                        processor.accept(generatedItems);
                        log.info("Successfully processed {} generated items.", generatedItems.size());
                    }
                })
                .whenComplete(this::exceptionHandler);
    }

    @Async
    public <T extends ChecksumAware> void generateUniqueContent(String about, int numberOfItems, Class<T> targetType, DeduplicationService<T> deduplicationService, Consumer<List<T>> contentProcessor) {
        CompletableFuture<List<T>> generateAndConvertAsync = generateAndConvertAsync(about, numberOfItems, targetType);
        generateAndConvertAsync
                .thenAcceptAsync(generatedItems -> {
                    List<T> itemsToProcess = generatedItems;
                    if (deduplicationService != null) {
                        log.info("Removing repeated items");
                        itemsToProcess = deduplicationService.removeRepeatedContent(generatedItems, about);
                    }
                    if (contentProcessor != null) {
                        contentProcessor.accept(itemsToProcess);
                        log.info("Successfully processed {} generated items.", generatedItems.size());
                    }
                })
                .whenComplete(this::exceptionHandler);
    }

    private <T> CompletableFuture<List<T>> generateAndConvertAsync(String about, int numberOfItems, Class<T> targetType) {
        return CompletableFuture.supplyAsync(() -> generateStep(about, numberOfItems))
                .thenApply(generatedContent -> {
                    List<T> transformedItems = generatedContent.getItems()
                            .stream()
                            .map(aiGenerated -> conversionService.convert(aiGenerated, targetType))
                            .collect(Collectors.toList());
                    return transformedItems;
                });
    }

    private void exceptionHandler(Object result, Throwable throwable) {
        if (throwable != null) {
            log.error("Error occurred when trying to process generated content.", throwable);
        }
    }

    private MultiItemGeneratedResponse<?> generateStep(String about, int numberOfItems) {
        log.info("Requesting new content about [{}] from AI", about);
        MultiItemGeneratedResponse<?> multiItemGeneratedResponse = contentGenerator.generateContent(about, numberOfItems);
        if (multiItemGeneratedResponse.getItems().isEmpty()) {
            throw new IllegalStateException("AI-generated response is empty");
        }
        log.info("Finish content generation");
        return multiItemGeneratedResponse;
    }

}

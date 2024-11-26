package io.ypolin.gecon.ai;

import lombok.extern.java.Log;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Log
public class CitationAIProvider {
    private final ChatClient chatClient;

    @Value("${gecon.numberOfItemsPerResponse}")
    private int numberOfItems;

    @Value("classpath:prompts/citation_prompt.txt")
    private Resource systemPromptResource;

    public CitationAIProvider(ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder.build();
    }

    @Async
    public CompletableFuture<CitationAIResponse> getCitations(String about) {
        log.info("Start content generation from Open AI");
        CitationAIResponse citationAIResponse = chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(systemSpec -> systemSpec.text(systemPromptResource).params(
                        Map.of("itemsNumber", numberOfItems,
                                "randomPart", System.currentTimeMillis())))
                .user(about)
                .call()
                .entity(CitationAIResponse.class);
        log.info("Finish content generation from Open AI");

        return CompletableFuture.completedFuture(citationAIResponse);
    }
}

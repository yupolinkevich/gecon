package io.ypolin.gecon.ai;

import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

import java.time.LocalDateTime;

public class ContentGenerator<Content>{

    private final ChatClient chatClient;
    private ContentGeneratorConfigurer<Content> configurer;

    public ContentGenerator(ChatClient.Builder chatClientBuilder, ContentGeneratorConfigurer<Content> configurer) {

        this.chatClient = chatClientBuilder.build();
        this.configurer = configurer;
    }

    public MultiItemGeneratedResponse<Content> generateContent(String about, int numberOfItems) {
        MultiItemGeneratedResponse<Content> response = chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(systemSpec -> systemSpec
                        .text(adjustSystemMessage(numberOfItems)))


                .user(about)
                .call()
                .entity(configurer.getTypeReference());
        return response;
    }

    private String adjustSystemMessage(int numberOfItems) {
        String systemMessage = configurer.defineSystemPrompt();
        if (systemMessage == null) {
            throw new IllegalStateException("System message to AI is not defined.");
        }
        StringBuilder sb  = new StringBuilder(systemMessage);
        sb.append(String.format("You should provide %d items.", numberOfItems));
        sb.append("As of" + LocalDateTime.now());
        return sb.toString();
    }

}

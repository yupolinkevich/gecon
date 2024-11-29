package io.ypolin.gecon.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class ContentGenerator<Content> {

    private final ChatClient chatClient;

    public ContentGenerator(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Content generateContent(String about) {
        Content result = chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(systemSpec -> systemSpec
                        .text(randomizeSystemMessage())
                        .params(systemMessageTemplateParams()))
                .user(about)
                .call()
                .entity(getDataType());
        return result;
    }

    private String randomizeSystemMessage() {
        String systemMessage = systemMessage();
        if (systemMessage == null) {
            throw new IllegalStateException("System message to AI is not defined");
        }
        return systemMessage.concat("As of" + LocalDateTime.now());
    }

    public abstract String systemMessage();

    public abstract Map<String, Object> systemMessageTemplateParams();

    public abstract Class<Content> getDataType();

}

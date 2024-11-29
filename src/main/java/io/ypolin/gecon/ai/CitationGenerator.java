package io.ypolin.gecon.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@Service
public class CitationGenerator extends ContentGenerator<CitationAIResponse> {

    @Value("${gecon.numberOfItemsPerResponse}")
    private int numberOfItems;

    @Value("classpath:prompts/citation_prompt.txt")
    private Resource systemPromptResource;

    public CitationGenerator(ChatClient.Builder chatClientBuilder) {
        super(chatClientBuilder);
    }

    @Override
    public String systemMessage() {
        try {
            return systemPromptResource.getContentAsString(Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when reading system prompt resource " + systemPromptResource.getFilename(), e);
        }
    }

    @Override
    public Map<String, Object> systemMessageTemplateParams() {
        return Map.of("itemsNumber", numberOfItems);
    }

    @Override
    public Class<CitationAIResponse> getDataType() {
        return CitationAIResponse.class;
    }
}

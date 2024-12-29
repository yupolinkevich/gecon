package io.ypolin.gecon.ai;

import io.ypolin.gecon.ai.domain.CitationItem;
import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;

public class CitationGeneratorConfigurer implements ContentGeneratorConfigurer<CitationItem> {
    private Resource citationPromptResource;

    public CitationGeneratorConfigurer(Resource citationPromptResource) {
        this.citationPromptResource = citationPromptResource;
    }

    @Override
    public String defineSystemPrompt() {
        try {
            return citationPromptResource.getContentAsString(Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when reading system prompt resource " + citationPromptResource.getFilename(), e);
        }
    }

    @Override
    public Class<CitationItem> getContentType() {
        return CitationItem.class;
    }

    @Override
    public ParameterizedTypeReference<MultiItemGeneratedResponse<CitationItem>> getTypeReference() {
        return new ParameterizedTypeReference<MultiItemGeneratedResponse<CitationItem>>() {
        };
    }
}

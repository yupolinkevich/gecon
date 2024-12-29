package io.ypolin.gecon.ai;

import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import org.springframework.core.ParameterizedTypeReference;

public interface ContentGeneratorConfigurer<Content>{
    String defineSystemPrompt();
    Class<Content> getContentType();

    ParameterizedTypeReference<MultiItemGeneratedResponse<Content>> getTypeReference();


}

package io.ypolin.gecon.ai;

import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import io.ypolin.gecon.ai.domain.PlaceToVisit;
import org.springframework.core.ParameterizedTypeReference;

public class TouristSpotsConfigurer implements ContentGeneratorConfigurer<PlaceToVisit> {
    @Override
    public String defineSystemPrompt() {
        return "You will be provided with a city name. Generate list of popular places to visit for the tourists.\n" +
                "Consider that they visit this city for the first time.";
    }

    @Override
    public Class<PlaceToVisit> getContentType() {
        return PlaceToVisit.class;
    }

    @Override
    public ParameterizedTypeReference<MultiItemGeneratedResponse<PlaceToVisit>> getTypeReference() {
        return new ParameterizedTypeReference<MultiItemGeneratedResponse<PlaceToVisit>>() {
        };
    }
}

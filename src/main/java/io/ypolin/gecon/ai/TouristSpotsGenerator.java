package io.ypolin.gecon.ai;

import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import io.ypolin.gecon.ai.domain.PlaceToVisit;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;
public class TouristSpotsGenerator {



    public ParameterizedTypeReference<MultiItemGeneratedResponse<PlaceToVisit>> getTypeReference() {
        return new ParameterizedTypeReference<MultiItemGeneratedResponse<PlaceToVisit>>() {
        };
    }

    public String systemMessage() {
        return "You will be provided with a city name. Generate list of popular places to visit for the tourists.\n" +
                "Consider that they visit this city for the first time.";
    }

    public Map<String, Object> systemMessageTemplateParams() {
        return new HashMap<>();
    }
}

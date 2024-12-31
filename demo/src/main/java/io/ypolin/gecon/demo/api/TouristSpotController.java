package io.ypolin.gecon.demo.api;

import io.ypolin.gecon.demo.ai.domain.CitationItem;
import io.ypolin.gecon.demo.conversion.AIGeneratedToCitation;
import io.ypolin.gecon.demo.dao.domain.TouristSpot;
import io.ypolin.gecon.demo.service.TouristSpotService;
import io.ypolin.gecon.service.AsyncContentPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/touristSpots")
public class TouristSpotController {
    @Value("${gecon.touristSpots.itemsNumber}")
    private int numberOfItems;
    @Autowired
    private AsyncContentPopulator touristSpotsPopulator;

    private TouristSpotService touristSpotService;

    @PostMapping("/generate")
    @ResponseStatus(value = HttpStatus.OK)
    public void populateWithFreshContent(@RequestParam String city) {
        touristSpotsPopulator.generateUniqueContent(city, numberOfItems, TouristSpot.class, touristSpotService, touristSpotService::processItems);

    }
}

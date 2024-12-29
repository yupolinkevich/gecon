package io.ypolin.gecon.api;

import io.ypolin.gecon.service.ContentPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/touristSpots")
public class TouristSpotController {
    @Autowired
    private ContentPopulator touristSpotsPopulator;
    @PostMapping("/generate")
    @ResponseStatus(value = HttpStatus.OK)
    public void populateWithFreshContent(@RequestParam String city) {
        touristSpotsPopulator.populateWithContent(city,10);
    }
}

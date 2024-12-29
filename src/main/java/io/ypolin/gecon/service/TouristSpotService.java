package io.ypolin.gecon.service;

import io.ypolin.gecon.mongo.domain.TouristSpot;
import io.ypolin.gecon.mongo.TouristSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TouristSpotService extends ContentManager<TouristSpot> {
    @Autowired
    private TouristSpotRepository touristSpotRepository;
    @Override
    public List<TouristSpot> getRecentItems(String searchCriteria, int numberOfItems) {
        Sort sort = Sort.by(Sort.Direction.DESC, "generatedAt");
        Limit limit = Limit.of(numberOfItems);
        List<TouristSpot> touristSpots = touristSpotRepository.findByCity(searchCriteria, sort, limit);
        return new ArrayList<>(touristSpots);
    }

    @Override
    public void storeContent(List<TouristSpot> items) {
        touristSpotRepository.saveAll(items);
    }
}

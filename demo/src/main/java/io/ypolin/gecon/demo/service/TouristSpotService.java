package io.ypolin.gecon.demo.service;


import io.ypolin.gecon.demo.dao.TouristSpotRepository;
import io.ypolin.gecon.demo.dao.domain.TouristSpot;
import io.ypolin.gecon.service.DeduplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TouristSpotService extends DeduplicationService<TouristSpot> {
    @Autowired
    private TouristSpotRepository touristSpotRepository;
    @Override
    public List<TouristSpot> fetchRecentlyStoredItems(String searchCriteria, int numberOfItems) {
        Sort sort = Sort.by(Sort.Direction.DESC, "generatedAt");
        Limit limit = Limit.of(numberOfItems);
        List<TouristSpot> touristSpots = touristSpotRepository.findByCity(searchCriteria, sort, limit);
        return new ArrayList<>(touristSpots);
    }


    public void processItems(List<TouristSpot> items) {
        touristSpotRepository.saveAll(items);
    }
}

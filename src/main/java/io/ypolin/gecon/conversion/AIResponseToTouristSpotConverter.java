package io.ypolin.gecon.conversion;

import io.ypolin.gecon.ai.domain.MultiItemGeneratedResponse;
import io.ypolin.gecon.ai.domain.PlaceToVisit;
import io.ypolin.gecon.mongo.domain.TouristSpot;
import io.ypolin.gecon.mongo.domain.ChecksumAware;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AIResponseToTouristSpotConverter implements Converter<MultiItemGeneratedResponse<PlaceToVisit>, List<ChecksumAware>> {
    @Override
    public List<ChecksumAware> convert(MultiItemGeneratedResponse<PlaceToVisit> source) {
        List<ChecksumAware> result = source.getItems().stream()
                .map(placeToVisit -> {
                    TouristSpot touristSpot = new TouristSpot();
                    touristSpot.setCity(placeToVisit.city());
                    touristSpot.setCountry(placeToVisit.country());
                    touristSpot.setName(placeToVisit.place());
                    touristSpot.setDescription(placeToVisit.description());
                    touristSpot.setGeneratedAt(LocalDateTime.now());
                    touristSpot.setChecksum(DigestUtils.md5Hex(placeToVisit.place()+placeToVisit.city()));
                    return touristSpot;
                }).collect(Collectors.toList());
        return result;
    }
}

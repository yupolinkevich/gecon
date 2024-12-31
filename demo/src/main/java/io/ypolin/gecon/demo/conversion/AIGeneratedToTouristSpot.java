package io.ypolin.gecon.demo.conversion;


import io.ypolin.gecon.demo.ai.domain.PlaceToVisit;
import io.ypolin.gecon.demo.dao.domain.TouristSpot;
import io.ypolin.gecon.service.checksum.ChecksumAware;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class AIGeneratedToTouristSpot implements Converter<PlaceToVisit, ChecksumAware> {
    @Override
    public ChecksumAware convert(PlaceToVisit source) {
        TouristSpot touristSpot = new TouristSpot();
        touristSpot.setCity(source.city());
        touristSpot.setCountry(source.country());
        touristSpot.setName(source.place());
        touristSpot.setDescription(source.description());
        return touristSpot;
    }
}

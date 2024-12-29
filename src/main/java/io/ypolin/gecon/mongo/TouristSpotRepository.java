package io.ypolin.gecon.mongo;

import io.ypolin.gecon.mongo.domain.TouristSpot;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TouristSpotRepository extends MongoRepository<TouristSpot, String> {

    List<TouristSpot> findByCity(String city, Sort sort, Limit limit);
}

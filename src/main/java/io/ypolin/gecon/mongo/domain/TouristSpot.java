package io.ypolin.gecon.mongo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Document("tourist_spot")
public class TouristSpot extends ChecksumAware {
    @Id
    private String id;
    private String name;
    private String country;
    private String city;
    private String description;
    private LocalDateTime generatedAt;

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}

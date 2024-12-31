package io.ypolin.gecon.demo.dao.domain;

import io.ypolin.gecon.service.checksum.Checksum;
import io.ypolin.gecon.service.checksum.ChecksumAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("tourist_spot")
public class TouristSpot extends ChecksumAware {
    @Id
    private String id;
    @Checksum
    private String name;
    private String country;
    @Indexed
    @Checksum
    private String city;
    private String description;
    private LocalDateTime generatedAt = LocalDateTime.now();
}

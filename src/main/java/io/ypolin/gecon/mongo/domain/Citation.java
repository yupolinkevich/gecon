package io.ypolin.gecon.mongo.domain;

import io.ypolin.gecon.service.checksum.Checksum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Document("citation")
public class Citation extends ChecksumAware {
    @Id
    private String id;
    @Checksum
    private String text;
    private String author;
    private String aboutAuthor;
    @Indexed
    private String category;
    @Indexed
    private LocalDateTime generatedAt;
}

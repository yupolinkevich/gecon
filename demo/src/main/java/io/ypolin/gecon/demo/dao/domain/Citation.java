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

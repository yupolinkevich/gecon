package io.ypolin.gecon.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document("citation")
public class Citation {
    @Id
    private String id;
    private String text;
    private String author;
    private String aboutAuthor;
    private String category;
    private LocalDateTime generatedAt;
}

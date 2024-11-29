package io.ypolin.gecon.mongo;

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
public class Citation {
    @Id
    private String id;
    private String text;
    private String author;
    private String aboutAuthor;
    @Indexed
    private String category;
    @Indexed
    private LocalDateTime generatedAt;
    private String checksum;

    public Citation(String text, String author, String aboutAuthor, String category, LocalDateTime generatedAt, String checksum) {
        this.text = text;
        this.author = author;
        this.aboutAuthor = aboutAuthor;
        this.category = category;
        this.generatedAt = generatedAt;
        this.checksum = checksum;
    }
}

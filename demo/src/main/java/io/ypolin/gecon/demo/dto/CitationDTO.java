package io.ypolin.gecon.demo.dto;

import java.time.LocalDateTime;

public record CitationDTO(String category, String text, String author, String aboutAuthor, LocalDateTime generatedAt) {
}

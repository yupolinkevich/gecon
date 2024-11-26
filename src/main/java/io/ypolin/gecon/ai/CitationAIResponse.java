package io.ypolin.gecon.ai;

import java.util.List;

public record CitationAIResponse(String category, List<CitationItem> items) {
     public record CitationItem(String text, String author, String aboutAuthor){}
}

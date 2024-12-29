package io.ypolin.gecon.conversion;

import io.ypolin.gecon.ai.domain.CitationItem;
import io.ypolin.gecon.mongo.domain.Citation;
import io.ypolin.gecon.mongo.domain.ChecksumAware;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class AIGeneratedToCitation implements Converter<CitationItem, ChecksumAware> {
    @Override
    public ChecksumAware convert(CitationItem source) {
        Citation citation = new Citation();
        citation.setCategory(source.topic());
        citation.setText(source.text());
        citation.setAuthor(source.author());
        citation.setAboutAuthor(source.aboutAuthor());
        citation.setGeneratedAt(LocalDateTime.now());
        return citation;
    }
}

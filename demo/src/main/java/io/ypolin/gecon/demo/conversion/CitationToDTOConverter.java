package io.ypolin.gecon.demo.conversion;

import io.ypolin.gecon.demo.dao.domain.Citation;
import io.ypolin.gecon.demo.dto.CitationDTO;
import org.springframework.core.convert.converter.Converter;

public class CitationToDTOConverter implements Converter<Citation, CitationDTO> {
    @Override
    public CitationDTO convert(Citation source) {
        return new CitationDTO(source.getCategory(),source.getText(), source.getAuthor(),source.getAboutAuthor(), source.getGeneratedAt());
    }
}

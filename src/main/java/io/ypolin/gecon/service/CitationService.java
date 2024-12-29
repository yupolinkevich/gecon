package io.ypolin.gecon.service;

import io.ypolin.gecon.dto.CitationDTO;
import io.ypolin.gecon.mongo.CitationRepository;
import io.ypolin.gecon.mongo.domain.Citation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CitationService extends ContentManager<Citation>{
    @Autowired
    private CitationRepository citationRepository;

    public Page<CitationDTO> findAllByPage(String category, int size, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex, size, Sort.Direction.ASC, "generatedAt");
        Page<Citation> all = citationRepository.findByCategory(category, pageable);
        Page<CitationDTO> citationDTOS = all.map(citation -> new CitationDTO(citation.getCategory(), citation.getText(), citation.getAuthor(), citation.getAboutAuthor()));
        if (pageIndex == all.getTotalPages() - 1) {
            log.info("Need fresh citations.");
        }
        return citationDTOS;
    }

    @Override
    public List<Citation> getRecentItems(String searchCriteria, int numberOfItems) {
        Sort sort = Sort.by(Sort.Direction.DESC, "generatedAt");
        Limit limit = Limit.of(numberOfItems);
        List<Citation> latestCits = citationRepository.findByCategory(searchCriteria, sort, limit);
        return latestCits;
    }

    @Override
    public void storeContent(List<Citation> items) {
        citationRepository.saveAll(items);
    }
}

package io.ypolin.gecon.demo.service;

import io.ypolin.gecon.demo.dao.CitationRepository;
import io.ypolin.gecon.demo.dao.domain.Citation;
import io.ypolin.gecon.service.DeduplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CitationService extends DeduplicationService<Citation> {
    @Autowired
    private CitationRepository citationRepository;

    public Page<Citation> findAllByPage(String category, int size, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex, size, Sort.Direction.ASC, "generatedAt");
        Page<Citation> result = citationRepository.findByCategory(category, pageable);
        if (pageIndex == result.getTotalPages() - 1) {
            log.info("Need fresh citations.");
        }
        return result;
    }

    @Override
    public List<Citation> fetchRecentlyStoredItems(String searchCriteria, int numberOfItems) {
        Sort sort = Sort.by(Sort.Direction.DESC, "generatedAt");
        Limit limit = Limit.of(numberOfItems);
        List<Citation> latestCits = citationRepository.findByCategory(searchCriteria, sort, limit);
        return latestCits;
    }

    public void saveCitations(List<Citation> items) {
        citationRepository.saveAll(items);
    }
}

package io.ypolin.gecon.service;

import io.ypolin.gecon.dto.CitationDTO;
import io.ypolin.gecon.mongo.Citation;
import io.ypolin.gecon.mongo.CitationRepository;
import io.ypolin.gecon.ai.CitationAIProvider;
import io.ypolin.gecon.ai.CitationAIResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Log
public class CitationService {
    @Value("${gecon.numberOfItemsPerResponse}")
    private int numberOfItems;
    @Autowired
    private CitationRepository citationRepository;
    @Autowired
    private CitationAIProvider citationAIProvider;

    @Async
    public void populateCitations(String category) {

        CompletableFuture<CitationAIResponse> citationCompFuture = citationAIProvider.getCitations(category);
        citationCompFuture.thenAccept(citationAIResponse -> {
            log.info("Received content from AI. Start writing to DB");
            Sort sort = Sort.by(Sort.Direction.DESC, "generatedAt");
            Limit limit = Limit.of(numberOfItems);
            List<Citation> latestCits = citationRepository.findByCategory(category, sort, limit);

            List<String> citTexts = latestCits.stream().map(Citation::getText).collect(Collectors.toList());
            log.info("Eliminating repeating content");
            //ensure we don't save duplicates
            LocalDateTime now = LocalDateTime.now();
            List<Citation> freshCits = citationAIResponse.items().stream()
                    .filter(item -> !citTexts.contains(item.text()))
                    .map(item ->
                            new Citation(null, item.text(), item.author(), item.aboutAuthor(), citationAIResponse.category(), now)
                    ).collect(Collectors.toList());
            citationRepository.saveAll(freshCits);
            log.info(String.format("New %d citations were stored to db.", freshCits.size()));

        });
    }

    public Page<CitationDTO> findAllByPage(String category, int size, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex, size, Sort.Direction.ASC, "text");
        Page<Citation> all = citationRepository.findByCategory(category, pageable);
        Page<CitationDTO> citationDTOS = all.map(citation -> new CitationDTO(citation.getCategory(), citation.getText(), citation.getAuthor(), citation.getAboutAuthor()));
        if (pageIndex == all.getTotalPages() - 1) {
            log.info("Need fresh citations.");
            populateCitations(category);
        }

        return citationDTOS;
    }
}

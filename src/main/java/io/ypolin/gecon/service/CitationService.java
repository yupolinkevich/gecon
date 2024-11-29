package io.ypolin.gecon.service;

import io.ypolin.gecon.dto.CitationDTO;
import io.ypolin.gecon.mongo.Citation;
import io.ypolin.gecon.mongo.CitationRepository;
import io.ypolin.gecon.ai.CitationGenerator;
import io.ypolin.gecon.ai.CitationAIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CitationService {
    @Value("${gecon.numberOfItemsPerResponse}")
    private int numberOfItems;
    @Autowired
    private CitationRepository citationRepository;
    @Autowired
    private CitationGenerator citationGenerator;

    @Async
    public void populateCitations(String category) {

        CompletableFuture<CitationAIResponse> generateCitationsStep = CompletableFuture.supplyAsync(() -> {
                    log.info("Requesting new content about [{}] from AI", category);
                    CitationAIResponse citationAIResponse = citationGenerator.generateContent(category);
                    log.info("Finish content generation");
                    return citationAIResponse;
                }
        );
        generateCitationsStep.thenAccept(citationAIResponse -> {
                    log.info("Start writing generated content to DB");
                    Sort sort = Sort.by(Sort.Direction.DESC, "generatedAt");
                    Limit limit = Limit.of(numberOfItems);
                    List<Citation> latestCits = citationRepository.findByCategory(category, sort, limit);

                    List<String> checksums = latestCits.stream().map(Citation::getChecksum).collect(Collectors.toList());
                    log.info("Eliminating repeating content");
                    //ensure we don't save duplicates
                    LocalDateTime now = LocalDateTime.now();
                    List<Citation> freshCits = citationAIResponse.items().stream()
                            .map(item -> new Citation(item.text(), item.author(), item.aboutAuthor(), citationAIResponse.category(), now, DigestUtils.md5Hex(item.text())))
                            .filter(item -> !checksums.contains(item.getChecksum()))
                            .collect(Collectors.toList());
                    citationRepository.saveAll(freshCits);
                    log.info("New {} citations were stored to db.", freshCits.size());

                })
                .handle((result, throwable) -> {
                    if (throwable != null) {
                        log.error("Error occurred when trying to generate new content.", throwable);
                    }
                    return result;
                });
    }

    public Page<CitationDTO> findAllByPage(String category, int size, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex, size, Sort.Direction.ASC, "generatedAt");
        Page<Citation> all = citationRepository.findByCategory(category, pageable);
        Page<CitationDTO> citationDTOS = all.map(citation -> new CitationDTO(citation.getCategory(), citation.getText(), citation.getAuthor(), citation.getAboutAuthor()));
        if (pageIndex == all.getTotalPages() - 1) {
            log.info("Need fresh citations.");
            populateCitations(category);
        }

        return citationDTOS;
    }
}

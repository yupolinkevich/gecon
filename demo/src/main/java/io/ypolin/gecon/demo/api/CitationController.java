package io.ypolin.gecon.demo.api;

import io.ypolin.gecon.demo.dao.domain.Citation;
import io.ypolin.gecon.demo.dto.CitationDTO;
import io.ypolin.gecon.demo.service.CitationService;
import io.ypolin.gecon.service.AsyncContentPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/citations")
public class CitationController {
    @Value("${gecon.citation.itemsNumber}")
    private int numberOfItems;
    @Autowired
    private CitationService citationService;
    @Autowired
    private AsyncContentPopulator citationPopulator;
    @Autowired
    private ConversionService citationConversionService;

    @PostMapping("/populate")
    @ResponseStatus(value = HttpStatus.OK)
    public void populateCitations(@RequestParam String category, @RequestParam(defaultValue = "10") int numberOfItems) {
        citationPopulator.generateUniqueContent(category, numberOfItems, Citation.class, citationService, citationService::saveCitations);
    }

    @GetMapping("/paginated")
    public Page<CitationDTO> getCitationPage(@RequestParam(defaultValue = "beauty") String category, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int pageIndex) {
        Page<Citation> citationByCategory = citationService.findAllByPage(category, size, pageIndex);
        List<CitationDTO> citationDTOs = citationByCategory.getContent().stream()
                .map(citation -> citationConversionService.convert(citation, CitationDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(citationDTOs,citationByCategory.getPageable(),citationByCategory.getTotalElements());
    }

}

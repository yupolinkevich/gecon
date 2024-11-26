package io.ypolin.gecon.api;

import io.ypolin.gecon.dto.CitationDTO;
import io.ypolin.gecon.service.CitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citations")
public class ContentGenerationController {
    @Autowired
    private CitationService citationService;

    @PostMapping("/generate")
    @ResponseStatus(value = HttpStatus.OK)
    public void populateWithFreshContent(@RequestParam String category) {
        citationService.populateCitations(category.toLowerCase());
    }

    @GetMapping()
    public ResponseEntity<Page<CitationDTO>> getCitationPage(@RequestParam(defaultValue = "beauty") String category, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int pageIndex) {
        Page<CitationDTO> allByPage = citationService.findAllByPage(category, size, pageIndex);
        Page<CitationDTO> all = new PageImpl<>(allByPage.getContent(), allByPage.getPageable(), allByPage.getTotalElements());
        return ResponseEntity.ok(all);
    }

}

package io.ypolin.gecon.mongo;

import io.ypolin.gecon.mongo.domain.Citation;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CitationRepository extends MongoRepository<Citation, String> {
    List<Citation> findByCategory(String category, Sort sort, Limit limit);

    Page<Citation> findByCategory(String category, Pageable pageable);
    List<Citation> findTopNByOrderByIdDesc(int limit);
}

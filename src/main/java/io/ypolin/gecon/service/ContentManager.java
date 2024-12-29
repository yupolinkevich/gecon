package io.ypolin.gecon.service;

import io.ypolin.gecon.mongo.domain.ChecksumAware;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public abstract class ContentManager<T extends ChecksumAware>{
    public abstract List<T> getRecentItems(String searchCriteria, int numberOfItems);
    public abstract void storeContent(List<T> items);

    public int storeUniqueContent(List<T> items, String searchCriteria){
        //ensure we don't save duplicates
        int n = items.size();
        List<T> lastNItems = getRecentItems(searchCriteria, n);
        List<String> checkSums = lastNItems.stream().map(ChecksumAware::getChecksum)
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.toList());
        if (checkSums.isEmpty()) {
            log.warn("Duplicated items may be stored. Could not find checksums for the last {} stored items. ", n);
            storeContent(items);
            return items.size();
        }
        List<T> newItems = items.stream()
                .filter(doc -> !checkSums.contains(doc.getChecksum()))
                .collect(Collectors.toList());
        storeContent(newItems);
        return newItems.size();
    }

}

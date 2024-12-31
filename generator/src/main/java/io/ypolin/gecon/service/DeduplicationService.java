package io.ypolin.gecon.service;

import io.ypolin.gecon.service.checksum.ChecksumAware;
import io.ypolin.gecon.service.checksum.ChecksumInjected;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class DeduplicationService<T extends ChecksumAware> {
    public abstract List<T> fetchRecentlyStoredItems(String searchCriteria, int numberOfItems);

    @ChecksumInjected
    public List<T> removeRepeatedContent(List<T> items, String searchCriteria) {
        List<T> lastNItems = fetchRecentlyStoredItems(searchCriteria, items.size());
        List<String> checkSums = lastNItems.stream()
                .map(ChecksumAware::getChecksum)
                .filter(c -> c != null)
                .collect(Collectors.toList());
        if (checkSums.isEmpty()) {
            log.warn("Duplicated items may be stored. Could not find checksums for the last {} stored items. ", items.size());
            return items;
        }
        List<T> uniqueItems = items.stream()
                .filter(doc -> !checkSums.contains(doc.getChecksum()))
                .collect(Collectors.toList());
        return uniqueItems;
    }

}

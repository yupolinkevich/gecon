package io.ypolin.gecon.service.checksum;

import io.ypolin.gecon.mongo.domain.ChecksumAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class ChecksumInterceptor {
    @Pointcut("execution(public * storeUniqueContent(..))")
    public void processItemsChecksums() {
    }

    @Before("processItemsChecksums() && args(items,..)")
    public void beforeStoringChecksumAwareItems(List<ChecksumAware> items) {
        List<ChecksumAware> requireChecksumItems = items.stream()
                .filter(item -> item.getChecksum() == null)
                .collect(Collectors.toList());
        if (requireChecksumItems.isEmpty()) {
            return;
        }

        log.info("Processing checksum for {} items", requireChecksumItems.size());
        var processed = 0;
        for (ChecksumAware item : requireChecksumItems) {
            Map<String, String> checksumFields = inspectFieldsForChecksumAnnotation(item);
            if (!checksumFields.isEmpty()) {
                String source = checksumFields.values().stream().collect(Collectors.joining());
                String md5Hex = DigestUtils.md5Hex(source);
                item.setChecksum(md5Hex);
                processed++;
            }
        }
        if (processed == 0) {
            log.info("Couldn't find the fields to be used for checksum calculation for {} items", requireChecksumItems.size() - processed);
        }

    }

    private Map<String, String> inspectFieldsForChecksumAnnotation(Object obj) {
        Map<String, String> checksumFieldsMap = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Checksum.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    if (value != null)
                        checksumFieldsMap.put(field.getName(), String.valueOf(value));

                } catch (IllegalAccessException e) {
                    log.warn("Unable to process checksum for the field [{}]. Object: ", field.getName(), obj);
                }
            }

        }
        return checksumFieldsMap;
    }
}

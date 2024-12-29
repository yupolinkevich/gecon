package io.ypolin.gecon.mongo.domain;

import lombok.Data;

@Data
public abstract class ChecksumAware {
    private String checksum;
}

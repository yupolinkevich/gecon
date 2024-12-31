package io.ypolin.gecon.ai.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MultiItemGeneratedResponse<Item> {
    private List<Item> items = new ArrayList<>();
}

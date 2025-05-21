package com.withins.response;

import com.withins.dto.NewsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PageWith<T> {

    private final PageResponse page;
    private final Condition condition;
    private final List<T> content;

    public PageWith(Condition condition, Page<T> page) {
        this.page = new PageResponse(page.getTotalElements(), page.getTotalPages(), page.getNumber());
        this.condition = condition;
        this.content = page.getContent();
    }
}

package com.withins.response;

import com.withins.enums.KoreanRegion;
import com.withins.enums.NewsType;
import lombok.*;

@Getter
@Setter
@ToString
public class NewsCondition implements Condition {

    private String word;
    private KoreanRegion region;
    private NewsType type;

}

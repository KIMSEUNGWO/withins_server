package com.withins.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewsType {

    ALL("전체소식"),
    NOTICE("공지사항"),
    WELFARE("복지관소식"),
    RECRUIT("채용"),
    EVENT("행사/프로그램");

    private final String koreanName;

}

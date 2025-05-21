package com.withins.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KoreanRegion {
    ALL("전국"),
    // 특별시/광역시
    SEOUL("서울시"),
    BUSAN("부산시"),
    DAEGU("대구시"),
    INCHEON("인천시"),
    GWANGJU("광주시"),
    DAEJEON("대전시"),
    ULSAN("울산시"),
    SEJONG("세종시"),

    // 경기도 지역 (시/군 단위)
    SUWON("수원시"),
    GOYANG("고양시"),
    YONGIN("용인시"),
    SEONGNAM("성남시"),
    BUCHEON("부천시"),
    ANSAN("안산시"),
    NAMYANGJU("남양주시"),
    ANYANG("안양시"),
    HWASEONG("화성시"),
    PYEONGTAEK("평택시"),
    UIJEONGBU("의정부시"),
    SIHEUNG("시흥시"),
    PAJU("파주시"),
    GIMPO("김포시"),
    GWANGMYEONG("광명시"),
    G_GWANGJU("광주시"),  // 경기도 광주시 Prefix = G (경기도)
    GUNPO("군포시"),
    OSAN("오산시"),
    ICHEON("이천시"),
    YANGJU("양주시"),
    ANSEONG("안성시"),
    GURI("구리시"),
    POCHEON("포천시"),
    UIWANG("의왕시"),
    HANAM("하남시"),
    YEOJU("여주시"),
    DONGDUCHEON("동두천시"),
    GWACHEON("과천시"),
    GAPYEONG("가평군"),
    YEONCHEON("연천군"),
    YANGPYEONG("양평군"),

    // 다른 도 단위는 통합
    GANGWON("강원도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    JEONBUK("전라북도"),
    JEONNAM("전라남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEJU("제주도");

    private final String koreanName;

}

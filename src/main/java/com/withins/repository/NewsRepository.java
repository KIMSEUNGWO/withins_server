package com.withins.repository;

import com.withins.component.NewsKeywordFilter;
import com.withins.entity.News;
import com.withins.entity.Organization;
import com.withins.enums.NewsType;
import com.withins.jparepository.NewsJpaRepository;
import com.withins.jparepository.OrganizationJpaRepository;
import com.withins.querydsl.NewsQueryDSL;
import com.withins.response.NewsCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NewsRepository {

    private final OrganizationJpaRepository organizationJpaRepository;
    private final NewsJpaRepository newsJpaRepository;
    private final NewsQueryDSL newsQueryDSL;

    private final NewsKeywordFilter  newsKeywordFilter;

    public Page<News> search(Pageable pageable, NewsCondition condition) {
        return newsQueryDSL.search(pageable, condition);
    }

    public void saveCrawlData(Organization organization, Map<String, Object> newsObjectData) {
        for (String type : newsObjectData.keySet()) {
            NewsType newsType = NewsType.valueOf(type);
            List<Map<String, Object>> newsData = (List<Map<String, Object>>) newsObjectData.get(type);

            for (Map<String, Object> data : newsData) {
                Long id = data.get("id") == null ? null : Long.parseLong(data.get("id").toString());
                String title = data.get("title").toString();
                LocalDate createdAt = parseToKoreanTime(data.get("createAt").toString());
                String link = data.get("link").toString();

                // 필터 적용
                if (!newsKeywordFilter.filter(title)) {
                    System.out.println(title + "  필터됨");
                    continue;
                }

                newsJpaRepository.save(News.builder()
                    .baseId(id)
                    .title(title)
                    .createdAt(createdAt)
                    .link(link)
                    .type(newsType)
                    .organization(organization)
                    .build());
            }
        }
    }

    public LocalDate parseToKoreanTime(String utcTimeString) {
        // 1. Instant로 UTC 시간 파싱
        Instant instant = Instant.parse(utcTimeString);

        // 2. 한국 시간대(Asia/Seoul)로 변환하여 LocalDateTime 반환
        return LocalDate.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }
}

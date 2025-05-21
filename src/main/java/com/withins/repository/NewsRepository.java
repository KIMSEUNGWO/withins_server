package com.withins.repository;

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

    public Page<News> search(Pageable pageable, NewsCondition condition) {
        return newsQueryDSL.search(pageable, condition);
    }

    public void saveCrawlData(String orgName, Map<String, Object> newsObjectData) {
        Optional<Organization> findOrganization = organizationJpaRepository.findByName(orgName);
        if (findOrganization.isEmpty()) {
            System.out.println(orgName + " 복지관데이터가 DB에 존재하지 않습니다.");
            return;
        }
        Organization organization = findOrganization.get();

        for (String type : newsObjectData.keySet()) {
            NewsType newsType = NewsType.valueOf(type);
            List<Map<String, Object>> newsData = (List<Map<String, Object>>) newsObjectData.get(type);

            for (Map<String, Object> data : newsData) {
                Long id = data.get("id") == null ? null : Long.parseLong(data.get("id").toString());
                String title = data.get("title").toString();
                LocalDateTime createAt = parseToKoreanTime(data.get("createAt").toString());
                String link = data.get("link").toString();

                News saveNews = News.builder()
                    .baseId(id)
                    .title(title)
                    .createdAt(createAt)
                    .link(link)
                    .type(newsType)
                    .organization(organization)
                    .build();

                newsJpaRepository.save(saveNews);
            }
        }
    }

    public LocalDateTime parseToKoreanTime(String utcTimeString) {
        // 1. Instant로 UTC 시간 파싱
        Instant instant = Instant.parse(utcTimeString);

        // 2. 한국 시간대(Asia/Seoul)로 변환하여 LocalDateTime 반환
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }
}

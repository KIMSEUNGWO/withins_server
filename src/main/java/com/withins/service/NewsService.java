package com.withins.service;

import com.withins.dto.NewsRequest;
import com.withins.dto.OrgRequest;
import com.withins.entity.News;
import com.withins.entity.Organization;
import com.withins.jparepository.OrganizationJpaRepository;
import com.withins.repository.NewsRepository;
import com.withins.response.NewsCondition;
import com.withins.response.PageWith;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final OrganizationJpaRepository organizationJpaRepository;

    public PageWith<NewsRequest> search(Pageable pageable, NewsCondition condition) {
        Page<News> pagination = newsRepository.search(pageable, condition);
        Page<NewsRequest> map = pagination.map(news -> new NewsRequest(
            news.getId(), news.getTitle(), news.getType(), news.getLink(), new OrgRequest(news.getOrganization()), news.getCreatedAt()
        ));
        return new PageWith<>(condition, map);
    }

    public void saveAllCrawlData(Map<String, Object> data) {
        for (String key : data.keySet()) {
            saveCrawlData(key, (Map<String, Object>) data.get(key));
        }
    }

    public void saveCrawlData(String orgName, Map<String, Object> newsObjectData) {
        organizationJpaRepository.findByName(orgName)
            .ifPresentOrElse(
                organization -> newsRepository.saveCrawlData(organization, newsObjectData),
                () -> System.out.println(orgName + " 복지관데이터가 DB에 존재하지 않습니다.")
        );
    }
}

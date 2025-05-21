package com.withins.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.withins.entity.News;
import com.withins.enums.KoreanRegion;
import com.withins.enums.NewsType;
import com.withins.response.NewsCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.withins.entity.QNews.*;
import static com.withins.entity.QOrganization.*;

@Repository
@RequiredArgsConstructor
public class NewsQueryDSL {

    private final JPAQueryFactory query;

    public Page<News> search(Pageable pageable, NewsCondition condition) {

        BooleanExpression newsTypeCondition = newsTypeCondition(news.type, condition);
        BooleanExpression regionCondition = regionCondition(news.organization.region, condition);
        BooleanExpression wordCondition = wordCondition(news.title, condition);

        JPAQuery<News> jpaQuery = query.selectFrom(news)
            .join(news.organization, organization)
            .where(
                newsTypeCondition,
                regionCondition,
                wordCondition
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(news.createdAt.desc());

        JPAQuery<Long> countResult = query.select(news.count())
            .from(news)
            .where(jpaQuery.getMetadata().getWhere());

        return PageableExecutionUtils.getPage(jpaQuery.fetch(), pageable, countResult::fetchOne);
    }


    private BooleanExpression wordCondition(StringExpression compareWord, NewsCondition condition) {
        if (condition == null || condition.getWord() == null || condition.getWord().isEmpty()) return null;
        String word = condition.getWord();
        return getReplaceExpression(compareWord).containsIgnoreCase(word.replace(" ", ""));
    }

    private BooleanExpression regionCondition(EnumPath<KoreanRegion> compareRegion, NewsCondition condition) {
        if (condition == null || condition.getRegion() == null || condition.getRegion() == KoreanRegion.ALL) return null;
        return compareRegion.eq(condition.getRegion());
    }
    private BooleanExpression newsTypeCondition(EnumPath<NewsType> compareNewsType, NewsCondition condition) {
        if (condition == null || condition.getType() == null || condition.getType() == NewsType.ALL) return null;
        return compareNewsType.eq(condition.getType());
    }

    private StringExpression getReplaceExpression(StringExpression tuple) {
        return Expressions.stringTemplate("replace({0}, ' ', '')", tuple);
    }
}

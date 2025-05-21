package com.withins.jparepository;

import com.withins.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsJpaRepository extends JpaRepository<News, Long> {
}

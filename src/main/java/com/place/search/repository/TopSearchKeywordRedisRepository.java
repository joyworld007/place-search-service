package com.place.search.repository;

import com.place.search.dto.TopSearchKeyword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopSearchKeywordRedisRepository extends CrudRepository<TopSearchKeyword, String> {

}

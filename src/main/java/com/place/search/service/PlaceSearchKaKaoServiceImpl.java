package com.place.search.service;

import com.place.search.domain.common.ApiResponse;
import com.place.search.domain.common.CommonResponseDto;
import com.place.search.domain.common.Result;
import com.place.search.domain.common.ResultCode;
import com.place.search.dto.TopSearchKeyword;
import com.place.search.repository.TopSearchKeywordRedisRepository;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceSearchKaKaoServiceImpl implements PlaceSearchService {

  private final RestTemplate restTemplate;
  private final TopSearchKeywordRedisRepository topSearchKeywordRedisRepository;

  @Value("${kakao-open-api.place-search-keyword.url}")
  private String url;
  @Value("${kakao-open-api.place-search-keyword.app-key}")
  private String appKey;

  final private String TOP_SEARCH_KEYWORD_REDIS_KEY = "topSearchKeyword";

  @Override
  public CommonResponseDto searchByKeyword(String query, int page, int size) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("query", query)
        .queryParam("page", page)
        .queryParam("size", size)
        .encode(StandardCharsets.UTF_8);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + appKey);
    HttpEntity entity = new HttpEntity<>(headers);
    try {
      ResponseEntity<ApiResponse> response = restTemplate
          .exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, ApiResponse.class);
      if (response.getStatusCode() != HttpStatus.OK) {
        return CommonResponseDto.builder().code(ResultCode.FAIL.toString()).build();
      }
      //탑 키워드 저장
      modifyTopSearchKeyword(query);
      return CommonResponseDto.builder()
          .result(Result.builder().entry(response.getBody()).build())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return CommonResponseDto.builder().code(ResultCode.FAIL.toString()).build();
    }
  }

  @Override
  public CommonResponseDto searchTopKeyword() {
    Optional<TopSearchKeyword> topSearchKeyword =
        topSearchKeywordRedisRepository.findById(TOP_SEARCH_KEYWORD_REDIS_KEY);
    if (topSearchKeyword.isPresent()) {



      return CommonResponseDto.builder()
          .result(Result.builder().entry(topSearchKeyword.get()).build())
          .build();
    } else {
      return CommonResponseDto.builder().code(ResultCode.FAIL.toString()).build();
    }
  }

  /**
   * TOP Keyword를 Redis에 저장
   * @param query 검색어
   */
  public void modifyTopSearchKeyword(String query) {
    try {
      Optional<TopSearchKeyword> topSearchKeyword =
          topSearchKeywordRedisRepository.findById(TOP_SEARCH_KEYWORD_REDIS_KEY);
      //데이터가 없다면 신규 생성
      if (!topSearchKeyword.isPresent()) {
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put(query, 1);
        TopSearchKeyword temp = TopSearchKeyword.builder()
            .id(TOP_SEARCH_KEYWORD_REDIS_KEY)
            .keywords(treeMap)
            .build();
        topSearchKeywordRedisRepository.save(temp);
      } else {
        //데이터가 있다면 검색어에 대한 조회 수 증가
        Map<String, Integer> map = topSearchKeyword.get().getKeywords();
        if (!Optional.ofNullable(map).isPresent()) {
          //null 이면 최초 입력
          Map<String, Integer> hashMap = new HashMap<>();
          hashMap.put(query, 1);
          topSearchKeyword.get().setKeywords(hashMap);
          topSearchKeywordRedisRepository.save(topSearchKeyword.get());
        } else if (map.containsKey(query)) {
          //키가 있으면 조회수 증가
          map.put(query, Integer.valueOf(map.get(query))+1);
          topSearchKeyword.get().setKeywords(convertSortMap(map));
          topSearchKeywordRedisRepository.save(topSearchKeyword.get());
        } else {
          //키 없으면 신규 입력
          map.put(query, 1);
          topSearchKeyword.get().setKeywords(convertSortMap(map));
          topSearchKeywordRedisRepository.save(topSearchKeyword.get());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Map을 Value 내림차순 으로 정렬
   * @param map
   * @return
   */
  public Map<String, Integer> convertSortMap(Map<String, Integer> map) {
    List<Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
      @Override
      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        int comparision = (o1.getValue() - o2.getValue()) * -1;
        return comparision == 0 ? o1.getKey().compareTo(o2.getKey()) : comparision;
      }
    });
    Map<String, Integer> sortedMap = new LinkedHashMap<>();
    for (Iterator<Entry<String, Integer>> iter = list.iterator(); iter.hasNext(); ) {
      Map.Entry<String, Integer> entry = iter.next();
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }
}

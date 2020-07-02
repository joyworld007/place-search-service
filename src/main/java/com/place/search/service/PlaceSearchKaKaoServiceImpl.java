package com.place.search.service;

import com.place.search.domain.common.ApiResponse;
import com.place.search.domain.common.CommonResponseDto;
import com.place.search.domain.common.Result;
import com.place.search.domain.common.ResultCode;
import com.place.search.dto.TopSearchKeyword;
import com.place.search.repository.TopSearchKeywordRedisRepository;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
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
  public CommonResponseDto placeSearchByKeyword(String query, int page, int size) {
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

  public void modifyTopSearchKeyword(String query) {
    try {
      //Top 키워드 load & update
      Optional<TopSearchKeyword> topSearchKeyword =
          topSearchKeywordRedisRepository.findById(TOP_SEARCH_KEYWORD_REDIS_KEY);

      //데이터가 없다면 신규 생성
      if (!topSearchKeyword.isPresent()) {
        TreeMap<String, Long> treeMap = new TreeMap<>();
        treeMap.put(query, 1L);
        TopSearchKeyword temp = TopSearchKeyword.builder()
            .id(TOP_SEARCH_KEYWORD_REDIS_KEY)
            .treeMap(treeMap)
            .build();
        topSearchKeywordRedisRepository.save(temp);
      } else {
        //데이터가 있다면 검색어에 대한 조회 수 증가
        TreeMap<String, Long> treeMap = topSearchKeyword.get().getTreeMap();

        if (!Optional.ofNullable(treeMap).isPresent()) {
          //tree map 이 null 이면 최초 입력
          TreeMap<String, Long> tempMap = new TreeMap<>();
          tempMap.put(query, Long.valueOf(1L));
          topSearchKeyword.get().setTreeMap(tempMap);
          topSearchKeywordRedisRepository.save(topSearchKeyword.get());
        } else if (treeMap.containsKey(query)) {
          //키가 있으면 조회수 증가
          treeMap.put(query, Long.valueOf(treeMap.get(query) + 1L));
          topSearchKeyword.get().setTreeMap(treeMap);
          topSearchKeywordRedisRepository.save(topSearchKeyword.get());
        } else {
          //키 없으면 신규 입력
          treeMap.put(query, Long.valueOf(1L));
          topSearchKeyword.get().setTreeMap(treeMap);
          topSearchKeywordRedisRepository.save(topSearchKeyword.get());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}

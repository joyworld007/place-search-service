package com.place.search.service;

import com.place.search.domain.common.ApiResponse;
import com.place.search.domain.common.CommonResponseDto;
import com.place.search.domain.common.Result;
import com.place.search.domain.common.ResultCode;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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

  @Value("${kakao-open-api.place-search-keyword.url}")
  private String url;
  @Value("${kakao-open-api.place-search-keyword.app-key}")
  private String appKey;

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

      return CommonResponseDto.builder()
          .result(Result.builder().entry(response.getBody()).build())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return CommonResponseDto.builder().code(ResultCode.FAIL.toString()).build();
    }
  }
}

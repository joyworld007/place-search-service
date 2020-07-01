package com.place.search.service;

import com.place.search.domain.common.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
  public CommonResponseDto placeSearchByKeyword(String keyword, int page, int size) {

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("keyword", keyword)
        .queryParam("page", page)
        .queryParam("size", size);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("Authorization", "KakaoAK " + appKey);
    HttpEntity entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate
        .exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

    System.out.println("Result : " + response);

    return null;
  }
}

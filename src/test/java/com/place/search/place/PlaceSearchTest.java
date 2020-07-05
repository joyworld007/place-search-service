package com.place.search.place;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.place.search.controller.SearchApiController;
import com.place.search.domain.common.ApiResponse;
import com.place.search.domain.common.CommonResponseDto;
import com.place.search.domain.common.Document;
import com.place.search.domain.common.Result;
import com.place.search.dto.TopKeyword;
import com.place.search.repository.TopSearchKeywordRedisRepository;
import com.place.search.service.MemberService;
import com.place.search.service.PlaceSearchKaKaoServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

@WebMvcTest(SearchApiController.class)
@SuppressWarnings("unchecked")
public class PlaceSearchTest {

  @MockBean
  MemberService memberService;

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private PlaceSearchKaKaoServiceImpl placeSearchKaKaoService;

  @MockBean
  private TopSearchKeywordRedisRepository topSearchKeywordRedisRepository;

  @BeforeEach
  public void setup(TestInfo testInfo) {
    rawSetup(testInfo);
  }

  private ApiResponse apiResponse;
  private Result result;
  private ResponseEntity<ApiResponse> responseEntity;
  private UriComponentsBuilder builder;
  HttpEntity entity;
  List<Document> documents;
  List<TopKeyword> topKeywords;

  private void rawSetup(TestInfo testInfo) {
    switch (testInfo.getDisplayName()) {
      case "searchByKeyword Test":
        //create top search keyowrd
        documents = new ArrayList<>();
        Document document1 = Document.builder()
            .address_name("삼성역")
            .id(21160620L).build();
        documents.add(document1);
        apiResponse = ApiResponse.builder().documents(documents).build();
        break;

      case "searchTopKeyword Test":
        //create top search keyowrd
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("강남역", 9991);
        hashMap.put("서울역", 10000);
        hashMap.put("삼성역", 9999);
        hashMap.put("상갈역", 9998);
        hashMap.put("잠실역", 9997);
        hashMap.put("교대역", 9996);
        hashMap.put("남대문", 9995);
        hashMap.put("에버랜드", 9994);
        hashMap.put("롯데월드", 9993);
        hashMap.put("대공원", 9992);
        Map<String, Integer> sortedMap = convertSortMap(hashMap);
        topKeywords = convertMapToList(sortedMap);
        break;
    }
  }

  @DisplayName("searchByKeyword Test")
  @Test
  public void searchByKeywordTest() throws Exception {

    CommonResponseDto commonResponseDto = CommonResponseDto.builder().result(
        Result.builder().entry(apiResponse).build()
    ).code("SUCCESS").build();

    given(placeSearchKaKaoService.searchByKeyword(anyString(), anyInt(), anyInt()))
        .willReturn(commonResponseDto);

    mockMvc.perform(get("/v1/places")
        .contentType(MediaType.APPLICATION_JSON)
        .queryParam("query", "삼성역")
        .queryParam("page", "1")
        .queryParam("size", "5"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.result.entry.documents[0].id")
            .value(21160620L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.code")
            .value("SUCCESS"));
  }

  @Test
  @DisplayName("searchTopKeyword Test")
  public void searchTopKeywordTest() throws Exception {
    CommonResponseDto commonResponseDto = CommonResponseDto.builder().result(
        Result.builder().entry(topKeywords).build()
    ).code("SUCCESS").build();

    given(placeSearchKaKaoService.searchTopKeyword())
        .willReturn(commonResponseDto);

    mockMvc.perform(get("/v1/places/top-keywords")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.result.entry[0].count")
            .value(10000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.code")
            .value("SUCCESS"));
  }

  public Map<String, Integer> convertSortMap(Map<String, Integer> map) {
    List<Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, new Comparator<Entry<String, Integer>>() {
      @Override
      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        int comparision = (o1.getValue() - o2.getValue()) * -1;
        return comparision == 0 ? o1.getKey().compareTo(o2.getKey()) : comparision;
      }
    });
    Map<String, Integer> sortedMap = new LinkedHashMap<>();
    list.forEach(t -> sortedMap.put(t.getKey(), t.getValue()));
    return sortedMap;
  }

  public List<TopKeyword> convertMapToList(Map<String, Integer> map) {
    List<TopKeyword> topKeywords = new ArrayList<>();
    map.entrySet().forEach(t ->
        topKeywords.add(TopKeyword.builder().keyword(t.getKey()).count(t.getValue()).build())
    );
    return topKeywords;
  }

}

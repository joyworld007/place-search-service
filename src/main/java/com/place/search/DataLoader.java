package com.place.search;

import com.place.search.dto.MemberDto;
import com.place.search.dto.TopKeyword;
import com.place.search.dto.TopSearchKeyword;
import com.place.search.repository.TopSearchKeywordRedisRepository;
import com.place.search.service.MemberService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

  private MemberService memberService;
  private final TopSearchKeywordRedisRepository topSearchKeywordRedisRepository;
  final private String TOP_SEARCH_KEYWORD_REDIS_KEY = "topSearchKeyword";

  public void run(ApplicationArguments args) {
    //create user
    memberService.joinUser(MemberDto.builder().email("test1@gmail.com").password("1234").build());
    memberService.joinUser(MemberDto.builder().email("test2@gmail.com").password("1234").build());
    memberService.joinUser(MemberDto.builder().email("test3@gmail.com").password("1234").build());

    //create top search keyowrd
    Map<String, Integer> hashMap = new HashMap<>();
    hashMap.put("서울역", 10000);
    hashMap.put("삼성역", 9999);
    hashMap.put("상갈역", 9998);
    hashMap.put("잠실역", 9997);
    hashMap.put("교대역", 9996);
    hashMap.put("남대문", 9995);
    hashMap.put("에버랜드", 9994);
    hashMap.put("롯데월드", 9993);
    hashMap.put("대공원", 9992);
    hashMap.put("강남역", 9991);
    hashMap.put("역삼역", 9990);

    Map<String, Integer> sortedMap = convertSortMap(hashMap);
    TopSearchKeyword temp = TopSearchKeyword.builder()
        .id(TOP_SEARCH_KEYWORD_REDIS_KEY)
        .keywords(sortedMap)
        .topKeywords(convertMapToList(sortedMap))
        .build();
    topSearchKeywordRedisRepository.save(temp);
  }

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

  public List<TopKeyword> convertMapToList(Map<String, Integer> map) {
    List<TopKeyword> topKeywords = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      String keyword = entry.getKey();
      int count = entry.getValue();
      topKeywords.add(TopKeyword.builder().keyword(keyword).count(count).build());
    }
    return topKeywords;
  }
}

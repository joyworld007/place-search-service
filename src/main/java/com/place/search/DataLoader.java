package com.place.search;

import com.place.search.dto.MemberDto;
import com.place.search.dto.TopSearchKeyword;
import com.place.search.repository.TopSearchKeywordRedisRepository;
import com.place.search.service.MemberService;
import java.util.TreeMap;
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
    TreeMap<String, Long> treeMap = new TreeMap<>();
    TopSearchKeyword temp = TopSearchKeyword.builder()
        .id(TOP_SEARCH_KEYWORD_REDIS_KEY)
        .treeMap(treeMap)
        .build();
    topSearchKeywordRedisRepository.save(temp);
  }
}

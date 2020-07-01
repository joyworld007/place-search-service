package com.place.search;

import com.place.search.dto.MemberDto;
import com.place.search.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

  private MemberService memberService;

  public void run(ApplicationArguments args) {
    memberService.joinUser(MemberDto.builder().email("test1@gmail.com").password("1234").build());
    memberService.joinUser(MemberDto.builder().email("test2@gmail.com").password("1234").build());
    memberService.joinUser(MemberDto.builder().email("test3@gmail.com").password("1234").build());
    memberService.joinUser(MemberDto.builder().email("test4@gmail.com").password("1234").build());
    memberService.joinUser(MemberDto.builder().email("test5@gmail.com").password("1234").build());
  }
}

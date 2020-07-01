package com.place.search.controller;

import com.place.search.dto.MemberDto;
import com.place.search.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MemberController {

  private MemberService memberService;

  // 메인 페이지
  @GetMapping("/")
  public String index() {

    return "redirect:/user/login";
  }

  // 회원가입 페이지
  @GetMapping("/user/signup")
  public String dispSignup() {

    return "/signup";
  }

  // 회원가입 처리
  @PostMapping("/user/signup")
  public String execSignup(MemberDto memberDto) {
    memberService.joinUser(memberDto);

    return "redirect:/user/login";
  }

  // 로그인 페이지
  @GetMapping("/user/login")
  public String dispLogin() {

    return "/login";
  }

  // 로그인 성공 페이지
  @GetMapping("/user/login/result")
  public String dispLoginResult() {
    return "redirect:/search";
  }

  // 로그인 실패 페이지
  @GetMapping("/user/login/fail")
  public String dispLoginFailResult() {

    return "/loginFail";
  }

  // 로그아웃 결과 페이지
  @GetMapping("/user/logout/result")
  public String dispLogout() {

    return "/logout";
  }

  // 접근 거부 페이지
  @GetMapping("/user/denied")
  public String dispDenied() {

    return "/denied";
  }

}

package com.place.search.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class SearchController {

  // 검색 홈
  @GetMapping("/search")
  public String dispSignup() {
    return "/placeSearch";
  }
}

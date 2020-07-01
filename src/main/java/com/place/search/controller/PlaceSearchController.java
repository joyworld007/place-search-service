package com.place.search.controller;

import com.place.search.domain.common.CommonResponseDto;
import com.place.search.service.PlaceSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.yaml.snakeyaml.util.UriEncoder;

@Controller
@AllArgsConstructor
public class PlaceSearchController {

  // 검색 홈
  @GetMapping("/search")
  public String dispSignup() {
    return "/placeSearch";
  }
}

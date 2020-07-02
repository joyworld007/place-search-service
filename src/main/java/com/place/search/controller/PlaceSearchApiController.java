package com.place.search.controller;

import com.place.search.domain.common.CommonResponseDto;
import com.place.search.domain.common.CommonResponseEntity;
import com.place.search.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/places")
public class PlaceSearchApiController {

  final private PlaceSearchService placeSearchService;

  @GetMapping("")
  public ResponseEntity search(
      @RequestParam(value = "query") String query,
      @RequestParam(value = "page") int page,
      @RequestParam(value = "size") int size
  ) {
    try {
      CommonResponseDto commonResponseDto = placeSearchService
          .searchByKeyword(query, page, size);
      return CommonResponseEntity.ok(commonResponseDto.getResult());
    } catch (Exception e) {
      CommonResponseEntity.fail("Fail", "Fail");
    }
    return CommonResponseEntity.ok();
  }

  @GetMapping("/top-keyword")
  public ResponseEntity searchTopKeyword() {
    try {
      CommonResponseDto commonResponseDto = placeSearchService
          .searchTopKeyword();
      return CommonResponseEntity.ok(commonResponseDto.getResult());
    } catch (Exception e) {
      CommonResponseEntity.fail("Fail", "Fail");
    }
    return CommonResponseEntity.ok();
  }
}

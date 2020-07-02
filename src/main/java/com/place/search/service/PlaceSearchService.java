package com.place.search.service;


import com.place.search.domain.common.CommonResponseDto;

public interface PlaceSearchService {

  CommonResponseDto searchByKeyword(String keyword, int page, int size);
  CommonResponseDto searchTopKeyword();
}

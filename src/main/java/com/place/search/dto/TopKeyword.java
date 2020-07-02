package com.place.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopKeyword {

  private String keyword;
  private int count;
}

package com.place.search.domain.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Pagination {

  private Long total;
  private int offset;
  private int limit;

}

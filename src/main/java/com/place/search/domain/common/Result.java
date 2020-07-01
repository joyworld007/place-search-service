package com.place.search.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties
public class Result<T> {

  private T entry;

  @JsonInclude(Include.NON_NULL)
  private Pagination pagination;
}

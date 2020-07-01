package com.place.search.domain.common;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class Meta {

  private String is_end;
  private Long pageable_count;
  private Map same_name;
  private Long total_count;
}

package com.place.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("topSearchKeyword")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TopSearchKeyword {

  @Id
  private String id;
  @JsonIgnoreProperties
  private Map<String, Integer> keywords;
  @JsonIgnoreProperties
  private List<TopKeyword> topKeywords;
}

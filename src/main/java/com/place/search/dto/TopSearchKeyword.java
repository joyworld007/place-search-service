package com.place.search.dto;

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
  private Map<String, Integer> keywords;

}

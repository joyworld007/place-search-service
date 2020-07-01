package com.place.search.domain.common;

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
public class Document {

  private String address_name;
  private String category_group_code;
  private String category_group_name;
  private String category_name;
  private String distance;
  private Long id;
  private String phone;
  private String place_name;
  private String place_url;
  private String road_address_name;
  private String x;
  private String y;

}

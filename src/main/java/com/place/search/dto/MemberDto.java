package com.place.search.dto;

import com.place.search.domain.entity.MemberEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

  private Long id;
  private String email;
  private String password;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public MemberEntity toEntity() {
    return MemberEntity.builder()
        .id(id)
        .email(email)
        .password(password)
        .build();
  }

  @Builder
  public MemberDto(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }
}

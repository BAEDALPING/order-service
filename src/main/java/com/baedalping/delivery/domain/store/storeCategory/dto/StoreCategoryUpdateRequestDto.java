package com.baedalping.delivery.domain.store.storeCategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class StoreCategoryUpdateRequestDto {
  @NotBlank(message = "가게 분류명을 입력해주세요.")
  private String storeCategoryName;
}

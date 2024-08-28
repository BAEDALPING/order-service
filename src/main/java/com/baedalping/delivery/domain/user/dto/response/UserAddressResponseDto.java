package com.baedalping.delivery.domain.user.dto.response;

import com.baedalping.delivery.domain.user.entity.UserAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAddressResponseDto {
  private Long userId;
  private String address;
  private String zipcode;
  private String alias;

  @Builder
  private UserAddressResponseDto(Long userId, String address, String zipcode, String alias) {
    this.userId = userId;
    this.address = address;
    this.zipcode = zipcode;
    this.alias = alias;
  }

  public static UserAddressResponseDto fromEntity(Long userId, UserAddress userAddress) {
    return UserAddressResponseDto.builder()
        .userId(userId)
        .address(userAddress.getAddress())
        .zipcode(userAddress.getZipcode())
        .alias(userAddress.getAlias())
        .build();
  }
}

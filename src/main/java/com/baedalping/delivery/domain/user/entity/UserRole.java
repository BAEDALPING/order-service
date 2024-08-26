package com.baedalping.delivery.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
  CUSTOMER(101, "CUSTOMER"),
  OWNER(102, "OWNER"),

  MANAGER(201, "MANAGER"),
  ADMIN(202, "ADMIN")
  ;

  private long roleNum;
  private String roleName;
}

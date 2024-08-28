package com.baedalping.delivery.user;

import com.baedalping.delivery.domain.user.entity.User;

public class UserFixture {
  public static User get(String username, String password, String email){
    return User.builder()
        .username(username)
        .password(password)
        .email(email)
        .build();
  }
}

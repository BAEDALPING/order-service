package com.baedalping.delivery.domain.user.dto;

import com.baedalping.delivery.domain.user.entity.UserRole;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailsImpl implements UserDetails {
  private Long userId;
  private String email;
  private String username;
  private UserRole userRole;
  private boolean isPublic;

  @Builder
  private UserDetailsImpl(
      Long userId,
      String email,
      String username,
      UserRole userRole,
      boolean isPublic) {
    this.userId = userId;
    this.email = email;
    this.username = username;
    this.userRole = userRole;
    this.isPublic = isPublic;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(this.getUserRole().getRoleName()));
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public Long getUserId() {
    return userId;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isPublic;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isPublic;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isPublic;
  }

  @Override
  public boolean isEnabled() {
    return isPublic;
  }
}

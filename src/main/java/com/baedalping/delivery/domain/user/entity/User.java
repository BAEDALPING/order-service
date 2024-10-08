package com.baedalping.delivery.domain.user.entity;

import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "P_USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE p_users SET deleted_at = NOW() where user_id = ?")
@Getter
public class User extends AuditField {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  @Email
  private String email;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private UserRole role = UserRole.CUSTOMER;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<UserAddress> addressList = new ArrayList<>();

  @Column(name = "is_public", nullable = false)
  private boolean isPublic = true;

  @Builder
  private User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public void updateInfo(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public void updateUserRole(UserRole role) {
    this.role = role;
  }

  public void setInvisible() {
    this.isPublic = false;
  }

  public void addAddress(UserAddress address) {
    addressList.add(address);
    address.setUser(this);
  }
}

package com.baedalping.delivery.domain.user.entity;

import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "P_USER_ADDRESS")
@Entity
@NoArgsConstructor
@SQLRestriction("deleted_at is NULL")
@Getter
public class UserAddress extends AuditField {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "address_id")
  private UUID addressId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "zipcode", nullable = false, length = 5)
  private String zipcode;

  @Column(name = "address_alias", nullable = true)
  private String alias;

  @Column(name = "is_public")
  private boolean isPublic = true;

  @Builder
  private UserAddress(String address, String zipcode, String alias) {
    this.address = address;
    this.zipcode = zipcode;
    this.alias = alias;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setInvisible() {
    this.isPublic = false;
  }

  public void update(String address, String zipcode, String alias) {
    this.address = address;
    this.zipcode = zipcode;
    this.alias = alias;
  }
}

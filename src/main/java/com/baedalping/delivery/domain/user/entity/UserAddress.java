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
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "P_USER_ADDRESS")
@Entity
@NoArgsConstructor
@SQLRestriction("deleted_at is NULL")
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

  @Builder
  private UserAddress(User user, String address, String zipcode, String alias) {
    this.user = user;
    this.address = address;
    this.zipcode = zipcode;
    this.alias = alias;
  }

  public void setUser(User user) {
    this.user = user;
  }
}

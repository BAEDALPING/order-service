package com.baedalping.delivery.global.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AuditField {
  @CreatedDate private LocalDateTime createdAt;

  @CreatedBy private String createdBy;

  @LastModifiedDate private LocalDateTime updatedAt;

  @LastModifiedBy private String updatedBy;

  private LocalDateTime deletedAt;

  private String deletedBy;

  public void delete(String deletedBy){
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = deletedBy;
  }
}

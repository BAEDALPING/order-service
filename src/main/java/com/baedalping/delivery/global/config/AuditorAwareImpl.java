package com.baedalping.delivery.global.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    // TODO :: principle 적용시 유저name 가져와서 저장하는것으로 구현
    return Optional.empty();
  }
}

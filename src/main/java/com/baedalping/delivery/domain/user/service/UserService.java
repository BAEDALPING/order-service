package com.baedalping.delivery.domain.user.service;

import com.baedalping.delivery.domain.user.dto.response.UserAddressResponseDto;
import com.baedalping.delivery.domain.user.dto.response.UserCreateResponseDto;
import com.baedalping.delivery.domain.user.dto.response.UserReadResponseDto;
import com.baedalping.delivery.domain.user.dto.response.UserUpdateResponseDto;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.entity.UserAddress;
import com.baedalping.delivery.domain.user.repository.UserAddressRepository;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final UserAddressRepository addressRepository;
  private final BCryptPasswordEncoder encoder;

  @Transactional
  public UserCreateResponseDto create(String username, String password, String email) {
    validateUniqueEmail(email);
    User newUser =
        User.builder().username(username).password(encoder.encode(password)).email(email).build();
    return UserCreateResponseDto.ofEntity(userRepository.save(newUser));
  }

  public UserReadResponseDto get(Long userId) {
    return UserReadResponseDto.ofEntity(findByUser(userId));
  }

  @Transactional
  public UserUpdateResponseDto updateUserInfo(
      Long userId, String username, String password, String email) {
    User user = findByUser(userId);
    user.updateInfo(username, encoder.encode(password), email);
    userRepository.saveAndFlush(user);

    return UserUpdateResponseDto.ofEntity(user);
  }

  @Transactional
  public UserReadResponseDto delete(Long userId) {
    User user = findByUser(userId);
    user.setInvisible();
    userRepository.delete(user);
    userRepository.flush();
    return UserReadResponseDto.ofEntity(user);
  }

  @Transactional
  public UserAddressResponseDto addAddress(
      Long userId, String address, String zipcode, String alias) {
    User user = findByUser(userId);
    UserAddress newAddress =
        UserAddress.builder().address(address).zipcode(zipcode).alias(alias).build();
    user.addAddress(newAddress);
    addressRepository.save(newAddress);
    return UserAddressResponseDto.fromEntity(newAddress);
  }

  public List<UserAddressResponseDto> getAddressList(Long userId) {
    User user = findByUser(userId);
    return user.getAddressList().stream()
        .map(address -> UserAddressResponseDto.fromEntity(address))
        .collect(Collectors.toList());
  }

  @Transactional
  public UserAddressResponseDto updateAddress(
      UUID addressId, Long userId, String address, String zipcode, String alias) {
    UserAddress savedAddress = validateUserAddress(addressId, userId);
    savedAddress.update(address, zipcode, alias);
    userRepository.flush();
    return UserAddressResponseDto.fromEntity(savedAddress);
  }

  private UserAddress validateUserAddress(UUID addressId, Long userId) {
    UserAddress savedAddress =
        addressRepository
            .findByAddressId(addressId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER));

    if (userId != savedAddress.getUser().getUserId())
      throw new DeliveryApplicationException(ErrorCode.INVALID_PERMISSION);

    return savedAddress;
  }

  private User findByUser(Long userId) {
    return userRepository
        .findByUserId(userId)
        .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER));
  }

  private void validateUniqueEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DeliveryApplicationException(ErrorCode.DUPLICATED_USER);
    }
  }
}

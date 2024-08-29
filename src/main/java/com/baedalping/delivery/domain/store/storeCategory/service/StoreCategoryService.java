package com.baedalping.delivery.domain.store.storeCategory.service;


import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import com.baedalping.delivery.domain.store.storeCategory.repository.StoreCategoryRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StoreCategoryService {

  private final StoreCategoryRepository storeCategoryRepository;

  @Transactional
  public StoreCategoryResponseDto createStoreCategory(StoreCategoryRequestDto storeCategoryRequestDto) {
    String storeCategoryName = storeCategoryRequestDto.getStoreCategoryName();
    if(storeCategoryRepository.findByStoreCategoryName(storeCategoryName).isPresent()) {
      new DeliveryApplicationException(ErrorCode.DUPLICATE_STORE_CATEGORY_NAME);
    }

    StoreCategory storeCategory = storeCategoryRepository.save(new StoreCategory(storeCategoryRequestDto));
    return new StoreCategoryResponseDto(storeCategory);
  }

  @Transactional
  public StoreCategoryResponseDto updateStoreCategory(UUID storeCategoryId, StoreCategoryRequestDto storeCategoryRequestDto) {
    StoreCategory storeCategory = findById(storeCategoryId);
    storeCategory.setStoreCategoryName(storeCategoryRequestDto.getStoreCategoryName());
    return new StoreCategoryResponseDto(storeCategory);
  }

  @Transactional
  public void deleteStoreCategory(UUID storeCategoryId) {
    findById(storeCategoryId).delete(null);
  }

  @Transactional
  public Page<StoreCategoryResponseDto> getStoreCategories(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<StoreCategory> storeCategoryList = storeCategoryRepository.findAllByOrderByCreatedAtAscUpdatedAtAsc(pageable);
    return storeCategoryList.map(StoreCategoryResponseDto::new);

  }

  public StoreCategory findById(UUID storeCategoryId) {
    return storeCategoryRepository.findById(storeCategoryId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE_CATEGORY)
    );
  }



}

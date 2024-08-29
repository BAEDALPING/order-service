package com.baedalping.delivery.domain.store.storeCategory.service;


import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryCreateRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryCreateResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryUpdateRequestDto;
import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryUpdateResponseDto;
import com.baedalping.delivery.domain.store.storeCategory.repository.StoreCategoryRepository;
import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
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
  public StoreCategoryCreateResponseDto createStoreCategory(StoreCategoryCreateRequestDto storeCategoryCreateRequestDto) {
    String storeCategoryName = storeCategoryCreateRequestDto.getStoreCategoryName();
    if(storeCategoryRepository.findByStoreCategoryName(storeCategoryName).isPresent()) {
      new DeliveryApplicationException(ErrorCode.DUPLICATE_STORE_CATEGORY_NAME);
    }

    StoreCategory storeCategory = storeCategoryRepository.save(new StoreCategory(storeCategoryCreateRequestDto));
    return new StoreCategoryCreateResponseDto(storeCategory);
  }

  @Transactional
  public StoreCategoryUpdateResponseDto updateStoreCategory(UUID storeCategoryId, StoreCategoryUpdateRequestDto storeCategoryUpdateRequestDto) {
    StoreCategory storeCategory = findById(storeCategoryId);
    storeCategory.setStoreCategoryName(storeCategoryUpdateRequestDto.getStoreCategoryName());
    return new StoreCategoryUpdateResponseDto(storeCategory);
  }

  @Transactional
  public void deleteStoreCategory(UUID storeCategoryId) {
    findById(storeCategoryId).delete(null);
  }

  @Transactional
  public Page<StoreCategoryCreateResponseDto> getStoreCategories(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<StoreCategory> storeCategoryList = storeCategoryRepository.findAllByOrderByCreatedAtAscUpdatedAtAsc(pageable);
    return storeCategoryList.map(StoreCategoryCreateResponseDto::new);

  }

  public StoreCategory findById(UUID storeCategoryId) {
    return storeCategoryRepository.findById(storeCategoryId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE_CATEGORY)
    );
  }



}

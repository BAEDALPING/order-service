package com.baedalping.delivery.domain.product.productCategory.service;


import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryRequestDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryResponseDto;
import com.baedalping.delivery.domain.product.productCategory.entity.ProductCategory;
import com.baedalping.delivery.domain.product.productCategory.repository.ProductCategoryRepository;
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

@Service
@AllArgsConstructor
public class ProductCategoryService {

  private final ProductCategoryRepository productCategoryRepository;

  @Transactional
  public ProductCategoryResponseDto createProductCategory(ProductCategoryRequestDto productCategoryRequestDto) {
    String productCategoryName = productCategoryRequestDto.getProductCategoryName();
    if(productCategoryRepository.findByProductCategoryName(productCategoryName).isPresent()) {
      new DeliveryApplicationException(ErrorCode.DUPLICATE_PRODUCT_CATEGORY_NAME);
    }

    ProductCategory productCategory = productCategoryRepository.save(new ProductCategory(productCategoryRequestDto));
    return new ProductCategoryResponseDto(productCategory);
  }

  @Transactional
  public ProductCategoryResponseDto updateProductCatgegory(UUID productCategoryId, ProductCategoryRequestDto productCategoryRequestDto) {
    ProductCategory productCategory = findById(productCategoryId);
    productCategory.setProductCategoryName(productCategoryRequestDto.getProductCategoryName());
    return new ProductCategoryResponseDto(productCategory);
  }

  @Transactional
  public void deleteProductCatgegory(UUID productCategoryId) {
    findById(productCategoryId).delete(null);
  }

  @Transactional
  public Page<ProductCategoryResponseDto> getProductCategories(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<ProductCategory> productCategoryList = productCategoryRepository.findAllByOrderByCreatedAtAscUpdatedAtAsc(pageable);
    return productCategoryList.map(ProductCategoryResponseDto::new);
  }


  public ProductCategory findById(UUID productCategoryId) {
    return productCategoryRepository.findById(productCategoryId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT_CATEGORY)
    );
  }
}

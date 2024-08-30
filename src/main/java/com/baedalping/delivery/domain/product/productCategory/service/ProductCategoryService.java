package com.baedalping.delivery.domain.product.productCategory.service;


import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryCreateRequestDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryCreateResponseDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryUpdateRequestDto;
import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryUpdateResponseDto;
import com.baedalping.delivery.domain.product.productCategory.entity.ProductCategory;
import com.baedalping.delivery.domain.product.productCategory.repository.ProductCategoryRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductCategoryService {

  private final ProductCategoryRepository productCategoryRepository;

  @Transactional
  public ProductCategoryCreateResponseDto createProductCategory(ProductCategoryCreateRequestDto productCategoryCreateRequestDto) {
    String productCategoryName = productCategoryCreateRequestDto.getProductCategoryName();
    if(productCategoryRepository.findByProductCategoryName(productCategoryName).isPresent()) {
      new DeliveryApplicationException(ErrorCode.DUPLICATE_PRODUCT_CATEGORY_NAME);
    }

    ProductCategory productCategory = productCategoryRepository.save(new ProductCategory(productCategoryCreateRequestDto));
    return new ProductCategoryCreateResponseDto(productCategory);
  }

  @Transactional
  public ProductCategoryUpdateResponseDto updateProductCatgegory(UUID productCategoryId, ProductCategoryUpdateRequestDto productCategoryUpdateRequestDto) {
    ProductCategory productCategory = findById(productCategoryId);
    productCategory.setProductCategoryName(productCategoryUpdateRequestDto.getProductCategoryName());
    return new ProductCategoryUpdateResponseDto(productCategory);
  }

  @Transactional
  public void deleteProductCatgegory(UUID productCategoryId) {
    findById(productCategoryId).delete(null);
  }

  public ProductCategory findById(UUID productCategoryId) {
    return productCategoryRepository.findById(productCategoryId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT_CATEGORY)
    );
  }
}

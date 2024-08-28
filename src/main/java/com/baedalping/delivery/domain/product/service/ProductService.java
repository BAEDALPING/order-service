package com.baedalping.delivery.domain.product.service;


import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.product.dto.ProductCreateRequestDto;
import com.baedalping.delivery.domain.product.dto.ProductCreateResponseDto;
import com.baedalping.delivery.domain.product.repository.ProductRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.domain.productCategory.entity.ProductCategory;
import com.baedalping.delivery.domain.productCategory.repository.ProductCategoryRepository;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductCategoryRepository productCategoryRepository;
  private final StoreRepository storeRepository;

  @Transactional
  public ProductCreateResponseDto createProduct(ProductCreateRequestDto productCreateRequestDto) {
    UUID productCategoryId = productCreateRequestDto.getProductCategoryId();
    ProductCategory productCategory = productCategoryRepository.findById(productCategoryId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT_CATEGORY)
    );

    UUID storeId = productCreateRequestDto.getStoreId();
    Store store = storeRepository.findById(storeId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE)
    );

    Product product = productRepository.save(new Product(productCreateRequestDto, productCategory, store));
    return new ProductCreateResponseDto(product);
  }

}

package com.baedalping.delivery.product;


import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.productCategory.ProductCategory;
import com.baedalping.delivery.productCategory.ProductCategoryRepository;
import com.baedalping.delivery.store.Store;
import com.baedalping.delivery.store.StoreRepository;
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

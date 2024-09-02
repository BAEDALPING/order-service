package com.baedalping.delivery.domain.product.service;


import com.baedalping.delivery.domain.product.dto.ProductRequestDto;
import com.baedalping.delivery.domain.product.dto.ProductResponseDto;
import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.product.productCategory.entity.ProductCategory;
import com.baedalping.delivery.domain.product.productCategory.service.ProductCategoryService;
import com.baedalping.delivery.domain.product.repository.ProductRepository;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.service.StoreService;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.service.UserService;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductCategoryService productCategoryService;
  private final StoreService storeService;
  private final UserService userService;


  @Transactional
  public ProductResponseDto createProduct(ProductRequestDto ProductRequestDto) {
    ProductCategory productCategory = productCategoryService.findById(ProductRequestDto.getProductCategoryId());
    Store store = storeService.findById(ProductRequestDto.getStoreId());
    Product product = productRepository.save(new Product(ProductRequestDto, productCategory, store));
    return new ProductResponseDto(product);
  }

  @Transactional
  public ProductResponseDto updateProduct(UUID productId, ProductRequestDto ProductRequestDto, Long userId) {
    Product product = findById(productId);
    ProductCategory productCategory = productCategoryService.findById(ProductRequestDto.getProductCategoryId());
    Store store = storeService.findById(ProductRequestDto.getStoreId());

    User user = userService.findByUser(userId);
    if(!product.getCreatedBy().equals(user.getUsername())){
      throw new DeliveryApplicationException(ErrorCode.NOT_PERMITTED_OPTION);
    }

    product.updateProduct(ProductRequestDto, productCategory, store);
    return new ProductResponseDto(product);
  }

  @Transactional
  public void deleteProduct(UUID productId, Long userId) {
    Product product = findById(productId);
    User user = userService.findByUser(userId);

    if(!product.getCreatedBy().equals(user.getUsername())){
      throw new DeliveryApplicationException(ErrorCode.NOT_PERMITTED_OPTION);
    }

    findById(productId).delete(null);
  }

  @Transactional
  public ProductResponseDto getProduct(UUID productId) {
    return new ProductResponseDto(findById(productId));
  }

  @Transactional
  public Page<ProductResponseDto> getProducts(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Product> productList = productRepository.findAllByOrderByCreatedAtAscUpdatedAtAsc(pageable);
    return productList.map(ProductResponseDto::new);
  }

  @Transactional
  public List<ProductResponseDto> getProductByStoreId(UUID storeId) {
    Store store = storeService.findById(storeId);
    List<Product> productList = productRepository.findAllByStore(store);

    return productList.stream()
        .map(ProductResponseDto::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public List<ProductResponseDto> getProductSearch(String keyword) {
    List<Product> productList = productRepository.findAllByProductNameContaining(keyword);
    return productList.stream()
        .map(ProductResponseDto::new)
        .collect(Collectors.toList());
  }


  public Product findById(UUID productId) {
    return productRepository.findById(productId).orElseThrow(
        () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT)
    );
  }
}

package com.lcwd.electronic.store.services.interfaces;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    //create
    ProductDto createProduct(ProductDto productDto);
    //update
    ProductDto updateProduct(ProductDto productDto , String productId);
    //delete
    void deleteProduct(String productId);
    //get single
    ProductDto getProductById(String productId);
    //all product
    PagebleResponse<ProductDto> getAllProduct(int pageNumber,int pageSize , String sortBy , String sortDir);
    //get all: live
    PagebleResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize , String sortBy , String sortDir);
    //get all: not live
    PagebleResponse<ProductDto> getAllNotLiveProduct(int pageNumber,int pageSize , String sortBy , String sortDir);
    //search product
    PagebleResponse<ProductDto> searchProductByTitle(int pageNumber,int pageSize , String sortBy , String sortDir,String title);
    //create product with category
    ProductDto createWithCategory(ProductDto productDto,String categoryId);
    //assign category to product
    ProductDto updateProductCategory(String categoryId,String productId);
}

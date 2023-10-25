package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.services.interfaces.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
           @RequestBody ProductDto productDto
    ){
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity(product , HttpStatus.CREATED);
    };
    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
           @RequestBody ProductDto productDto ,
           @PathVariable String productId
    ){
         ProductDto product = productService.updateProduct(productDto , productId);
         return new ResponseEntity<>(product , HttpStatus.OK);
    };

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(
            @PathVariable String productId
    ){
        productService.deleteProduct(productId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("product is deleted")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage , HttpStatus.OK);
    };
    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable String productId
    ){
       ProductDto product = productService.getProductById(productId);
       return new ResponseEntity<>(product,HttpStatus.OK);
    };
    //all product
    @GetMapping
    public ResponseEntity<PagebleResponse<PagebleResponse>> getAllProduct(
           @RequestParam(required = false , defaultValue = "0") int pageNumber,
           @RequestParam(required = false , defaultValue = "20") int pageSize ,
           @RequestParam(required = false , defaultValue = "description") String sortBy ,
           @RequestParam(required = false , defaultValue = "asc") String sortDir
    ){
        PagebleResponse<ProductDto> allProduct = productService.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity(allProduct,HttpStatus.OK);
    };
    //get all: live
    @GetMapping("/live")
    public ResponseEntity<PagebleResponse<PagebleResponse>> getAllLiveProduct(
            @RequestParam(required = false , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , defaultValue = "20") int pageSize ,
            @RequestParam(required = false , defaultValue = "description") String sortBy ,
            @RequestParam(required = false , defaultValue = "asc") String sortDir
    ){
        PagebleResponse<ProductDto> allProduct = productService.getAllLiveProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity(allProduct,HttpStatus.OK);
    };

    //get all: not live
    @GetMapping("/inactive")
    public ResponseEntity<PagebleResponse<PagebleResponse>> getAllNotLiveProduct(
            @RequestParam(required = false , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , defaultValue = "20") int pageSize ,
            @RequestParam(required = false , defaultValue = "description") String sortBy ,
            @RequestParam(required = false , defaultValue = "asc") String sortDir
    ){
        PagebleResponse<ProductDto> allProduct = productService.getAllNotLiveProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity(allProduct,HttpStatus.OK);
    };
    //search product
    @GetMapping("/title/{title}")
    public ResponseEntity<PagebleResponse<PagebleResponse>> searchProductByTitle(
            @RequestParam(required = false , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , defaultValue = "20") int pageSize ,
            @RequestParam(required = false , defaultValue = "description") String sortBy ,
            @RequestParam(required = false , defaultValue = "asc") String sortDir,
            @PathVariable String title
    ){
        PagebleResponse<ProductDto> allProduct = productService.searchProductByTitle(pageNumber,pageSize,sortBy,sortDir,title);
        return new ResponseEntity(allProduct,HttpStatus.OK);
    };

    @PostMapping("/{categoryId}")
    public ResponseEntity<Product> createWithCategory(
            @PathVariable String categoryId,
            @RequestBody ProductDto productDto
    ){
        ProductDto savedProduct = productService.createWithCategory(productDto,categoryId);
        return new ResponseEntity(savedProduct , HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/{productId}")
    public ResponseEntity<ProductDto> updateProductCategory(
            @PathVariable String categoryId,
            @PathVariable String productId
    ){
         ProductDto productDto = productService.updateProductCategory(categoryId,productId);
         return new ResponseEntity(productDto , HttpStatus.OK);
    }

}

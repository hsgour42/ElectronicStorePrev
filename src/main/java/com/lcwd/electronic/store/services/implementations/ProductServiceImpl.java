package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.interfaces.CategoryService;
import com.lcwd.electronic.store.services.interfaces.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper mapper;

    Logger logger  = LoggerFactory.getLogger(ProductServiceImpl.class);

    //create
    @Override
    public ProductDto createProduct(ProductDto productDto) {
         String productId = UUID.randomUUID().toString();
         productDto.setProductId(productId);
         productDto.setAddedDate(new Date());
         Product product = mapper.map(productDto , Product.class);
         Product savedProduct = productRepository.save(product);
         return mapper.map(savedProduct , ProductDto.class);
    }

    //update
    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product oldProduct= productRepository.findById(productId).orElseThrow(() -> new RuntimeException("invalid product id"));
        //update
        oldProduct.setTitle(productDto.getTitle());
        oldProduct.setDescription(productDto.getDescription());
        oldProduct.setPrice(productDto.getPrice());
        oldProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        oldProduct.setQuantity(productDto.getQuantity());
        oldProduct.setLive(productDto.isLive());
        oldProduct.setStock(productDto.isStock());
        Product product = productRepository.save(oldProduct);
        return mapper.map(product,ProductDto.class);
    }

    //delete
    @Override
    public void deleteProduct(String productId) {
        Product oldProduct= productRepository.findById(productId).orElseThrow(() -> new RuntimeException("invalid product id"));
        productRepository.delete(oldProduct);
    }

    //search by id
    @Override
    public ProductDto getProductById(String productId) {
        Product product= productRepository.findById(productId).orElseThrow(() -> new RuntimeException("invalid product id"));
        return mapper.map(product,ProductDto.class);
    }

    //all products
    @Override
    public PagebleResponse<ProductDto> getAllProduct(int pageNumber,int pageSize , String sortBy , String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber , pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        return Helper.getPageableResponse(page , ProductDto.class);
    }

    //all live products
    @Override
    public PagebleResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize , String sortBy , String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber , pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page , ProductDto.class);
    }

    //all non live products
    @Override
    public PagebleResponse<ProductDto> getAllNotLiveProduct(int pageNumber,int pageSize , String sortBy , String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber , pageSize, sort);
        Page<Product> page = productRepository.findByLiveFalse(pageable);
        return Helper.getPageableResponse(page , ProductDto.class);
    }

    //get product by title
    @Override
    public PagebleResponse<ProductDto> searchProductByTitle(int pageNumber,int pageSize , String sortBy , String sortDir,String title) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber , pageSize, sort);
        Page<Product> page = productRepository.findByTitleContaining(title ,pageable);
        return Helper.getPageableResponse(page , ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        //first fetch the category
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        Category category = mapper.map(categoryDto, Category.class);

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());

        Product product = mapper.map(productDto , Product.class);
        //set category
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct , ProductDto.class);
    }

    @Override
    public ProductDto updateProductCategory(String categoryId, String productId) {
        //fetch the category
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        Category category = mapper.map(categoryDto, Category.class);

        ProductDto productDto= getProductById(productId);
        Product product = mapper.map(productDto , Product.class);
        product.setCategory(category);
        Product savedProduct= productRepository.save(product);
        return mapper.map(savedProduct , ProductDto.class);
    }
}

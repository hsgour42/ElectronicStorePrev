package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.ImageResponse;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.services.interfaces.CategoryService;
import com.lcwd.electronic.store.services.interfaces.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.category.path}")
    private String imageUploadPath;


    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
           @Valid @RequestBody CategoryDto categoryDto
    ) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return  new ResponseEntity<>(category , HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
           @Valid @RequestBody CategoryDto categoryDto,
           @PathVariable String categoryId
    ) {
         CategoryDto updatedCategory = categoryService.updateCategory(categoryDto,categoryId);
        return new ResponseEntity(updatedCategory , HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(
           @PathVariable String categoryId
    ) throws IOException {
         categoryService.deleteCategory(categoryId);
         ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("category is deleted")
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();
         return new ResponseEntity<>(apiResponseMessage , HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagebleResponse<Category>> getAllCategory(
            @RequestParam(defaultValue = "0",required = false) int pageNumber,
            @RequestParam(defaultValue = "20",required = false) int pageSize,
            @RequestParam(defaultValue = "title",required = false) String sortBy,
            @RequestParam(defaultValue = "asc",required = false) String sortDir
    ) {
         PagebleResponse<CategoryDto> allCategory = categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortDir);
         return new ResponseEntity(allCategory,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable String categoryId
    ){
         CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
         return new ResponseEntity(categoryDto , HttpStatus.OK);
    }

    //upload file
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadFile(
            @RequestParam("categoryImage") MultipartFile image,
            @PathVariable("categoryId") String categoryId
    ) throws IOException {
         ImageResponse imageResponse = categoryService.uploadFile(image , categoryId);
         return new ResponseEntity(imageResponse , HttpStatus.CREATED);
    }



    //get file
    @GetMapping("/image/{categoryId}")
    public void getFile(
            @PathVariable String categoryId,
            HttpServletResponse response

    ) throws IOException {
        InputStream file = categoryService.getFile(categoryId);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(file , response.getOutputStream());
    };
}

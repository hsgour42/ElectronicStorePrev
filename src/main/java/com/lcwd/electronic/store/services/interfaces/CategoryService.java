package com.lcwd.electronic.store.services.interfaces;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.ImageResponse;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface CategoryService {
    //create
    CategoryDto createCategory(CategoryDto categoryDto);
    //update
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);
    //delete
    void deleteCategory(String categoryId) throws IOException;
    //get all
    PagebleResponse<CategoryDto> getAllCategory(int pageNumber ,int pageSize,String sortBy, String sortDir);
    //get by id
    CategoryDto getCategoryById(String categoryId);


    //upload file
    ImageResponse uploadFile(MultipartFile multipartFile,String categoryId) throws IOException;
    //get file
    InputStream getFile(String categoryId) throws FileNotFoundException;



}

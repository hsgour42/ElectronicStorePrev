package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.ImageResponse;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.interfaces.CategoryService;
import com.lcwd.electronic.store.services.interfaces.FileService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Value("${user.profile.image.category.path}")
    private String imageUploadPath;

    @Autowired
            private ModelMapper mapper;

    @Autowired
            private CategoryRepository categoryRepository;

    @Autowired
            private FileService fileService;



    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    //create
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto , Category.class);
        Category savedCategory= categoryRepository.save(category);
        return mapper.map(savedCategory , CategoryDto.class);
    }

    //update
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
          Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No Catefory avialable with give categoryId"));
          category.setCoverImage(categoryDto.getCoverImage());
          category.setDescription(categoryDto.getDescription());
          category.setTitle(categoryDto.getTitle());
          Category updatedCategory = categoryRepository.save(category);
          return mapper.map(updatedCategory,CategoryDto.class);
    }

    //delete
    @Override
    public void deleteCategory(String categoryId) throws IOException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No Catefory avialable with give categoryId"));
        //delete image
        String coverImage = category.getCoverImage();
        String fullPath = imageUploadPath + coverImage;
        Path path  = Paths.get(fullPath);
        Boolean file = Files.deleteIfExists(path);
        //delete category
        categoryRepository.delete(category);
    }

    //get all
    @Override
    public PagebleResponse<CategoryDto> getAllCategory(int pageNumber ,int pageSize,String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending());
        Pageable pageable  = PageRequest.of(pageNumber , pageSize, sort);
        Page<Category> categoryPageable = categoryRepository.findAll(pageable);
        return Helper.getPageableResponse(categoryPageable , CategoryDto.class);
    }

    //get by id
    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No Catefory avialable with give categoryId"));
        return mapper.map(category , CategoryDto.class);
    }

    @Override
    public ImageResponse uploadFile(MultipartFile multipartFile, String categoryId) throws IOException {
        //save image
        String imageName = fileService.uploadFile(multipartFile , imageUploadPath);
        //update category
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No Catefory avialable with give categoryId"));
        category.setCoverImage(imageName);
        categoryRepository.save(category);

        return ImageResponse.builder()
                .imageName(imageName)
                .httpStatus(HttpStatus.CREATED)
                .message("Image uploaded successfully")
                .success(true)
                .build();
    }

    @Override
    public InputStream getFile(String categoryId) throws FileNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No Catefory avialable with give categoryId"));
        String imageName = category.getCoverImage();
        return fileService.getResource(imageUploadPath , imageName);

    }
}

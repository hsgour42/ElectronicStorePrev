package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.dtoResponseHelper.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.ImageResponse;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.interfaces.FileService;
import com.lcwd.electronic.store.services.interfaces.UserService;
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
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    public UserController() {
    }

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto userDto
    ){
        UserDto createdUserDto = userService.createUser(userDto);
       return new ResponseEntity<>(createdUserDto , HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId,
            @Valid @RequestBody UserDto userDto
    ){
        UserDto updatedUserDto = userService.updateUser(userDto ,userId);
        return new ResponseEntity<>(updatedUserDto , HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage>  deleteUser(
            @PathVariable("userId") String userId
    ) throws IOException {
         userService.deleteUser(userId);
         ApiResponseMessage apiResponseMessage = ApiResponseMessage
                 .builder()
                 .message("User is deleted successfully !!")
                 .httpStatus(HttpStatus.OK).success(true)
                 .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PagebleResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber",defaultValue = "0" ,required = false) int pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = "2"  ,required = false) int pageSize,
            @RequestParam(value = "sortBy" ,defaultValue = "name"  ,required = false) String sortBy,
            @RequestParam(value = "sortDir" ,defaultValue = "asc"  ,required = false) String sortDir
    ){
        PagebleResponse<UserDto> userDtoList = userService.getAllUser(pageNumber , pageSize ,sortBy , sortDir);
        return new ResponseEntity<>(userDtoList , HttpStatus.OK);
    }

    //get by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable("userId") String userId
    ){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{userEmail}")
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable("userEmail") String userEmail
    ){
        UserDto userDto = userService.getUserByEmail(userEmail);
        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(
            @PathVariable("keywords") String keyword
    ){
        List<UserDto> userDtoList = userService.searchUser(keyword);
        return new ResponseEntity<>(userDtoList , HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,
            @PathVariable("userId") String userId

    ) throws IOException {
        String fileName = fileService.uploadFile(image,imageUploadPath);
        //update user
        UserDto userDto = userService.getUserById(userId);
        userDto.setImageName(fileName);
        UserDto user = userService.updateUser(userDto , userId);
        logger.info("controller update userDto : {}" , userDto);
        logger.info("controller update user : {}" , user);


        //upload file
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(fileName)
                .httpStatus(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity(imageResponse , HttpStatus.CREATED);
    }


    //fetch user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
          UserDto userDto = userService.getUserById(userId);
          String name =  userDto.getImageName();
          InputStream resource = fileService.getResource(imageUploadPath , name);
          response.setContentType(MediaType.IMAGE_PNG_VALUE);
          StreamUtils.copy(resource,response.getOutputStream());
    }

}

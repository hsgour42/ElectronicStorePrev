package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public UserDto createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        return entityToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
         User oldUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user avialable"));
         oldUser.setName(userDto.getName());
         oldUser.setGender(userDto.getGender());
         oldUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
         oldUser.setImageName(userDto.getImageName());
         User user = userRepository.save(oldUser);
         logger.info("update user : {}" , user);
         return entityToDto(user);
    }

    @Override
    public void deleteUser(String userId) throws IOException {
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user avialable"));

        String userImageName = oldUser.getImageName();
        String fullImagePath = imageUploadPath + userImageName;

        try{
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        }catch (NoSuchFileException e){
            logger.info("user image not found");
            e.printStackTrace();
        }

        userRepository.delete(oldUser);
    }

    @Override
    public PagebleResponse<UserDto> getAllUser(int pageNumber , int pageSize, String sortBy, String sortDir) {
        Sort sortby = (sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() );
        Pageable pageable = PageRequest.of(pageNumber , pageSize , sortby);
        Page<User> page = userRepository.findAll(pageable);
        return Helper.getPageableResponse(page , UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user avialable"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("No user avialable"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
         List<User> userList= userRepository.findByNameContaining(keyword);
         return userList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private UserDto entityToDto(User savedUser) {
//         return UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();

        return modelMapper.map(savedUser , UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        return User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
        return  modelMapper.map(userDto , User.class);
    }
}

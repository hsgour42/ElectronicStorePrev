package com.lcwd.electronic.store.services.interfaces;

import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import com.lcwd.electronic.store.dtos.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userid);

    //delete
    void deleteUser(String userId) throws IOException;

    //get all user
    PagebleResponse<UserDto> getAllUser(int pageNumber , int pageSize, String sortBy , String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String userEmail);

    //search user
    List<UserDto> searchUser(String keyword);

    //other user specific features
}

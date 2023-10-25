package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min = 3 , max = 50,message = "invalid name!!")
    private String name;
    //@Email(message = "invalid user email!!")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "invalid user email!!")
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    @Size(min = 4 , max = 6,message = "invalid gender!!")
    private String gender;
    @NotBlank(message = "write something about yourself")
    private String about;

    @ImageNameValid
    private String imageName;
    private String role;

    //@Pattern()
    //CustomValidatorBean
}

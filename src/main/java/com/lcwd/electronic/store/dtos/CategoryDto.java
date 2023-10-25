package com.lcwd.electronic.store.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;
    @NotBlank
    @Size(min = 4 , message = "title must be of minimum 4 character")
    private String title;
    @NotBlank(message = "description required")
    private String description;

    private String coverImage;

}

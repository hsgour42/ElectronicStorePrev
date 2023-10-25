package com.lcwd.electronic.store.dtos.dtoResponseHelper;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMessage {
    private String message;
    public Boolean success;
    private HttpStatus httpStatus;
}

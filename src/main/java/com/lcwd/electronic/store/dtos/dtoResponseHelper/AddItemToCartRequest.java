package com.lcwd.electronic.store.dtos.dtoResponseHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddItemToCartRequest {
    private String productId;
    private int quantity;


}

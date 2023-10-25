package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.ApiResponseMessage;
import com.lcwd.electronic.store.services.interfaces.CartService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;
    Logger logger = LoggerFactory.getLogger(CartController.class);

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(
           @PathVariable String userId,
           @RequestBody AddItemToCartRequest request
    ){
        CartDto cartDto = cartService.addItemToCart(userId,request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    };

    //remove item from cart
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
           @PathVariable int cartItemId
    ){
         cartService.removeItemFromCart(cartItemId);
         ApiResponseMessage responseMessage =ApiResponseMessage.builder()
                 .message("Item is removed")
                 .success(true)
                 .httpStatus(HttpStatus.CREATED)
                 .build();
         return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    };

    //remove or clear cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponseMessage>  clearCart(
           @PathVariable String userId
    ){
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Now cart is cleared")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response ,HttpStatus.OK);
    };

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(
           @PathVariable String userId
    ){
            logger.info("inside get Cart User");
            CartDto cart = cartService.getCartByUser(userId);
            return new ResponseEntity<>(cart , HttpStatus.OK);
    };


}

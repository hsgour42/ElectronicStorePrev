package com.lcwd.electronic.store.services.interfaces;

import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.AddItemToCartRequest;

public interface CartService {
    //add item to cart :
    //case 1 : if cart is not available for user first create cart and then add the item
    //case 2 : cart available create cart item
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(int cartItemId);

    //remove or clear cart
    void  clearCart(String userId);

    CartDto getCartByUser(String userId);


}

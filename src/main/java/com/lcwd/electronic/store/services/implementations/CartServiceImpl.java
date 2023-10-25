package com.lcwd.electronic.store.services.implementations;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.dtos.dtoResponseHelper.AddItemToCartRequest;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.services.interfaces.CartService;
import com.lcwd.electronic.store.services.interfaces.ProductService;
import com.lcwd.electronic.store.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;


    Logger logger  = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if(quantity <= 0){
            throw new BadApiRequestException("Request quantity is not valid");
        }
        //fetch product
        ProductDto productDto = productService.getProductById(productId);
        Product product = mapper.map(productDto,Product.class);
        //fetch user
        UserDto userDto = userService.getUserById(userId);
        User user = mapper.map(userDto,User.class);

        //handle cart
        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException ex){
            cart = new Cart();
            cart.setCreatedAt(new Date());
            cart.setUser(user);
        }

        //if cart item already present increase price and quantity only
        AtomicBoolean itemUpdated = new AtomicBoolean(false);
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItem> updatedCartItemList;
        updatedCartItemList = cartItems.stream().map(item -> {
            if(item.getProduct().getProductId().equals(productId)){
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                itemUpdated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        //create items
        if(!itemUpdated.get()){
            CartItem newCartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getCartItems().add(newCartItem);
        }else {
            cart.setCartItems(updatedCartItemList);
        }


        //perform cart operation
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart , CartDto.class);

    }

    @Override
    public void removeItemFromCart(int cartItemId) {
        CartItem cartItem  = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("item not avialable"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
         UserDto userDto = userService.getUserById(userId);
         User user = mapper.map(userDto,User.class);
         Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Given user has no item"));
         cart.getCartItems().clear();
         cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        UserDto userDto = userService.getUserById(userId);
        User user = mapper.map(userDto,User.class);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Given user has no item"));
        return mapper.map(cart,CartDto.class);
    }
}

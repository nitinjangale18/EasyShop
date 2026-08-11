package com.amazone_clone.cart_service.service;

import com.amazone_clone.cart_service.dto.AddToCartRequest;
import com.amazone_clone.cart_service.entity.Cart;
import com.amazone_clone.cart_service.entity.CartItem;
import com.amazone_clone.cart_service.repository.CartItemRepository;
import com.amazone_clone.cart_service.repository.CartRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.amazone_clone.cart_service.dto.CartResponse;
import com.amazone_clone.cart_service.dto.UpdateCartItemRequest;
import com.amazone_clone.cart_service.dto.CartItemResponse;

import java.util.List;
import com.amazone_clone.cart_service.client.ProductClient;
import com.amazone_clone.cart_service.dto.ProductResponse;
import feign.FeignException;

import com.amazone_clone.cart_service.dto.CartItemResponse;
import com.amazone_clone.cart_service.dto.CartResponse;
import com.amazone_clone.cart_service.dto.ProductResponse;

import java.util.List;

import com.amazone_clone.cart_service.exception.InsufficientStockException;
import com.amazone_clone.cart_service.exception.ProductNotFoundException;
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository, ProductClient productClient
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productClient = productClient;

    }

    @Transactional
    public void addToCart(
            String userEmail,
            AddToCartRequest request
    ) {
    	
    	
    	
    	
    	
    	ProductResponse product;

    	try {

    	    product = productClient.getProductById(
    	            request.getProductId()
    	    );

    	} catch (FeignException.NotFound exception) {

    		throw new ProductNotFoundException("Product not found");
    	}

    	if (product == null) {
    	    throw new RuntimeException("Product not found");
    	}

    	if (product.getStock() <= 0) {
    		throw new InsufficientStockException(
    		        "Requested quantity exceeds available stock"
    		);    	}

    	if (request.getQuantity() > product.getStock()) {
    	    throw new InsufficientStockException(
    	            "Requested quantity exceeds available stock"
    	    );
    	}
    	
    	

        // Find user's cart, or create one if it doesn't exist
        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseGet(() ->
                        cartRepository.save(
                                new Cart(userEmail)
                        )
                );

        // Check whether this product is already in the cart
        CartItem cartItem = cartItemRepository
                .findByCartAndProductId(
                        cart,
                        request.getProductId()
                )
                .orElse(null);

        if (cartItem != null) {

            int newQuantity =
                    cartItem.getQuantity()
                            + request.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new RuntimeException(
                        "Total cart quantity exceeds available stock"
                );
            }

            cartItem.setQuantity(newQuantity);

        } else {

            // New product -> create cart item
            cartItem = new CartItem(
                    request.getProductId(),
                    request.getQuantity(),
                    cart
            );
        }

        cartItemRepository.save(cartItem);
    }
    
    @Transactional(readOnly = true)
    public CartResponse getCart(String userEmail) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElse(null);

        if (cart == null) {
            return new CartResponse(
                    null,
                    userEmail,
                    List.of(),
                    0.0
            );
        }

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(item -> {

                    ProductResponse product =
                            productClient.getProductById(
                                    item.getProductId()
                            );

                    double subtotal =
                            product.getPrice()
                                    * item.getQuantity();

                    return new CartItemResponse(
                            item.getId(),
                            item.getProductId(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl(),
                            item.getQuantity(),
                            subtotal
                    );
                })
                .toList();

        double total = items.stream()
                .mapToDouble(CartItemResponse::getSubtotal)
                .sum();

        return new CartResponse(
                cart.getId(),
                cart.getUserEmail(),
                items,
                total
        );
    }
    
    @Transactional
    public void updateQuantity(
            String userEmail,
            Long productId,
            UpdateCartItemRequest request
    ) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        CartItem cartItem = cartItemRepository
                .findByCartAndProductId(cart, productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in cart")
                );

        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);
    }
    
    
    @Transactional
    public void removeItem(
            String userEmail,
            Long productId
    ) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        CartItem cartItem = cartItemRepository
                .findByCartAndProductId(cart, productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in cart")
                );

        cartItemRepository.delete(cartItem);
    }
    
    
    @Transactional
    public void clearCart(String userEmail) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        cart.getItems().clear();

        cartRepository.save(cart);
    }
    
}
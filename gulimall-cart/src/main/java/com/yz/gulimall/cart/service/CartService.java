package com.yz.gulimall.cart.service;

import com.yz.gulimall.cart.vo.Cart;
import com.yz.gulimall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {
    Cart getCart() throws ExecutionException, InterruptedException;

    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    void deleteItem(Long skuId);

    void checkItem(Long skuId, Integer check);

    void changeItemCount(Long skuId, Integer num);
}

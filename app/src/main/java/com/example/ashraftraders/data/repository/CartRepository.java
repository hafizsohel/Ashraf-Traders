package com.example.ashraftraders.data.repository;


import androidx.lifecycle.LiveData;

import com.example.ashraftraders.data.room.CartDao;
import com.example.ashraftraders.data.room.entity.CartEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {

    private final CartDao cartDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CartRepository(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    public LiveData<List<CartEntity>> getCartItems() {
        return cartDao.getAllCartItems();
    }

    public LiveData<Integer> getCartCount() {
        return cartDao.getCartCount();
    }

    public LiveData<Double> getGrandTotal() {
        return cartDao.getGrandTotal();
    }

    public void addToCart(CartEntity item) {

        executor.execute(() -> {

            CartEntity exist = cartDao.getCartItem(item.getId());

            if (exist == null) {

                cartDao.insert(item);

            } else {

                cartDao.updateQuantity(
                        item.getId(),
                        exist.getQuantity() + 1
                );
            }
        });
    }

    public void increaseQuantity(CartEntity item) {

        executor.execute(() ->
                cartDao.updateQuantity(
                        item.getId(),
                        item.getQuantity() + 1));
    }

    public void decreaseQuantity(CartEntity item) {

        executor.execute(() -> {

            if (item.getQuantity() <= 1) {

                cartDao.deleteById(item.getId());

            } else {

                cartDao.updateQuantity(
                        item.getId(),
                        item.getQuantity() - 1
                );
            }
        });
    }

    public void removeItem(int id) {

        executor.execute(() ->
                cartDao.deleteById(id));
    }

    public void clearCart() {
        executor.execute(() -> cartDao.clearCart());
    }
}
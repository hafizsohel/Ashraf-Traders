package com.example.ashraftraders.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ashraftraders.data.room.entity.CartEntity;

import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cart ORDER BY productName ASC")
    LiveData<List<CartEntity>> getAllCartItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartEntity cart);

    @Update
    void update(CartEntity cart);

    @Query("DELETE FROM cart WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM cart")
    void clearCart();

    @Query("SELECT * FROM cart WHERE id = :id LIMIT 1")
    CartEntity getCartItem(int id);

    @Query("UPDATE cart SET quantity = :qty WHERE id = :id")
    void updateQuantity(int id, int qty);

    @Query("SELECT COUNT(*) FROM cart")
    LiveData<Integer> getCartCount();

    @Query("SELECT IFNULL(SUM(price * quantity),0) FROM cart")
    LiveData<Double> getGrandTotal();

    @Query("SELECT EXISTS(SELECT 1 FROM cart WHERE id = :id)")
    boolean exists(int id);

    @Query("UPDATE cart SET quantity = quantity + 1 WHERE id = :id")
    void increaseQuantity(int id);

    @Query("UPDATE cart SET quantity = quantity - 1 WHERE id = :id AND quantity > 1")
    void decreaseQuantity(int id);

}
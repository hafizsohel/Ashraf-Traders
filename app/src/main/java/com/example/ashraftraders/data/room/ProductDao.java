package com.example.ashraftraders.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductEntity product);

    // ফিক্স: একবারে পুরো লিস্ট ইনসার্ট করার মেথড (ফ্লিকারিং বন্ধ করতে এটি ম্যাজিকের মতো কাজ করবে)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductEntity> products);

    @Update
    void update(ProductEntity product);

    @Delete
    void delete(ProductEntity product);

    @Query("DELETE FROM products")
    void deleteAll();

    @Query("SELECT * FROM products ORDER BY productName ASC")
    LiveData<List<ProductEntity>> getAllProducts();

    @Query("SELECT COUNT(*) FROM products")
    LiveData<Integer> getTotalProductsCount();

    @Query("SELECT COUNT(DISTINCT brandName) FROM products")
    LiveData<Integer> getTotalBrandsCount();

    @Query("SELECT COALESCE(SUM(purchasePrice * stock), 0) FROM products")
    LiveData<Double> getTotalPurchaseAmount();

    @Query("SELECT COUNT(*) FROM products WHERE stock <= 10 AND stock > 0")
    LiveData<Integer> getLowStockCount();

    @Query("SELECT DISTINCT productName FROM products ORDER BY productName ASC")
    List<String> getProductNamesSync();
}
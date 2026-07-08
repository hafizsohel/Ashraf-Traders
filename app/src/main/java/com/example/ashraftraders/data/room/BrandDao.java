package com.example.ashraftraders.data.room;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BrandDao {

    @Query("SELECT brand_name FROM brands ORDER BY brand_name ASC")
    List<String> getAllBrandNames();
}
package com.example.ashraftraders.data.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "brands")
public class BrandEntity {

    @PrimaryKey
    private long id;

    @ColumnInfo(name = "brand_name")
    private String brandName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
package com.example.ashraftraders.data.model;
import com.example.ashraftraders.data.room.entity.CartEntity;

public class CartMapper {

    public static CartEntity fromProduct(ProductModel product){

        CartEntity cart = new CartEntity();

        cart.setId(Math.toIntExact(product.getId()));
        cart.setProductCode(product.getProduct_code());
        cart.setProductName(product.getProductName());
        cart.setBrandName(product.getBrandName());

        cart.setPrice(product.getPurchasePrice());

        cart.setQuantity(1);

        cart.setStock(product.getStock());

        return cart;
    }

}
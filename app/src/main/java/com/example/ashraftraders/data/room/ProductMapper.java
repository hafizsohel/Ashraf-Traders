package com.example.ashraftraders.data.room;

import com.example.ashraftraders.data.model.ProductModel;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    private ProductMapper(){}

    public static ProductEntity toEntity(ProductModel model){

        return new ProductEntity(

                model.getId(),

                model.getProduct_code(),

                model.getProductName(),

                model.getBrandName(),

                model.getPurchasePrice(),

                model.getStock()

        );

    }

    public static List<ProductEntity> toEntityList(List<ProductModel> models){

        List<ProductEntity> list=new ArrayList<>();

        for(ProductModel model:models){

            list.add(toEntity(model));

        }

        return list;

    }

    public static ProductModel toModel(ProductEntity entity){

        ProductModel model=new ProductModel();

        model.setId(entity.getId());

        model.setProduct_code(entity.getProductCode());

        model.setProductName(entity.getProductName());

        model.setBrandName(entity.getBrandName());

        model.setPurchasePrice(entity.getPurchasePrice());

        model.setStock(entity.getStock());

        return model;

    }

    public static List<ProductModel> toModelList(List<ProductEntity> entities){

        List<ProductModel> list=new ArrayList<>();

        for(ProductEntity entity:entities){

            list.add(toModel(entity));

        }

        return list;

    }

}
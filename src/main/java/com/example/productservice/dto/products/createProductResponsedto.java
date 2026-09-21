package com.example.productservice.dto.products;

import com.example.productservice.models.productModel;
import com.example.productservice.models.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class createProductResponsedto {
   private Long id;
    private String title;
    private String description;
    private Double price;
    private Long priceMinor;
    private String currency;
    private Long version;
    private String category;
    private String ImageUrl;


    public static createProductResponsedto fromproductModel(productModel productModel){
        createProductResponsedto responsedto= new createProductResponsedto();
        responsedto.setId(productModel.getId());
        responsedto.setDescription(productModel.getDescription());
        responsedto.setTitle(productModel.getTitle());
        responsedto.setPrice(productModel.getPrice());
        responsedto.setPriceMinor(productModel.getPriceMinor());
        responsedto.setCurrency(productModel.getCurrency());
        responsedto.setVersion(productModel.getVersion());
        responsedto.category = productModel.getCategory().getName();
       return responsedto;
    }


}

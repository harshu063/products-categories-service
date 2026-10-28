package com.example.productservice.dto.products;

import com.example.productservice.models.Category;
import com.example.productservice.models.productModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class createProductRequestdto {
    private String title;
    private String description;
    private Double price;
    private Long priceMinor;
    private String currency;
    private String categoryName;


    public productModel toproductModel(){
        productModel productModel = new productModel();
        productModel.setTitle(this.title);
        productModel.setDescription(this.description);
        if (this.priceMinor != null) {
            productModel.setPriceMinor(this.priceMinor);
        } else {
            productModel.setPrice(this.price);
        }
        productModel.setCurrency(this.currency == null ? "INR" : this.currency);
        Category  category = new Category();
        category.setName(categoryName);
        productModel.setCategory(category);
//        productModel.setCategory(this.Category);
        return productModel;
    }
}

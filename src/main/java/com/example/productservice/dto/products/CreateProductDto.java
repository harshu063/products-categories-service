package com.example.productservice.dto.products;

import com.example.productservice.models.Category;
import com.example.productservice.models.productModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long priceMinor;
    private String currency;
    private Long version;
    private String imageUrl;
    private String categoryName;

    public static CreateProductDto fromProduct(productModel product) {
        CreateProductDto responseDto = new CreateProductDto();
        responseDto.setId(product.getId());
        responseDto.setDescription(product.getDescription());
        responseDto.setTitle(product.getTitle());
        responseDto.setPrice(product.getPrice());
        responseDto.setPriceMinor(product.getPriceMinor());
        responseDto.setCurrency(product.getCurrency());
        responseDto.setVersion(product.getVersion());
        responseDto.setImageUrl(product.getImageURL());

        return responseDto;
    }

    public productModel toProduct() {
        productModel product = new productModel();
        product.setTitle(this.title);
        product.setDescription(this.description);
        if (this.priceMinor != null) {
            product.setPriceMinor(this.priceMinor);
        } else {
            product.setPrice(this.price);
        }
        product.setCurrency(this.currency);
        product.setImageURL(this.imageUrl);
        Category category = new Category();
        category.setName(categoryName);
        product.setCategory(category);

        return product;
    }
}


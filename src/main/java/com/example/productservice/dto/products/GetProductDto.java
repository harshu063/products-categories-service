package com.example.productservice.dto.products;


import com.example.productservice.models.productModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetProductDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private Long priceMinor;
    private String currency;
    private Long version;
    private String imageUrl;

    public static GetProductDto from(productModel product) {
        GetProductDto getProductResponseDto = new GetProductDto();
        getProductResponseDto.setId(product.getId());
        getProductResponseDto.setDescription(product.getDescription());
        getProductResponseDto.setImageUrl(product.getImageURL());
        getProductResponseDto.setPrice(product.getPrice());
        getProductResponseDto.setPriceMinor(product.getPriceMinor());
        getProductResponseDto.setCurrency(product.getCurrency());
        getProductResponseDto.setVersion(product.getVersion());
        getProductResponseDto.setTitle(product.getTitle());
        return getProductResponseDto;
    }
}
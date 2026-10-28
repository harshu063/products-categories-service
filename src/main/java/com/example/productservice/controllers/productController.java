package com.example.productservice.controllers;

import com.example.productservice.dto.products.*;
import com.example.productservice.models.productModel;
import com.example.productservice.service.productService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class productController {

    //    @Value("${productService")
//    private String productServiceType;

    private productService productService;

//    public productController (@Qualifier("fakeStoreProductService") productService productService){
//        this.productService=productService;
//
//    }

        public productController (productService productService){
        this.productService=productService;

    }

        @PostMapping()
        public createProductResponsedto creatProduct(@RequestBody createProductRequestdto createProductRequestdto){
//        return "This product is priced at " +createProductRequestdto.getPrice();
            productModel productModel= productService.createProduct(
                    createProductRequestdto.toproductModel()
            );
            return createProductResponsedto.fromproductModel(
                   productModel
            );
    }

        @DeleteMapping()
        public void deleteProduct(@PathVariable("id") Long id) {

        }


         @GetMapping("/{id}")
         public GetProductDto GetSingleProduct( @PathVariable Long id){
            productModel product = productService.getProductById(id);
            return GetProductDto.from(product);
//            return "your product is here :" +id;
    }
        @GetMapping
            public List <getAllProductsResponseDto> getAllProducts(){
             List<productModel> Products = productService.getAllProducts();
             List<getAllProductsResponseDto>getAllProductsResponseDtos= new ArrayList<>();
                for( productModel product : Products){
                    getAllProductsResponseDtos.add(getAllProductsResponseDto.from(product));
        }

                return getAllProductsResponseDtos;
    }
        @PatchMapping("/{id}")
        public PatchProductResponsedto updateProduct(
                @PathVariable("id") Long productId,
                @RequestBody CreateProductDto productDto) {

            productModel product = productService.partialUpdateProduct(
                    productId,
                    productDto.toProduct());
            if(product==null){
                throw new RuntimeException("update is failed");
            }

            PatchProductResponsedto response = new PatchProductResponsedto();
            response.setProduct(GetProductDto.from(product));


       return response;
    }
    @PutMapping("")
    public productModel replaceProduct(
            @PathVariable Long id,
            @RequestBody productModel product
    ) {
        return productService.replaceProduct(id, product);
    }


    }


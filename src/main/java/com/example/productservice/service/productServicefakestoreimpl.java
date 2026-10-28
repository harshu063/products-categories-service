package com.example.productservice.service;

import com.example.productservice.Exceptions.ProductNotFoundException;
import com.example.productservice.dto.fakeStore.fakeStorebodyRequestdto;
import com.example.productservice.dto.fakeStore.fakeStoreGetbodyResponsedto;
import com.example.productservice.models.productModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class productServicefakestoreimpl implements productService {
    private RestTemplate restTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PRODUCT_CACHE_KEY = "PRODUCTS";


    public productServicefakestoreimpl(RestTemplate restTemplate,
                                       RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public productModel createProduct(productModel productModel) {
        fakeStorebodyRequestdto fakeStorebodyRequestdto = new fakeStorebodyRequestdto();

        fakeStorebodyRequestdto.setCategory(productModel.getCategory().getName());
        fakeStorebodyRequestdto.setTitle(productModel.getTitle());
        fakeStorebodyRequestdto.setPrice(productModel.getPrice());
        fakeStorebodyRequestdto.setImage(productModel.getImageURL());
        fakeStorebodyRequestdto.setDescription(productModel.getDescription());


        fakeStoreGetbodyResponsedto response = restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                fakeStorebodyRequestdto,
                fakeStoreGetbodyResponsedto.class

        );
        return response.toProduct();
    }

    @Override
    public List<productModel> getAllProducts() {
        throw new RuntimeException("kjdkwkd wekjnewkjw");
//           fakeStoreGetbodyResponsedto[] response= restTemplate.getForObject(
//                    "https://fakestoreapi.com/products",
//                    fakeStoreGetbodyResponsedto[].class
//            );
//           List<fakeStoreGetbodyResponsedto> responsedtoList = Stream.of(response).toList();
//           List<productModel> products= new ArrayList<>();
//           for(fakeStoreGetbodyResponsedto fakeStoreGetbodyResponsedto: responsedtoList){
//               products.add(fakeStoreGetbodyResponsedto.toProduct());
//           }
//           return products;
    }

    @Override
    public productModel partialUpdateProduct(Long productId, productModel product) {

        return null;
    }

    //    @Override
//    public productModel getProductById(Long id) throws ProductNotFoundException {
//
//        // 1. CHeck if product is available in Redis
//            productModel product = (productModel) redisTemplate.opsForHash().get("Key" , "sample" );
//            if (product != null) {
//                // CACHE HITTTTTTT
//                return product;
//            }
//
//        fakeStoreGetbodyResponsedto response =
//                restTemplate.getForObject(
//                        "https://fakestoreapi.com/products/" + id,
//                        fakeStoreGetbodyResponsedto.class
//                );
//
//        if (response == null) {
//            throw new ProductNotFoundException("Product with id " + id + " not found");
//        }
//
//        product =   response.toProduct();
//        redisTemplate.opsForHash().put("PRODUCTS" , "Products_"  +id , product);
//        return product;
//    }
    @Override
    public productModel getProductById(Long id) throws ProductNotFoundException {

        String key = "PRODUCT_" + id;

        try {
            productModel cached = (productModel) redisTemplate.opsForValue().get(key);

            if (cached != null) {
                System.out.println("CACHE HIT for product ID: " + id);
                return cached;
            }
        } catch (Exception e) {
            System.err.println("Redis GET error: " + e.getMessage());
        }

        System.out.println("CACHE MISS for product ID: " + id);

        fakeStoreGetbodyResponsedto response = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + id,
                fakeStoreGetbodyResponsedto.class
        );

        if (response == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        productModel product = response.toProduct();

        try {
            redisTemplate.opsForValue().set(key, product, 10, TimeUnit.MINUTES);
            System.out.println("Cached product ID: " + id);
        } catch (Exception e) {
            System.err.println("Redis SET error: " + e.getMessage());
        }

        return product;
    }


    @Override
    public productModel replaceProduct(Long productId, productModel product) {
        return null;
    }

}

package com.example.productservice.service;

import com.example.productservice.Exceptions.ProductNotFoundException;
import com.example.productservice.models.Category;
import com.example.productservice.models.productModel;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("DBimpl")
public class productServiceDBimpl implements productService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;


    public productServiceDBimpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
    }


    @Override
    public productModel createProduct(productModel product) {

       String categoryName = product.getCategory().getName();

        Optional<Category> category =
                categoryRepository.findByName(categoryName);
        Category toBePutInProduct = null;

        if(category.isEmpty()){
            Category toSaveCategory = new Category();
            toSaveCategory.setName(categoryName);
            toBePutInProduct= toSaveCategory;



        }else {
            toBePutInProduct =category.get();
        }
       product.setCategory(toBePutInProduct);

        productModel savedProduct = productRepository.save(product);
     return savedProduct;
    }

    @Override
    public List<productModel> getAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public productModel partialUpdateProduct(Long productId, productModel product) {
        product.setId(productId);
        Optional<productModel> productToUpdateOptional = productRepository.findById(productId);
       if(productToUpdateOptional.isEmpty()){
           throw new ProductNotFoundException("ProductNotFOund");
       }
       productModel productToUpdate = productToUpdateOptional.get();

        if(product.getDescription() !=null){
         productToUpdate.setDescription(product.getDescription());
        }
        if(product.getPriceMinor()!=null){
            productToUpdate.setPriceMinor(product.getPriceMinor());
        }
        if(product.getCurrency()!=null){
            productToUpdate.setCurrency(product.getCurrency());
        }
        if(product.getTitle()!=null){
            productToUpdate.setTitle(product.getTitle());
        }
        if(product.getImageURL()!=null){
            productToUpdate.setImageURL(product.getImageURL());

        }

        if(product.getCategory()!=null){
            String categoryName = product.getCategory().getName();

            Optional<Category> category =
                    categoryRepository.findByName(categoryName);
            Category toBePutInProduct = null;

            if(category.isEmpty()){
                Category toSaveCategory = new Category();
                toSaveCategory.setName(categoryName);
                toBePutInProduct= categoryRepository.save(toSaveCategory);



            }else {
                toBePutInProduct =category.get();
            }
            productToUpdate.setCategory(toBePutInProduct);
        }

        return productRepository.save(productToUpdate);
    }

    @Override
    public productModel getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
    }
    @Override
    public productModel replaceProduct(Long productId, productModel product) {

        Optional<productModel> existingOptional =
                productRepository.findById(productId);

        if (existingOptional.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id " + productId);
        }

        productModel existingProduct = existingOptional.get();


        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPriceMinor(product.getPriceMinor());
        existingProduct.setCurrency(product.getCurrency());
        existingProduct.setImageURL(product.getImageURL());

        // handle category (same logic )
        if (product.getCategory() != null) {
            String categoryName = product.getCategory().getName();

            Optional<Category> categoryOptional =
                    categoryRepository.findByName(categoryName);

            Category finalCategory;

            if (categoryOptional.isEmpty()) {
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                finalCategory = categoryRepository.save(newCategory);
            } else {
                finalCategory = categoryOptional.get();
            }

            existingProduct.setCategory(finalCategory);
        } else {
            existingProduct.setCategory(null); // PUT means overwrite
        }

        return productRepository.save(existingProduct);
    }

}

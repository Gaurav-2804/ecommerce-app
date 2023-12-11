package com.userauth.userauth.service;

import java.util.List;

import com.userauth.userauth.entity.Category;
import com.userauth.userauth.entity.ProductData;
import com.userauth.userauth.entity.ProductDetails;
import com.userauth.userauth.entity.ProductRequest;
import com.userauth.userauth.entity.UserProducts;

public interface ProductDetailsService {
    // ProductDetails getProductDetails(Long id);

    // void saveProduct(ProductRequest product);
    void saveProduct(ProductRequest product, ProductData productData);

    void updateProdut(Long id, ProductDetails product);

    void deleteProduct(Long id);

    List<UserProducts> getAllProducts();

    // List<String> getCategories();

    List<Category> getCategories();
}

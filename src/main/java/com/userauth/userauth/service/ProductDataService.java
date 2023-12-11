package com.userauth.userauth.service;

import java.util.List;

import com.userauth.userauth.entity.ProductData;

public interface ProductDataService {
    ProductData getProductData(String uuid);

    List<String> getCarouselImages();
}

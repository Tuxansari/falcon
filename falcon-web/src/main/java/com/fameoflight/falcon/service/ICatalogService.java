package com.fameoflight.falcon.service;

import com.fameoflight.falcon.model.ProductInfo;

public interface ICatalogService {
    ProductInfo getProduct(Integer productId);
}

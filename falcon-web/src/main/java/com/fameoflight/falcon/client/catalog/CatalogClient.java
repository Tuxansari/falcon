package com.fameoflight.falcon.client.catalog;

import com.fameoflight.falcon.model.ProductInfo;
import feign.Headers;
import feign.RequestLine;

import java.net.URI;

public interface CatalogClient {

    @RequestLine("GET")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    ProductInfo getProduct(URI uri);
}
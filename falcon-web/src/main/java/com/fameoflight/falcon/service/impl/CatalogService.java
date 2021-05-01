package com.fameoflight.falcon.service.impl;

import com.fameoflight.falcon.client.catalog.CatalogClient;
import com.fameoflight.falcon.model.ProductInfo;
import com.fameoflight.falcon.model.CatalogClientConfig;
import com.fameoflight.falcon.service.ICatalogService;
import com.fameoflight.falcon.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;


@Slf4j
@Service
public class CatalogService implements ICatalogService {

    @Autowired
    private CatalogClient catalogClient;

    @Autowired
    private CatalogClientConfig catalogClientConfig;

    @Override
    public ProductInfo getProduct(Integer productId) {
        log.info("Calling catalog service for product details : " + UserContextHolder.getContext().getRequestId());
        String productUrl = String.format(catalogClientConfig.getProductUrl(), productId);
        String finalUrl = catalogClientConfig.getBaseUrl() + productUrl;
        URI uri = URI.create(finalUrl);
        return catalogClient.getProduct(uri);
    }
}

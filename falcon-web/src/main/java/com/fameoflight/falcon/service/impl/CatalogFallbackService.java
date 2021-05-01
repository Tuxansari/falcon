package com.fameoflight.falcon.service.impl;

import com.fameoflight.falcon.client.catalog.CatalogClient;
import com.fameoflight.falcon.model.ProductInfo;
import com.fameoflight.falcon.service.ICatalogService;
import com.fameoflight.falcon.utils.UserContextHolder;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

@Slf4j
@Component
public class CatalogFallbackService implements FallbackFactory<CatalogClient> {

    @Override
    public CatalogClient create(Throwable cause) {
        return new CatalogClient() {
            @Override
            public ProductInfo getProduct(URI uri) {
                log.error("fallback called for getProduct  : " + UserContextHolder.getContext().getRequestId());
                return ProductInfo.builder()
                        .id(100L)
                        .name("fallback_product")
                        .build();
            }
        };
    }
}

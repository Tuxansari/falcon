package com.fameoflight.falcon.client.catalog;

import com.fameoflight.falcon.feignUtil.CustomFeignInterceptor;
import com.fameoflight.falcon.feignUtil.CustomFeignLogger;
import com.fameoflight.falcon.feignUtil.CustomFeignRetryer;
import com.fameoflight.falcon.model.CatalogClientConfig;
import com.fameoflight.falcon.service.impl.CatalogFallbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.httpclient.ApacheHttpClient;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.fameoflight.falcon.feignUtil.HystrixSetterFactoryBuilder.buildSetterFactory;

@Component
public class CatalogFeignConfig {

    static final String HYSTRIX_GROUP_NAME = "Catalog";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomFeignLogger customFeignLogger;

    @Autowired
    private CustomFeignRetryer customFeignRetryer;

    @Autowired
    private CustomFeignInterceptor customFeignInterceptor;

    @Autowired(required = false)
    FallbackFactory<CatalogClient> catalogClientFallbackFactory;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    @Qualifier("catalogHttpClient")
    private ApacheHttpClient apacheHttpClient;

    @Autowired
    private CatalogClientConfig catalogClientConfig;

    @Bean
    CatalogClient catalogClient() {
        Integer socketTimeout = catalogClientConfig.getSocketTimeout();
        Integer connectTimeout = catalogClientConfig.getConnectionTimeout();
        String url = catalogClientConfig.getBaseUrl();
        Integer webContainerMaxThreads = catalogClientConfig.getMaxTotalConnections();
        return HystrixFeign.builder()
                .client(apacheHttpClient)
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logLevel(Logger.Level.FULL)
                .logger(customFeignLogger)
                .retryer(customFeignRetryer)
                .requestInterceptor(customFeignInterceptor)
                .setterFactory(buildSetterFactory(socketTimeout,
                        connectTimeout,
                        HYSTRIX_GROUP_NAME,
                        webContainerMaxThreads))
                .target(CatalogClient.class, url, catalogClientFallbackFactory);
    }
}
